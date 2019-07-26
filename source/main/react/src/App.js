import React from 'react';
import './App.css';
import {Platform} from "./Platform";

const ROOT_URL = "https://www.avereon.com/download";
const AVEREON_ICON_URL = ROOT_URL + "/latest/v2/avereon/product/icon";
const XENON_ICON_URL = ROOT_URL + "/latest/v2/xenon/product/icon";
const XENON_STABLE_URL = ROOT_URL + "/stable/v2/xenon/" + Platform.KEY + "/product/card";
const XENON_LATEST_URL = ROOT_URL + "/latest/v2/xenon/" + Platform.KEY + "/product/card";

function productCard(url, success, failure) {
	console.log("Request: " + url);
	return fetch(url)
		.then((response) => response.status === 200 ? response.json() : {})
		.then((card) => success(card))
}

export default class App extends React.Component {

	state = {
		xenonStableProductCard: {
			version: 'stable'
		},
		xenonLatestProductCard: {
			version: 'latest'
		}
	};

	componentDidMount() {
		productCard(XENON_STABLE_URL, (card) => {
			this.setState({xenonStableProductCard: {...card}});
		});
		productCard(XENON_LATEST_URL, (card) => {
			this.setState({xenonLatestProductCard: {...card}});
		})
	}

	render() {
		return (
			<div className='app'>

				<div className='header'>
					<div className='header-row'>
						<img className='header-logo' alt="" src={AVEREON_ICON_URL}/>
						<div className='header-name'>Avereon</div>
					</div>
				</div>

				<div className='product'>
					<div className='product space'>
						<div className='product row'>
							<img className="product icon" alt="" src={XENON_ICON_URL}/>
							<div className='product title'>Xenon</div>
						</div>

						<p>
							Xenon is a simple application framework that provides common
							services for product features. Product features are provided as
							packages, called mods, that provide the specific functionality.
							Users are encouraged to discover and utilize the mods that best
							suit their needs.
						</p>

						<h1>Download for {Platform.NAME}</h1>

						<div>
							<div className="download disabled" href={'https://www.avereon.com/download/stable/v2/xenon/' + Platform.KEY + '/install/jar'}>
								<div className='title'>Stable Release</div>
								<div>unavailable</div>
							</div>

							{/*
							<a className="download stable" href={'https://www.avereon.com/download/stable/v2/xenon/' + Platform.KEY + '/install/jar'}>
								<div className='title'>Stable Release</div>
								<div>xenon-{Platform.KEY}-{this.state.xenonStableProductCard.version}</div>
							</a>
							*/}

							<a className="download latest" href={'https://www.avereon.com/download/latest/v2/xenon/' + Platform.KEY + '/install/jar'}>
								<div className='title'>Developer Build</div>
								<div>xenon-{Platform.KEY}-{this.state.xenonLatestProductCard.version}</div>
							</a>
						</div>
					</div>
				</div>

				<div className='footer'>
					<div className='copyright'>Copyright &copy; 2019 Avereon</div>
					<div className='tag-line'>Unique products for unique work</div>
				</div>
			</div>
		);
	}

}
