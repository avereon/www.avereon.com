package com.xeomar.web.root;

import com.xeomar.util.FileUtil;
import com.xeomar.util.TextUtil;
import com.xeomar.util.Version;
import com.xeomar.util.XmlDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.*;

public class MavenDownload implements Comparable<MavenDownload> {

	private static final Logger log = LoggerFactory.getLogger( MavenDownload.class );

	private static final String DEFAULT_EXTENSION = "jar";

	private static final Map<String, List<MavenDownload>> cache = new ConcurrentHashMap<>();

	private String key;

	private String groupId;

	private String artifactId;

	private String name;

	private String classifier;

	private String type;

	private String identifier;

	private Version version;

	private String link;

	private String md5Link;

	private String sha1Link;

	private int length;

	private Date date;

	private MavenDownload( String key, String groupId, String artifactId, Version version, String classifier, String type, String name, String link, String md5Link, String sha1Link ) {
		this.key = key;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.classifier = classifier;
		this.type = type;

		this.name = name;
		this.link = link;
		this.md5Link = md5Link;
		this.sha1Link = sha1Link;

		identifier = String.format( "%s-%s-%s-%s-%s", groupId, artifactId, version, classifier, type );
	}

	public String getKey() {
		return key;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getName() {
		return name;
	}

	public Version getVersion() {
		return version;
	}

	public String getClassifier() {
		return classifier;
	}

	public String getType() {
		return type;
	}

	public String getLink() {
		return link;
	}

	public String getMd5Link() {
		return md5Link;
	}

	public String getSha1Link() {
		return sha1Link;
	}

	public int getLength() {
		return length;
	}

	public String getHumanReadableLength() {
		return FileUtil.getHumanBinSize( length );
	}

	public Date getDate() {
		return date;
	}

	public String formatDate( DateFormat format, String unknownText ) {
		if( date == null ) {
			return unknownText;
		} else {
			return format.format( date );
		}
	}

	@Override
	public int compareTo( MavenDownload that ) {
		return this.getVersion().compareTo( that.getVersion() );
	}

	@Override
	public boolean equals( Object object ) {
		if( !(object instanceof MavenDownload) ) return false;

		MavenDownload that = (MavenDownload)object;

		return this.identifier.equals( that.identifier );
	}

	public static List<MavenDownload> getDownloads( String uri, String classifier, String type ) {
		List<String> uris = new ArrayList<>();
		uris.add( uri );
		return getDownloads( uris, classifier, type );
	}

	public static List<MavenDownload> getDownloads( List<String> uris, String classifier, String type ) {
		List<MavenDownload> downloads = new ArrayList<>();

		// Check the cache.
		List<String> neededUris = new ArrayList<>();
		for( String uri : uris ) {
			List<MavenDownload> cachedDownloads = cache.get( getDownloadKey( uri, classifier, type ) );
			if( cachedDownloads == null ) {
				neededUris.add( uri );
			} else {
				downloads.addAll( cachedDownloads );
			}
		}

		// Get direct downloads.
		if( neededUris.size() > 0 ) {
			List<MavenDownload> directDownloads = getDownloadsDirect( neededUris, classifier, type );

			for( MavenDownload download : directDownloads ) {
				String key = download.key;

				// Add download to cache.
				List<MavenDownload> cachedDownloads = cache.get( key );
				if( cachedDownloads == null ) {
					cachedDownloads = new CopyOnWriteArrayList<>();
					cache.put( key, cachedDownloads );
				}
				cachedDownloads.add( download );
			}
			downloads.addAll( directDownloads );
		}

		return downloads;
	}

	public static void clearCache() {
		cache.clear();
	}

	public static void clearCache( String uri, String classifier, String type ) {
		String key = getDownloadKey( uri, classifier, type );

		for( String cacheKey : new HashSet<>( cache.keySet() ) ) {
			if( cacheKey.startsWith( key ) ) {
				cache.remove( cacheKey );
			}
		}

		// TODO Start the background refresh thread.
	}

	private static String getDownloadKey( String uri, String classifier, String type ) {
		StringBuilder builder = new StringBuilder( uri );

		if( classifier != null ) {
			builder.append( "-" );
			builder.append( classifier );
		}

		builder.append( "-" );
		builder.append( type == null ? DEFAULT_EXTENSION : type );

		return builder.toString();
	}

	/**
	 * Retrieves all the applicable downloads for the specified URIs, classifier and type.
	 *
	 * @param uris The list of URIs to search
	 * @param classifier The artifact classifier
	 * @param type The artifact file type
	 * @return A list of all applicable downloads
	 */
	private static List<MavenDownload> getDownloadsDirect( List<String> uris, String classifier, String type ) {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<?>> futures = new ArrayList<>();

		if( type == null ) type = DEFAULT_EXTENSION;

		try {
			// Construct context object map.
			List<Context> contexts = new ArrayList<>();
			for( String uri : uris ) {
				contexts.add( new Context( uri ) );
			}

			// FIXME If the URI is a release location there is not a metadata file.

			// Load the metadata descriptor.
			futures.clear();
			for( Context context : contexts ) {
				futures.add( executor.submit( new LoadMavenMetadata( context ) ) );
			}

			// Wait for the futures to return.
			waitFor( futures );
			futures.clear();

			// Load the release descriptors.
			for( Context context : contexts ) {
				if( !context.isValid() ) continue;

				List<ReleaseContext> releaseContexts;
				String metadataVersion = context.getRootDescriptor().getValue( "metadata/version" );
				if( TextUtil.isEmpty( metadataVersion ) ) {
					releaseContexts = getReleaseContexts( context );
				} else {
					releaseContexts = new ArrayList<>();
					if( context.uri.endsWith( metadataVersion ) ) {
						releaseContexts.add( getReleaseContext( context ) );
					} else {
						releaseContexts.add( getReleaseContext( context, metadataVersion ) );
					}
				}
				for( ReleaseContext releaseContext : releaseContexts ) {
					context.addReleaseContext( releaseContext );
					futures.add( executor.submit( new LoadReleasePom( context, releaseContext ) ) );
				}
			}

			// Wait for the futures to return.
			waitFor( futures );

			List<MavenDownload> downloads = new ArrayList<>();
			for( Context context : contexts ) {
				if( !context.isValid() ) continue;
				String key = getDownloadKey( context.getUri(), classifier, type );

				for( ReleaseContext releaseContext : context.getReleaseContexts() ) {
					if( !releaseContext.isValid() ) continue;
					String groupId = releaseContext.getPom().getValue( "project/groupId" );
					String artifactId = releaseContext.getPom().getValue( "project/artifactId" );
					Version version = releaseContext.getVersion();

					String name = releaseContext.getPom().getValue( "project/name" );
					String link = releaseContext.getPath() + (classifier == null ? "" : "-" + classifier) + "." + type;
					String md5Link = link + ".md5";
					String sha1Link = link + ".sha1";

					downloads.add( new MavenDownload( key, groupId, artifactId, version, classifier, type, name, link, md5Link, sha1Link ) );
				}
			}

			// Load the artifact metadata.
			futures.clear();
			for( MavenDownload download : downloads ) {
				futures.add( executor.submit( new LoadArtifactData( download ) ) );
			}

			// Wait for the futures to return.
			waitFor( futures );

			// Reverse sort the versions.
			Collections.sort( downloads );
			Collections.reverse( downloads );

			return downloads;
		} finally {
			executor.shutdown();
		}
	}

	private static final void waitFor( List<Future<?>> futures ) {
		for( Future<?> future : futures ) {
			try {
				future.get();
			} catch( InterruptedException exception ) {
				return;
			} catch( ExecutionException exception ) {
				log.error( "Error executing future", exception );
			}
		}
	}

	/**
	 * Get the release contexts list.
	 *
	 * @param context
	 * @return Returns a list of release contexts reverse sorted according to the
	 * version number.
	 */
	private static final List<ReleaseContext> getReleaseContexts( Context context ) {
		String uri = context.getUri();
		XmlDescriptor metadata = context.getRootDescriptor();
		String artifact = metadata.getValue( "metadata/artifactId" );
		String[] versionStrings = metadata.getValues( "metadata/versioning/versions/version" );

		// Convert the version strings to version objects.
		List<Version> versions = new ArrayList<Version>();
		for( String versionString : versionStrings ) {
			versions.add( new Version( versionString ) );
		}

		List<ReleaseContext> contexts = new ArrayList<ReleaseContext>();

		for( Version version : versions ) {
			contexts.add( new ReleaseContext( uri + "/" + version.toString(), artifact, version ) );
		}

		return contexts;
	}

	private static final ReleaseContext getReleaseContext( Context context ) {
		String uri = context.getUri();
		XmlDescriptor metadata = context.getRootDescriptor();
		String artifact = metadata.getValue( "metadata/artifactId" );

		String uriPath = URI.create( uri ).getPath();
		String uriVersion = uriPath.substring( uriPath.lastIndexOf( "/" ) + 1 );
		Version version = new Version( uriVersion );

		return new ReleaseContext( uri, artifact, version );
	}

	private static final ReleaseContext getReleaseContext( Context context, String version ) {
		String uri = context.getUri();
		XmlDescriptor metadata = context.getRootDescriptor();
		String artifact = metadata.getValue( "metadata/artifactId" );
		Version versionObject = new Version( version );
		return new ReleaseContext( uri + "/" + version, artifact, versionObject );
	}

	private static final class Context {

		private String uri;

		private XmlDescriptor rootDescriptor;

		private List<ReleaseContext> releaseContexts;

		public Context( String uri ) {
			this.uri = uri;
			releaseContexts = new CopyOnWriteArrayList<>();
		}

		public String getUri() {
			return uri;
		}

		public XmlDescriptor getRootDescriptor() {
			return rootDescriptor;
		}

		public void setRootDescriptor( XmlDescriptor rootDescriptor ) {
			this.rootDescriptor = rootDescriptor;
		}

		public List<ReleaseContext> getReleaseContexts() {
			return Collections.unmodifiableList( releaseContexts );
		}

		public void addReleaseContext( ReleaseContext context ) {
			releaseContexts.add( context );
		}

		public boolean isValid() {
			return rootDescriptor != null;
		}

	}

	private static final class ReleaseContext {

		private String base;

		private String artifact;

		private Version version;

		private String unique;

		private XmlDescriptor pom;

		public ReleaseContext( String base, String artifact, Version version ) {
			this.base = base;
			this.artifact = artifact;
			this.version = version;
		}

		public String getBase() {
			return base;
		}

		public String getArtifact() {
			return artifact;
		}

		public Version getVersion() {
			return version;
		}

		public String getPath() {
			if( unique != null ) {
				return base + "/" + artifact + "-" + version.toString().replace( "SNAPSHOT", unique );
			} else {
				return base + "/" + artifact + "-" + version.toString();
			}
		}

		public XmlDescriptor getPom() {
			return pom;
		}

		public void setPom( XmlDescriptor pom ) {
			this.pom = pom;
		}

		public void setUnique( String unique ) {
			this.unique = unique;
		}

		public boolean isValid() {
			return pom != null;
		}

	}

	private static final class LoadMavenMetadata implements Callable<Context> {

		private Context context;

		public LoadMavenMetadata( Context context ) {
			this.context = context;
		}

		@Override
		public Context call() throws Exception {
			log.debug( "Retrieve: " + URI.create( context.getUri() + "/maven-metadata.xml" ) );
			context.setRootDescriptor( new XmlDescriptor( URI.create( context.getUri() + "/maven-metadata.xml" ) ) );
			return context;
		}

	}

	private static final class LoadReleasePom implements Callable<Context> {

		private Context context;

		private ReleaseContext releaseContext;

		public LoadReleasePom( Context context, ReleaseContext releaseContext ) {
			this.context = context;
			this.releaseContext = releaseContext;
		}

		@Override
		public Context call() throws Exception {
			// The following example paths will help in debugging.
			// Release:
			// http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/utility/1.0.3/utility-1.0.3.pom
			// <uri>/<version>/<artifact>-<version>.pom
			// <url>/<version>/<artifact>-<version>.<extension>
			// Snapshot:
			// http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/terrace/1.0.0-SNAPSHOT/maven-metadata.xml
			// <uri>/<version>/maven-metadata.xml
			// <url>/<version>/<artifact>-<version with SNAPSHOT replaced>.<extension>

			String pomPath;
			if( releaseContext.getVersion().isSnapshot() ) {
				XmlDescriptor descriptor = new XmlDescriptor( URI.create( releaseContext.getBase() + "/maven-metadata.xml" ) );

				String timestamp = descriptor.getValue( "/metadata/versioning/snapshot/timestamp" );
				if( timestamp != null ) {
					String build = descriptor.getValue( "/metadata/versioning/snapshot/buildNumber" );
					releaseContext.setUnique( timestamp + "-" + build );
				}

				pomPath = releaseContext.getPath() + ".pom";
			} else {
				pomPath = releaseContext.getBase() + "/" + releaseContext.getArtifact() + "-" + releaseContext.getVersion().toString() + ".pom";
			}

			log.debug( "Retrieve:  " + pomPath );

			releaseContext.setPom( new XmlDescriptor( URI.create( pomPath ) ) );

			return context;
		}

	}

	private static final class LoadArtifactData implements Callable<MavenDownload> {

		private MavenDownload download;

		public LoadArtifactData( MavenDownload download ) {
			this.download = download;
		}

		@Override
		public MavenDownload call() throws Exception {
			String link = download.getLink();

			URL url = new URL( link );

			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			int length = connection.getContentLength();
			long date = connection.getLastModified();

			if( length > -1 ) download.length = length;
			if( date != 0 ) download.date = new Date( date );

			return download;
		}

	}

}
