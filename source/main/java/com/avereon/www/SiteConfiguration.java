package com.avereon.www;

import com.avereon.www.download.DownloadProvider;
import com.avereon.www.download.LocalStoreDownloadProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteConfiguration {

	@Bean
	public DownloadProvider getDownloadProvider() {
		return new LocalStoreDownloadProvider( "/opt/avn/store" );
	}

}
