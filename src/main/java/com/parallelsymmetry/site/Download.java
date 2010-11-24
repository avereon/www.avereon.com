package com.parallelsymmetry.site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.parallelsymmetry.util.Descriptor;

public class Download {

	private String name;

	private String link;

	private String version;

	public Download( String name, String link, String version ) {
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

	public String getVersion() {
		return version;
	}

	public static final List<Download> getVersions( String uri ) {
		return getVersions( uri, "jar" );
	}

	public static final List<Download> getVersions( String uri, String extension ) {
		List<Download> links = new ArrayList<Download>();

		try {
			Descriptor metadata = new Descriptor( uri + "/maven-metadata.xml" );

			String artifact = metadata.getValue( "metadata/artifactId" );
			String[] versions = metadata.getValues( "metadata/versioning/versions/version" );

			for( String version : versions ) {
				if( version.contains( "SNAPSHOT" ) ) {
					// TODO Handle snapshots.
				} else {
					Descriptor pom = new Descriptor( uri + "/" + version + "/" + artifact + "-" + version + ".pom" );

					String name = pom.getValue( "project/name" );
					if( name == null ) name = artifact;

					String link = uri + "/" + version + "/" + artifact + "-" + version + "." + extension;

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

}
