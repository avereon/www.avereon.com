<%@ page language="java"%>

<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.List"%>

<%@ page import="com.parallelsymmetry.site.*"%>

<%
	String unknown = "Unknown";

	String redirect = request.getParameter( "redirect" );
	String resource = request.getParameter( "resource" );

	int index = resource.lastIndexOf( "/" );
	String group = null;
	String artifact = resource;
	if( index > -1 ) {
		group = resource.substring( 0, index ).replace( '/', '.' );
		artifact = resource.substring( index + 1 );
	}

	char[] chars = artifact.toCharArray();
	chars[0] = Character.toUpperCase( chars[0] );
	String name = new String( chars );

	String mavenRelease = "http://mvn.parallelsymmetry.com/release";
	String mavenSnapshot = "http://mvn.parallelsymmetry.com/snapshot";

	Calendar calendar = Calendar.getInstance( request.getLocale() );
	SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm a" );
	dateFormat.setCalendar( calendar );

	// Get the entire list of downloads for an artifact.
	List<MavenDownload> downloads = MavenDownload.getDownloads( mavenRelease + resource, mavenSnapshot + resource );

	// Get the metadata from the most recent download.
	if( downloads.size() > 0 ) {
		MavenDownload download = downloads.get( 0 );
		name = download.getName();
		group = download.getGroupId();
		artifact = download.getArtifactId();
	}

	// Split the downloads into groups.
	List<MavenDownload> prod = new ArrayList<MavenDownload>();
	List<MavenDownload> beta = new ArrayList<MavenDownload>();
	List<MavenDownload> alpha = new ArrayList<MavenDownload>();
	List<MavenDownload> snapshot = new ArrayList<MavenDownload>();

	for( MavenDownload download : downloads ) {
		if( !download.getVersion().isSnapshot() ) {
			prod.add( download );
		} else if( "beta".equalsIgnoreCase( download.getVersion().getState() ) ) {
			beta.add( download );
		} else if( "alpha".equalsIgnoreCase( download.getVersion().getState() ) ) {
			alpha.add( download );
		} else if( download.getVersion().isSnapshot() ) {
			snapshot.add( download );
		}
	}
%>

<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>Download</title>
</head>

<body>

<h1><%=name%> Downloads</h1>

<%
	if( resource == null ) {
%>
<p>Please select a product to download...</p>
<ul>
	<li><a href="?resource=/com/parallelsymmetry/terrace">Terrace</a></li>
</ul>
<%
	} else if( downloads.size() == 0 ) {
%>

<p>No downloads available.</p>

<%
	} else {
%>

<table class="download" width="80%">
	<colgroup>
		<col width="30%" />
		<col width="15%" />
		<col width="15%" />
		<col width="30%" />
		<col width="5%" />
		<col width="5%" />
	</colgroup>

	<%
		if( prod.size() > 0 ) {
				MavenDownload download = prod.get( 0 );
	%>
	<tr>
		<th colspan="100">Current Release</th>
	</tr>
	<tr>
		<td><a href="<%=download.getLink()%>"><%=download.getName()%></a></td>
		<td><%=download.getVersion().getFullVersion()%></td>
		<td><%=download.getLength()%></td>
		<td><%=download.formatDate( dateFormat, unknown )%></td>
		<td><a href="<%=download.getMd5Link()%>">MD5</a></td>
		<td><a href="<%=download.getSha1Link()%>">SHA1</a></td>
	</tr>

	<%
		}
	%>

	<%
		if( snapshot.size() > 0 ) {
				MavenDownload download = snapshot.get( 0 );
	%>
	<tr>
		<th colspan="100">Development Release</th>
	</tr>
	<tr>
		<td><a href="<%=download.getLink()%>"><%=download.getName()%></a></td>
		<td><%=download.getVersion().getFullVersion()%></td>
		<td><%=download.getLength()%></td>
		<td><%=download.formatDate( dateFormat, unknown )%></td>
		<td><a href="<%=download.getMd5Link()%>">MD5</a></td>
		<td><a href="<%=download.getSha1Link()%>">SHA1</a></td>
	</tr>
	<%
		}
	%>

	<%
		if( prod.size() > 1 ) {
	%>
	<tr>
		<th colspan="100">Previous Releases</th>
	</tr>

	<%
		int count = prod.size();
				for( int downloadIndex = 1; downloadIndex < count; downloadIndex++ ) {
					MavenDownload download = prod.get( downloadIndex );
	%>
	<tr>
		<td><a href="<%=download.getLink()%>"><%=download.getName()%></a></td>
		<td><%=download.getVersion().getFullVersion()%></td>
		<td><%=download.getLength()%></td>
		<td><%=download.formatDate( dateFormat, unknown )%></td>
		<td><a href="<%=download.getMd5Link()%>">MD5</a></td>
		<td><a href="<%=download.getSha1Link()%>">SHA1</a></td>
	</tr>
	<%
		}
	%>

	<%
		}
	%>

</table>

<%
	}
%>

</body>

</html>
