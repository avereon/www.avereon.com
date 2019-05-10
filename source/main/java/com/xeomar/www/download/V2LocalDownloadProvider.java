package com.xeomar.www.download;

import com.xeomar.product.ProductCard;
import com.xeomar.util.LogUtil;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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
	public V2Download getCatalog() throws IOException {
		V2Download download = new V2Download( "catalog", "card" );
		log.info( "Get artifact: " + download.getKey() );

		String products = Files.list( root ).filter( ( f ) -> Files.isDirectory( f ) ).filter( this::hasProductCard ).map( ( f ) -> "\"" + f.getFileName().toString() + "\"" ).collect( Collectors.joining( "," ) );

		StringBuilder builder = new StringBuilder();
		builder.append( "{" );
		builder.append( "\"timestamp\":\"" ).append( System.currentTimeMillis() ).append( "\"," );
		builder.append( "[" );
		builder.append( products );
		builder.append( "]" );
		builder.append( "}" );

		// TODO The catalog should probably be cached
		byte[] catalog = builder.toString().getBytes( StandardCharsets.UTF_8 );

		download.setSize( catalog.length );
		download.setInputStream( new ByteArrayInputStream( catalog ) );

		return download;
	}

	@Override
	public V2Download getDownload( String artifact, String platform, String asset, String format ) throws IOException {
		V2Download download = new V2Download( artifact, platform, asset, format );
		log.info( "Get artifact: " + download.getKey() );

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

	private boolean hasProductCard( Path path ) {
		if( Files.exists( path.resolve( "product.card" ) ) ) {
			return true;
		} else {
			try {
				for( Path child : Files.list( path ).filter( Files::isDirectory ).collect( Collectors.toList() ) ) {
					if( hasProductCard( child ) ) return true;
				}
			} catch( IOException exception ) {
				log.warn( "Error listing path: " + path, exception );
			}
		}
		return false;
	}

}
