package com.xeomar.www.download;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LocalStoreDownloadProviderTest {

	private LocalStoreDownloadProvider provider;

	private Path root = Paths.get( "source/test/repos/xeo/stable" );

	@Before
	public void setup() {
		provider = new LocalStoreDownloadProvider( root );
	}

	@Test
	public void testGetLatestDownload() {
		assertDownloads( "stable", "xenon", "linux", "product", "card", "Xenon", "0.0u0" );
	}

	@Test
	public void testGetStableDownload() {
		assertDownloads( "stable", "xenon", "windows", "product", "card", "Xenon", "0.0u0" );
	}

	@Test
	public void testGetLatestDownloadWithoutPlatform() {
		assertDownloads( "latest", "mouse", null, "product", "card", "Mouse", "0.0u0" );
	}

	@Test
	public void testGetLatestDownloadProductPack() {
		assertDownloads( "latest", "mouse", null, "product", "pack", "Mouse", "0.0u0" );
	}

	private void assertDownloads( String channel, String artifact, String platform, String category, String type, String name, String version ) {
		List<ProductDownload> downloads = provider.getDownloads( List.of( artifact ), category, type, channel, platform );

		String key = String.format( "%s-%s-%s-%s", channel, artifact, category, type );
		String link = String.format( "file://%s/%s/%s.%s", root.toAbsolutePath(), artifact, category, Download.type( type ) );
		if( platform != null ) {
			key = String.format( "%s-%s-%s-%s-%s", channel, artifact, platform, category, type );
			link = String.format( "file://%s/%s/%s/%s.%s", root.toAbsolutePath(), artifact, platform, category, Download.type( type ) );
		}

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( key ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( category ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getLink(), is( link ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getVersion(), is( version ) );
		assertThat( downloads.size(), is( 1 ) );
	}

}
