<%@ page language="java"%>

<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.List"%>

<%@ page import="com.parallelsymmetry.site.*"%>

<%
	String resource = request.getParameter( "resource" );
	String redirect = request.getParameter( "redirect" );

	String mavenRelease = "http://mvn.parallelsymmetry.com/release";
	String mavenSnapshot = "http://mvn.parallelsymmetry.com/snapshot";

	Calendar calendar = Calendar.getInstance( request.getLocale() );
	SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm a" );
	dateFormat.setCalendar( calendar );

	List<MavenDownload> downloads = MavenDownload.getDownloads( mavenRelease + resource, mavenSnapshot + resource );

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

<h1>Download</h1>

<%
	if( resource == null ) {
%>
<p>Please select a product to download...</p>
<ul>
	<li><a href="?resource=/com/parallelsymmetry/terrace">Terrace</a></li>
</ul>
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
		<td colspan="100">
		<h2>Current Release</h2>
		</td>
	</tr>
	<tr>
		<td><a href="<%=download.getLink()%>"><%=download.getName()%></a></td>
		<td><%=download.getVersion().getFullVersion()%></td>
		<td><%=download.getLength()%></td>
		<td><%=dateFormat.format( download.getDate() )%></td>
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
		<td colspan="100">
		<h2>Development Release</h2>
		</td>
	</tr>
	<tr>
		<td><a href="<%=download.getLink()%>"><%=download.getName()%></a></td>
		<td><%=download.getVersion().getFullVersion()%></td>
		<td><%=download.getLength()%></td>
		<td><%=dateFormat.format( download.getDate() )%></td>
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
		<td colspan="100">
		<h2>Previous Releases</h2>
		</td>
	</tr>

	<%
		int count = prod.size();
				for( int index = 1; index < count; index++ ) {
					MavenDownload download = prod.get( index );
	%>
	<tr>
		<td><a href="<%=download.getLink()%>"><%=download.getName()%></a></td>
		<td><%=download.getVersion().getFullVersion()%></td>
		<td><%=download.getLength()%></td>
		<td><%=dateFormat.format( download.getDate() )%></td>
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
