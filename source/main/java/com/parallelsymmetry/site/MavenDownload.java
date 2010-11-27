package com.parallelsymmetry.site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.parallelsymmetry.util.Descriptor;
import com.parallelsymmetry.util.Version;

public class MavenDownload implements Comparable<MavenDownload> {

	private static final String DEFAULT_EXTENSION = "jar";

	private String name;

	private Version version;

	private String link;

	private String md5Link;

	private String sha1Link;

	public MavenDownload( String name, Version version, String link, String md5Link, String sha1Link ) {
		this.name = name;
		this.version = version;

		this.link = link;
		this.md5Link = md5Link;
		this.sha1Link = sha1Link;
	}

	public String getName() {
		return name;
	}

	public Version getVersion() {
		return version;
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

	@Override
	public int compareTo( MavenDownload that ) {
		return this.getVersion().compareTo( that.getVersion() );
	}

	public static final List<MavenDownload> getDownloads( String... uris ) throws Exception {
		return getDownloads( DEFAULT_EXTENSION, uris );
	}

	private static final List<MavenDownload> getDownloads( String extension, String... uris ) throws Exception {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<Context>> futures = new ArrayList<Future<Context>>();

		try {
			// Construct context object map.
			List<Context> contexts = new ArrayList<Context>();
			for( String uri : uris ) {
				contexts.add( new Context( uri ) );
			}

			// Load the root metadata descriptors.
			futures.clear();
			for( Context context : contexts ) {
				Future<Context> future = executor.submit( new LoadRootDescriptor( context ) );
				futures.add( future );
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
				for( ReleaseContext releaseContext : context.getReleaseContexts() ) {
					if( !releaseContext.isValid() ) continue;
					String name = releaseContext.getPom().getValue( "project/name" );
					Version version = releaseContext.getVersion();
					String link = releaseContext.getPath() + "." + extension;
					String md5Link = releaseContext.getPath() + "." + extension + ".md5";
					String sha1Link = releaseContext.getPath() + "." + extension + ".sha1";
					downloads.add( new MavenDownload( name, version, link, md5Link, sha1Link ) );
				}
			}

			// Reverse sort the versions.
			Collections.sort( downloads );
			Collections.reverse( downloads );

			return downloads;
		} finally {
			executor.shutdown();
		}
	}

	private static final void waitFor( List<Future<Context>> futures ) {
		for( Future<Context> future : futures ) {
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
			versions.add( Version.parse( versionString ) );
		}

		for( Version version : versions ) {
			contexts.add( new ReleaseContext( uri + "/" + version.getFullVersion(), artifact, version ) );
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
				return base + "/" + artifact + "-" + version.getFullVersion().replace( "SNAPSHOT", unique );
			} else {
				return base + "/" + artifact + "-" + version.getFullVersion();
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
				pomPath = releaseContext.getBase() + "/" + releaseContext.getArtifact() + "-" + releaseContext.getVersion().getFullVersion() + ".pom";
			}

			releaseContext.setPom( new Descriptor( pomPath ) );

			return context;
		}

	}

}
