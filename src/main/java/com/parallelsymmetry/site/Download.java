package com.parallelsymmetry.site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.parallelsymmetry.util.Descriptor;
import com.parallelsymmetry.util.Version;

public class Download implements Comparable<Download> {

	public static final int RELEASE = 1;

	public static final int SNAPSHOT = 2;

	public static final int BOTH = RELEASE & SNAPSHOT;

	//	private static final String RELEASE = "http://mvn.parallelsymmetry.com/release";
	//
	//	private static final String SNAPSHOT = "http://mvn.parallelsymmetry.com/snapshot";

	private String name;

	private String link;

	private Version version;

	public Download( String name, String link, Version version ) {
		this.name = name;
		this.link = link;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public String getLink() {
		return link;
	}

	public Version getVersion() {
		return version;
	}

	@Override
	public int compareTo( Download that ) {
		return this.getVersion().compareTo( that.getVersion() );
	}

	public static final List<Download> getDownloads( String uri ) {
		return getDownloads( uri, "jar", BOTH );
	}

	public static final List<Download> getDownloads( String uri, int mode ) {
		return getDownloads( uri, "jar", mode );
	}

	public static final List<Download> getDownloads( String uri, String extension, int mode ) {
		List<Download> links = new ArrayList<Download>();

		boolean release = ( mode & RELEASE ) > 0;
		boolean snapshot = ( mode & SNAPSHOT ) > 0;

		try {
			Descriptor metadata = new Descriptor( uri + "/maven-metadata.xml" );

			String artifact = metadata.getValue( "metadata/artifactId" );
			String[] versions = metadata.getValues( "metadata/versioning/versions/version" );

			for( String metaVersion : versions ) {
				Version version = Version.parse( metaVersion );

				links.add( new Download( "Test", ".", version ) );

				if( snapshot && version.isSnapshot() ) {
					Descriptor snapshotMetadata = new Descriptor( uri + "/" + metaVersion + "/maven-metadata.xml" );

					String qualifier = metaVersion;
					String timestamp = snapshotMetadata.getValue( "/metadata/versioning/snapshot/timestamp" );
					if( timestamp != null ) {
						String build = snapshotMetadata.getValue( "/metadata/versioning/snapshot/buildNumber" );
						qualifier = metaVersion + "-" + timestamp + "-" + build;
					}

					Descriptor pom = new Descriptor( uri + "/" + metaVersion + "/" + artifact + "-" + qualifier + ".pom" );

					String name = pom.getValue( "project/name" );
					String link = uri + "/" + metaVersion + "/" + artifact + "-" + qualifier + "." + extension;
					if( name == null ) name = artifact;

					links.add( new Download( name, link, version ) );
				}

				if( release && !version.isSnapshot() ) {
					Descriptor pom = new Descriptor( uri + "/" + metaVersion + "/" + artifact + "-" + metaVersion + ".pom" );

					String name = pom.getValue( "project/name" );
					String link = uri + "/" + metaVersion + "/" + artifact + "-" + metaVersion + "." + extension;
					if( name == null ) name = artifact;

					links.add( new Download( name, link, version ) );
				}
			}

		} catch( SAXException e ) {
			// Intentionally ignore exception.
		} catch( IOException e ) {
			// Intentionally ignore exception.
		} catch( ParserConfigurationException e ) {
			// Intentionally ignore exception.
		}

		return links;
	}

	public static final Download getLatestProductionDownload( String uri, String extension ) {
		List<Download> downloads = getDownloads( uri, extension, RELEASE );
		Collections.sort( downloads );
		Collections.reverse( downloads );
		return downloads.get( 0 );
	}

}
