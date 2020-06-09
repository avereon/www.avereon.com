import React from "react";
import * as Icon from "./Icon";
import * as Image from "./Image";

export default class Home extends React.Component {

	render() {
		// /screenshots/welcome-tool@2x.png 2x, /screenshots/welcome-tool.png
		const imageSet = Image.XENON_WELCOME_2x + " 2x, " + Image.XENON_WELCOME_1x + " 1x";

		return (
			<div className='content'>
				<div className='announcement'>
					<img className="product-icon" alt="" src={Icon.XENON}/>
					<div className='body'>
						<div className='title'>
							Xenon 1.2 Released
						</div>
						<div>
							<a href='/product/xenon'>Download now</a> to get the latest features:
							<ul>
								<li>Improved wallpaper settings</li>
								<li>More themes</li>
								<li>Dark themes</li>
							</ul>
						</div>
					</div>
				</div>
				<a className='screenshot' href='/product/xenon'>
					<img className='screenshot' src={Image.XENON_WELCOME_2x} srcSet={imageSet} alt="Xenon Welcome Tool"/>
				</a>
			</div>
		)
	}

}