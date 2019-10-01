import React from "react";
import * as Icon from "./Icon";

export default class Header extends React.Component {

	render() {
		return (
			<div className='header'>
				<div className='hbox'>
					<div>
						<a className='banner' href='/'><img className='logo' alt="" src={Icon.AVEREON}/>Avereon</a>
					</div>
					<div className='menu'>
						<a className='menuitem products' href='/products'>Products</a>
					</div>
				</div>
				<div className='menu'>
					<a className='menuitem about' href='/about'>About</a>
				</div>
			</div>
		)
	}

}
