package com.avereon.www.download;

import com.avereon.util.TextUtil;

import java.io.InputStream;
import java.util.Map;

public class V2Download {

	private String key;

	private String group;

	private String artifact;

	private String platform;

	private String asset;

	private String format;

	private String name;

	private String version;

	private String localFilename;

	private String responseFilename;

	private long size;

	private InputStream inputStream;

	public V2Download() {}

	public V2Download( String asset, String format, Map<String, String> query ) {
		this.key = key( "null", "null", asset, format, query );
		this.localFilename = localFilename( null, null, asset, format, query );
		this.responseFilename = responseFilename( null, null, asset, format, query );
		this.asset = asset;
		this.format = format;
	}

	public V2Download( String artifact, String platform, String asset, String format, Map<String, String> query ) {
		this.key = key( artifact, platform, asset, format, query );
		this.localFilename = localFilename( artifact, platform, asset, format, query );
		this.responseFilename = responseFilename( artifact, platform, asset, format, query );
		this.artifact = artifact;
		this.platform = platform;
		this.asset = asset;
		this.format = format;
	}

	public String getKey() {
		return key;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup( String group ) {
		this.group = group;
	}

	public String getArtifact() {
		return artifact;
	}

	public void setArtifact( String artifact ) {
		this.artifact = artifact;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform( String platform ) {
		this.platform = platform;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset( String asset ) {
		this.asset = asset;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat( String format ) {
		this.format = format;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion( String version ) {
		this.version = version;
	}

	public String getLocalFilename() {
		return localFilename;
	}

	public void setLocalFilename( String localFilename ) {
		this.localFilename = localFilename;
	}

	public String getResponseFilename() {
		return responseFilename;
	}

	public void setResponseFilename( String responseFilename ) {
		this.responseFilename = responseFilename;
	}

	public long getSize() {
		return size;
	}

	public void setSize( long size ) {
		this.size = size;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream( InputStream inputStream ) {
		this.inputStream = inputStream;
	}

	static String key( String artifact, String platform, String asset, String format, Map<String, String> query ) {
		String theme = query.get( "theme" );
		StringBuilder builder = new StringBuilder();
		builder.append( artifact );
		if( !TextUtil.isEmpty( platform ) ) builder.append( "-" ).append( platform );
		builder.append( "-" ).append( asset );
		if( theme != null ) builder.append( "-" ).append( theme );
		builder.append( "-" ).append( format );
		return builder.toString();
	}

	static String localFilename( String artifact, String platform, String asset, String format, Map<String, String> query ) {
		String theme = V2Download.removeSpecial( query.get( "theme" ) );
		StringBuilder name = new StringBuilder();
		if( asset != null ) {
			name.append( asset );
			if( theme != null ) name.append( "-" ).append( theme );
		}
		name.append( "." ).append( resolveFormat( format ) );
		return name.toString();
	}

	static String responseFilename( String artifact, String platform, String asset, String format, Map<String, String> query ) {
		String theme = query.get( "theme" );
		StringBuilder name = new StringBuilder();
		if( artifact != null ) name.append( artifact ).append( "-" );
		if( asset != null ) {
			name.append( asset );
			if( theme != null ) name.append( "-" ).append( theme );
		}
		if( platform != null ) name.append( "-" ).append( platform );
		name.append( "." ).append( resolveFormat( format ) );
		return name.toString();
	}

	static String resolveFormat( String format ) {
		if( format == null ) return null;

        return switch (format) {
            case "pack" -> "jar";
            case "icon" -> "png";
            default -> format;
        };
    }

	static String resolveContentType( String format ) {
		if( format == null ) return null;

        return switch (format) {
            case "card" -> "application/json";
            case "pack" -> "application/java-archive";
            default -> "application/octet-stream";
        };
    }

	static String removeSpecial( String string ) {
		if( string == null ) return null;
		return string.replaceAll( "[^a-zA-Z0-9-. ]", "" );
	}

}
