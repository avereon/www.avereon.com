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
		xenonStableProductCard: {},
		xenonLatestProductCard: {}
	};

	componentDidMount() {
		productCard(XENON_STABLE_URL, (card) => {
			this.setState({xenonStableProductCard: {...card}});
		});
		productCard(XENON_LATEST_URL, (card) => {
			this.setState({xenonLatestProductCard: {...card}});
		})
	}

	createChicklet(type, category, title, artifact, version, platform) {
		let style = "download " + type;
		const platformSize = type === "primary" ? '3x' : '2x';
		const platformIcon = type === "primary" ? 'download' : ['fab', platform.ICON];
		if (version === undefined) {
			return <div className={style + " disabled"}>
				<div className='download-layout'>
					<FontAwesomeIcon className="download-icon" icon={platformIcon} size={platformSize}/>
					<div className='download-metadata'>
						<div className='title'>{title}</div>
						<div>unavailable</div>
					</div>
				</div>
			</div>
		} else {
			return <a className={style + " " + category} href={'https://www.avereon.com/download/' + category + '/' + artifact + '/' + platform.KEY + '/install/jar'}>
				<div className='download-layout'>
					<FontAwesomeIcon className="download-icon" icon={platformIcon} size={platformSize}/>
					<div className='download-metadata'>
						<div className='title'>{title}</div>
						<div>{version}</div>
					</div>
				</div>
			</a>
		}
	}

	render() {
		const stableProduct = this.state.xenonStableProductCard;
		const latestProduct = this.state.xenonLatestProductCard;

		let stableDownload = this.createChicklet("primary", "stable", "Xenon for " + Platform.NAME, stableProduct.artifact, stableProduct.version, Platform);

		let stableDownloads = <div className='download-row'>
			{this.createChicklet("secondary", "stable", "Xenon", stableProduct.artifact, stableProduct.version, Platform.LINUX)}
			{this.createChicklet("secondary", "stable", "Xenon", stableProduct.artifact, stableProduct.version, Platform.MAC)}
			{this.createChicklet("secondary", "stable", "Xenon", stableProduct.artifact, stableProduct.version, Platform.WINDOWS)}
		</div>;

		let latestDownloads = <div className='download-row'>
			{this.createChicklet("secondary", "latest", "Xenon", latestProduct.artifact, latestProduct.version, Platform.LINUX)}
			{this.createChicklet("secondary", "latest", "Xenon", latestProduct.artifact, latestProduct.version, Platform.MAC)}
			{this.createChicklet("secondary", "latest", "Xenon", latestProduct.artifact, latestProduct.version, Platform.WINDOWS)}
		</div>;

		return (
			<div className='app'>

				<div className='header'>
					<img className='logo' alt="" src={AVEREON_ICON_URL}/>
					<div className='title'>Avereon</div>
				</div>

				<div className='content'>
					<div className='product'>

						<div className='product-header'>
							<img className="product-icon" alt="" src={XENON_ICON_URL}/>
							<div className='product-title'>Xenon</div>
						</div>

						<div className='product-content'>
							Xenon is a simple application framework that provides common
							services for product features. Product features are provided as
							packages, called mods, that provide the specific functionality.
							Users are encouraged to discover and utilize the mods that best
							suit their needs.
						</div>

						{stableDownload}

						<h2>Other Platforms</h2>
						{stableDownloads}
						<h2>Development Builds</h2>
						{latestDownloads}

						<div className='resource-row'>
							<a href='https://github.com/avereon/xenon' className='resource-tile'>
								<FontAwesomeIcon icon={['fab', 'github']} size='4x'/>
								<span>GitHub</span>
							</a>
							<a href='https://travis-ci.org/avereon/xenon' className='resource-tile'>
								<img alt='' style={{width: '4em', height: '4em'}} src='https://cdn.travis-ci.org/favicon-b4e438ec85b9ae88d26b49538bc4e5ce.png'/>
								<span>Travis-CI</span>
							</a>
						</div>
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
