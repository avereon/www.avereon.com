package com.avereon.www.download;

import com.avereon.util.FileUtil;

import java.text.DateFormat;
import java.util.Date;

@Deprecated
public class ProductDownload implements Download, Comparable<ProductDownload> {

	private String key;

	private String group;

	private String artifact;

	private String name;

	private String category;

	private String type;

	private String identifier;

	private String channel;

	private String platform;

	private String version;

	private String link;

	private String md5Link;

	private String sha1Link;

	private int length;

	private Date date;

	ProductDownload( String group, String artifact, String channel, String category, String type, String platform, String version, String name, String link, String md5Link, String sha1Link ) {
		this.key = key( artifact, category, type, channel, platform );
		this.group = group;
		this.artifact = artifact;
		this.channel = channel;
		this.category = category;
		this.type = type;
		this.platform = platform;
		this.version = version;

		this.name = name;
		this.link = link;
		this.md5Link = md5Link;
		this.sha1Link = sha1Link;

		identifier = String.format( "%s-%s-%s-%s-%s-%s", channel, group, artifact, category, type, platform );
	}

	public String getKey() {
		return key;
	}

	public String getGroup() {
		return group;
	}

	public String getArtifact() {
		return artifact;
	}

	public String getName() {
		return name;
	}

	public String getChannel() {
		return channel;
	}

	public String getCategory() {
		return category;
	}

	public String getType() {
		return type;
	}

	public String getPlatform() {
		return platform;
	}

	public String getVersion() {
		return version;
	}

	public String getLink() {
		return link;
	}

	public String getMd5Link() {
		return md5Link;
	}

	public String getSha1Link() {
		return sha1Link;
	}

	public int getLength() {
		return length;
	}

	public void setLength( int length ) {
		this.length = length;
	}

	public Date getDate() {
		return date;
	}

	public void setDate( Date date ) {
		this.date = date;
	}

	public String getHumanReadableLength() {
		return FileUtil.getHumanSizeBase2( length );
	}

	public String formatDate( DateFormat format, String unknownText ) {
		if( date == null ) {
			return unknownText;
		} else {
			return format.format( date );
		}
	}

	@Override
	public int compareTo( ProductDownload that ) {
		return this.getChannel().compareTo( that.getChannel() );
	}

	@Override
	public boolean equals( Object object ) {
		if( !(object instanceof ProductDownload) ) return false;

		ProductDownload that = (ProductDownload)object;

		return this.identifier.equals( that.identifier );
	}

}
