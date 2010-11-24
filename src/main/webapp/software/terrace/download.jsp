<%@ page language="java"%>
<%@ page import="com.parallelsymmetry.site.Descriptor"%>

<%
	long timestamp = System.currentTimeMillis();

	String mavenRelease = "http://mvn.parallelsymmetry.com/release/com/parallelsymmetry/terrace";
	String mavenSnapshot = "http://mvn.parallelsymmetry.com/snapshot/com/parallelsymmetry/terrace";

	Descriptor descriptor = new Descriptor( mavenRelease + "/maven-metadata.xml" );
	String version = descriptor.getValue( "metadata/versioning/release" );
%>

<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>Download</title>
<meta name="keywords" content="java, application, terrace" />
</head>

<body>

<h1>Download</h1>

<p>Version found: <%=version%></p>

</body>

</html>
