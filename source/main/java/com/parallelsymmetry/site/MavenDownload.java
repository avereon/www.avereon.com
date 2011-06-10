package com.parallelsymmetry.site;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.parallelsymmetry.escape.utility.Descriptor;
import com.parallelsymmetry.escape.utility.FileUtil;
import com.parallelsymmetry.escape.utility.Version;
import com.parallelsymmetry.escape.utility.log.Log;

public class MavenDownload implements Comparable<MavenDownload> {

	private static final String DEFAULT_EXTENSION = "jar";

	private static final Map<String, List<MavenDownload>> cache = new ConcurrentHashMap<String, List<MavenDownload>>();

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

		StringBuilder builder = new StringBuilder();
		builder.append( groupId );
		builder.append( "-" );
		builder.append( artifactId );
		builder.append( "-" );
		builder.append( version );
		builder.append( "-" );
		builder.append( classifier );
		builder.append( "-" );
		builder.append( type );
		identifier = builder.toString();

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

	public String getDownloadLink( String contextPath ) {
		StringBuilder builder = new StringBuilder();

		builder.append( contextPath );
		builder.append( "/download/" + getArtifactId() );
		if( classifier != null ) builder.append( "-" + classifier );
		if( type != null ) builder.append( "." + type );
		builder.append( "?source=" + link );

		return builder.toString();
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
		if( !( object instanceof MavenDownload ) ) return false;

		MavenDownload that = (MavenDownload)object;

		return this.identifier.equals( that.identifier );
	}

	public static final List<MavenDownload> getDownloads( String uri, String classifier, String type ) throws Exception {
		List<String> uris = new ArrayList<String>();
		uris.add( uri );
		return getDownloads( uris, classifier, type );
	}

	public static final List<MavenDownload> getDownloads( List<String> uris, String classifier, String type ) throws Exception {
		List<MavenDownload> downloads = new ArrayList<MavenDownload>();

		// Check the cache.
		List<String> neededUris = new ArrayList<String>();
		for( String uri : uris ) {
			String key = getDownloadKey( uri, classifier, type );

			System.err.println( "Key: " + key );
			List<MavenDownload> cachedDownloads = cache.get( key );

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

				System.err.println( "Key: " + key );
				// Add download to cache.
				List<MavenDownload> cachedDownloads = cache.get( key );
				if( cachedDownloads == null ) {
					cachedDownloads = new CopyOnWriteArrayList<MavenDownload>();
					cache.put( key, cachedDownloads );
				}
				cachedDownloads.add( download );
			}
			downloads.addAll( directDownloads );
		}

