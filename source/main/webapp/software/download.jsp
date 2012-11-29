<%@ page language="java"%>

<%@ page import="java.net.URL"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.TimeZone"%>

<%@ page import="com.parallelsymmetry.site.*"%>

<%
	String unknown = "Unknown";

	boolean refresh = "true".equals( request.getParameter( "refresh" ) );
	String resource = request.getParameter( "resource" );
	String classifier = request.getParameter( "classifier" );
	String type = request.getParameter( "type" );
	String redirect = request.getParameter( "redirect" );

	String contextPath = request.getContextPath();

	String name = "";
	String group = null;
	String artifact = resource;
	String repository = "http://code.parallelsymmetry.com/repo/psm";

	Calendar calendar = Calendar.getInstance( request.getLocale() );
	calendar.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
	SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	dateFormat.setCalendar( calendar );

	// If there is a resource get an initial name.
	if( resource != null ) {
		int index = resource.lastIndexOf( "/" );
		if( index > -1 ) {
			group = resource.substring( 0, index ).replace( '/', '.' );
			artifact = resource.substring( index + 1 );
		}

		char[] chars = artifact.toCharArray();
		chars[0] = Character.toUpperCase( chars[0] );
		name = new String( chars );
	}

	// Get the entire list of downloads for an artifact.
	List<MavenDownload> downloads = MavenDownload.getDownloads( repository + resource, classifier, type );

	// Get the metadata from the most recent download.
	if( downloads.size() > 0 ) {
		MavenDownload download = downloads.get( 0 );
		String downloadName = download.getName();
		group = download.getGroupId();
		artifact = download.getArtifactId();
		if( downloadName != null ) name = downloadName;
	}

	// Split the downloads into groups.
	List<MavenDownload> prod = new ArrayList<MavenDownload>();
	List<MavenDownload> beta = new ArrayList<MavenDownload>();
	List<MavenDownload> alpha = new ArrayList<MavenDownload>();
	List<MavenDownload> snapshot = new ArrayList<MavenDownload>();

	for( MavenDownload download : downloads ) {
		if( download.getVersion().isSnapshot() ) {
			snapshot.add( download );
		} else {
			if( download.getVersion().hasQualifier( "alpha" ) ) {
				alpha.add( download );
			} else if( download.getVersion().hasQualifier( "beta" ) ) {
				beta.add( download );
			} else {
				prod.add( download );
			}
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

	<h1><%=name%>
		Downloads
	</h1>

	<%
		if( refresh ) {
			MavenDownload.clearCache();
	%>
	<p>Download cache refreshed.</p>
	<%
		} else if( resource == null ) {
	%>
	<p>Please select a product to download...</p>

	<table class="layout">
		<tr>
			<td>
				<!-- Escape download ticket --> <a class="ticket" href="?resource=/com/parallelsymmetry/escape&classifier=install">
					<span class="ticket"><img src="../images/badges/escape.png" /><span><span class="title">Escape</span>Download
							Now</span> </span> </a>
			</td>
			<td>
				<p>
					<a href="escape/">Escape</a> is an open, lightweight application framework for the development of cross-platform
					applications. As a framework, Escape relies on modules to provide specific functionality. Several modules are
					available from this web site.
				</p>
			</td>
		</tr>
		<tr>
			<td>
				<!--  Terrace download ticket --> <a class="ticket" href="?resource=/com/parallelsymmetry/terrace"> <span
					class="ticket"><img src="../images/badges/terrace.png" /><span><span class="title">Terrace</span>Download
							Now</span> </span> </a>
			</td>
			<td>

				<p>
					<a href="terrace/">Terrace</a> is a lightweight web content generation tool. Web content is created using simple
					HTML or JSP files and merged with a template and navigation tools are dynamically generated.
				</p>
			</td>
		</tr>
		<tr>

			<td>
				<!--  Velocity download ticket --> <a class="ticket"
				href="?resource=/com/parallelsymmetry/velocity&classifier=install"> <span class="ticket"><img
						src="../images/badges/velocity.png" /><span><span class="title">Velocity</span>Download Now</span> </span> </a>
			</td>
			<td>

				<p>
					<a href="velocity/">Velocity</a> is an open, lightweight application framework for the development of
					cross-platform applications. As a framework Velocity has no specific capabilities. It relies on modules to provide
					functionality. Several modules are available from this web site.
				</p>
			</td>
		</tr>
		<tr>
			<td>
				<!--  Elements download ticket --> <a class="ticket" href="elements/install.jsp"> <span class="ticket"><img
						src="../images/badges/elements.png" /><span><span class="title">Elements</span>Install Now</span> </span> </a>
			</td>
			<td>

				<p>
					<a href="elements/">Elements</a> is a 3D computer aided engineering module for Velocity.It is intended for general
					engineering needs and features common 2D/3D entities and editing tools.
				</p>
			</td>
		</tr>
	</table>
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
			<col width="20%" />
			<col width="30%" />
			<col width="10%" />
			<col width="10%" />
		</colgroup>

		<%
			if( prod.size() > 0 ) {
					MavenDownload download = prod.get( 0 );
		%>
		<tr>
			<th colspan="100">Current Release</th>
		</tr>
		<tr>
			<td><a href="<%=download.getDownloadLink( contextPath )%>"><%=name%> <%=download.getVersion().toString()%></a></td>
			<td><%=download.getHumanReadableLength()%></td>
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
			<td><a href="<%=download.getDownloadLink( contextPath )%>"><%=name%> <%=download.getVersion().toString()%></a></td>
			<td><%=download.getHumanReadableLength()%></td>
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
			<td><a href="<%=download.getDownloadLink( contextPath )%>"><%=name%> <%=download.getVersion().toString()%></a></td>
			<td><%=download.getHumanReadableLength()%></td>
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
