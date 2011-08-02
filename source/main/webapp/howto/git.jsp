<%@ page language="java"%>

<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>

<head>
<title>GIT Over HTTP</title>
</head>

<body>
	<h1>How To Configure GIT Over HTTP</h1>

	<p>
		<a href="http://git-scm.com/">Git</a> is a free & open source, distributed version control system designed to handle
		everything from small to very large projects with speed and efficiency. This how-to provides consice instructions how
		to configure an <a href="http://httpd.apache.org/">Apache HTTP server</a> to provide access to a Git repository over
		HTTP.
	</p>


	<!-- Prerequisites -->
	<h2 id="prerequisites">Prerequisites</h2>
	<ul>
		<li>Apache Web Server installed on server.</li>
		<li>Existing bare Git repositories on server.</li>
		<li>Git installed on server.</li>
		<li>Git installed on client.</li>
	</ul>
	<p>The examples use the following assumtions:</p>
	<ul>
		<li>The git server repositories are all located under /var/www/git.</li>
		<li>Git scripts are installed in /usr/lib/git-core.</li>
	</ul>


	<!-- Base Configuration -->
	<h2 id="base">Base Configuration</h2>
	<p>
		The most basic configuration allows anonymous read access to the Git repository using the <a
			href="http://www.kernel.org/pub/software/scm/git/docs/git-http-backend.html"> <code>git-http-backend</code> </a>
		script provided with Git versions 1.6.6 and greater.
	</p>
	<p>
		The only thing to do is modify the Apache configuration to use the
		<code>git-http-backend</code>
		script. Change the VirtualHost section to look like the following:
	</p>
	<pre>&lt;VirtualHost 10.20.30.40:80&gt;
DocumentRoot /var/www/git
ServerName git.example.com

&lt;Directory "/var/www/git"&gt;
Allow from All
Options +ExecCGI
AllowOverride All
&lt;/Directory&gt;

SetEnv GIT_HTTP_EXPORT_ALL
SetEnv GIT_PROJECT_ROOT /var/www/git
ScriptAlias /git /usr/lib/git-core/git-http-backend
&lt;/VirtualHost&gt;
</pre>
	<p>At this point an anonymous user should be able to clone the project repository by issuing the following command:</p>
	<pre>git clone http://www.example.com/git/projects/example</pre>
	<p>
		<span class="note">Note:</span> Users are not yet able to push changes back to the repository.
	</p>
	<p>
		<span class="note">Note:</span> Adding the SuExec directive in the base configuration will cause the
		<code>git-http-backend</code>
		script to cease functioning correctly. See <a href="#suexec">Adding the SuExec Directive</a> section below.
	</p>


	<!-- Anonymous Write Access -->
	<h2 id="anonymous-write">Anonymous Write Access</h2>
	<p>
		<span class="warning">Warning:</span> This section describes how to allow anonymous write access to the repositories.
		This information is useful for debugging purposes but should not be considered for production purposes. For production
		see the <a href="#authenticated-access">Authenticated Access</a> section below.
	</p>
	<p>
		The <a href="#base">base configuration</a> above does not allow for users to push changes back to the repository. This
		is because the default settings of the
		<code>git-http-backend</code>
		script disable this feature by default if users are not authenticated. To enable write access to a repository the
		<code>http.receivepack</code>
		setting must be set to true. This can be done on the server by changing to the repository directory and using git to
		change the setting.
	</p>
	<pre>cd /var/www/git/projects/example
