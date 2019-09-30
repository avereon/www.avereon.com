import React from "react";
import * as Icon from "./Icon";

export default class Header extends React.Component {

	render() {
		return (
			<div className='header'>
				<a className='banner' href='/'><img className='logo' alt="" src={Icon.AVEREON}/><span>Avereon</span></a>
				{/*
				<span className='menu'>
					<a className='menuitem products' href='/products'>Products</a>
					<a className='menuitem docs' href='/docs'>Documentation</a>
					<a className='menuitem support' href='/support'>Support</a>
					<a className='menuitem about' href='/about'>About</a>
				</span>
				*/}
			</div>
		)
	}

}