<%@ page language="java"%>

<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
DateFormat UPDATED_DATE_FORMAT = new SimpleDateFormat( "MMM dd, yyyy" );
DateFormat COPYRIGHT_DATE_FORMAT = new SimpleDateFormat( "yyyy" );
%>

<%@page import="java.text.DateFormat;"%>
<img class="hdivider" style="margin: 10px 0px 0px 0px;" src="images/pixel.gif"/>
<table class="layout" style="width: 100%; margin=0px;" border="0" cellpadding="0" cellspacing="2">
	<tr>
		<td style="white-space: nowrap; vertical-align: top;">
			<a class="footer" href="copyright.html">&copy;&nbsp;2000-<%=COPYRIGHT_DATE_FORMAT.format(new Date())%>&nbsp;Mark&nbsp;Soderquist&nbsp;All&nbsp;rights&nbsp;reserved.</a><br/>
			<a class="footer" href="mailto:support@parallelsymmetry.com">Support: support@parallelsymmetry.com</a>
		</td>
		<td style="white-space: nowrap; text-align: right; vertical-align: top;">
			<div class="footer">Updated: <%=UPDATED_DATE_FORMAT.format( new Date() )%></div>
		</td>
	</tr>
</table>
