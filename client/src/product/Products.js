import React from "react";
import * as Icon from "../Icon";

export default class Products extends React.Component {

	render() {
		return (
			<div className='content'>
				<h1 className='title'>Products</h1>
				<div className='product-bar'>
					<a href='/products/xenon'> <img className="product-icon" alt="" src={Icon.XENON}/> </a>
					Xenon is a simple application framework that provides services common
					to modern desktop applications. Specific functionality is implemented
					with downloadable packages called mods.
				</div>
			</div>
		)
	}

}
