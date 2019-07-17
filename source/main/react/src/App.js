import React from 'react';
import './App.css';
import {Platform} from "./Platform";

const ROOT_URL = "https://www.avereon.com/download";
const XEOMAR_ICON_URL = ROOT_URL + "/latest/v2/avereon/product/icon";
const XENON_ICON_URL = ROOT_URL + "/latest/v2/xenon/product/icon";
const XENON_STABLE_URL = ROOT_URL + "/stable/v2/xenon/" + Platform.PLATFORM + "/product/card";
const XENON_LATEST_URL = ROOT_URL + "/latest/v2/xenon/" + Platform.PLATFORM + "/product/card";

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
			<div className="App">
				<div>
					<div className='title-row'>
						<img className="logo" alt="" src={XEOMAR_ICON_URL}/>
						<div className='title-block'>
							<div className='title'>Avereon</div>
							<div className='subtitle'>Specialized products for specialized work</div>
						</div>
					</div>
				</div>
				<div className='title-row'>
					<img className="product-icon" alt="" src={XENON_ICON_URL}/>
					<h1>Xenon Installers</h1>
				</div>
				<div>
					<span className="download disabled" href={'https://www.avereon.com/download/stable/v2/xenon/' + Platform.PLATFORM + '/install/jar'}>
						<div className='title'>Stable Release</div>
						<div>xenon-{Platform.PLATFORM}-{this.state.xenonStableProductCard.version}</div>
					</span>
					{/*<a className="download official" href={'https://www.avereon.com/download/stable/v2/xenon/' + Platform.PLATFORM + '/install/jar'}>*/}
					{/*	<div className='title'>Stable Release</div>*/}
					{/*	<div>xenon-{Platform.PLATFORM}-{this.state.xenonStableProductCard.version}</div>*/}
					{/*</a>*/}
					<a className="download nightly" href={'https://www.avereon.com/download/latest/v2/xenon/' + Platform.PLATFORM + '/install/jar'}>
						<div className='title'>Nightly Release</div>
						<div>xenon-{Platform.PLATFORM}-{this.state.xenonLatestProductCard.version}</div>
					</a>
				</div>
			</div>
		);
	}

}
