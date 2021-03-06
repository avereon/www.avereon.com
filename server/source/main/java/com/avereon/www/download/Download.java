package com.avereon.www.download;

import com.avereon.util.TextUtil;

@Deprecated
public interface Download {

	String getArtifact();

	String getVersion();

	String getCategory();

	String getType();

	// FIXME I'm not sure channel should be in the download key for the repo
	// but might need to if hosting multiple repositories
	static String key( String artifact, String category, String type, String channel, String platform ) {
		StringBuilder builder = new StringBuilder();
		builder.append( channel ).append( "-" );
		builder.append( artifact );
		if( !TextUtil.isEmpty( platform ) ) builder.append( "-" ).append( platform );
		builder.append( "-" ).append( category );
		builder.append( "-" ).append( type );
		return builder.toString();
	}

	static String name( String artifact, String platform, String category, String type ) {
		StringBuilder name = new StringBuilder( artifact );
		name.append( "-" ).append( category );
		if( platform != null ) name.append( "-" ).append( platform );
		name.append( "." ).append( type );
		return name.toString();
	}

	static String name( ProductDownload download ) {
		return name( download.getArtifact(), download.getPlatform(), download. getCategory(), download.getType() );
	}

	static String type( String type ) {
		switch( type ) {
			case "pack": return "jar";
		}
		return type;
	}

}