		return downloads;
	}

	private static final String getDownloadKey( String uri, String classifier, String type ) {
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
	 * Retrieves all the applicable downloads for the specified URI's and
	 * 
	 * @param classifier
	 * @param type
	 * @param uris
	 * @return
	 * @throws Exception
	 */
	private static final List<MavenDownload> getDownloadsDirect( List<String> uris, String classifier, String type ) throws Exception {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<?>> futures = new ArrayList<Future<?>>();

		if( type == null ) type = DEFAULT_EXTENSION;

		try {
			// Construct context object map.
			List<Context> contexts = new ArrayList<Context>();
			for( String uri : uris ) {
				Log.write( "URI: " + uri );
				contexts.add( new Context( uri ) );
			}

			// Load the root metadata descriptors.
			futures.clear();
			for( Context context : contexts ) {
				futures.add( executor.submit( new LoadRootDescriptor( context ) ) );
			}

			// Wait for the futures to return.
			waitFor( futures );

			// Load the release descriptors.
			futures.clear();
			for( Context context : contexts ) {
				if( !context.isValid() ) continue;
				List<ReleaseContext> releaseContexts = getReleaseContexts( context );
				for( ReleaseContext releaseContext : releaseContexts ) {
					context.addReleaseContext( releaseContext );
					futures.add( executor.submit( new LoadReleasePom( context, releaseContext ) ) );
				}
			}

			// Wait for the futures to return.
			waitFor( futures );

			List<MavenDownload> downloads = new ArrayList<MavenDownload>();
			for( Context context : contexts ) {
				if( !context.isValid() ) continue;
				String key = getDownloadKey( context.getUri(), classifier, type );
				for( ReleaseContext releaseContext : context.getReleaseContexts() ) {
					if( !releaseContext.isValid() ) continue;
					String groupId = releaseContext.getPom().getValue( "project/groupId" );
					String artifactId = releaseContext.getPom().getValue( "project/artifactId" );
					Version version = releaseContext.getVersion();

					String name = releaseContext.getPom().getValue( "project/name" );
					String link = releaseContext.getPath() + ( classifier == null ? "" : "-" + classifier ) + "." + type;
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
				// Intentionally ignore exception.
			}
		}
	}

	/**
	 * Get the release context objects.
	 * 
	 * @param context
	 * @return Returns a list of release context objects reverse sorted according
	 *         to the version number.
	 */
	private static final List<ReleaseContext> getReleaseContexts( Context context ) {
		List<ReleaseContext> contexts = new ArrayList<ReleaseContext>();

		String uri = context.getUri();
		Descriptor metadata = context.getRootDescriptor();
		String artifact = metadata.getValue( "metadata/artifactId" );
		String[] versionStrings = metadata.getValues( "metadata/versioning/versions/version" );

		// Convert the version strings to version objects.
		List<Version> versions = new ArrayList<Version>();
		for( String versionString : versionStrings ) {
			versions.add( new Version( versionString ) );
		}

		for( Version version : versions ) {
			contexts.add( new ReleaseContext( uri + "/" + version.toString(), artifact, version ) );
		}

		return contexts;
	}

	private static final class Context {

		private String uri;

		private Descriptor rootDescriptor;

		private List<ReleaseContext> releaseContexts;

		public Context( String uri ) {
			this.uri = uri;
			releaseContexts = new CopyOnWriteArrayList<ReleaseContext>();
		}

		public String getUri() {
			return uri;
		}

		public Descriptor getRootDescriptor() {
			return rootDescriptor;
		}

		public void setRootDescriptor( Descriptor rootDescriptor ) {
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

		private Descriptor pom;

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

		public Descriptor getPom() {
			return pom;
		}

		public void setPom( Descriptor pom ) {
			this.pom = pom;
		}

		public void setUnique( String unique ) {
			this.unique = unique;
		}

		public boolean isValid() {
			return pom != null;
		}

	}

	private static final class LoadRootDescriptor implements Callable<Context> {

		private Context context;

		public LoadRootDescriptor( Context context ) {
			this.context = context;
		}

		@Override
		public Context call() throws Exception {
			context.setRootDescriptor( new Descriptor( context.getUri() + "/maven-metadata.xml" ) );
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
			// http://mvn.parallelsymmetry.com/release/com/parallelsymmetry/utility/1.0.3/utility-1.0.3.pom
			// <uri>/<version>/<artifact>-<version>.pom
			// <url>/<version>/<artifact>-<version>.<extension>
			// Snapshot:
			// http://mvn.parallelsymmetry.com/snapshot/com/parallelsymmetry/terrace/1.0.0-SNAPSHOT/maven-metadata.xml
			// <uri>/<version>/maven-metadata.xml
			// <url>/<version>/<artifact>-<version with SNAPSHOT replaced>.<extension>

			String pomPath = null;
			if( releaseContext.getVersion().isSnapshot() ) {
				Descriptor descriptor = new Descriptor( releaseContext.getBase() + "/maven-metadata.xml" );

				String timestamp = descriptor.getValue( "/metadata/versioning/snapshot/timestamp" );
				if( timestamp != null ) {
					String build = descriptor.getValue( "/metadata/versioning/snapshot/buildNumber" );
					releaseContext.setUnique( timestamp + "-" + build );
				}

				pomPath = releaseContext.getPath() + ".pom";
			} else {
				pomPath = releaseContext.getBase() + "/" + releaseContext.getArtifact() + "-" + releaseContext.getVersion().toString() + ".pom";
			}

			releaseContext.setPom( new Descriptor( pomPath ) );

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
