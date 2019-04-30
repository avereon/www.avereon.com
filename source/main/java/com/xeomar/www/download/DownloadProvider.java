package com.xeomar.www.download;

import java.util.List;

public interface DownloadProvider {

	List<ProductDownload> getDownloads( String artifact, String category, String type, String channel, String platform );

	List<ProductDownload> getDownloads( List<String> artifacts, String category, String type, String channel, String platform );

	String clearCache( String artifact, String category, String type, String channel, String platform );

	String clearCache();

}
