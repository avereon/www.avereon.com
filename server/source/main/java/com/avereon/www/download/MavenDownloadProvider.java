package com.avereon.www.download;

import com.avereon.util.Log;
import com.avereon.util.TextUtil;
import com.avereon.util.Version;
import com.avereon.util.XmlDescriptor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class MavenDownloadProvider extends AbstractDownloadProvider {

	private static final System.Logger log = Log.log();

	private static final String REPO = "https://repo.avereon.com/avn/";

	private static final String GROUP = "com/avereon/";

	private static String ROOT = REPO + GROUP;

	private static final String DEFAULT_EXTENSION = "jar";

	private static final Map<String, List<ProductDownload>> cache = new ConcurrentHashMap<>();

	@Override
	public List<ProductDownload> getDownloads( List<String> artifacts, String classifier, String type, String version, String platform ) {
		List<ProductDownload> downloads = new ArrayList<>();

		// Check the cache.
		List<String> neededUris = new ArrayList<>();
		for( String artifact : artifacts ) {
			String uri = createUri( artifact );
			List<ProductDownload> cachedDownloads = cache.get( Download.key( artifact, classifier, type, version, platform ) );
			if( cachedDownloads == null ) {
				neededUris.add( uri );
			} else {
				downloads.addAll( cachedDownloads );
			}
		}

		// Get direct downloads.
		if( neededUris.size() > 0 ) {
			List<ProductDownload> directDownloads = getDownloadsDirect( neededUris, classifier, type, version, platform );

			for( ProductDownload download : directDownloads ) {
				String key = download.getKey();

				// Add download to cache.
				List<ProductDownload> cachedDownloads = cache.get( key );
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

	public String clearCache() {
		cache.clear();

		log.log( Log.INFO, "Cache cleared for all" );
		return "Cache cleared for all";
	}

	public String clearCache( String artifact, String classifier, String type, String channel, String platform ) {
		String key = Download.key( artifact, classifier, type, channel, platform );

		for( String cacheKey : new HashSet<>( cache.keySet() ) ) {
			if( cacheKey.startsWith( key ) ) {
				log.log( Log.INFO, "Evict cache for: " + cacheKey );
				cache.remove( cacheKey );
			}
		}

		log.log( Log.INFO, "Cache cleared for " + key );
		return "Cache cleared for " + key;
	}

	XmlDescriptor getXmlDescriptor( String uri ) throws IOException {
		log.log( Log.DEBUG, "Retrieve " + uri );
		return new XmlDescriptor( URI.create( uri ) );
	}

	private String createUri( String artifact ) {
		return ROOT + artifact;
	}

	/**
	 * Retrieves all the applicable downloads for the specified URIs, classifier and type.
	 *
	 * @param uris The list of URIs to search
	 * @param classifier The artifact classifier
	 * @param type The artifact file type
	 * @return A list of all applicable downloads
	 */
	private List<ProductDownload> getDownloadsDirect( List<String> uris, String classifier, String type, String version, String platform ) {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<?>> futures = new ArrayList<>();

		if( type == null ) type = DEFAULT_EXTENSION;

		try {
			// Construct context objects
			List<Context> contexts = loadContexts( executor, uris );

			// Load the release descriptors.
			for( Context context : contexts ) {
				if( !context.isValid() ) continue;

				String latestVersion = context.getRootDescriptor().getValue( "metadata/versioning/latest" );
				String releaseVersion = context.getRootDescriptor().getValue( "metadata/versioning/release" );

				List<ReleaseContext> releaseContexts = new ArrayList<>();

				if( "latest".equals( version ) && !TextUtil.isEmpty( latestVersion ) ) {
					releaseContexts.add( getReleaseContext( context, latestVersion ) );
				} else if( "release".equals( version ) && !TextUtil.isEmpty( releaseVersion ) ) {
					releaseContexts.add( getReleaseContext( context, releaseVersion ) );
				} else {
					releaseContexts = getReleaseContexts( context );
				}

				for( ReleaseContext releaseContext : releaseContexts ) {
					context.addReleaseContext( releaseContext );
					futures.add( executor.submit( new LoadReleasePom( context, releaseContext ) ) );
				}
				waitFor( futures );
			}

			List<ProductDownload> downloads = new ArrayList<>();
			for( Context context : contexts ) {
				if( !context.isValid() ) continue;

				for( ReleaseContext releaseContext : context.getReleaseContexts() ) {
					if( !releaseContext.isValid() ) continue;
					String groupId = releaseContext.getPom().getValue( "project/groupId" );
					String artifactId = releaseContext.getPom().getValue( "project/artifactId" );
					Version artifactVersion = releaseContext.getVersion();

					// Check if the version matches
					if( !versionMatch( artifactVersion, version ) ) continue;

					String name = releaseContext.getPom().getValue( "project/name" );
					String link = releaseContext.getPath() + (classifier == null ? "" : "-" + classifier) + "." + type;
					String md5Link = link + ".md5";
					String sha1Link = link + ".sha1";

					downloads.add( new ProductDownload( groupId, artifactId, version, classifier, type, platform, version, name, link, md5Link, sha1Link ) );
				}
			}

			// Load the artifact metadata.
			futures.clear();
			for( ProductDownload download : downloads ) {
				futures.add( executor.submit( new LoadArtifactData( download ) ) );
			}
			waitFor( futures );

			// Reverse sort the versions.
			Collections.sort( downloads );
			Collections.reverse( downloads );

			return downloads;
		} finally {
			executor.shutdown();
		}
	}

	private List<Context> loadContexts( ExecutorService executor, List<String> uris ) {
		List<Future<?>> futures = new ArrayList<>();

		// Construct context object map.
		List<Context> contexts = new ArrayList<>();
		for( String uri : uris ) {
			contexts.add( new Context( uri ) );
		}

		// Load the metadata descriptor.
		futures.clear();
		for( Context context : contexts ) {
			futures.add( executor.submit( new LoadMavenMetadata( context ) ) );
		}

		// Wait for the futures to return.
		waitFor( futures );
		futures.clear();

		return contexts;
	}

	/**
	 * Check if a version matches a requested version.
	 *
	 * @param downloadVersion The version string to match.
	 * @param requestedVersion The requested version string. Can be "latest", "release" or a version number.
	 * @return True if the version matches, false otherwise
	 */
	private boolean versionMatch( Version downloadVersion, String requestedVersion ) {
		// Versions are checking in descending order so latest would match the first
		if( "latest".equalsIgnoreCase( requestedVersion ) ) return true;
		if( "release".equalsIgnoreCase( requestedVersion ) && !downloadVersion.isSnapshot() ) return true;
		return downloadVersion.toString().startsWith( requestedVersion );
	}

	private void waitFor( List<Future<?>> futures ) {
		for( Future<?> future : futures ) {
			try {
				future.get();
			} catch( InterruptedException exception ) {
				return;
			} catch( ExecutionException exception ) {
				log.log( Log.ERROR, "Error executing future", exception );
			}
		}
	}

	/**
	 * Get the release contexts list.
	 *
	 * @param context The download context
	 * @return Returns a list of release contexts reverse sorted according to the version number.
	 */
	private List<ReleaseContext> getReleaseContexts( Context context ) {
		String uri = context.getUri();
		XmlDescriptor metadata = context.getRootDescriptor();
		String artifact = metadata.getValue( "metadata/artifactId" );
		String[] versionStrings = metadata.getValues( "metadata/versioning/versions/version" );

		// Convert the version strings to version objects.
		List<Version> versions = new ArrayList<>();
		for( String versionString : versionStrings ) {
			versions.add( new Version( versionString ) );
		}

		List<ReleaseContext> contexts = new ArrayList<>();

		for( Version version : versions ) {
			contexts.add( new ReleaseContext( uri + "/" + version.toString(), artifact, version ) );
		}

		return contexts;
	}

	//	private ReleaseContext getReleaseContext( Context context ) {
	//		String uri = context.getUri();
	//		XmlDescriptor metadata = context.getRootDescriptor();
	//		String artifact = metadata.getValue( "metadata/artifactId" );
	//
	//		String uriPath = URI.create( uri ).getPath();
	//		String uriVersion = uriPath.substring( uriPath.lastIndexOf( "/" ) + 1 );
	//		Version version = new Version( uriVersion );
	//
	//		return new ReleaseContext( uri, artifact, version );
	//	}

	private ReleaseContext getReleaseContext( Context context, String version ) {
		String uri = context.getUri();
		XmlDescriptor metadata = context.getRootDescriptor();
		String artifact = metadata.getValue( "metadata/artifactId" );
		return new ReleaseContext( uri + "/" + version, artifact, new Version( version ) );
	}

	static final class Context {

		private String uri;

		private XmlDescriptor rootDescriptor;

		private List<ReleaseContext> releaseContexts;

		public Context( String uri ) {
			this.uri = uri;
			releaseContexts = new CopyOnWriteArrayList<>();
		}

		String getUri() {
			return uri;
		}

		XmlDescriptor getRootDescriptor() {
			return rootDescriptor;
		}

		void setRootDescriptor( XmlDescriptor rootDescriptor ) {
			this.rootDescriptor = rootDescriptor;
		}

		List<ReleaseContext> getReleaseContexts() {
			return Collections.unmodifiableList( releaseContexts );
		}

		void addReleaseContext( ReleaseContext context ) {
			releaseContexts.add( context );
		}

		boolean isValid() {
			return rootDescriptor != null;
		}

	}

	static final class ReleaseContext {

		private String base;

		private String artifact;

		private Version version;

		private String unique;

		private XmlDescriptor pom;

		ReleaseContext( String base, String artifact, Version version ) {
			this.base = base;
			this.artifact = artifact;
			this.version = version;
		}

		String getBase() {
			return base;
		}

		String getArtifact() {
			return artifact;
		}

		Version getVersion() {
			return version;
		}

		String getPath() {
			if( unique != null ) {
				return base + "/" + artifact + "-" + version.toString().replace( "SNAPSHOT", unique );
			} else {
				return base + "/" + artifact + "-" + version.toString();
			}
		}

		XmlDescriptor getPom() {
			return pom;
		}

		void setPom( XmlDescriptor pom ) {
			this.pom = pom;
		}

		void setUnique( String unique ) {
			this.unique = unique;
		}

		boolean isValid() {
			return pom != null;
		}

	}

	private class LoadMavenMetadata implements Callable<Context> {

		private Context context;

		LoadMavenMetadata( Context context ) {
			this.context = context;
		}

		@Override
		public Context call() throws Exception {
			context.setRootDescriptor( getXmlDescriptor( context.getUri() + "/maven-metadata.xml" ) );
			return context;
		}

	}

	private class LoadReleasePom implements Callable<Context> {

		private Context context;

		private ReleaseContext releaseContext;

		LoadReleasePom( Context context, ReleaseContext releaseContext ) {
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
				XmlDescriptor descriptor = getXmlDescriptor( releaseContext.getBase() + "/maven-metadata.xml" );

				String timestamp = descriptor.getValue( "/metadata/versioning/snapshot/timestamp" );
				if( timestamp != null ) {
					String build = descriptor.getValue( "/metadata/versioning/snapshot/buildNumber" );
					releaseContext.setUnique( timestamp + "-" + build );
				}

				pomPath = releaseContext.getPath() + ".pom";
			} else {
				pomPath = releaseContext.getBase() + "/" + releaseContext.getArtifact() + "-" + releaseContext.getVersion().toString() + ".pom";
			}

			releaseContext.setPom( getXmlDescriptor( pomPath ) );

			return context;
		}

	}

	private class LoadArtifactData implements Callable<ProductDownload> {

		private ProductDownload download;

		LoadArtifactData( ProductDownload download ) {
			this.download = download;
		}

		@Override
		public ProductDownload call() throws Exception {
			String link = download.getLink();

			URL url = new URL( link );

			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			int length = connection.getContentLength();
			long date = connection.getLastModified();

			if( length > -1 ) download.setLength( length );
			if( date != 0 ) download.setDate( new Date( date ) );

			return download;
		}

	}

}
