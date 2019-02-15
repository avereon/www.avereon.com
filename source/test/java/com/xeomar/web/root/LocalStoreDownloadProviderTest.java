package com.xeomar.web.root;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LocalStoreDownloadProviderTest {

	private LocalStoreDownloadProvider provider;

	private String artifact = "xenon";

	private String classifier = "product";

	private String type = "card";

	@Before
	public void setup() {
		provider = mock( LocalStoreDownloadProvider.class );

		// Needed because the methods are mocked
		when( provider.getDownloads( anyString(), anyString(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();
		when( provider.getDownloads( anyList(), anyString(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();
		when( provider.getDownloadKey( anyString(), anyString(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();
	}

	@Test
	public void testGetLatestDownload() throws Exception {
		String channel = "latest";
		String platform= "linux";

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( artifact, classifier, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( "xenon-product-card-" + channel + "-" + platform ) );
		//		assertThat( downloads.get( 0 ).getVersion().toString(), is( "0.8-SNAPSHOT" ) );
		//		assertThat( downloads.get( 0 ).getGroupId(), is( group ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( classifier ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		//		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file:///opt/xeo/store/latest/xenon/linux-product.card" ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetStableDownload() throws Exception {
		String channel = "stable";
		String platform = "windows";

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( artifact, classifier, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( "xenon-product-card-" + channel + "-" + platform ) );
				assertThat( downloads.get( 0 ).getChannel().toString(), is( "stable" ) );
		//		assertThat( downloads.get( 0 ).getGroupId(), is( group ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( classifier ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		//		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file:///opt/xeo/store/stable/xenon/windows-product.card" ) );
		assertThat( downloads.size(), is( 1 ) );
	}

}
