import React from 'react';
import './App.css';
import {Platform} from "./Platform";

const ROOT_URL = "https://www.xeomar.com/download";
const ICON_URL = ROOT_URL + "/latest/v2/xeomar/product/icon";
const XENON_STABLE_URL = ROOT_URL + "/stable/v2/xenon/" + Platform.PLATFORM + "/product/card";
const XENON_LATEST_URL = ROOT_URL + "/latest/v2/xenon/" + Platform.PLATFORM + "/product/card";

function productCard(url, success, failure) {
	console.log("Request: " + url);
	return fetch(url)
		.then((response) => response.status === 200 ? response.json() : {})
		.then((card) => {
			console.log(card);
			return card;
		})
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
						<img className="logo" alt="" src={ICON_URL}/>
						<div className='title-block'>
							<div className='title'>Xeomar</div>
							<div className='subtitle'>Specialized products for specialized work</div>
						</div>
					</div>

					<h1>Xenon Installers</h1>
				</div>
				<div>
					<a className="download official" href={'https://www.xeomar.com/download/stable/v2/xenon/' + Platform.PLATFORM + '/install/jar'}>
						<div className='title'>Stable Release</div>
						<div>{this.state.xenonStableProductCard.version} - {'size'} MB</div>
					</a>
					<a className="download nightly" href={'https://www.xeomar.com/download/latest/v2/xenon/' + Platform.PLATFORM + '/install/jar'}>
						<div className='title'>Nightly Release</div>
						<div>{this.state.xenonLatestProductCard.version} - {'size'} MB</div>
					</a>
				</div>
			</div>
		);
	}

}
