import React from "react";
import * as Icon from "./Icon";

export default class Header extends React.Component {

	render() {
		return (
			<div className='header'>
				<div>
					<a className='banner' href='/'><img className='logo' alt="" src={Icon.AVEREON}/>Avereon</a>
				</div>
				<div className='menu'>
					<a className='menuitem products' href='/products'>Products</a>
					<a className='menuitem about' href='/about'>About</a>
				</div>
				<div className='banner-mirror'>

				</div>
			</div>
		)
	}

}
