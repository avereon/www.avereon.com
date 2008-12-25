<%@ page language="java"%>

<%@ page import="java.util.Date"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%
	DateFormat format = new SimpleDateFormat( "MMM dd, yyyy" );
	DateFormat COPYRIGHT_DATE_FORMAT = new SimpleDateFormat( "yyyy" );
%>

<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>Parallel Symmetry</title>

<meta http-equiv="keywords" content="aerodynamics,aviation,java" />
<meta http-equiv="description" content="Parallel Symmetry home page." />
<meta name="verify-v1" content="u/Bs7Tymn57Yc5qYRfRyelWL4AZeVynsFA72klY0MFI=" />
</head>

<body>
<h1>Welcome</h1>

<p>Parallel Symmetry provides resources for specialized projects related to
model aircraft engineering and software development as well as related topics
like mathematics, geometry, and physics.</p>

<table class="layout">
	<tr>
		<td><a href="projects/velocity/index.jsp"><img
			style="vertical-align: middle;" src="images/velocity.64.png" width="64"
			height="64" /></a></td>
		<td>
		<h2>Velocity</h2>
		<a href="projects/velocity/install.jsp">Install</a></td>
	</tr>
</table>
<p><a href="projects/velocity/index.jsp">Velocity</a> is an open,
lightweight application framework for the development of cross-platform
applications. As a framework Velocity has no specific capabilities. It relies on
modules to provide specific functionality. Several modules are available from
this web site.</p>

<table class="layout">
	<tr>
		<td><a href="projects/elements/index.jsp"><img
			style="vertical-align: middle;" src="images/elements.64.png" width="64"
			height="64" /></a></td>
		<td>
		<h2>Elements</h2>
		<a href="projects/elements/install.jsp">Install</a></td>
	</tr>
</table>
<p><a href="projects/elements/index.jsp">Elements</a> is a 3D computer aided
engineering module for Velocity.It is intended for general engineering needs and
features common 2D/3D entities and editing tools.</p>

</body>

</html>
