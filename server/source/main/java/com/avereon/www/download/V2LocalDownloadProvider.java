package com.avereon.www.download;

import com.avereon.log.LazyEval;
import com.avereon.product.ProductCard;
import lombok.CustomLog;

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

@CustomLog
public class V2LocalDownloadProvider implements V2DownloadProvider {

	private final Path root;

	V2LocalDownloadProvider( String root ) {
		this( Paths.get( root ) );
	}

	V2LocalDownloadProvider( Path root ) {
		this.root = root;
	}

	@Override
	public V2Download getCatalog( Map<String, String> query ) throws IOException {
		V2Download download = new V2Download( "catalog", "card", query );
		log.atInfo().log( "Get artifact: %s", LazyEval.of( download::getKey ) );

		String products;
		try( Stream<Path> children = Files.list( root ) ) {
			products = children.filter( Files::isDirectory ).filter( this::hasProductCard ).map( ( f ) -> "\"" + f.getFileName().toString() + "\"" ).collect( Collectors.joining( "," ) );
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
	public V2Download getDownload( String artifact, String platform, String asset, String format, Map<String, String> query ) throws IOException {
		V2Download download = doGetDownload( artifact, platform, asset, format, query );

		// Fallbacks
		if( download == null ) download = doGetDownload( artifact, null, asset, format, query );
		if( download == null ) download = doGetDownload( artifact, platform, asset, null, query );
		if( download == null ) download = doGetDownload( artifact, null, asset, null, query );

		return download;
	}

	private V2Download doGetDownload( String artifact, String platform, String asset, String format, Map<String, String> query ) throws IOException {
		V2Download download = new V2Download( artifact, platform, asset, format, query );
		log.atInfo().log( "Get artifact: %s", LazyEval.of( download::getKey ) );

		// TODO Opportunity to provide a cache here

		Path path = root.resolve( V2Download.removeSpecial( artifact ) );
		if( platform != null ) path = path.resolve( V2Download.removeSpecial( platform ) );
		path = path.resolve( V2Download.removeSpecial( download.getLocalFilename() ) );
		if( !Files.exists( path ) ) log.atWarn().log( "Artifact path not found: %s", path );
		if( !Files.exists( path ) ) return null;

		ProductCard card = getProductCard( path.getParent(), query );
		if( card != null ) {
			download.setGroup( card.getGroup() );
			download.setName( card.getName() );
			download.setVersion( card.getVersion() );
		}
		download.setSize( Files.size( path ) );
		download.setInputStream( Files.newInputStream( path ) );

		return download;
	}

	private ProductCard getProductCard( Path root, Map<String, String> query ) {
		Path path = root.resolve( V2Download.localFilename( null, null, "product", "card", query ) );
		if( !Files.exists( path ) ) return null;

		ProductCard card = new ProductCard();
		try {
			card.fromJson( new FileInputStream( path.toFile() ), null );
		} catch( IOException exception ) {
			log.atError( exception ).log( "Could not load product card: %s", path );
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
				log.atWarn( exception ).log( "Error listing path: %s", path );
			}
		}
		return false;
	}

}
