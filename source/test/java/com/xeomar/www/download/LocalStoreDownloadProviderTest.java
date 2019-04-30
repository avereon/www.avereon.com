package com.xeomar.www.download;

import com.xeomar.product.ProductCard;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalStoreDownloadProviderTest {

	private LocalStoreDownloadProvider provider;

	private String name = "Xenon";

	private String artifact = "xenon";

	private String category = "product";

	private String type = "card";

	private String version = "0.0u0";

	private ProductCard card = new ProductCard();

	@Before
	public void setup() {
		card.setName( name );
		card.setVersion( version );

		provider = mock( LocalStoreDownloadProvider.class );

		// Needed because the methods are mocked
		when( provider.getDownloads( anyString(), anyString(), anyString(), anyString(), or( isNull(), anyString() ) ) ).thenCallRealMethod();
		when( provider.getDownloads( anyList(), anyString(), anyString(), anyString(), or( isNull(), anyString() ) ) ).thenCallRealMethod();
		when( provider.getDownloadKey( anyString(), anyString(), anyString(), anyString(), or( isNull(), anyString() ) ) ).thenCallRealMethod();
		when( provider.getProductCard( any( Path.class ), anyString() ) ).thenReturn( card );
	}

	@Test
	public void testGetLatestDownload() {
		String channel = "latest";
		String platform = "linux";

		when( provider.exists( Paths.get( "/opt/xeo/store/latest/xenon/linux/product.card" ) ) ).thenReturn( true );

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( artifact, category, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-xenon-" + platform + "-product-card" ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( category ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getVersion(), is( version ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file:///opt/xeo/store/" + channel + "/" + artifact + "/" + platform + "/" + category + "." + type ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetStableDownload() {
		String channel = "stable";
		String platform = "windows";

		when( provider.exists( Paths.get( "/opt/xeo/store/stable/xenon/windows/product.card" ) ) ).thenReturn( true );

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( artifact, category, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-xenon-" + platform + "-product-card" ) );
		assertThat( downloads.get( 0 ).getChannel(), is( "stable" ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( category ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getVersion(), is( version ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file:///opt/xeo/store/" + channel + "/" + artifact + "/" + platform + "/" + category + "." + type ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetLatestDownloadWithoutPlatform() {
		String channel = "latest";
		String platform = null;

		when( provider.exists( Paths.get( "/opt/xeo/store/latest/xenon/product.card" ) ) ).thenReturn( true );

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( artifact, category, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-xenon-product-card" ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( category ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getVersion(), is( version ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file:///opt/xeo/store/" + channel + "/" + artifact + "/" + category + "." + type ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetLatestDownloadMissingPlatform() {
		String channel = "latest";
		String platform = "linux";
		type = "pack";

		when( provider.exists( Paths.get( "/opt/xeo/store/latest/xenon/linux/product.pack" ) ) ).thenReturn( false );
		when( provider.exists( Paths.get( "/opt/xeo/store/latest/xenon/product.pack" ) ) ).thenReturn( true );

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( artifact, category, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-xenon-product-pack" ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( category ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file:///opt/xeo/store/" + channel + "/" + artifact + "/" + category + "." + type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getVersion(), is( version ) );
		assertThat( downloads.size(), is( 1 ) );
	}

}
