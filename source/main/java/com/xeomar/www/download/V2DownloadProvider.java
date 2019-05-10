package com.xeomar.www.download;

import java.io.IOException;

/**
 * The V2DownloadProvider is an interface for retrieving an individual artifact.
 */
public interface V2DownloadProvider {

	/**
	 * Get the catalog download.
	 *
	 * @return
	 * @throws IOException
	 */
	V2Download getCatalog() throws IOException;

	/**
	 * Get a product artifact download.
	 * <ul>
	 * <li>artifact - The artifact identifier. A lowercase string.</li>
	 * <li>platform - The artifact platform. This might include os and arch:
	 * linux, macosx, windows, linux-x64, windows-x32</li>
	 * <li>asset - The artifact asset type: product, install, catalog</li>
	 * <li>format - The artifact format: card, pack</li>
	 * </ul>
	 *
	 * This should return one specific version of the artifact.
	 *
	 * @param artifact The artifact identifier
	 * @param platform The artifact platform
	 * @param asset The artifact asset type
	 * @param format The artifact format
	 * @return
	 */
	V2Download getDownload( String artifact, String platform, String asset, String format ) throws IOException;

}
