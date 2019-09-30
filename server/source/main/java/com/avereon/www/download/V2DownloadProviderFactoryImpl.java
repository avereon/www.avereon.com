package com.avereon.www.download;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class V2DownloadProviderFactoryImpl implements V2DownloadProviderFactory {

	private Map<String, V2DownloadProvider> providers;

	@Override
	public Map<String, V2DownloadProvider> getProviders() {
		if( providers == null ) {
			this.providers = new ConcurrentHashMap<>();
			providers.put( "stable", new V2LocalDownloadProvider( "/opt/avn/store/stable" ) );
			providers.put( "latest", new V2LocalDownloadProvider( "/opt/avn/store/latest" ) );
		}
		return providers;
	}

}
