import React from "react";
import * as Icon from "../../Icon";

export default class XenonGettingStarted extends React.Component {

	render() {
		return (
			<div className='content'>
				<div className='product'>

					<div className='product-title'>
						<img className="product-icon" alt="" src={Icon.XENON}/>
						<div className='product-name'>Xenon</div>
					</div>

					<div className='product-header'>Getting Started</div>

					<div className='product-content'>
						<ul>
							<li><a href='/products/xenon'>Download the Xenon installer</a> for your operating system</li>
							<li>Run the installer to install Xenon</li>
							<li>Start Xenon using the program shortcut</li>
						</ul>
					</div>

				</div>
			</div>
		)
	}

}