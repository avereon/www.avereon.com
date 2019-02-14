package com.xeomar.web.root;

import com.xeomar.util.TextUtil;

public interface Download {

	String getArtifact();

	String getVersion();

	// FIXME Rename to getCategory
	String getClassifier();

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

}
