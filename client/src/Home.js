import React from "react";
import Icon from "./Icon";
import * as Image from "./Image";

export default class Home extends React.Component {

	render() {
		const imageSet = Image.XENON_WELCOME_2x + " 2x, " + Image.XENON_WELCOME_1x + " 1x";

		return (
			<div className='content'>
				<div className='announcement'>
					<div className='icon'>
					<a href='/product/xenon'>
						<img className="product-icon" alt="" src={Icon.XENON_LIGHT}/>
					</a>
					</div>

					<div className='body'>
						<div className='title'>
							Xenon 1.3 Released
						</div>
						<div>
							<a href='/product/xenon'>Download now</a> to get the latest features:
							<ul>
								<li>Java 14</li>
								<li>Java FX 14</li>
								<li>Material design flat icons</li>
								<li>Platform specific installers</li>
							</ul>
						</div>
					</div>
				</div>
				<a className='screenshot' href='/product/xenon'>
					<img className='screenshot' src={Image.XENON_WELCOME_1x} srcSet={imageSet} alt="Xenon Welcome Tool"/>
				</a>
			</div>
		)
	}

}
