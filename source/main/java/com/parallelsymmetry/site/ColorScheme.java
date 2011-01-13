package com.parallelsymmetry.site;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.parallelsymmetry.util.Descriptor;

public class ColorScheme {

	private Descriptor descriptor;

	public ColorScheme( String uri ) throws SAXException, IOException, ParserConfigurationException {
		descriptor = new Descriptor( uri );
	}

	public String getPrimary( int index ) {
		return descriptor.getValue( "/palette/colorset[@id='primary']/color[@id='primary-" + index + "']/@rgb" );
	}

	public String getSecondaryA( int index ) {
		return descriptor.getValue( "/palette/colorset[@id='secondary-a']/color[@id='secondary-a-" + index + "']/@rgb" );
	}

	public String getSecondaryB( int index ) {
		String value = descriptor.getValue( "/palette/colorset[@id='secondary-b']/color[@id='secondary-b-" + index + "']/@rgb" );
		return value == null ? getSecondaryA( index ) : value;
	}

	public String getComplement( int index ) {
		String value = descriptor.getValue( "/palette/colorset[@id='complement']/color[@id='complement-" + index + "']/@rgb" );
		return value == null ? getPrimary( index ) : value;
	}

}
