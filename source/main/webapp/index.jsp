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
<h1>Get Something Done</h1>

<table class="layout">
	<tr>
		<td>
		
		<!-- Escape download ticket -->
		<a class="ticket" href="software/escape/">
			<span class="ticket"><img src="images/badges/escape.png" /><span><span class="title">Escape</span>View Project</span></span>
		</a>
		
		<p><a href="software/escape/">Escape</a> is an open, lightweight
		application framework for the development of cross-platform applications. As a
		framework, Escape relies on modules to provide specific functionality. Several
		modules are available from this web site.</p>

		<!--  Terrace download ticket -->
		<a class="ticket" href="download/index.jsp?resource=/com/parallelsymmetry/terrace">
			<span class="ticket"><img src="images/badges/terrace.png" /><span><span class="title">Terrace</span>Download Now</span></span>
		</a>

		<p><a href="software/terrace/index.jsp">Terrace</a> is a lightweight web content
		generation tool. Web content is created using simple HTML or JSP files
		and merged with a template and navigation tools are dynamically generated.</p>
		</td>

		<td>

		<!--  Velocity download ticket -->
		<a class="ticket" href="http://www.parallelsymmetry.com/software/velocity/update/velocity-install.jar">
			<span class="ticket"><img src="images/badges/velocity.png" /><span><span class="title">Velocity</span>Download Now</span></span>
		</a>

		<p><a href="software/velocity/index.jsp">Velocity</a> is an open,
		lightweight application framework for the development of cross-platform
		applications. As a framework Velocity has no specific capabilities. It relies
		on modules to provide functionality. Several modules are available from this
		web site.</p>

		<!--  Elements download ticket -->
		<a class="ticket" href="http://www.parallelsymmetry.com/software/elements/install.jsp">
			<span class="ticket"><img src="images/badges/elements.png" /><span><span class="title">Elements</span>Install Now</span></span>
		</a>

		<p><a href="software/elements/index.jsp">Elements</a> is a 3D computer
		aided engineering module for Velocity.It is intended for general engineering
		needs and features common 2D/3D entities and editing tools.</p>
		</td>
	</tr>
</table>
</body>

</html>
