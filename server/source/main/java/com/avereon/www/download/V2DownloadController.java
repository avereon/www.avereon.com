package com.avereon.www.download;

import com.avereon.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all requests for artifacts for all repos and providers.
 * <p>
 * This class has three responsibilities:
 * <ol>
 * <li>Handle requests for available downloads and return their metadata</li>
 * <li>Handle requests for specific artifacts and return the stream</li>
 * <li>Handle requests for an artifact with general parameters and return the stream</li>
 * </ol>
 */
@CrossOrigin
@RestController
@RequestMapping( value = { "/download/{channel}", "/download/{channel}/v2" } )
public class V2DownloadController {

	private static final System.Logger log = Log.get();

	private V2DownloadProviderFactory factory;

	V2DownloadController( V2DownloadProviderFactory factory ) {
		this.factory = factory;
	}

	@RequestMapping( method = RequestMethod.GET, path = "/catalog" )
	public void getCatalog( HttpServletResponse response, @PathVariable( "channel" ) String channel, @RequestParam Map<String,String> query ) throws IOException {
		HttpStatus status = doGetCatalog( response, channel, query );
		response.setStatus( status.value() );
	}

	@RequestMapping( method = RequestMethod.GET, path = "/cards/{artifact}" )
	public Map<String, Object> getProductCards( @PathVariable String artifact, @RequestParam Map<String,String> query ) {
		Map<String, Object> cards = new HashMap<>();

		for( String key : factory.getProviders().keySet() ) {
			V2DownloadProvider provider = factory.getProviders().get( key );
			cards.put( key, getProductCards( provider, artifact, query ) );
		}

		return cards;
	}

	@RequestMapping( method = RequestMethod.HEAD, path = "/{artifact}/{asset}/{format}" )
	public void getMetadata(
		HttpServletResponse response,
		@PathVariable( "channel" ) String channel,
		@PathVariable( "artifact" ) String artifact,
		@PathVariable( "asset" ) String asset,
		@PathVariable( "format" ) String format,
		@RequestParam Map<String,String> query
	) throws IOException {
		getMetadata( response, channel, artifact, null, asset, format, query );
	}

	@RequestMapping( method = RequestMethod.HEAD, path = "/{artifact}/{platform}/{asset}/{format}" )
	public void getMetadata(
		HttpServletResponse response,
		@PathVariable( "channel" ) String channel,
		@PathVariable( "artifact" ) String artifact,
		@PathVariable( "platform" ) String platform,
		@PathVariable( "asset" ) String asset,
		@PathVariable( "format" ) String format,
		@RequestParam Map<String,String> query
	) throws IOException {
		HttpStatus status = doGetArtifact( RequestMethod.HEAD, response, channel, artifact, platform, asset, format,query );
		response.setStatus( status.value() );
	}

	@GetMapping( path = "/{artifact}/{asset}/{format}" )
	public void getArtifact(
		HttpServletResponse response,
		@PathVariable( "channel" ) String channel,
		@PathVariable( "artifact" ) String artifact,
		@PathVariable( "asset" ) String asset,
		@PathVariable( "format" ) String format,
		@RequestParam Map<String, String> query
	) throws IOException {
		getArtifact( response, channel, artifact, null, asset, format, query );
	}

	@GetMapping( path = "/{artifact}/{platform}/{asset}/{format}" )
	public void getArtifact(
		HttpServletResponse response,
		@PathVariable( "channel" ) String channel,
		@PathVariable( "artifact" ) String artifact,
		@PathVariable( "platform" ) String platform,
		@PathVariable( "asset" ) String asset,
		@PathVariable( "format" ) String format,
		@RequestParam Map<String, String> query
	) throws IOException {
		HttpStatus status = doGetArtifact( RequestMethod.GET, response, channel, artifact, platform, asset, format, query );
		response.setStatus( status.value() );
	}

	private HttpStatus doGetCatalog( HttpServletResponse response, String channel, Map<String,String> query ) throws IOException {
		V2DownloadProvider provider = factory.getProviders().get( channel );
		if( provider == null ) log.log( Log.WARN, "The download provider is null: " + channel );
		if( provider == null ) return HttpStatus.NOT_FOUND;

		V2Download download = provider.getCatalog(query);
		if( download == null ) log.log( Log.WARN, "The catalog is null" );
		if( download == null ) return HttpStatus.NOT_FOUND;

		response.setContentType( V2Download.resolveContentType( "card" ) );
		stream( response, download.getInputStream(), download.getResponseFilename(), download.getSize() );

		return HttpStatus.OK;
	}

	private Map<String, Object> getProductCards( V2DownloadProvider provider, String artifact, Map<String,String> query ) {
		Map<String, Object> cards = new HashMap<>();

		addCardToMap( cards, provider, artifact, "linux", query );
		addCardToMap( cards, provider, artifact, "macosx", query );
		addCardToMap( cards, provider, artifact, "windows", query );
		addCardToMap( cards, provider, artifact, null, query );

		return cards;
	}

	private void addCardToMap( Map<String, Object> map, V2DownloadProvider provider, String artifact, String platform, Map<String,String> query ) {
		try {
			V2Download download = provider.getDownload( artifact, platform, "product", "card", query );
			if( platform == null ) platform = "card";
			map.put( platform, download == null ? Map.of() : new ObjectMapper().readValue( download.getInputStream(), Map.class ) );
		} catch( Throwable throwable ) {
			throwable.printStackTrace( System.err );
			// Intentionally ignore exception
		}
	}

	private HttpStatus doGetArtifact(
		RequestMethod method, HttpServletResponse response, String channel, String artifact, String platform, String asset, String format, Map<String, String> query
	) throws IOException {
		V2DownloadProvider provider = factory.getProviders().get( channel );
		if( provider == null ) log.log( Log.WARN, "The download provider is null: " + channel );
		if( provider == null ) return HttpStatus.NOT_FOUND;

		V2Download download = provider.getDownload( artifact, platform, asset, format, query );
		if( download == null ) log.log( Log.WARN, "The download is null: " + V2Download.key( artifact, platform, asset, format, query ) );
		if( download == null ) return HttpStatus.NOT_FOUND;

		response.setContentType( V2Download.resolveContentType( format ) );
		addHeaders( download, response );

		if( method == RequestMethod.GET ) stream( response, download.getInputStream(), download.getResponseFilename(), download.getSize() );

		return HttpStatus.OK;
	}

	private void addHeaders( V2Download download, HttpServletResponse response ) {
		response.addHeader( "group", download.getGroup() );
		response.addHeader( "artifact", download.getArtifact() );
		if( download.getPlatform() != null ) response.addHeader( "platform", download.getPlatform() );
		response.addHeader( "asset", download.getAsset() );
		response.addHeader( "format", download.getFormat() );
		response.addHeader( "name", download.getName() );
		response.addHeader( "version", download.getVersion() );
	}

	/**
	 * @param response The HttpServletResponse object to use
	 * @param input The InputStream from which to get the data
	 * @param name The name to use for the resource
	 * @throws IOException If an IO error occurs
	 */
	private void stream( HttpServletResponse response, InputStream input, String name, long size ) throws IOException {
		response.setHeader( "Content-Disposition", "inline; filename=\"" + name + "\"" );
		response.setHeader( "Content-Length", String.valueOf( size ) );

		// Stream the data
		try( input; OutputStream output = response.getOutputStream() ) {
			input.transferTo( output );
		}
	}

}
