package com.avereon.www.download;

import com.avereon.product.ProductCard;
import com.avereon.util.Log;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class V2LocalDownloadProvider implements V2DownloadProvider {

	private static final System.Logger log = Log.get();

	private Path root;

	V2LocalDownloadProvider( String root ) {
		this( Paths.get( root ) );
	}

	V2LocalDownloadProvider( Path root ) {
		this.root = root;
	}

	@Override
	public V2Download getCatalog(Map<String,String> query) throws IOException {
		V2Download download = new V2Download( "catalog", "card", query );
		log.log( Log.INFO, "Get artifact: " + download.getKey() );

		String products;
		try( Stream<Path> children = Files.list( root ) ) {
			products = children
				.filter( ( f ) -> Files.isDirectory( f ) )
				.filter( this::hasProductCard )
				.map( ( f ) -> "\"" + f.getFileName().toString() + "\"" )
				.collect( Collectors.joining( "," ) );
		}

		StringBuilder builder = new StringBuilder();
		builder.append( "{" );
		builder.append( "\"timestamp\":\"" ).append( System.currentTimeMillis() ).append( "\"," );
		builder.append( "\"products\":[" );
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
	public V2Download getDownload( String artifact, String platform, String asset, String format, Map<String,String> query ) throws IOException {
		V2Download download = doGetDownload( artifact, platform, asset, format, query );

		// Fallbacks
		if( download == null ) download = doGetDownload( artifact, null, asset, format, query );
		if( download == null ) download = doGetDownload( artifact, platform, asset, null, query );
		if( download == null ) download = doGetDownload( artifact, null, asset, null, query );

		return download;
	}

	private V2Download doGetDownload( String artifact, String platform, String asset, String format, Map<String,String> query ) throws IOException {
		V2Download download = new V2Download( artifact, platform, asset, format, query );
		log.log( Log.INFO, "Get artifact: " + download.getKey() );

		// TODO Opportunity to provide a cache here

		Path path = root.resolve( artifact );
		if( platform != null ) path = path.resolve( platform );
		path = path.resolve( download.getLocalFilename() );
		if( !Files.exists( path ) ) log.log( Log.WARN, "Artifact path not found: " + path );
		if( !Files.exists( path ) ) return null;

		ProductCard card = getProductCard( path.getParent() );
		if( card != null ) {
			download.setGroup( card.getGroup() );
			download.setName( card.getName() );
			download.setVersion( card.getVersion() );
		}
		download.setSize( Files.size( path ) );
		download.setInputStream( Files.newInputStream( path ) );

		return download;
	}

	private ProductCard getProductCard( Path root ) {
		Path path = root.resolve( "product.card" );
		if( !Files.exists( path ) ) return null;

		ProductCard card = new ProductCard();
		try {
			card.load( new FileInputStream( path.toFile() ), null );
		} catch( IOException exception ) {
			log.log( Log.ERROR, "Could not load product card: " + path, exception );
			return null;
		}

		return card;
	}

	private boolean hasProductCard( Path path ) {
		if( Files.exists( path.resolve( "product.card" ) ) ) {
			return true;
		} else {
			try( Stream<Path> children = Files.list( path ) ) {
				for( Path child : children.filter( Files::isDirectory ).collect( Collectors.toList() ) ) {
					if( hasProductCard( child ) ) return true;
				}
			} catch( IOException exception ) {
				log.log( Log.WARN, "Error listing path: " + path, exception );
			}
		}
		return false;
	}

}
