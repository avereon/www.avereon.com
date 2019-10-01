import React from "react";
import * as Icon from "./Icon";

export default class Products extends React.Component {

	render() {
		return (
			<div className='content'>
				<h1 className='title'>Products</h1>
				<div className='product-bar'>
					<a href='/products/xenon'> <img className="product-icon" alt="" src={Icon.XENON}/> </a>
					Xenon is a lightweight Java framework for modular programs. The
					framework provides services common to modern desktop applications on
					which specific functionality can be implemented.
				</div>
			</div>
		)
	}

}
