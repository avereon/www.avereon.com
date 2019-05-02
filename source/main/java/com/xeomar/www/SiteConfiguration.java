package com.xeomar.www;

import com.xeomar.www.download.DownloadProvider;
import com.xeomar.www.download.LocalStoreDownloadProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteConfiguration {

	@Bean
	public DownloadProvider getDownloadProvider() {
		return new LocalStoreDownloadProvider( "/opt/xeo/store" );
	}

}
