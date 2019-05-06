package com.xeomar.www.download;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class has three responsibilities:
 * <ol>
 * <li>Handle requests for available downloads and return their metadata</li>
 * <li>Handle requests for specific artifacts and return the stream</li>
 * <li>Handle requests for an artifact with general parameters and return the stream</li>
 * </ol>
 */
@RestController
@RequestMapping( "/repo/v2" )
public class V2DownloadController {

	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, path = "/metadata/{artifact}/{asset}/{format}" )
	public ResponseEntity<List<V2Download>> getMetadata(){
		return new ResponseEntity<List<V2Download>>(List.of(), HttpStatus.OK );
	}

	@RequestMapping( method = RequestMethod.GET, path = "/download/{artifact}/{asset}/{format}" )
	public void getDownload() {
		// TODO Return a stream in the HttpResponse
	}

}
