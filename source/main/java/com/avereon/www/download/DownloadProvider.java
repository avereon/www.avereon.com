package com.avereon.www.download;

import java.util.List;

@Deprecated
public interface DownloadProvider {

	List<ProductDownload> getDownloads( List<String> artifacts, String category, String type, String channel, String platform );

	String clearCache( String artifact, String category, String type, String channel, String platform );

	String clearCache();

}
