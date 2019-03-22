package com.xeomar.web.root;

import com.xeomar.product.ProductCard;
import com.xeomar.util.LogUtil;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LocalStoreDownloadProvider extends AbstractDownloadProvider {

	private static final Logger log = LogUtil.get( MethodHandles.lookup().lookupClass() );

	private static String ROOT = "/opt/xeo/store";

	private static String GROUP = "com.xeomar";

	@Override
	public List<ProductDownload> getDownloads( String artifact, String category, String type, String channel, String platform ) {
		return getDownloads( List.of( artifact ), category, type, channel, platform );
	}

	@Override
	public List<ProductDownload> getDownloads( List<String> artifacts, String category, String type, String channel, String platform ) {
		List<ProductDownload> downloads = new ArrayList<>();

		for( String artifact : artifacts ) {
			String key = getDownloadKey( artifact, category, type, channel, platform );
			log.info( "Get artifact by key: " + key );

			Path path = Paths.get( ROOT, channel, artifact );
			if( platform != null ) path = path.resolve( platform );
			path = path.resolve( getFilename( category, type ) );
			if( !exists( path ) ) continue;

			ProductCard card = getProductCard( path, category );

			String name = card.getName();
			String version = card.getVersion();
			String link = path.toUri().toString();
			String md5Link = "";
			String sha1Link = "";
			downloads.add( new ProductDownload( key, GROUP, artifact, channel, category, type, platform, version, name, link, md5Link, sha1Link ) );
		}

		// If now downloads were found check for non-platform specific downloads
		if( platform != null && downloads.size() == 0 ) downloads.addAll( getDownloads( artifacts, category, type, channel, null ) );

		return downloads;
	}

	@Override
	public String clearCache( String artifact, String category, String type, String channel, String platform ) {
		return null;
	}

	@Override
	public String clearCache() {
		log.info( "Cache cleared for all" );
		return "Cache cleared for all";
	}

	boolean exists( Path path ) {
		return Files.exists( path );
	}

	ProductCard getProductCard( Path path, String category ) {
		path = path.resolve( getFilename( category, "card" ) );
		ProductCard card = new ProductCard();
		try {
			card.load( new FileInputStream( path.toFile() ), null );
		} catch( IOException exception ) {
			log.error( "Could not load product card: " + path, exception );
		}
		return card;
	}

	private String getFilename( String category, String type ) {
		return category + "." + type;
	}

}
