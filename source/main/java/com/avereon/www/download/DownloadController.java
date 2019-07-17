package com.avereon.www.download;

import com.avereon.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class has two responsibilities:
 * <ol>
 * <li>Handle the download requests and return the artifact as a stream</li>
 * <li>Handle requests about available downloads and returning their metadata</li>
 * </ol>
 * <p>
 * This class handles the simple download requests and
 * maps them to the location of the actual artifacts, wherever they may
 * be.
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
 * -> repo/avn/com/avereon/xenon/<version>/xenon-<version>-catalog-prod.card
 * <p>
 * /download/catalog/xenon/snap - Download the snap catalog card file
 * -> repo/avn/com/avereon/xenon/<version>/xenon-<version>-catalog-snap.card
 * <p>
 * /download/product/xenon/card - Download the product card file
 * -> repo/avn/com/avereon/xenon/<version>/xenon-<version>-product.card
 * <p>
 * /download/product/xenon/pack - Download the product pack file
 * -> repo/avn/com/avereon/xenon/<version>/xenon-<version>-product.pack
 */

@Deprecated
@RestController
public class DownloadController {

	private static final Logger log = LogUtil.get( MethodHandles.lookup().lookupClass() );

	private static final int IO_BUFFER_SIZE = 1024 * 1024;

	private static final int CONNECT_TIMEOUT = 1000;

	private static final int CONNECT_RETRY = 5;

	private static final int READ_TIMEOUT = 500;

	private DownloadProvider downloadProvider;

	private Map<String, V2DownloadProvider> v2providers = new ConcurrentHashMap<>();

	public DownloadController() {
		// TODO Create a composite download provider
		//providers.put( "latest", new LocalStoreDownloadProvider( "/opt/avn/repo/latest") );
		//providers.put( "stable", new LocalStoreDownloadProvider( "/opt/avn/repo/stable") );
	}

	@Autowired
	public void setDownloadProvider( DownloadProvider provider ) {
		this.downloadProvider = provider;
	}

	@Deprecated
	@SuppressWarnings( "unused" )
	@RequestMapping( method = { RequestMethod.GET, RequestMethod.POST }, value = "/extirpate/{artifact}" )
	public String clearCache( @PathVariable( "artifact" ) String artifact ) throws IOException {
		return downloadProvider.clearCache( artifact, null, null, null, null );
	}

	@SuppressWarnings( "unused" )
	@RequestMapping( method = { RequestMethod.GET, RequestMethod.POST }, value = "/extirpate" )
	public String clearCache( @RequestParam( value = "artifact" ) String artifact, @RequestParam( value = "platform", required = false ) String platform, @RequestParam( value = "channel", required = false ) String channel, @RequestParam( value = "category", required = false ) String category, @RequestParam( value = "type", required = false ) String type ) throws IOException {
		return downloadProvider.clearCache( artifact, category, type, channel, platform );
	}

	@Deprecated
	@RequestMapping( method = RequestMethod.GET, value = "/download/{artifact}/{category}/{type}/{version:.+}" )
	public void v0DownloadArtifact( HttpServletRequest request, HttpServletResponse response, @PathVariable( "artifact" ) String artifact, @PathVariable( "category" ) String category, @PathVariable( "type" ) String type, @PathVariable( "version" ) String version ) throws IOException {
		v0DownloadArtifact( request, response, artifact, null, version, category, type );
	}

	@Deprecated
	@RequestMapping( method = RequestMethod.GET, value = "/download" )
	private void v0DownloadArtifact( HttpServletRequest request, HttpServletResponse response, @RequestParam( value = "artifact" ) String artifact, @RequestParam( value = "platform", required = false ) String platform, @RequestParam( value = "channel", required = false, defaultValue = "stable" ) String channel, @RequestParam( value = "category", required = false, defaultValue = "product" ) String category, @RequestParam( value = "type", required = false, defaultValue = "pack" ) String type ) throws IOException {
		v1DownloadArtifact( request, response, artifact, platform, channel, category, type );
	}

	@Deprecated
	@RequestMapping( method = RequestMethod.GET, value = "/download/v1/{artifact}/{category}/{type}/{version:.+}" )
	public void v1DownloadArtifact( HttpServletRequest request, HttpServletResponse response, @PathVariable( "artifact" ) String artifact, @PathVariable( "category" ) String category, @PathVariable( "type" ) String type, @PathVariable( "version" ) String channel ) throws IOException {
		v1DownloadArtifact( request, response, artifact, null, channel, category, type );
	}

	/**
	 * @param request The HTTP request object
	 * @param response The HTTP response object
	 * @param artifact The artifact id
	 * @param platform The platform (e.g. linux, macos, windows, etc.)
	 * @param category The category (e.g. catalog, product, etc.)
	 * @param type The artifact type (e.g. card, pack, jar, etc.)
	 * @param channel The artifact channel (e.g. stable, beta, nightly, latest, etc.)
	 * @throws IOException If an IO error occurs
	 */
	@Deprecated
	@SuppressWarnings( "unused" )
	@RequestMapping( method = RequestMethod.GET, value = "/download/v1" )
	public void v1DownloadArtifact( HttpServletRequest request, HttpServletResponse response, @RequestParam( value = "artifact" ) String artifact, @RequestParam( value = "platform", required = false ) String platform, @RequestParam( value = "channel", required = false, defaultValue = "stable" ) String channel, @RequestParam( value = "category", required = false, defaultValue = "product" ) String category, @RequestParam( value = "type", required = false, defaultValue = "pack" ) String type ) throws IOException {
		log.info( "Requested: " + Download.key( artifact, category, type, channel, platform ) );

		channel = normalizeChannel( channel );

		System.out.println( "Provider class: " + downloadProvider.getClass().toString() );
		List<ProductDownload> downloads = downloadProvider.getDownloads( List.of( artifact ), category, type, channel, platform );
		if( downloads.size() == 0 ) throw new FileNotFoundException( "Download not found: " + Download.key( artifact, category, type, channel, platform ) );

		ProductDownload download = downloads.get( 0 );
		String link = download == null ? null : download.getLink();
		if( link == null ) {
			response.getOutputStream().close();
		} else {
			log.info( "Return stream: " + link );
			stream( response, new URL( link ), Download.name( download ) );
		}
	}

	@RequestMapping( method = RequestMethod.GET, value = "/download/v2/{artifact}/{asset}/{format}" )
	public void v2DownloadArtifact( HttpServletRequest request, HttpServletResponse response, @PathVariable( "artifact" ) String artifact, @PathVariable( "asset" ) String asset, @PathVariable( "format" ) String format ) throws IOException {
		v2DownloadArtifact( request, response, artifact, "", asset, format );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/download/v2/{artifact}/{platform}/{asset}/{format}" )
	public void v2DownloadArtifact( HttpServletRequest request, HttpServletResponse response, @PathVariable( "artifact" ) String artifact, @PathVariable( "platform" ) String platform, @PathVariable( "asset" ) String asset, @PathVariable( "format" ) String format ) throws IOException {
		//v2DownloadArtifact( request, response, artifact, platform, asset, format );
	}

	@Deprecated
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
