package com.xeomar.web.root;

import java.util.List;

public class LocalStoreDownloadProvider implements DownloadFactory {

	private static String ROOT = "/opt/xeo/store";

	@Override
	public List<MavenDownload> getDownloads( String artifact, String category, String type ) {
		return null;
	}

	@Override
	public List<MavenDownload> getDownloads( String artifact, String category, String type, String channel ) {
		return null;
	}

	@Override
	public List<MavenDownload> getDownloads( List<String> artifacts, String classifier, String type, String version ) {
		return null;
	}

	@Override
	public String clearCache( String artifact, String category, String type, String channel ) {
		return null;
	}

	@Override
	public String clearCache() {
		return null;
	}

}
