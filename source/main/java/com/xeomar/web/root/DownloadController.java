package com.xeomar.web.root;

import com.xeomar.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for handling the simple download requests and
 * mapping them to the location of the actual artifacts, wherever they may
 * be. In the past, mapping a request to the artifact was a combination
 * of URL rewriting in the Apache web server and calling the download
 * servlet to interpret a complex set of query parameters. This left the
 * download servlet open to abuse if given the correct set of parameters.
 * <p>
 * This code will take the download request and perform the mapping and
 * hide the complexity of artifact resolution internally instead of
 * interpreting the complex query parameters from before.
 * <h2>Mapping Rules</h2>
 * - All requests start with the prefix /download
 * - The second parameter determines the type of download
 * - catalog - A product catalog for a program
 * - product - A product artifact like a jar file
 * - The third parameter determines the product name
 * - The fourth parameter is artifact dependent
 * - For catalogs, this parameter defines the distribution channel
 * - For products, this parameter defines the file type to return
 * <h2>Media Types</h2>
 * The resource to media type map:
 * Pack > application/zip
 * Card > text/yaml
 * <h2>Examples</h2>
 * /download/catalog/xenon/prod - Download the prod catalog card file
 * -> repo/xeo/com/xeomar/xenon/<version>/xenon-<version>-catalog-prod.card
 * <p>
 * /download/catalog/xenon/snap - Download the snap catalog card file
 * -> repo/xeo/com/xeomar/xenon/<version>/xenon-<version>-catalog-snap.card
 * <p>
 * /download/product/xenon/card - Download the product card file
 * -> repo/xeo/com/xeomar/xenon/<version>/xenon-<version>-product.card
 * <p>
 * /download/product/xenon/pack - Download the product pack file
 * -> repo/xeo/com/xeomar/xenon/<version>/xenon-<version>-product.pack
 */

@RestController
public class DownloadController {

	private static Logger log = LoggerFactory.getLogger( DownloadController.class );

	private static String REPO = "https://code.xeomar.com/repo/xeo/";

	private static String GROUP = "com/xeomar/";

	private static final int CONNECT_TIMEOUT = 4500;

	private static final int CONNECT_RETRY = 3;

	private static final int READ_TIMEOUT = 1000;

	@RequestMapping( "/download" )
	public String download( HttpServletRequest request, HttpServletResponse response ) {
		return "<h1>Xeomar Download Page</h1>";
	}

	@RequestMapping( method = RequestMethod.GET, value = "/download/catalog/{product}/{version:.+}" )
	private void downloadCatalog( HttpServletRequest request, HttpServletResponse response, @PathVariable( "product" ) String product, @PathVariable( "version" ) String version ) throws IOException {
		log.info( "Requested: catalog > " + product + " > " + version );

		String path = REPO + GROUP + product;
		MavenDownload download = getVersion( MavenDownload.getDownloads( path, "catalog", "card" ), version );
		String link = download == null ? null : download.getLink();
		if( link == null ) {
			response.getOutputStream().close();
		} else {
			log.info("Returned: " + link );
			try {
				stream( response, new URL( link ), product + "-catalog.card" );
			} catch( FileNotFoundException exception ) {
				throw new FileNotFoundException( request.getRequestURI() );
			}
		}
	}

	/**
	 * Get a Maven download from a list of downloads by matching on a version.
	 *
	 * @param downloads The list of downloads obtained from MavenDownload.getDownloads()
	 * @param version The version string to match. Can be "latest", "release", "snapshot" or a version number.
	 * @return The matching download
	 */
	private MavenDownload getVersion( List<MavenDownload> downloads, String version ) {
		// Version can be release, snapshot or a version number

		boolean latest = "latest".equalsIgnoreCase( version );
		boolean release = "release".equalsIgnoreCase( version );
		boolean snapshot = "snapshot".equalsIgnoreCase( version );

		for( MavenDownload download : downloads ) {
			Version downloadVersion = download.getVersion();

			if( latest ) return download;
			if( release && !downloadVersion.isSnapshot() ) return download;
			if( snapshot && downloadVersion.isSnapshot() ) return download;
			if( downloadVersion.toString().startsWith( version ) ) return download;
		}

		return null;
	}

	/**
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
		int count;
		while( (count = input.read( buffer )) >= 0 ) {
			output.write( buffer, 0, count );
		}
		output.close();
		input.close();
	}

}
