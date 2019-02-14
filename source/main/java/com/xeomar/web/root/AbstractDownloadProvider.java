package com.xeomar.web.root;

public abstract class AbstractDownloadProvider implements DownloadProvider {

	protected String getDownloadKey( String artifact, String category, String type, String channel, String platform ) {
		StringBuilder builder = new StringBuilder( artifact );

		if( category != null ) builder.append( "-" ).append( category );
		if( type != null ) builder.append( "-" ).append( type );
		if( channel != null ) builder.append( "-" ).append( channel );
		if( platform != null ) builder.append( "-" ).append( platform );

		return builder.toString();
	}

}
