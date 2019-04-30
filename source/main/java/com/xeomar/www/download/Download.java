package com.xeomar.www.download;

import com.xeomar.util.TextUtil;

public interface Download {

	String getArtifact();

	String getVersion();

	String getCategory();

	String getType();

	static String key( String artifact, String category, String type, String channel, String platform ) {
		StringBuilder builder = new StringBuilder();
		builder.append( channel ).append( "/" );
		builder.append( artifact );
		builder.append( "-" ).append( category );
		if( !TextUtil.isEmpty( platform ) ) builder.append( "-" ).append( platform );
		builder.append( "." ).append( type );
		return builder.toString();
	}

	static String name( String artifact, String category, String platform, String type ) {
		StringBuilder name = new StringBuilder( artifact );
		name.append( "-" ).append( category );
		if( platform != null ) name.append( "-" ).append( platform );
		name.append( "." ).append( type );
		return name.toString();
	}

}
