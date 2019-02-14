package com.xeomar.web.root;

import com.xeomar.util.LogUtil;
import com.xeomar.util.TextUtil;
import com.xeomar.util.Version;
import org.slf4j.Logger;
import org.w3c.dom.Text;

import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LocalStoreDownloadProvider extends AbstractDownloadFactory {

	private static final Logger log = LogUtil.get( MethodHandles.lookup().lookupClass() );

	private static String ROOT = "/opt/xeo/store";

	private static String GROUP = "com.xeomar";

	@Override
	public List<ProductDownload> getDownloads( String artifact, String classifier, String type ) {
		return getDownloads( List.of( artifact ), classifier, type, null );
	}

	@Override
	public List<ProductDownload> getDownloads( String artifact, String classifier, String type, String version ) {
		return getDownloads( List.of( artifact ), classifier, type, version );
	}

	@Override
	public List<ProductDownload> getDownloads( List<String> artifacts, String classifier, String type, String version ) {
		List<ProductDownload> downloads = new ArrayList<>();

		String platform = "linux";

		for( String artifact : artifacts ) {
			String key = getDownloadKey( artifact, classifier, type, version );
			Version artifactVersion = new Version( version );
			// TODO Get the name and version from the product card
			String name = "Xenon";
			String filename = getFilename( platform, classifier, type );
			String link = Paths.get( ROOT, version, artifact, filename ).toUri().toString();
			String md5Link = "";
			String sha1Link = "";
			downloads.add( new ProductDownload( key, GROUP, artifact, artifactVersion, classifier, type, name, link, md5Link, sha1Link ) );
		}

		return downloads;
	}

	@Override
	public String clearCache( String artifact, String category, String type, String channel ) {
		return null;
	}

	@Override
	public String clearCache() {
		log.info( "Cache cleared for all" );
		return "Cache cleared for all";
	}

	private String getFilename( String platform, String category, String type ) {
		StringBuilder builder = new StringBuilder();

		if( !TextUtil.isEmpty( platform ) ) builder.append( platform ).append( "-" );
		builder.append( category ).append( "." ).append( type );

		return builder.toString();
	}

}
