package com.xeomar.www.download;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.List;
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
@RestController
@RequestMapping( "/download" )
public class V2DownloadController {

	private Map<String, V2DownloadProvider> providers;

	V2DownloadController() {
		providers.put( "stable", new V2LocalDownloadProvider( "/opt/xeo/store/stable" ) );
		providers.put( "latest", new V2LocalDownloadProvider( "/opt/xeo/store/latest" ) );
	}

	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, path = "/{channel}/v2/metadata/{artifact}/{asset}/{format}" )
	public ResponseEntity<List<V2Download>> getMetadata( String channel, String artifact, String asset, String format ) {
		return new ResponseEntity<>( doGetMetadata( channel, artifact, asset, format ), HttpStatus.OK );
	}

	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, path = "/metadata/{artifact}/{asset}/{format}" )
	public ResponseEntity<List<V2Download>> getMetadata( String artifact, String asset, String format ) {
		return new ResponseEntity<>( List.of(), HttpStatus.OK );
	}

	@RequestMapping( method = RequestMethod.GET, path = "/download/{artifact}/{asset}/{format}" )
	public void getDownload( String channel, String artifact, String asset, String format ) {
		// TODO Return a stream in the HttpResponse
	}

	private List<V2Download> doGetMetadata( String channel, String artifact, String asset, String format ) {
		return List.of();
	}

	private void doGetDownload( HttpResponse response, String channel, String artifact, String asset, String format ) {

	}

}
