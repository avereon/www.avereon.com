package com.parallelsymmetry.site;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.parallelsymmetry.utility.Accessor;

public class MavenDownloadTest extends TestCase {

	public void testGetUpdaterDownloads() throws Exception {
		List<MavenDownload> escapeServiceDownloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/updater", "standalone", null );
		assertTrue( escapeServiceDownloads.size() > 0 );
		
		Map<String, List<MavenDownload>> cache = Accessor.getField( MavenDownload.class, "cache" );
		assertTrue( cache.get("http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/updater-standalone-jar").size() > 0 );
	}

	public void testGetServiceDownloads() throws Exception {
		List<MavenDownload> escapeServiceDownloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/service", null, null );
		assertTrue( escapeServiceDownloads.size() > 0 );
	}

	public void testGetUtilityDownloads() throws Exception {
		List<MavenDownload> escapeUtilityDownloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/utility", null, null );
		assertTrue( escapeUtilityDownloads.size() > 0 );
	}

	public void testGetTerraceDownloads() throws Exception {
		List<MavenDownload> terraceDownloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/terrace", null, null );
		assertTrue( terraceDownloads.size() > 0 );
	}

	public void testMavenDownloadCache() throws Exception {
		List<MavenDownload> escapeServiceDownloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/updater", "standalone", null );
		assertTrue( escapeServiceDownloads.size() > 0 );
		
		Map<String, List<MavenDownload>> cache = Accessor.getField( MavenDownload.class, "cache" );
		assertTrue( cache.get("http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/updater-standalone-jar").size() > 0 );
		
		MavenDownload.clearCache();
		assertTrue( cache.size() == 0 );
	}

}
