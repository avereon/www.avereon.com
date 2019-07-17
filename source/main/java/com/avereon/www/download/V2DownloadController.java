package com.avereon.www.download;

import com.avereon.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;

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
@RequestMapping( "/download/{channel}/v2" )
public class V2DownloadController {

	private static final Logger log = LogUtil.get( MethodHandles.lookup().lookupClass() );

	V2DownloadProviderFactory factory;

	V2DownloadController( V2DownloadProviderFactory factory ) {
		this.factory = factory;
	}

	// TODO Should there be a path to get artifact metadata from all providers?

	@GetMapping( path = "/catalog" )
	public void getCatalog( HttpServletResponse response, @PathVariable( "channel" ) String channel ) throws IOException {
		HttpStatus status = doGetCatalog( response, channel );
		response.setStatus( status.value() );
	}

	@RequestMapping( method = RequestMethod.HEAD, path = "/{artifact}/{asset}/{format}" )
	public void getMetadata(
			HttpServletResponse response, @PathVariable( "channel" ) String channel, @PathVariable( "artifact" ) String artifact, @PathVariable( "asset" ) String asset, @PathVariable( "format" ) String format
	) throws IOException {
		getMetadata( response, channel, artifact, null, asset, format );
	}

	@RequestMapping( method = RequestMethod.HEAD, path = "/{artifact}/{platform}/{asset}/{format}" )
	public void getMetadata(
			HttpServletResponse response,
			@PathVariable( "channel" ) String channel,
			@PathVariable( "artifact" ) String artifact,
			@PathVariable( "platform" ) String platform,
			@PathVariable( "asset" ) String asset,
			@PathVariable( "format" ) String format
	) throws IOException {
		HttpStatus status = doGetArtifact( RequestMethod.HEAD, response, channel, artifact, platform, asset, format );
		response.setStatus( status.value() );
	}

	@GetMapping( path = "/{artifact}/{asset}/{format}" )
	public void getArtifact(
			HttpServletResponse response, @PathVariable( "channel" ) String channel, @PathVariable( "artifact" ) String artifact, @PathVariable( "asset" ) String asset, @PathVariable( "format" ) String format
	) throws IOException {
		getArtifact( response, channel, artifact, null, asset, format );
	}

	@GetMapping( path = "/{artifact}/{platform}/{asset}/{format}" )
	public void getArtifact(
			HttpServletResponse response,
			@PathVariable( "channel" ) String channel,
			@PathVariable( "artifact" ) String artifact,
			@PathVariable( "platform" ) String platform,
			@PathVariable( "asset" ) String asset,
			@PathVariable( "format" ) String format
	) throws IOException {
		HttpStatus status = doGetArtifact( RequestMethod.GET, response, channel, artifact, platform, asset, format );
		response.setStatus( status.value() );
	}

	private HttpStatus doGetCatalog( HttpServletResponse response, String channel ) throws IOException {
		V2DownloadProvider provider = factory.getProviders().get( channel );
		if( provider == null ) log.warn( "The download provider is null: " + channel );
		if( provider == null ) return HttpStatus.NOT_FOUND;

		V2Download download = provider.getCatalog();
		if( download == null ) log.warn( "The catalog is null" );
		if( download == null ) return HttpStatus.NOT_FOUND;

		response.setContentType( V2Download.resolveContentType( "card" ) );
		stream( response, download.getInputStream(), download.getFilename(), download.getSize() );

		return HttpStatus.OK;
	}

	private HttpStatus doGetArtifact( RequestMethod method, HttpServletResponse response, String channel, String artifact, String platform, String asset, String format ) throws IOException {
		V2DownloadProvider provider = factory.getProviders().get( channel );
		if( provider == null ) log.warn( "The download provider is null: " + channel );
		if( provider == null ) return HttpStatus.NOT_FOUND;

		V2Download download = provider.getDownload( artifact, platform, asset, format );
		if( download == null ) log.warn( "The download is null: " + V2Download.key( artifact, platform, asset, format ) );
		if( download == null ) return HttpStatus.NOT_FOUND;

		response.setContentType( V2Download.resolveContentType( format ) );
		addHeaders( download, response );

		if( method == RequestMethod.GET ) stream( response, download.getInputStream(), download.getFilename(), download.getSize() );

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
