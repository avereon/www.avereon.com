package com.xeomar.www.download;

public abstract class AbstractDownloadProvider implements DownloadProvider {

	protected String getDownloadKey( String artifact, String category, String type, String channel, String platform ) {
		StringBuilder builder = new StringBuilder( channel );
		builder.append( "-" ).append( artifact );
		if( platform != null ) builder.append( "-" ).append( platform );
		if( category != null ) builder.append( "-" ).append( category );
		if( type != null ) builder.append( "-" ).append( type );

		return builder.toString();
	}

}
