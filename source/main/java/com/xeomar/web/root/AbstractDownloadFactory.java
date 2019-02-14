package com.xeomar.web.root;

public abstract class AbstractDownloadFactory implements DownloadFactory {

	protected String getDownloadKey( String artifact, String category, String type, String channel ) {
		StringBuilder builder = new StringBuilder( artifact );

		if( category != null ) {
			builder.append( "-" );
			builder.append( category );
		}

		if( type != null ) {
			builder.append( "-" );
			builder.append( type );
		}

		if( channel != null ) {
			builder.append( "-" );
			builder.append( channel );
		}

		return builder.toString();
	}

}
