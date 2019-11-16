import React from "react";
import * as Icon from "../../Icon";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

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

					<div className='product-bar'>
						<FontAwesomeIcon icon={['fas', 'arrow-circle-down']} size='4x'/>
						<div className='item'>
							<div><a href='/product/xenon'>Download</a> and install Xenon for your operating system</div>
						</div>
					</div>

					<div className='product-bar'><img className="product-icon" alt="" src={Icon.XENON}/>
						<div className='item'>Start Xenon using the program shortcut</div>
					</div>

					<div className='product-bar'><img width="480" height="270" alt="" src="/screenshots/welcome-tool.png"/></div>

				</div>
			</div>
		)
	}

}