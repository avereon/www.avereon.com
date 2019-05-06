package com.xeomar.www.download;

public class V2LocalDownloadProvider implements V2DownloadProvider {

	private String root;

	V2LocalDownloadProvider( String root ) {
		this.root = root;
	}

	@Override
	public V2Download getDownload( String artifact, String platform, String asset, String format ) {
		return null;
	}

}