git config http.receivepack true</pre>
	<p>
		Now any user can push changes back to the repository using
		<code>git pull</code>
		.
	</p>
	<p>
		<span class="note">Note:</span> The following error usually indicates the
		<code>http.receivepack</code>
		setting is not set correctly.
	</p>
	<pre>error: Cannot access URL http://www.example.com/git/projects/example/, return code 22</pre>
	<p>
		<span class="note">Note:</span> Once testing is complete this should be disabled by changing the
		<code>http.receivepack</code>
		value back to
		<code>false</code>
		.
	</p>


	<!-- Authenticated Access -->
	<h2 id="authenticated-access">Authenticated Access</h2>
	<p>Under normal circumstances users should be authenticated before accessing the repositories. The simplest thing
		to do is force authentication for all access, both reading from and writing to the repository. To enable
		authentication for both reads and writes add the following section to the Apache configuration:</p>
	<pre>&lt;VirtualHost 10.20.30.40:80&gt;
...
&lt;Location /git&gt;
  AuthType Basic
  AuthName "Private Git Access"
  AuthUserFile "/etc/git-auth-file"
  Require valid-user
&lt;/Location&gt;
...
&lt;/VirtualHost&gt;</pre>
	<p>
		Users can be added to the
		<code>/etc/git-auth-file</code>
		using the htpasswd tool available with Apache. There are <a
			href="http://httpd.apache.org/docs/current/howto/auth.html">other ways authentication can be configured through
			Apache</a> that will not be covered here. There are also <a
			href="http://www.kernel.org/pub/software/scm/git/docs/git-http-backend.html">examples of how to allow anonymous
			read access with authenticated write access</a> in the
		<code>git-http-backend</code>
		documentation.
	</p>


	<!-- Adding SuExec Directive -->
	<h2 id="suexec">Adding the SuExec Directive</h2>
	<p>
		It is generally good practice to execute CGI scripts as a different user than the web server user (usually apache).
		This is done by adding the SuExec directive to the Apache configuration. SuExec has a rigorous <a
			href="http://httpd.apache.org/docs/current/suexec.html#model">security model</a> in Apache and it is necessary to
		work within the scope of that model. Simply adding this directive in the base configuration will not work under normal
		circumstances because SuExec only passes through those variables whose names are listed in the safe environment list.
		In short the
		<code>GIT_HTTP_EXPORT_ALL</code>
		and the
		<code>GIT_PROJECT_ROOT</code>
		are not passed on to the
		<code>git-http-backend</code>
		script.
	</p>
	<p>
		To work with the SuExec security model a wrapper script needs to be create that configures the environment when SuExec
		executes the script. The script simply sets the correct environment variable and calls
		<code>git-http-backend</code>
		like before.
	</p>
	<pre>#!/bin/bash
PATH_INFO=$SCRIPT_URL
GIT_PROJECT_ROOT=/var/www/git
REMOTE_USER=$REDIRECT_REMOTE_USER
export GIT_HTTP_EXPORT_ALL=true
/usr/lib/git-core/git-http-backend</pre>
	<p>Once the wrapper script is in place the Apache configuration needs to be modified to use the wrapper script.</p>
	<pre>&lt;VirtualHost 10.20.30.40:80&gt;
DocumentRoot /var/www/git
ServerName git.example.com

&lt;Directory "/var/www/git"&gt;
Allow from All
Options +ExecCGI
AllowOverride All
&lt;/Directory&gt;

ScriptAlias /git /var/www/bin/git-http-backend
&lt;/VirtualHost&gt;
</pre>
	<p>
		After adding the SuExec directive Apache will now execute the
		<code>git-http-backend</code>
		script with the git user and group.
	</p>
	<p>
		<span class="note">Note:</span> The repositories must be modifiable by the user running the
		<code>git-http-backend</code>
		script.
	</p>


</body>

</html>

<!--
<VirtualHost 68.169.50.215:80>
DocumentRoot /git
ServerName git.parallelsymmetry.com

<Directory "/git">
Allow from All
Options ExecCGI Includes FollowSymLinks
AllowOverride All
</Directory>

SuexecUserGroup git git
ScriptAlias /git /home/git/bin/git-http-backend
CustomLog "/home/git/log/access_log" "combined"
ErrorLog "/home/git/log/error_log"
</VirtualHost>
-->
