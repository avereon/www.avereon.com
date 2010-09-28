<%@ page language="java"%>

<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Modeling</title>
</head>

<body>

<h1>Modeling</h1>

<p>One of the first tasks when modeling geometry with computers is
to determine how to represent geometry with computer data. At first this
seems simple but later on we will see how this is more involved than
first thought.</p>

<h2>Space</h2>
<p>Lets start with the first thing that we need and that is the
space that we will use to describe shapes. Most of us are familiar with
a space that does not bend or twist. This type of space is called by
mathematicians <a href="http://en.wikipedia.org/wiki/Euclidean_space">Euclidean
space</a>, named after the mathematician <a
	href="http://en.wikipedia.org/wiki/Euclid">Euclid</a>. Euclidean space
is sufficient to handle most computer applications and other types of
spaces are often transformed to Euclidean space for display.</p>

<h2>Coordinates</h2>
<p>In order to orient things in any space one needs to set up a
system, commonly called a coordinate system, to locate and orient shapes
relative to one another. There are several coordinate system options.
The one most commonly used is the <a
	href="http://en.wikipedia.org/wiki/Cartesian_coordinate_system">Cartesian
coordinate system</a>, named after the mathematician <a
	href="http://en.wikipedia.org/wiki/Ren%C3%A9_Descartes">Ren%C3%A9
Descartes</a>. This system chooses a point in space to use as an origin and
three perpendicular axes for measurement relative to the origin. This
way any location in space can be described by three numbers, referred to
as the x, y, and z coordinates.</p>

<h2>Shapes</h2>
<p>Shapes can be categorized into four simple groups: points,
curves, surfaces, and solids. Curves include lines, circles, ellipses,
arcs, and other curves like splines including Bezier curves and NURBs.
Surfaces include planar shapes like planes, discs, patches, and even
surfaces of solid objects like spheres. Solids include solid objects
like spheres, cones, blocks, and anything that is solid inside. All
shapes can generally be described with parametric equations. Curves are
described with single parameter equation, surfaces with a two parameter
equation, and solids with a three parameter equation.</p>

</body>

</html>