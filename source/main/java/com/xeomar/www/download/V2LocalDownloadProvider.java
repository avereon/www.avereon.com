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

public class V2LocalDownloadProvider implements V2DownloadProvider {

	private static final Logger log = LogUtil.get( MethodHandles.lookup().lookupClass() );

	private Path root;

	V2LocalDownloadProvider( String root ) {
		this( Paths.get( root ) );
	}

	V2LocalDownloadProvider( Path root ) {
		this.root = root;
	}

	@Override
	public V2Download getDownload( String artifact, String platform, String asset, String format ) throws IOException {
		V2Download download = new V2Download( artifact, platform, asset, format );

		String key = download.getKey();
		log.info( "Get artifact: " + key );
		System.err.println( "Get artifact: " + key );

		// TODO Opportunity to provide a cache here

		Path path = root.resolve( artifact );
		if( platform != null ) path = path.resolve( platform );
		path = path.resolve( getFilename( asset, format ) );
		if( !Files.exists( path ) ) log.warn( "Artifact path not found: " + path );
		if( !Files.exists( path ) ) return null;

		ProductCard card = getCard( path.getParent(), asset );
		if( card == null ) log.warn( "Product card not found: " + path );
		if( card == null ) return null;

		download.setGroup( card.getGroup() );
		download.setName( card.getName() );
		download.setVersion( card.getVersion() );
		download.setSize( Files.size( path ) );
		download.setInputStream( Files.newInputStream( path ) );

		return download;
	}

	private ProductCard getCard( Path root, String asset ) {
		Path path = root.resolve( getFilename( asset, "card" ) );
		if( !Files.exists( path ) ) return null;

		ProductCard card = new ProductCard();
		try {
			card.load( new FileInputStream( path.toFile() ), null );
		} catch( IOException exception ) {
			log.error( "Could not load product card: " + path, exception );
			return null;
		}

		return card;
	}

	private String getFilename( String asset, String format ) {
		return asset + "." + V2Download.resolveFormat( format );
	}

}
