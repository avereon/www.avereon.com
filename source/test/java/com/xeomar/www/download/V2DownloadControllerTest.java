package com.xeomar.www.download;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith( SpringRunner.class )
public class V2DownloadControllerTest {

	private String ROOT = "/repo/stable/v2";

	private MockMvc mvc;

	@Autowired
	public void setMockMvc( MockMvc mvc ) {
		this.mvc = mvc;
	}

	@Test
	public void testGetMetadata() throws Exception {
		mvc.perform( MockMvcRequestBuilders.get( ROOT + "/notapath" ) ).andExpect( status().is4xxClientError() );
	}

}
