package com.parallelsymmetry.site;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StyleSheet extends HttpServlet {

	private static final long serialVersionUID = -7395157863360880610L;

	private static final String TAG_PREFIX = "value(";

	private static final String TAG_SUFFIX = ")";

	private Map<String, String> replacements;

	private String content;

	public synchronized void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		if( content == null ) {
			loadReplacements();
			content = process( getServletContext(), loadResource( request.getServletPath() ) );
		}

		response.setContentType( "text/css" );
		response.getWriter().println( content );
	}

	private void loadReplacements() throws IOException {
		ColorScheme scheme = null;
		try {
			scheme = new ColorScheme( getServletContext().getResource( "/colorscheme.xml" ).toURI().toString() );
		} catch( Exception exception ) {
			throw new IOException( exception );
		}

		replacements = new ConcurrentHashMap<String, String>();
		replacements.put( "color-B", "#000000" );
		replacements.put( "color-W", "#FFFFFF" );

		for( int index = 1; index <= 5; index++ ) {
			replacements.put( "color-P" + index, "#" + scheme.getPrimary( index ) );
			replacements.put( "color-Sa" + index, "#" + scheme.getSecondaryA( index ) );
			replacements.put( "color-Sb" + index, "#" + scheme.getSecondaryB( index ) );
			replacements.put( "color-C" + index, "#" + scheme.getComplement( index ) );
		}
	}

	private String loadResource( String path ) throws IOException {
		Reader reader = new InputStreamReader( getServletContext().getResourceAsStream( path ), "UTF-8" );
		Writer writer = new StringWriter();

		int read = 0;
		char[] data = new char[1024];
		while( ( read = reader.read( data ) ) > -1 ) {
			writer.write( data, 0, read );
		}

		return writer.toString();
	}

	private String process( ServletContext context, String content ) {
		int index = 0;
		while( ( index = content.indexOf( TAG_PREFIX, index ) ) > -1 ) {
			int suffixIndex = content.indexOf( TAG_SUFFIX, index + TAG_PREFIX.length() );

			String key = content.substring( index + TAG_PREFIX.length(), suffixIndex );

			StringBuilder builder = new StringBuilder( content.substring( 0, index ) );
			builder.append( getReplacement( key ) );
			builder.append( content.substring( suffixIndex + TAG_SUFFIX.length() ) );
			content = builder.toString();
		}
		return content;
	}

	private String getReplacement( String key ) {
		String replacement = replacements.get( key );
		return replacement == null ? "" : replacement;
	}

}
