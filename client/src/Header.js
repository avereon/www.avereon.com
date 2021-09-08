import React from "react";
import Icon from "./Icon";

export default class Header extends React.Component {

	render() {
		return (
			<div className='header'>
				<div className='menu'>
					<a className='organization' href='/'><img className='logo' alt="" src={Icon.AVEREON_DARK}/>Avereon</a>
					<a className='products' href='/products'>Software</a>
				</div>

				<div className='menu'>
					<a className='about' href='/about'>About</a>
				</div>
			</div>
		)
	}

}
