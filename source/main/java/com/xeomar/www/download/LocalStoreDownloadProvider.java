package com.xeomar.www.download;

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

	private static String GROUP = "com.xeomar";

	private Path root;

	public LocalStoreDownloadProvider( String root ) {
		this( Paths.get( root ) );
	}

	public LocalStoreDownloadProvider( Path root ) {
		this.root = root.toAbsolutePath();
	}

	@Override
	public List<ProductDownload> getDownloads( List<String> artifacts, String category, String type, String channel, String platform ) {
		List<ProductDownload> downloads = new ArrayList<>();

		for( String artifact : artifacts ) {
			String key = Download.key( artifact, category, type, channel, platform );
			log.info( "Get artifact by key: " + key );

			Path path = root.resolve( artifact );
			if( platform != null ) path = path.resolve( platform );
			path = path.resolve( getFilename( category, Download.type( type ) ) );
			if( !Files.exists( path ) ) continue;

			String name = null;
			String version = null;
			ProductCard card = getProductCard( path.getParent(), category );
			if( card != null ) {
				name = card.getName();
				version = card.getVersion();
			}

			String link = path.toUri().toString();
			String md5Link = "";
			String sha1Link = "";
			downloads.add( new ProductDownload( GROUP, artifact, channel, category, type, platform, version, name, link, md5Link, sha1Link ) );
		}

		// If no downloads were found check for non-platform specific downloads
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

	private ProductCard getProductCard( Path path, String category ) {
		path = path.resolve( getFilename( category, "card" ) );
		if( !Files.exists( path ) ) return null;

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
