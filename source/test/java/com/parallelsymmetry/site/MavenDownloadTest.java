package com.parallelsymmetry.site;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.parallelsymmetry.utility.Accessor;
import com.parallelsymmetry.utility.log.Log;

public class MavenDownloadTest extends TestCase {
	
	public void testGetElementsByVersionDownloads() throws Exception {
		Log.setLevel( Log.DEBUG );
		List<MavenDownload> downloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/elements/0.0.1-SNAPSHOT", "module", "jnlp" );
		assertTrue( downloads.size() > 0 );
	}

	public void testGetUpdaterDownloads() throws Exception {
		List<MavenDownload> downloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/updater", "standalone", null );
		assertTrue( downloads.size() > 0 );
		
		Map<String, List<MavenDownload>> cache = Accessor.getField( MavenDownload.class, "cache" );
		assertTrue( cache.get("http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/updater-standalone-jar").size() > 0 );
	}

	public void testGetServiceDownloads() throws Exception {
		List<MavenDownload> downloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/service", null, null );
		assertTrue( downloads.size() > 0 );
	}

	public void testGetUtilityDownloads() throws Exception {
		List<MavenDownload> downloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/utility", null, null );
		assertTrue( downloads.size() > 0 );
	}

	public void testGetTerraceDownloads() throws Exception {
		List<MavenDownload> downloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/terrace", null, null );
		assertTrue( downloads.size() > 0 );
	}

	public void testMavenDownloadCache() throws Exception {
		List<MavenDownload> downloads = MavenDownload.getDownloads( "http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/updater", "standalone", null );
		assertTrue( downloads.size() > 0 );
		
		Map<String, List<MavenDownload>> cache = Accessor.getField( MavenDownload.class, "cache" );
		assertTrue( cache.get("http://code.parallelsymmetry.com/repo/psm/com/parallelsymmetry/updater-standalone-jar").size() > 0 );
		
		MavenDownload.clearCache();
		assertTrue( cache.size() == 0 );
	}

}
