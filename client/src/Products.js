import React from "react";
import * as Icon from "./Icon";

export default class Products extends React.Component {

	render() {
		return (
			<div className='content'>
				<h1 className='title'>Products</h1>
				<div className='product'>
					<a className='product-banner' href='/xenon'>
						<img className="product-icon" alt="" src={Icon.XENON}/>
						<div>
							Xenon is a lightweight Java framework for modular programs. The
							framework provides services common to modern desktop applications on
							which specific functionality can be implemented.
						</div>
					</a>
				</div>
			</div>
		)
	}

}
