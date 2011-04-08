<%@ page language="java"%>

<%@ page import="java.util.Date"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%
	DateFormat UPDATED_DATE_FORMAT = new SimpleDateFormat( "MMM dd, yyyy" );
	DateFormat COPYRIGHT_DATE_FORMAT = new SimpleDateFormat( "yyyy" );
%>

<%@page import="java.text.DateFormat;"%>

<div id="footer">
	<table class="layout" style="width: 100%;" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="white-space: nowrap; vertical-align: top;"><a href="copyright.html">&copy; <%=COPYRIGHT_DATE_FORMAT.format( new Date() )%> Parallel Symmetry</a><br />
				<a href="mailto:contact@parallelsymmetry.com">Contact Us</a>
			</td>
			<td style="white-space: nowrap; text-align: right; vertical-align: top;">Updated: <%=UPDATED_DATE_FORMAT.format( new Date() )%>
			</td>
		</tr>
	</table>
</div>