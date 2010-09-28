<%@ page language="java"%>

<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Line Line</title>
</head>

<body>

<h1>Line Line Intersections</h1>
<p>Computing the intersection between two lines is fairly straight
forward. There are four possible scenarios when handling line
intersections.</p>
<ul>
	<li>Skew</li>
	<li>Parallel</li>
	<li>Intersecting</li>
	<li>Intersecting and Crossing</li>
</ul>
<p>The process in determining the intersection is as follows:</p>
<ol>
	<li>Check for skew lines.</li>
	<li>Check for parallel lines.</li>
	<li>Determine the common plane.</li>
	<li>Transform the points to the common plane.</li>
	<li>Determine the intersection point.</li>
	<li>Transform the intersection point to world orientation.</li>
</ol>

</body>

</html>