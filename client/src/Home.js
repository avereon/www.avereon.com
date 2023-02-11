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
							Xenon 1.6
						</div>
						<div>
							<a href='/product/xenon'>Download now</a> to get the latest features:
							<ul>
								<li>Java 17</li>
								<li>Updated mods</li>
								<li>Bug fixes</li>
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
