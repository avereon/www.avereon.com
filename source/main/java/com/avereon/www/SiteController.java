package com.avereon.www;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

//@Controller
public class SiteController {

	private final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );

	@RequestMapping( "/" )
	public InputStream index() {
		log.warn( "Getting index page" );
		//return getClass().getClassLoader().getResourceAsStream( "/avnweb/index.html" );
		return new ByteArrayInputStream( "Index page".getBytes( StandardCharsets.UTF_8 ) );
	}

//		@RequestMapping( "/{path}" )
//	public InputStream index( @PathVariable String path ) {
//		log.warn( "Getting " + path );
//		return getClass().getResourceAsStream( "/avnweb/" + path );
//	}

}
