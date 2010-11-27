<%@ page language="java"%>

<%@ page import="java.util.List"%>
<%@ page import="com.parallelsymmetry.site.*"%>

<%
	String resource = request.getParameter( "resource" );
	String redirect = request.getParameter( "redirect" );

	String mavenRelease = "http://mvn.parallelsymmetry.com/release";
	String mavenSnapshot = "http://mvn.parallelsymmetry.com/snapshot";

	List<MavenDownload> releases = MavenDownload.getDownloads( mavenRelease + resource, mavenSnapshot + resource );
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

<p>
<%
	for( MavenDownload download : releases ) {
			if( !download.getVersion().isSnapshot() ) {
%><a href="<%=download.getLink()%>"><%=download.getName()%> <%=download.getVersion().getFullVersion()%></a><br />
<%
	}
		}
%>
</p>

<h2>Development Releases</h2>

<p>
<%
	for( MavenDownload download : releases ) {
			if( download.getVersion().isSnapshot() ) {
%><a href="<%=download.getLink()%>"><%=download.getName()%> <%=download.getVersion().getFullVersion()%></a><br />
<%
	}
		}
%>
</p>

<!-- Give instructions how to add the latest release to a Maven project. -->

<%
	}
%>

</body>

</html>
