<%@ page language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.parallelsymmetry.site.Download"%>

<%
	long timestamp = System.currentTimeMillis();

	String mavenRelease = "http://mvn.parallelsymmetry.com/release";
	String mavenSnapshot = "http://mvn.parallelsymmetry.com/snapshot";

	List<Download> releaseVersions = Download.getDownloads( mavenRelease + "/com/parallelsymmetry/terrace", Download.RELEASE );
	List<Download> snapshotVersions = Download.getDownloads( mavenSnapshot + "/com/parallelsymmetry/terrace", Download.SNAPSHOT );
%>

<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>Download</title>
</head>

<body>

<h1>Download</h1>

<h2>Production Releases</h2>

<p>
<%
	for( Download download : releaseVersions ) {
%><a href="<%=download.getLink()%>"><%=download.getName()%> <%=download.getVersion()%></a><br />
<%
	}
%>
</p>

<h2>Development Releases</h2>

<p>
<%
	for( Download download : snapshotVersions ) {
%><a href="<%=download.getLink()%>"><%=download.getName()%> <%=download.getVersion()%></a><br />
<%
	}
%>
</p>


</body>

</html>
