package com.xeomar.www.download;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Paths;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith( SpringRunner.class )
public class DownloadControllerTest {

	private DownloadController controller;

	private String channel = "latest";

	// TODO Should this be provider???
	private String group = "com.xeomar";

	private String artifact = "xenon";

	// TODO Change to resource/asset
	private String category = "product";

	// TODO Change to format
	private String type = "pack";

	private String platform;

	private MockMvc mvc;

	@MockBean
	private DownloadProvider provider;

	@Autowired
	public void setMockMvc( MockMvc mvc ) {
		this.mvc = mvc;
	}

	@Before
	public void setup() {
		String key = Download.key( artifact, category, type, channel, platform );
		String name = Download.name( artifact, platform, category, type );
		String version = "0.0";
		String link = Paths.get( "source/test/repos/maven/0.8-SNAPSHOT/maven-metadata.xml" ).toUri().toString();
		ProductDownload download = new ProductDownload( group, artifact, channel, category, type, platform, version, name, link, "", "" );

		// NEXT Continue improving these tests

		//when( provider.getDownloads( anyList(), anyString(), anyString(), anyString(), or( isNull(), anyString() ) ) ).thenReturn( List.of( download ) );
		when( provider.getDownloads( anyList(), anyString(), anyString(), anyString(), eq( platform ) ) ).thenReturn( List.of( download ) );
	}

	@Test
	public void testDownload() throws Exception {
		mvc.perform( MockMvcRequestBuilders.get( "/download" ) ).andExpect( status().is4xxClientError() );
	}

	@Test
	public void testDownloadV0() throws Exception {
		String path = String.format( "/download/%s/%s/%s/%s", artifact, category, type, channel );
		mvc.perform( MockMvcRequestBuilders.get( path ) ).andExpect( status().isOk() );
		verify( provider, times( 1 ) ).getDownloads( anyList(), eq( category ), eq( type ), eq( channel ), eq( platform ) );
	}

	@Test
	public void testDownloadV1() throws Exception {
		String path = String.format( "/download?artifact=%s&category=%s&type=%s&channel=%s", artifact, category, type, channel );
		mvc.perform( MockMvcRequestBuilders.get( path ) ).andExpect( status().isOk() );
		verify( provider, times( 1 ) ).getDownloads( anyList(), eq( category ), eq( type ), eq( channel ), eq( platform ) );
	}

}
