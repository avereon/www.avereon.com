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

	String resource = request.getParameter( "resource" );
	String classifier = request.getParameter( "classifier" );
	String type = request.getParameter( "type" );
	String redirect = request.getParameter( "redirect" );

	String contextPath = request.getContextPath();

	String name = "";
	String group = null;
	String artifact = resource;
	String repository = "http://mvn.parallelsymmetry.com/content/groups/psm";

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
	List<MavenDownload> downloads = MavenDownload.getDownloads( classifier, type, repository + resource );

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

<h1><%=name%> Downloads</h1>

<%
	if( resource == null ) {
%>
<p>Please select a product to download...</p>

<ul>
	<li><a href="?resource=/com/parallelsymmetry/escape&classifier=install">Escape</a></li>
	<li><a href="?resource=/com/parallelsymmetry/terrace">Terrace</a></li>
	<li><a href="?resource=/com/parallelsymmetry/velocity&classifier=install">Velocity</a></li>
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
		<td><a href="<%=download.getDownloadLink( contextPath )%>"><%=name%>
		<%=download.getVersion().toString()%></a></td>
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