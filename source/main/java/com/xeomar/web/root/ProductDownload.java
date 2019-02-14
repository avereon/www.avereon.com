package com.xeomar.web.root;

import com.xeomar.util.FileUtil;
import com.xeomar.util.Version;

import java.text.DateFormat;
import java.util.Date;

public class ProductDownload implements Comparable<ProductDownload> {

	private String key;

	private String groupId;

	private String artifactId;

	private String name;

	private String classifier;

	private String type;

	private String identifier;

	private Version version;

	private String link;

	private String md5Link;

	private String sha1Link;

	private int length;

	private Date date;

	ProductDownload( String key, String groupId, String artifactId, Version version, String classifier, String type, String name, String link, String md5Link, String sha1Link ) {
		this.key = key;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.classifier = classifier;
		this.type = type;

		this.name = name;
		this.link = link;
		this.md5Link = md5Link;
		this.sha1Link = sha1Link;

		identifier = String.format( "%s-%s-%s-%s-%s", groupId, artifactId, version, classifier, type );
	}

	public String getKey() {
		return key;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getName() {
		return name;
	}

	public Version getVersion() {
		return version;
	}

	public String getClassifier() {
		return classifier;
	}

	public String getType() {
		return type;
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
		return this.getVersion().compareTo( that.getVersion() );
	}

	@Override
	public boolean equals( Object object ) {
		if( !(object instanceof ProductDownload) ) return false;

		ProductDownload that = (ProductDownload)object;

		return this.identifier.equals( that.identifier );
	}

}
