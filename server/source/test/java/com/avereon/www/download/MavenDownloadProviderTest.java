package com.avereon.www.download;

import com.avereon.www.download.MavenDownloadProvider;
import com.avereon.www.download.ProductDownload;
import com.avereon.util.XmlDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class MavenDownloadProviderTest {

	private MavenDownloadProvider provider;

	private static final String TEST_MAVEN_REPOSITORY = "source/test/repos/maven";

	private String group = "com.avereon";

	private String artifact = "xenon";

	private String classifier = "product";

	private String type = "card";

	private String name = "Xenon";

	private String uri = "https://repo.avereon.com/avn/" + group.replace( '.', '/' ) + "/" + artifact;

	@Before
	public void before() throws Exception {
		provider = mock( MavenDownloadProvider.class );

		// Needed because the methods are mocked
		//when( provider.getDownloads( anyString(), anyString(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();
		when( provider.getDownloads( anyList(), anyString(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();
		//when( provider.getDownloadKey( anyString(), anyString(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();

		// Setup the repository descriptors
		stageDescriptor( uri, "/maven-metadata.xml" );
		stageDescriptor( uri, "/0.4-SNAPSHOT/maven-metadata.xml" );
		stageDescriptor( uri, "/0.4-SNAPSHOT/xenon-0.4-20170910.200106-43.pom" );
		stageDescriptor( uri, "/0.5-SNAPSHOT/maven-metadata.xml" );
		stageDescriptor( uri, "/0.5-SNAPSHOT/xenon-0.5-20171018.023208-23.pom" );
		stageDescriptor( uri, "/0.6-SNAPSHOT/maven-metadata.xml" );
		stageDescriptor( uri, "/0.6-SNAPSHOT/xenon-0.6-20180604.142938-243.pom" );
		stageDescriptor( uri, "/0.6/xenon-0.6.pom" );
		stageDescriptor( uri, "/0.7-SNAPSHOT/maven-metadata.xml" );
		stageDescriptor( uri, "/0.7-SNAPSHOT/xenon-0.7-20190124.141909-409.pom" );
		stageDescriptor( uri, "/0.7/xenon-0.7.pom" );
		stageDescriptor( uri, "/0.8-SNAPSHOT/maven-metadata.xml" );
		stageDescriptor( uri, "/0.8-SNAPSHOT/xenon-0.8-20190125.123121-4.pom" );
	}

	@After
	public void after() throws Exception {
		//verify( factory, times(2) ).getXmlDescriptor( anyString() );
	}

	@Test
	public void testGetLatestDownload() throws Exception {
		String channel = "latest";
		String platform = "linux";

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( List.of( artifact ), classifier, type, channel, platform );
		verify( provider, times( 3 ) ).getXmlDescriptor( anyString() );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-xenon-" + platform + "-product-card" ) );
		//assertThat( downloads.get( 0 ).getChannel(), is( "0.8-SNAPSHOT" ) );
		assertThat( downloads.get( 0 ).getGroup(), is( group ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( classifier ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getPlatform(), is( platform ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetReleaseDownload() throws Exception {
		String channel = "release";
		String platform = "macos";

		// Execute the method
		List<ProductDownload> downloads = provider.getDownloads( List.of( artifact ), classifier, type, channel, platform );
		verify( provider, times( 2 ) ).getXmlDescriptor( anyString() );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( channel + "-xenon-" + platform + "-product-card" ) );
		//assertThat( downloads.get( 0 ).getChannel(), is( "0.7" ) );
		assertThat( downloads.get( 0 ).getGroup(), is( group ) );
		assertThat( downloads.get( 0 ).getArtifact(), is( artifact ) );
		assertThat( downloads.get( 0 ).getCategory(), is( classifier ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getPlatform(), is( platform ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	private void stageDescriptor( String uri, String path ) throws IOException {
		Path resource = Paths.get( TEST_MAVEN_REPOSITORY, path );
		XmlDescriptor releasePom = new XmlDescriptor( resource.toUri() );
		when( provider.getXmlDescriptor( uri + path ) ).thenReturn( releasePom );
	}

}
