package com.parallelsymmetry.site;

import java.util.List;
import java.util.Map;

import com.parallelsymmetry.escape.utility.Accessor;

import junit.framework.TestCase;

public class MavenDownloadTest extends TestCase {

	public void testGetEscapeUpdaterDownloads() throws Exception {
		List<MavenDownload> escapeServiceDownloads = MavenDownload.getDownloads( "http://mvn.parallelsymmetry.com/content/groups/psm/com/parallelsymmetry/escape/updater", "standalone", null );
		assertTrue( escapeServiceDownloads.size() > 0 );
		
		Map<String, List<MavenDownload>> cache = Accessor.getField( MavenDownload.class, "cache" );
		assertTrue( cache.get("http://mvn.parallelsymmetry.com/content/groups/psm/com/parallelsymmetry/escape/updater-standalone-jar").size() > 0 );
	}

	public void testGetEscapeServiceDownloads() throws Exception {
		List<MavenDownload> escapeServiceDownloads = MavenDownload.getDownloads( "http://mvn.parallelsymmetry.com/content/groups/psm/com/parallelsymmetry/escape/service", null, null );
		assertTrue( escapeServiceDownloads.size() > 0 );
	}

	public void testGetEscapeUtilityDownloads() throws Exception {
		List<MavenDownload> escapeUtilityDownloads = MavenDownload.getDownloads( "http://mvn.parallelsymmetry.com/content/groups/psm/com/parallelsymmetry/escape/utility", null, null );
		assertTrue( escapeUtilityDownloads.size() > 0 );
	}

	public void testGetTerraceDownloads() throws Exception {
		List<MavenDownload> terraceDownloads = MavenDownload.getDownloads( "http://mvn.parallelsymmetry.com/content/groups/psm/com/parallelsymmetry/terrace", null, null );
		assertTrue( terraceDownloads.size() > 0 );
	}

}
