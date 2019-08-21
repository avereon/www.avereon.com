import React from 'react';
import './App.css';
import {Platform} from "./Platform";
import {library} from '@fortawesome/fontawesome-svg-core';
import {fas} from '@fortawesome/free-solid-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const ROOT_URL = "https://www.avereon.com/download";
const AVEREON_ICON_URL = ROOT_URL + "/stable/avereon/provider/icon";
const XENON_ICON_URL = ROOT_URL + "/latest/xenon/product/icon";
const XENON_STABLE_URL = ROOT_URL + "/stable/xenon/" + Platform.KEY + "/product/card";
const XENON_LATEST_URL = ROOT_URL + "/latest/xenon/" + Platform.KEY + "/product/card";

library.add(fas, fab);

function productCard(url, success, failure) {
	console.log("Request: " + url);
	return fetch(url)
		.then((response) => response.status === 200 ? response.json() : {})
		.then((card) => success(card))
		.catch(failure)
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

	createChicklet( type, category, title, artifact, version, platform ) {
		let style = "download " + type;
		const icon = type === "primary" ? <FontAwesomeIcon icon='download'/> : '';
		if (version === undefined) {
			return <div className={style + " disabled"}>
				<div className='title'>{icon} {title}</div>
				<div>unavailable</div>
			</div>
		} else {
			return <a className={style + " " + category} href={'https://www.avereon.com/download/' + category +'/' + artifact + '/' + platform + '/install/jar'}>
				<div className='title'>{icon} {title}</div>
				<div>{version}</div>
			</a>
		}
	}

	render() {
		const stableProduct = this.state.xenonStableProductCard;
		const latestProduct = this.state.xenonLatestProductCard;

		let stableDownload = this.createChicklet("primary", "stable", "Download for " + Platform.NAME, stableProduct.artifact, stableProduct.version, Platform.KEY);
		let latestDownload = this.createChicklet("secondary", "latest", "Dev for " + Platform.NAME, latestProduct.artifact, latestProduct.version, Platform.KEY);

		let stableDownloads = <div>
			{this.createChicklet("secondary", "stable", "Download for " + Platform.LINUX.NAME, stableProduct.artifact, stableProduct.version, Platform.LINUX.KEY)}
			{this.createChicklet("secondary", "stable", "Download for " + Platform.MAC.NAME, stableProduct.artifact, stableProduct.version, Platform.MAC.KEY)}
			{this.createChicklet("secondary", "stable", "Download for " + Platform.WINDOWS.NAME, stableProduct.artifact, stableProduct.version, Platform.WINDOWS.KEY)}
		</div>;

		let latestDownloads = <div>
			{this.createChicklet("secondary", "latest", "Dev for " + Platform.LINUX.NAME, latestProduct.artifact, latestProduct.version, Platform.LINUX.KEY)}
			{this.createChicklet("secondary", "latest", "Dev for " + Platform.MAC.NAME, latestProduct.artifact, latestProduct.version, Platform.MAC.KEY)}
			{this.createChicklet("secondary", "latest", "Dev for " + Platform.WINDOWS.NAME, latestProduct.artifact, latestProduct.version, Platform.WINDOWS.KEY)}
		</div>;

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

						{stableDownload}
						{stableDownloads}
						{latestDownloads}
					</div>
				</div>

				<div className='footer'>
					<div className='tag-line'>Unique products for unique work</div>
					<div className='copyright'>&copy; 2019 Avereon</div>
				</div>
			</div>
		);
	}

}
