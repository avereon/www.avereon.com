package com.parallelsymmetry.site;

import java.util.List;

import junit.framework.TestCase;

public class MavenDownloadTest extends TestCase {

	public void testGetEscapeUpdaterDownloads() throws Exception {
		List<MavenDownload> escapeServiceDownloads = MavenDownload.getDownloads( "standalone", "http://mvn.parallelsymmetry.com/content/groups/psm/com/parallelsymmetry/escape/updater" );
		assertTrue( escapeServiceDownloads.size() > 0 );
	}

	public void testGetEscapeServiceDownloads() throws Exception {
		List<MavenDownload> escapeServiceDownloads = MavenDownload.getDownloads( null, "http://mvn.parallelsymmetry.com/content/groups/psm/com/parallelsymmetry/escape/service" );
		assertTrue( escapeServiceDownloads.size() > 0 );
	}

	public void testGetEscapeUtilityDownloads() throws Exception {
		List<MavenDownload> escapeUtilityDownloads = MavenDownload.getDownloads( null, "http://mvn.parallelsymmetry.com/content/groups/psm/com/parallelsymmetry/escape/utility" );
		assertTrue( escapeUtilityDownloads.size() > 0 );
	}

	public void testGetTerraceDownloads() throws Exception {
		List<MavenDownload> terraceDownloads = MavenDownload.getDownloads( null, "http://mvn.parallelsymmetry.com/content/groups/psm/com/parallelsymmetry/terrace" );
		assertTrue( terraceDownloads.size() > 0 );
	}

}
