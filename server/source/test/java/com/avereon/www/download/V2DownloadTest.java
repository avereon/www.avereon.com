package com.avereon.www.download;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

public class V2DownloadTest {

	@Test
	public void testFilename() {
		assertThat( V2Download.filename( "artifact", "platform", "asset", "format", Map.of() ), is( "artifact-asset-platform.format" ) );
		assertThat( V2Download.filename( null, "platform", "asset", "format", Map.of() ), is( "asset-platform.format" ) );
		assertThat( V2Download.filename( null, null, "asset", "format", Map.of() ), is( "asset.format" ) );
		assertThat( V2Download.filename( "artifact", null, "asset", "format", Map.of() ), is( "artifact-asset.format" ) );

		assertThat( V2Download.filename( "artifact", "platform", "asset", "format", Map.of( "theme", "grey") ), is( "artifact-asset-grey-platform.format" ) );
		assertThat( V2Download.filename( null, "platform", "asset", "format", Map.of("theme", "grey") ), is( "asset-grey-platform.format" ) );
		assertThat( V2Download.filename( null, null, "asset", "format", Map.of("theme", "grey") ), is( "asset-grey.format" ) );
		assertThat( V2Download.filename( "artifact", null, "asset", "format", Map.of("theme", "grey") ), is( "artifact-asset-grey.format" ) );

		// Errata
		assertThat( V2Download.filename( "artifact", "platform", null, "format", Map.of() ), is( "artifact--platform.format" ) );
		assertThat( V2Download.filename( "artifact", "platform", "asset", null, Map.of() ), is( "artifact-asset-platform.null" ) );
	}

}
