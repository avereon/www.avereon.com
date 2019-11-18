import React from "react";
import * as Icon from "./Icon";

export default class Home extends React.Component {

	render() {
		return (
			<div className='content'>
				<div className='announcement'>
					<a href='/product/xenon'><img className="product-icon" alt="" src={Icon.XENON}/></a>
					<div className='body'>
						<div className='title'>
							Xenon 1.0 Arriving Soon
						</div>
						<div>
							<a href='/product/xenon'>Download an early release</a> to preview the features
						</div>
					</div>
				</div>
			</div>
		)
	}

}