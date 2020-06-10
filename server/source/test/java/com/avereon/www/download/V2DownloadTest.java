package com.avereon.www.download;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

public class V2DownloadTest {

	@Test
	public void testFilename() {
		assertThat( V2Download.filename( "artifact", "platform", "asset", "format", Map.of() ), is( "artifact-asset-platform.format" ) );
		assertThat( V2Download.filename( null, null, "asset", "format", Map.of() ), is( "asset.format" ) );
	}

}
