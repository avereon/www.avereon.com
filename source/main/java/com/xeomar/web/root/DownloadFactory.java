package com.xeomar.web.root;

import java.util.List;

public interface DownloadFactory {

	List<ProductDownload> getDownloads( String artifact, String category, String type );

	List<ProductDownload> getDownloads( String artifact, String category, String type, String channel );

	List<ProductDownload> getDownloads( List<String> artifacts, String classifier, String type, String version );

	/**
	 * @param artifact
	 * @param category
	 * @param type
	 * @param channel
	 * @return A message that the cache was cleared
	 */
	String clearCache( String artifact, String category, String type, String channel );

	String clearCache();

}
