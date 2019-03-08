package com.xeomar.web.root;

import com.xeomar.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * This class has to responsibilities: 1. Handling the simple download requests
 * and returning the artifact as a stream. 2. Handling requests about what is
 * available and returning metadata about the downloads.
 * <p>
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

	private static final Logger log = LogUtil.get( MethodHandles.lookup().lookupClass() );

	private static final String REPO = "https://repo.xeomar.com/xeo/";

	private static final String GROUP = "com/xeomar/";

	private static final int IO_BUFFER_SIZE = 1024 * 1024;

	private static final int CONNECT_TIMEOUT = 1000;

	private static final int CONNECT_RETRY = 5;

	private static final int READ_TIMEOUT = 500;

	private DownloadProvider downloadProvider;

	public DownloadController() {
		this.downloadProvider = new LocalStoreDownloadProvider();
		//this.downloadProvider = new MavenDownloadProvider();
	}

	//	@SuppressWarnings( "unused" )
	//	@RequestMapping( "/download" )
	//	public String download( HttpServletRequest request, HttpServletResponse response ) {
	//		return "<h1>Xeomar Download Page</h1>";
	//	}

	@Deprecated
	@SuppressWarnings( "unused" )
	@RequestMapping( method = { RequestMethod.GET, RequestMethod.POST }, value = "/extirpate/{artifact}" )
	public String clearCache( @PathVariable( "artifact" ) String artifact ) throws IOException {
		return downloadProvider.clearCache( artifact, null, null, null, null );
	}

	@SuppressWarnings( "unused" )
	@RequestMapping( method = { RequestMethod.GET, RequestMethod.POST }, value = "/extirpate" )
	public String clearCache(
			@RequestParam( value = "artifact" ) String artifact,
			@RequestParam( value = "platform", required = false ) String platform,
			@RequestParam( value = "channel", required = false ) String channel,
			@RequestParam( value = "category", required = false ) String category,
			@RequestParam( value = "type", required = false ) String type
	) throws IOException {
		return downloadProvider.clearCache( artifact, category, type, channel, platform );
	}

	@Deprecated
	@SuppressWarnings( "unused" )
	@RequestMapping( method = RequestMethod.GET, value = "/download/{artifact}/{category}/{type}/{version:.+}" )
	private void downloadArtifact(
			HttpServletRequest request, HttpServletResponse response, @PathVariable( "artifact" ) String artifact, @PathVariable( "category" ) String category, @PathVariable( "type" ) String type, @PathVariable( "version" ) String version
	) throws IOException {
		downloadArtifact( request, response, artifact, null, version, category, type );
	}

	/**
	 * Examples:
	 * <ul>
	 * <li>http://xeomar.com/download/xenon?category=catalog&type=card&version=latest</li>
	 * <li>http://xeomar.com/download/xenon?category=product&type=card&version=latest</li>
	 * <li>http://xeomar.com/download/xenon?category=product&type=pack&version=release</li>
	 * <li>http://xeomar.com/download/xenon?category=product&type=card&version=snapshot</li>
	 * </ul>
	 *
	 * @param request The HTTP request object
	 * @param response The HTTP response object
	 * @param artifact The artifact id
	 * @param platform The platform (e.g. linux, mac, windows, etc.)
	 * @param category The category (e.g. catalog, product, etc.)
	 * @param type The artifact type (e.g. card, pack, jar, etc.)
	 * @param channel The artifact channel (e.g. stable, beta, nightly, latest, etc.)
	 * @throws IOException If an IO error occurs
	 */
	@SuppressWarnings( "unused" )
	@RequestMapping( method = RequestMethod.GET, value = "/download" )
	private void downloadArtifact(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam( value = "artifact" ) String artifact,
			@RequestParam( value = "platform", required = false ) String platform,
			@RequestParam( value = "channel", required = false, defaultValue = "stable" ) String channel,
			@RequestParam( value = "category", required = false, defaultValue = "product" ) String category,
			@RequestParam( value = "type", required = false, defaultValue = "pack" ) String type
	) throws IOException {
		log.info( "Requested: " + Download.key( artifact, category, type, channel, platform ) );

		if( "pack".equals( type ) ) type = "jar";
		channel = normalizeChannel( channel );

		List<ProductDownload> downloads = downloadProvider.getDownloads( artifact, category, type, channel, platform );
		if( downloads.size() == 0 ) throw new FileNotFoundException( "Download not found: " + Download.key( artifact, category, type, channel, platform ) );

		ProductDownload download = downloads.get( 0 );
		String link = download == null ? null : download.getLink();
		if( link == null ) {
			response.getOutputStream().close();
		} else {
			log.info( "Return stream: " + link );
			StringBuilder name = new StringBuilder( artifact );
			name.append( "-" ).append( category );
			if( platform != null ) name.append( "-" ).append( platform );
			name.append( "." ).append( type );
			stream( response, new URL( link ), name.toString() );
		}
	}

	private String normalizeChannel( String channel ) {
		switch( channel ) {
			case "release":
				return "stable";
			//case "beta":
			//	return "earlyaccess";
			case "nightly":
				return "latest";
		}
		return channel;
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
					log.info( "Header: " + key + "=" + value );
				}
				if( "Content-Disposition".equals( key ) ) response.setHeader( "Content-Disposition", "inline; filename=\"" + name + "\"" );
			}
		}

		if( "file".equals( source.getProtocol() ) ) {
			try {
				response.addHeader( "Content-Length", String.valueOf( Files.size( Paths.get( source.toURI() ) ) ) );
				response.setHeader( "Content-Disposition", "inline; filename=\"" + name + "\"" );
			} catch( URISyntaxException exception ) {
				throw new FileNotFoundException( source.toString() );
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
