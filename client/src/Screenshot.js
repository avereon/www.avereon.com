import Config from "./Config";
import React from "react";
import {Link, useLocation} from "react-router-dom";

function X1(name) {
	return Config.IMAGE_ROOT + "/screenshots/" + name + ".png";
}

function X2(name) {
	return Config.IMAGE_ROOT + "/screenshots/" + name + "@2x.png";
}

export default function Screenshot() {
	/* The original screenshots are 800x500 */
	let width = 80 * 10
	let height = 50 * 10

	const location = useLocation();
	let title = location.state.title
	let path = location.state.path
	let link = location.state.returnLink

	return (
		<div className='content'>
			<h1>{title}</h1>
			<Link className='screenshot-tile' to={link}>
				<img width={width} height={height} srcSet={X2(path) + " 2x, " + X1(path) + " 1x"} src={X1(path)} alt={title}/>
			</Link>
		</div>
	)
}

export function ScreenshotTile(props) {

	let width = 80 * 3
	let height = 50 * 3
	let title = props.title
	let path = props.path
	let returnLink = window.location.pathname

	return (
		<div className='content'>
			<h6>{title}</h6>
			<Link className='screenshot-tile' to='/product/screenshot' state={{title, path, returnLink}}>
				<img width={width} height={height} srcSet={X2(path) + " 2x, " + X1(path) + " 1x"} src={X1(path)} alt={title}/>
			</Link>
			{props.children}
		</div>
	)

}
