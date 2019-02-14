package com.xeomar.web.root;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LocalStoreDownloadProviderTest {

	private LocalStoreDownloadProvider factory;

	private String artifact = "xenon";

	private String classifier = "product";

	private String type = "card";

	@Before
	public void setup() {
		factory = mock( LocalStoreDownloadProvider.class );

		// Needed because the methods are mocked
		when( factory.getDownloads( anyString(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();
		when( factory.getDownloads( anyList(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();
		when( factory.getDownloadKey( anyString(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();
	}

	@Test
	public void testGetLatestDownload() throws Exception {
		String channel = "latest";

		when( factory.getDownloads( artifact, classifier, type, channel ) ).thenCallRealMethod();

		// Execute the method
		List<ProductDownload> downloads = factory.getDownloads( artifact, classifier, type, channel );
		//verify( factory, times( 3 ) ).getXmlDescriptor( anyString() );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( "xenon-product-card-" + channel ) );
		//		assertThat( downloads.get( 0 ).getVersion().toString(), is( "0.8-SNAPSHOT" ) );
		//		assertThat( downloads.get( 0 ).getGroupId(), is( group ) );
		assertThat( downloads.get( 0 ).getArtifactId(), is( artifact ) );
		assertThat( downloads.get( 0 ).getClassifier(), is( classifier ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		//		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.get( 0 ).getLink(), is( "file:///opt/xeo/store/latest/xenon/linux-product.card" ) );
		assertThat( downloads.size(), is( 1 ) );
	}

}
