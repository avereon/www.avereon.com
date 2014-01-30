package com.parallelsymmetry.site;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Download extends HttpServlet {

	private static final long serialVersionUID = 549372381756415485L;

	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		String url = null;
		String source = request.getParameter( "source" );
		String maven = request.getParameter( "maven" );

		if( source != null ) {
			url = source;
		} else if( maven != null ) {
			String classifier = request.getParameter( "classifier" );
			String type = request.getParameter( "type" );
			List<MavenDownload> downloads;
			try {
				downloads = MavenDownload.getDownloads( maven, classifier, type  );
				if( downloads.size() > 0 ) url = downloads.get( 0 ).getLink();
			} catch( Exception e ) {
				getServletContext().log( null, e );
			}
		}

		if( url == null ) {
			response.sendError( HttpServletResponse.SC_BAD_REQUEST );
			return;
		}

		//stream( response, url );
		response.sendRedirect( url );
	}

	private void stream( HttpServletResponse response, String source ) throws MalformedURLException, IOException {
		// Establish the URL connection.
		URL url = new URL( source );
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout( 1000 );
		connection.setReadTimeout( 1000 );

		// Obtain the input stream.
		InputStream input = connection.getInputStream();
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
			}
		}

		// Stream the data.
		byte[] buffer = new byte[4096];
		OutputStream output = response.getOutputStream();
		int count = 0;
		while( ( count = input.read( buffer ) ) >= 0 ) {
			output.write( buffer, 0, count );
		}
		output.close();
		input.close();
	}

}
