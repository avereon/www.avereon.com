import React from "react";
import * as Icon from "./Icon";

export default class Header extends React.Component {

	render() {
		return (
			<div className='header'>
				<div className='menu'>
					<a className='banner' href='/'><img className='logo' alt="" src={Icon.AVEREON}/>Avereon</a>
					<a className='products' href='/product'>Products</a>
				</div>

				<div className='menu'>
					<a className='about' href='/about'>About</a>
				</div>
			</div>
		)
	}

}