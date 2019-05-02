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

public class LocalStoreDownloadProviderTest {

	private LocalStoreDownloadProvider provider;

	private String name = "Xenon";

	private String artifact = "xenon";

	private String category = "product";

	private String type = "card";

	private String version = "0.0u0";

	//private ProductCard card = new ProductCard();

	private Path root = Paths.get( "source/test/repos/xeo/stable" );

	@Before
	public void setup() {
		//card.setName( name );
		//card.setVersion( version );

		provider = new LocalStoreDownloadProvider( root );
	}

	@Test
	public void testGetLatestDownload() {
		String channel = "latest";
		String platform = "linux";

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( List.of( artifact ), category, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-xenon-" + platform + "-product-card" ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( category ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getVersion(), is( version ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file://" + root.toAbsolutePath() + "/" + artifact + "/" + platform + "/" + category + "." + type ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetStableDownload() {
		String channel = "stable";
		String platform = "windows";

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( List.of( artifact ), category, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-xenon-" + platform + "-product-card" ) );
		assertThat( downloads.get( 0 ).getChannel(), is( "stable" ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( category ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getVersion(), is( version ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file://" + root.toAbsolutePath() + "/" + artifact + "/" + platform + "/" + category + "." + type ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetLatestDownloadWithoutPlatform() {
		String channel = "latest";
		artifact = "mouse";
		name = "Mouse";
		String platform = null;

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( List.of( artifact ), category, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-mouse-product-card" ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( category ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getVersion(), is( version ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file://" + root.toAbsolutePath() + "/" + artifact + "/" + category + "." + type ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetLatestDownloadProductPack() {
		String channel = "latest";
		String platform = "linux";
		String artifact = "mouse";
		//String name = "Mouse";
		type = "pack";

		//when( provider.exists( Paths.get( "/opt/xeo/store/latest/xenon/linux/product.pack" ) ) ).thenReturn( false );
		//when( provider.exists( Paths.get( "/opt/xeo/store/latest/xenon/product.pack" ) ) ).thenReturn( true );

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( List.of( artifact ), category, type, channel, platform );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-mouse-product-pack" ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( category ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file://" + root.toAbsolutePath() + "/" + artifact + "/" + category + ".jar" ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getVersion(), is( version ) );
		assertThat( downloads.size(), is( 1 ) );
	}

}
