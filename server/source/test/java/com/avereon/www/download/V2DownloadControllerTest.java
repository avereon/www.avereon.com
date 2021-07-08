package com.avereon.www.download;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith( SpringRunner.class )
public class V2DownloadControllerTest {

	private String API = "/download/stable/v2";

	private MockMvc mvc;

	@MockBean
	V2DownloadProviderFactory factory;

	@Before
	public void setup() {
		Path root = Paths.get( "source/test/repos/avn" ).toAbsolutePath();
		Map<String, V2DownloadProvider> providers = new HashMap<>();
		providers.put( "stable", new V2LocalDownloadProvider( root.resolve( "stable" ) ) );
		providers.put( "latest", new V2LocalDownloadProvider( root.resolve( "latest" ) ) );
		when( factory.getProviders() ).thenReturn( providers );
	}

	@Autowired
	public void setMockMvc( MockMvc mvc ) {
		this.mvc = mvc;
	}

	@Test
	public void testInvalidArtifact() throws Exception {
		mvc.perform( MockMvcRequestBuilders.head( API + "/invalid/product/pack" ) ).andExpect( status().is4xxClientError() );
	}

	@Test
	public void testInvalidPlatform() throws Exception {
		mvc.perform( MockMvcRequestBuilders.head( API + "/xenon/invalid/product/pack" ) ).andExpect( status().is4xxClientError() );
	}

	@Test
	public void testInvalidAsset() throws Exception {
		mvc.perform( MockMvcRequestBuilders.head( API + "/mouse/invalid/pack" ) ).andExpect( status().is4xxClientError() );
	}

	@Test
	public void testInvalidType() throws Exception {
		mvc.perform( MockMvcRequestBuilders.head( API + "/mouse/product/invalid" ) ).andExpect( status().is4xxClientError() );
	}

	@Test
	public void testGetCatalogCard() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.head( API + "/catalog" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getContentType(), is( "application/json" ) );
		assertThat( result.getResponse().getContentLength(), is( 58 ) );
	}

	@Test
	public void testGetProductCards() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.get( "/download/product/cards/xenon" ) ).andExpect( status().isOk() ).andReturn();

		assertThat( result.getResponse().getContentType(), is( "application/json" ) );
		//assertThat( result.getResponse().getContentAsString(), is( "" ));

		JsonNode json = new ObjectMapper().readValue( result.getResponse().getContentAsString(), JsonNode.class );
		assertThat( StreamSupport.stream( ((Iterable<String>)json::fieldNames).spliterator(), false ).collect( Collectors.toSet() ), Matchers.containsInAnyOrder( "stable", "latest" ) );

		JsonNode stable = json.get( "stable" );
		assertThat(
			StreamSupport.stream( ((Iterable<String>)stable::fieldNames).spliterator(), false ).collect( Collectors.toSet() ),
			Matchers.containsInAnyOrder( "linux", "windows", "macosx", "card" )
		);
		JsonNode latest = json.get( "latest" );
		assertThat(
			StreamSupport.stream( ((Iterable<String>)latest::fieldNames).spliterator(), false ).collect( Collectors.toSet() ),
			Matchers.containsInAnyOrder( "linux", "windows", "macosx", "card" )
		);
	}

	@Test
	public void testGetProductCardMetadataWithExtraPlatform() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.head( API + "/mouse/linux/product/card" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "mouse" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( nullValue() ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "card" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Mouse" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0" ) );
	}

	@Test
	public void testGetProductCardMetadata() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.head( API + "/mouse/product/card" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "mouse" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( nullValue() ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "card" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Mouse" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0" ) );
	}

	@Test
	public void testGetProductPackMetadata() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.head( API + "/mouse/product/pack" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "mouse" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( nullValue() ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "pack" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Mouse" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0" ) );
	}

	@Test
	public void testGetProductCardMetadataWithPlatform() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.head( API + "/xenon/linux/product/card" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "xenon" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( "linux" ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "card" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Xenon" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0" ) );
	}

	@Test
	public void testGetProductPackMetadataWithPlatform() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.head( API + "/xenon/linux/product/pack" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "xenon" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( "linux" ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "pack" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Xenon" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0" ) );
	}

	@Test
	public void testGetProductCard() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.get( API + "/mouse/product/card" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "mouse" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( nullValue() ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "card" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Mouse" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0" ) );

		assertThat( result.getResponse().getContentType(), is( "application/json" ) );
		assertThat( result.getResponse().getContentLength(), is( 427 ) );
	}

	@Test
	@SuppressWarnings( "unchecked" )
	public void testGetProductCardWithTheme() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.get( API + "/mouse/product/card?theme=grey" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "mouse" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( nullValue() ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "card" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Mouse" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0-g" ) );

		String content = result.getResponse().getContentAsString();
		Map<String, String> map = content == null ? Map.of() : new ObjectMapper().readValue( content, Map.class );
		assertThat( map.get( "theme" ), is( "grey" ) );
		assertThat( map.get( "version" ), is( "0.0u0-g" ) );

		assertThat( result.getResponse().getContentType(), is( "application/json" ) );
		assertThat( result.getResponse().getContentLength(), is( 449 ) );
	}

	@Test
	public void testGetProduct() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.get( API + "/mouse/product/pack" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "mouse" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( nullValue() ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "pack" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Mouse" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0" ) );

		assertThat( result.getResponse().getContentType(), is( "application/java-archive" ) );
		assertThat( result.getResponse().getContentLength(), is( 410 ) );
	}

	@Test
	public void testGetProductCardWithPlatform() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.get( API + "/xenon/linux/product/card" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "xenon" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( "linux" ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "card" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Xenon" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0" ) );

		assertThat( result.getResponse().getContentType(), is( "application/json" ) );
		assertThat( result.getResponse().getContentLength(), is( 429 ) );
	}

	@Test
	public void testGetProductWithPlatform() throws Exception {
		MvcResult result = mvc.perform( MockMvcRequestBuilders.get( API + "/xenon/linux/product/pack" ) ).andExpect( status().isOk() ).andReturn();
		verify( factory, times( 1 ) ).getProviders();

		assertThat( result.getResponse().getHeader( "group" ), is( "com.avereon" ) );
		assertThat( result.getResponse().getHeader( "artifact" ), is( "xenon" ) );
		assertThat( result.getResponse().getHeader( "platform" ), is( "linux" ) );
		assertThat( result.getResponse().getHeader( "asset" ), is( "product" ) );
		assertThat( result.getResponse().getHeader( "format" ), is( "pack" ) );
		assertThat( result.getResponse().getHeader( "name" ), is( "Xenon" ) );
		assertThat( result.getResponse().getHeader( "version" ), is( "0.0u0" ) );

		assertThat( result.getResponse().getContentType(), is( "application/java-archive" ) );
		assertThat( result.getResponse().getContentLength(), is( 412 ) );
	}

}
