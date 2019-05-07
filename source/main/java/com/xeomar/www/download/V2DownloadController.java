package com.xeomar.www.download;

import com.xeomar.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
@RestController
@RequestMapping( "/download/{channel}/v2" )
public class V2DownloadController {

	private static final Logger log = LogUtil.get( MethodHandles.lookup().lookupClass() );

	//private Map<String, V2DownloadProvider> providers;
	V2DownloadProviderFactory factory;

	V2DownloadController( V2DownloadProviderFactory factory ) {
		this.factory = factory;
	}

	// TODO Should there be a path to get artifact metadata from all providers?

	@RequestMapping( method = RequestMethod.HEAD, path = "/{artifact}/{asset}/{format}" )
	public void getMetadata( HttpServletResponse response, @PathVariable( "channel" ) String channel, @PathVariable( "artifact" ) String artifact, @PathVariable( "asset" ) String asset, @PathVariable( "format" ) String format ) {
		getMetadata( response, channel, artifact, null, asset, format );
	}

	@RequestMapping( method = RequestMethod.HEAD, path = "/{artifact}/{platform}/{asset}/{format}" )
	public void getMetadata( HttpServletResponse response, @PathVariable( "channel" ) String channel, @PathVariable( "artifact" ) String artifact, @PathVariable( "platform" ) String platform, @PathVariable( "asset" ) String asset, @PathVariable( "format" ) String format ) {
		HttpStatus status = doGetArtifact( response, channel, artifact, platform, asset, format );
		response.setStatus( status.value() );
	}

	//	@RequestMapping( method = RequestMethod.GET, path = "/download/{artifact}/{asset}/{format}" )
	//	public void getDownload( String channel, String artifact, String asset, String format ) {
	//		// TODO Return a stream in the HttpResponse
	//	}

	//	private List<V2Download> doGetMetadata( String channel, String artifact, String platform, String asset, String format ) {
	//		V2DownloadProvider provider = providers.get( channel );
	//		if( provider == null ) return List.of();
	//		return List.of( provider.getDownload( artifact, platform, asset, format ) );
	//	}

	//	private V2Download doGetMetadata( HttpServletResponse response, String channel, String artifact, String platform, String asset, String format ) {
	//		// TODO Return only header values
	//		V2DownloadProvider provider = providers.get( channel );
	//		if( provider == null ) return null;
	//		return provider.getDownload( artifact, platform, asset, format );
	//	}

	private HttpStatus doGetArtifact( HttpServletResponse response, String channel, String artifact, String platform, String asset, String format ) {
		V2DownloadProvider provider = factory.getProviders().get( channel );
		if( provider == null ) System.err.println( "The download provider is null: " + channel );
		if( provider == null ) return HttpStatus.NOT_FOUND;

		V2Download download = provider.getDownload( artifact, platform, asset, format );
		if( download == null ) System.err.println( "The download is null: " + artifact );
		if( download == null ) return HttpStatus.NOT_FOUND;

		addHeaders( download, response );

		return HttpStatus.OK;
	}

	private void doGetDownload( HttpServletResponse response, String channel, String artifact, String asset, String format ) {
		V2DownloadProvider provider = factory.getProviders().get( channel );
		if( provider == null ) return;

		// TODO Return a stream in the HttpResponse
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

}
