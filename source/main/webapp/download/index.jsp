<%@ page language="java"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.parallelsymmetry.site.*"%>

<%
	String resource = request.getParameter( "resource" );
	String redirect = request.getParameter( "redirect" );

	String mavenRelease = "http://mvn.parallelsymmetry.com/release";
	String mavenSnapshot = "http://mvn.parallelsymmetry.com/snapshot";

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
<h2>Production Releases</h2>

<table class="download" width="80%">
	<colgroup>
		<col width="30%" />
		<col width="30%" />
		<col width="20%" />
		<col width="10%" />
		<col width="10%" />
	</colgroup>
	<%
		for( MavenDownload download : prod ) {
	%>
	<tr>
		<td><%=download.getName()%></td>
		<td><%=download.getVersion().getFullVersion()%></td>
		<td><a href="<%=download.getLink()%>">Download</a></td>
		<td><a href="<%=download.getMd5Link()%>">MD5</a></td>
		<td><a href="<%=download.getSha1Link()%>">SHA1</a></td>
	</tr>
	<%
		}
	%>
</table>

<h2>Development Releases</h2>

<table class="download" width="80%">
	<colgroup>
		<col width="30%" />
		<col width="30%" />
		<col width="20%" />
		<col width="10%" />
		<col width="10%" />
	</colgroup>
	<%
		for( MavenDownload download : snapshot ) {
	%>
	<tr>
		<td><%=download.getName()%></td>
		<td><%=download.getVersion().getFullVersion()%></td>
		<td><a href="<%=download.getLink()%>">Download</a></td>
		<td><a href="<%=download.getMd5Link()%>">MD5</a></td>
		<td><a href="<%=download.getSha1Link()%>">SHA1</a></td>
	</tr>
	<%
		}
	%>
</table>

<!-- Give instructions how to add the latest release to a Maven project. -->

<%
	}
%>

</body>

</html>
