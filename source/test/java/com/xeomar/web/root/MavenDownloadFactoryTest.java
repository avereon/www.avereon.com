package com.xeomar.web.root;

import com.xeomar.util.XmlDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class MavenDownloadFactoryTest {

	private MavenDownloadFactory factory = mock( MavenDownloadFactory.class );

	private String group = "com.xeomar";

	private String artifact = "xenon";

	private String classifier = "product";

	private String type = "card";

	private String name = "Xenon";

	private String uri = "https://repo.xeomar.com/xeo/" + group.replace( '.', '/' ) + "/" + artifact;

	@Before
	public void before() throws Exception {
		factory = mock( MavenDownloadFactory.class );

		// Needed because the method is mocked
		when( factory.getDownloads( anyList(), anyString(), anyString(), anyString() ) ).thenCallRealMethod();

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
		String requestVersion = "latest";

		when( factory.getDownloads( uri, classifier, type, requestVersion ) ).thenCallRealMethod();

		// Execute the method
		List<MavenDownload> downloads = factory.getDownloads( uri, classifier, type, requestVersion );
		verify( factory, times( 3 ) ).getXmlDescriptor( anyString() );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( "https://repo.xeomar.com/xeo/com/xeomar/xenon-product-card-" + requestVersion ) );
		assertThat( downloads.get( 0 ).getVersion().toString(), is( "0.8-SNAPSHOT" ) );
		assertThat( downloads.get( 0 ).getGroupId(), is( group ) );
		assertThat( downloads.get( 0 ).getArtifactId(), is( artifact ) );
		assertThat( downloads.get( 0 ).getClassifier(), is( classifier ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetReleaseDownload() throws Exception {
		String requestVersion = "release";

		when( factory.getDownloads( uri, classifier, type, requestVersion ) ).thenCallRealMethod();

		// Execute the method
		List<MavenDownload> downloads = factory.getDownloads( uri, classifier, type, requestVersion );
		verify( factory, times( 2 ) ).getXmlDescriptor( anyString() );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( "https://repo.xeomar.com/xeo/com/xeomar/xenon-product-card-" + requestVersion ) );
		assertThat( downloads.get( 0 ).getVersion().toString(), is( "0.7" ) );
		assertThat( downloads.get( 0 ).getGroupId(), is( group ) );
		assertThat( downloads.get( 0 ).getArtifactId(), is( artifact ) );
		assertThat( downloads.get( 0 ).getClassifier(), is( classifier ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	@Test
	public void testGetVersionDownload() throws Exception {
		String requestVersion = "0.5-SNAPSHOT";

		when( factory.getDownloads( uri, classifier, type, requestVersion ) ).thenCallRealMethod();

		// Execute the method
		List<MavenDownload> downloads = factory.getDownloads( uri, classifier, type, requestVersion );
		verify( factory, times( 13 ) ).getXmlDescriptor( anyString() );

		assertTrue( "No downloads retrieved", downloads.size() > 0 );
		assertThat( downloads.get( 0 ).getKey(), is( "https://repo.xeomar.com/xeo/com/xeomar/xenon-product-card-" + requestVersion ) );
		assertThat( downloads.get( 0 ).getVersion().toString(), is( requestVersion ) );
		assertThat( downloads.get( 0 ).getGroupId(), is( group ) );
		assertThat( downloads.get( 0 ).getArtifactId(), is( artifact ) );
		assertThat( downloads.get( 0 ).getClassifier(), is( classifier ) );
		assertThat( downloads.get( 0 ).getType(), is( type ) );
		assertThat( downloads.get( 0 ).getName(), is( name ) );
		assertThat( downloads.size(), is( 1 ) );
	}

	private void stageDescriptor( String uri, String path ) throws IOException {
		XmlDescriptor releasePom = new XmlDescriptor( getClass().getResource( "/repo" + path ) );
		when( factory.getXmlDescriptor( uri + path ) ).thenReturn( releasePom );
	}

}
