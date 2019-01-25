package com.xeomar.web.root;

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
 * <ul>
 * <li>All requests start with the prefix /download</li>
 * <li>The second parameter determines the type of download
 * <ul>
 * <li>catalog - A product catalog for a program</li>
 * <li>product - A product artifact like a jar file</li>
 * </ul>
 * </li>
 * <li>The third parameter determines the artifact name</li>
 * <li>The fourth parameter is artifact dependent
 * <ul>
 * <li>For catalogs, this parameter defines the distribution channel</li>
 * <li>For products, this parameter defines the file type to return</li>
 * </ul></li>
 * </ul>
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

	private static final String REPO = "https://repo.xeomar.com/xeo/";

	private static final String GROUP = "com/xeomar/";

	private static final int IO_BUFFER_SIZE = 256 * 1024;

	private static final int CONNECT_TIMEOUT = 1000;

	private static final int CONNECT_RETRY = 5;

	private static final int READ_TIMEOUT = 500;

	private MavenDownloadFactory mavenDownloadFactory;

	public DownloadController( MavenDownloadFactory factory ) {
		this.mavenDownloadFactory = factory;
	}

	@SuppressWarnings( "unused" )
	@RequestMapping( "/download" )
	public String download( HttpServletRequest request, HttpServletResponse response ) {
		return "<h1>Xeomar Download Page</h1>";
	}

	@SuppressWarnings( "unused" )
	@RequestMapping( method = { RequestMethod.GET, RequestMethod.POST }, value = "/extirpate/{artifact}" )
	public String clearCache( @PathVariable( "artifact" ) String artifact ) throws IOException {
		return mavenDownloadFactory.clearCache( createUri( artifact ), null, null, null );
	}

	@SuppressWarnings( "unused" )
	@RequestMapping( method = { RequestMethod.GET, RequestMethod.POST }, value = "/extirpate/{artifact}/{category}" )
	public String clearCache( @PathVariable( "artifact" ) String artifact, @PathVariable( "category" ) String category ) throws IOException {
		return mavenDownloadFactory.clearCache( createUri( artifact ), category, null, null );
	}

	@SuppressWarnings( "unused" )
	@RequestMapping( method = { RequestMethod.GET, RequestMethod.POST }, value = "/extirpate/{artifact}/{category}/{type}" )
	public String clearCache( @PathVariable( "artifact" ) String artifact, @PathVariable( "category" ) String category, @PathVariable( "type" ) String type ) throws IOException {
		return mavenDownloadFactory.clearCache( createUri( artifact ), category, type, null );
	}

	@SuppressWarnings( "unused" )
	@RequestMapping( method = { RequestMethod.GET, RequestMethod.POST }, value = "/extirpate/{artifact}/{category}/{type}/{version:.+}" )
	public String clearCache( @PathVariable( "artifact" ) String artifact, @PathVariable( "category" ) String category, @PathVariable( "type" ) String type, @PathVariable( "version" ) String version ) throws IOException {
		return mavenDownloadFactory.clearCache( createUri( artifact ), category, type, version );
	}

	/**
	 * Examples:
	 * <ul>
	 * <li>http://xeomar.com/download/xenon/catalog/card/latest</li>
	 * <li>http://xeomar.com/download/xenon/product/card/latest</li>
	 * <li>http://xeomar.com/download/xenon/product/pack/release</li>
	 * <li>http://xeomar.com/download/xenon/product/card/snapshot</li>
	 * </ul>
	 *
	 * @param request The HTTP request object
	 * @param response The HTTP response object
	 * @param artifact The artifact id
	 * @param category The category (e.g. catalog, product, etc.)
	 * @param type The artifact type (e.g. card, pack, jar, etc.)
	 * @param version The artifact version (e.g. latest, release, snapshot, 2.0, etc.)
	 * @throws IOException If an IO error occurs
	 */
	@SuppressWarnings( "unused" )
	@RequestMapping( method = RequestMethod.GET, value = "/download/{artifact}/{category}/{type}/{version:.+}" )
	private void downloadArtifact( HttpServletRequest request, HttpServletResponse response, @PathVariable( "artifact" ) String artifact, @PathVariable( "category" ) String category, @PathVariable( "type" ) String type, @PathVariable( "version" ) String version ) throws IOException {
		log.info( "Requested: " + artifact + "-" + category + "-" + type + "-" + version );

		List<MavenDownload> downloads = mavenDownloadFactory.getDownloads( createUri( artifact ), category, type, version );
		if( downloads.size() == 0 ) throw new FileNotFoundException( "Now downloads found: " + artifact + "-" + category + "-" + type + "-" + version );

		MavenDownload download = downloads.get( 0 );
		String link = download == null ? null : download.getLink();
		if( link == null ) {
			response.getOutputStream().close();
		} else {
			log.info( "Returned: " + link );
			try {
				stream( response, new URL( link ), artifact + "-" + category + "." + type );
			} catch( FileNotFoundException exception ) {
				throw new FileNotFoundException( request.getRequestURI() );
			}
		}
	}

	private String createUri( String artifact ) {
		return REPO + GROUP + artifact;
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

		while( attempt < CONNECT_RETRY && input == null ) {
			// Increment the attempt count.
			attempt++;

			// Establish the connection.
			connection = source.openConnection();
			connection.setConnectTimeout( CONNECT_TIMEOUT );
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
		byte[] buffer = new byte[ IO_BUFFER_SIZE ];
		OutputStream output = response.getOutputStream();
		int count;
		while( (count = input.read( buffer )) >= 0 ) {
			output.write( buffer, 0, count );
		}
		output.close();
		input.close();
	}

}
