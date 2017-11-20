package com.xeomar.web.root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@RestController
public class RootController {

	private static Logger log = LoggerFactory.getLogger( RootController.class );

	private static String REPO = "https://code.xeomar.com/repo/xeo/";

	private static final int CONNECT_TIMEOUT = 4500;

	private static final int CONNECT_RETRY = 3;

	private static final int READ_TIMEOUT = 1000;

	//	@RequestMapping( "/" )
	//	public String index() {
	//		return "Greetings from Spring Boot!";
	//	}

	@RequestMapping( "/download/**" )
	public void download( HttpServletRequest request, HttpServletResponse response ) {
		/*
		 * This method is responsible for taking the simple download requests and
		 * mapping them to the location of the actual artifacts, wherever they may
		 * be. In the past, mapping a request to the artifact was a combination
		 * of URL rewriting in the Apache web server and calling the download
		 * servlet to interpret a complex set of query parameters. This left the
		 * download servlet open to abuse if given the correct set of parameters.
		 *
		 * This code will take the download request and perform the mapping and
		 * hide the complexity of artifact resolution internally instead of
		 * interpreting the complex query parameters from before.
		 *
		 * Mapping Rules
		 * - All requests start with the prefix /download
		 * - The second parameter determines the type of download
		 *   - catalog - A product catalog for a program
		 *   - product - A product artifact like a jar file
		 * - The third parameter determines the product name
		 * - The fourth parameter is artifact dependent
		 *   - For catalogs, this parameter defines the distribution channel
		 *   - For products, this parameter defines the file type to return
		 *
		 * Media Types
		 * The resource to media type map:
		 *   Pack > application/zip
		 *   Card > text/yaml
		 *
		 * Examples
		 *   /download/catalog/xenon/prod - Download the prod catalog card file
		 *   -> repo/xeo/com/xeomar/xenon/<version>/xenon-<version>-catalog-prod.card
		 *
		 *   /download/catalog/xenon/snap - Download the snap catalog card file
		 *   -> repo/xeo/com/xeomar/xenon/<version>/xenon-<version>-catalog-snap.card
		 *
		 *   /download/product/xenon/card - Download the product card file
		 *   -> repo/xeo/com/xeomar/xenon/<version>/xenon-<version>-product.card
		 *
		 *   /download/product/xenon/pack - Download the product pack file
		 *   -> repo/xeo/com/xeomar/xenon/<version>/xenon-<version>-product.pack
		 */

		String[] parts = request.getServletPath().split( "/" );
		if( parts.length != 5 ) return;

		String type = parts[ 2 ];
		String product = parts[ 3 ];
		String meta = parts[ 4 ];

		log.info( "Requested: " + type + " > " + product + " > " + meta );

		//		log.info( "MVS servlet path -> " + request.getServletPath() );
		//
		//		String[] parts = request.getServletPath().split( "/" );
		//		for( String part : parts ) {
		//			log.info( "MVS part -> " + part );
		//		}
		//
		//		String artifact = parts[ 3 ];
		//
		//		StringBuilder builder = new StringBuilder();
		//		builder.append( "<html>" );
		//		try {
		//
		//			List<MavenDownload> downloads = MavenDownload.getDownloads( REPO + "com/xeomar/" + artifact, "card", "jar" );
		//			for( MavenDownload download : downloads ) {
		//				builder.append( "<p>" );
		//				builder.append( download.getLink() );
		//				builder.append( "</p>" );
		//			}
		//		} catch( Exception exception ) {
		//			log.error( "Error getting maven downloads", exception );
		//		}
		//		builder.append( "</html>" );

		//return builder.toString();
	}

	private void downloadCatalog( HttpServletResponse response, String product, String channel ) {

	}

	/**
	 *
	 * @param response The HttpServletResponse object to use
	 * @param source The URL from which to get the data
	 * @param name The name to use for the resource
	 * @throws IOException If an IO error occurs
	 */
	private void stream( HttpServletResponse response, URL source, String name ) throws IOException {
		int attempt = 0;
		InputStream input = null;
		URLConnection connection = null;

		int connectTimeout = CONNECT_TIMEOUT / CONNECT_RETRY;

		while( attempt < CONNECT_RETRY && input == null ) {
			// Increment the attempt count.
			attempt++;

			// Establish the connection.
			connection = source.openConnection();
			connection.setConnectTimeout( connectTimeout );
			connection.setReadTimeout( READ_TIMEOUT );

			// Obtain the input stream.
			input = connection.getInputStream();
		}

		if( input == null ) {
			response.sendError( HttpServletResponse.SC_NOT_FOUND );
			return;
		}

		// Forward the header fields.
		Map<String, List<String>> fields = connection.getHeaderFields();
		for( String key : fields.keySet() ) {
			if( key != null ) {
				for( String value : fields.get( key ) ) {
					response.addHeader( key, value );
				}
				if( "Content-Disposition".equals( key ) ) {
					response.setHeader( "Content-Disposition", "inline; filename=\"" + name + "\"" );
				}
			}
		}

		// Stream the data.
		byte[] buffer = new byte[ 4096 ];
		OutputStream output = response.getOutputStream();
		int count = 0;
		while( (count = input.read( buffer )) >= 0 ) {
			output.write( buffer, 0, count );
		}
		output.close();
		input.close();
	}

}
