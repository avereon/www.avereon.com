import React from "react";
import * as Icon from "./Icon";

export default class Home extends React.Component {

	render() {
		return (
			<div className='content'>
				<div className='announcement'>
					<img className="product-icon" alt="" src={Icon.XENON}/>
					<div className='body'>
						<div className='title'>
							Xenon 1.1 Released
						</div>
						<div>
							<a href='/product/xenon'>Download now</a> to get the latest features
						</div>
					</div>
				</div>
				<a className='screenshot' href='/product/xenon'>
					<img className='screenshot' src="/screenshots/welcome-tool.png" srcSet="/screenshots/welcome-tool@2x.png 2x, /screenshots/welcome-tool.png" alt="Xenon Welcome Tool"/>
				</a>
			</div>
		)
	}

}