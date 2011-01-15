package com.parallelsymmetry.site;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.parallelsymmetry.util.Descriptor;

public class ColorScheme {

	private Descriptor descriptor;

	private boolean flipSecondary;

	public ColorScheme( String uri ) throws SAXException, IOException, ParserConfigurationException {
		this( uri, false );
	}

	public ColorScheme( String uri, boolean flipSecondary ) throws SAXException, IOException, ParserConfigurationException {
		descriptor = new Descriptor( uri );
		this.flipSecondary = flipSecondary;
	}

	public String getPrimary( int index ) {
		return descriptor.getValue( "/palette/colorset[@id='primary']/color[@id='primary-" + index + "']/@rgb" );
	}

	public String getSecondaryA( int index ) {
		String path = null;
		if( flipSecondary ) {
			path = "/palette/colorset[@id='secondary-b']/color[@id='secondary-b-" + index + "']/@rgb";
		} else {
			path = "/palette/colorset[@id='secondary-a']/color[@id='secondary-a-" + index + "']/@rgb";
		}
		String value = descriptor.getValue( path );
		return value == null ? getPrimary( index ) : value;
	}

	public String getSecondaryB( int index ) {
		String path = null;
		if( flipSecondary ) {
			path = "/palette/colorset[@id='secondary-a']/color[@id='secondary-a-" + index + "']/@rgb";
		} else {
			path = "/palette/colorset[@id='secondary-b']/color[@id='secondary-b-" + index + "']/@rgb";
		}
		String value = descriptor.getValue( path );
		return value == null ? getSecondaryA( index ) : value;
	}

	public String getComplement( int index ) {
		String value = descriptor.getValue( "/palette/colorset[@id='complement']/color[@id='complement-" + index + "']/@rgb" );
		return value == null ? getSecondaryA( index ) : value;
	}

}
