import React from 'react';
import {Platform} from "../Platform";
import {library} from '@fortawesome/fontawesome-svg-core';
import {fas} from '@fortawesome/free-solid-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Footer from "../Footer";
import Header from "../Header";
import './product.css';

const ROOT_URL = "https://www.avereon.com/download";
const XENON_ICON_URL = ROOT_URL + "/latest/xenon/product/icon";
const XENON_STABLE_URL = ROOT_URL + "/stable/xenon/" + Platform.KEY + "/product/card";
const XENON_LATEST_URL = ROOT_URL + "/latest/xenon/" + Platform.KEY + "/product/card";

library.add(fas, fab);

function productCardFromUrl(url, success, failure) {
	console.log("Request: " + url);
	return fetch(url)
		.then((response) => response.status === 200 ? response.json() : {})
		.then((card) => success(card))
		.catch(failure)
}

function productCard(release, platform, success, failure) {
	const url = ROOT_URL + "/" + release + "/xenon/" + platform + "/product/card";
	return productCardFromUrl(url, success, failure);
}

export default class Xenon extends React.Component {

	state = {
		stable: {
			linux: {},
			macosx: {},
			windows: {}
		},
		latest: {
			linux: {},
			macosx: {},
			windows: {}
		}
	};

	componentDidMount() {
		productCard("stable", "linux", (card) => {
			console.log("card=" + JSON.stringify(card));
			this.setState({stable: {...this.state.stable, linux: {...card}}});
		});
		productCard("stable", "macosx", (card) => {
			console.log("card=" + JSON.stringify(card));
			this.setState({stable: {...this.state.stable, macosx: {...card}}});
		});
		productCard("stable", "windows", (card) => {
			console.log("card=" + JSON.stringify(card));
			this.setState({stable: {...this.state.stable, windows: {...card}}});
		});
		productCard("latest", "linux", (card) => {
			console.log("card=" + JSON.stringify(card));
			this.setState({latest: {...this.state.latest, linux: {...card}}});
		});
		productCard("latest", "macosx", (card) => {
			console.log("card=" + JSON.stringify(card));
			this.setState({latest: {...this.state.latest, macosx: {...card}}});
		});
		productCard("latest", "windows", (card) => {
			console.log("card=" + JSON.stringify(card));
			this.setState({latest: {...this.state.latest, windows: {...card}}});
		});
	}

	static createDownloadTile(type, category, product, platform, card) {
		const artifact = card.artifact;
		const version = card.version;

		let style = "download " + type;
		const platformSize = type === "primary" ? '3x' : '2x';
		const platformIcon = type === "primary" ? 'download' : ['fab', platform.ICON];
		if (version === undefined) {
			return <div className={style + " disabled"}>
				<div className='download-layout'>
					<FontAwesomeIcon className="download-icon" icon={platformIcon} size={platformSize}/>
					<div className='download-metadata'>
						<div className='title'>{product} for {platform.NAME}</div>
						<div>unavailable</div>
					</div>
				</div>
			</div>
		} else {
			return <a className={style + " " + category} href={'https://www.avereon.com/download/' + category + '/' + artifact + '/' + platform.KEY + '/install/jar'}>
				<div className='download-layout'>
					<FontAwesomeIcon className="download-icon" icon={platformIcon} size={platformSize}/>
					<div className='download-metadata'>
						<div className='title'>{product} for {platform.NAME}</div>
						<div>{version}</div>
					</div>
				</div>
			</a>
		}
	}

	render() {
		let stableDownload = Xenon.createDownloadTile("primary", "stable", "Xenon",Platform, this.state.stable[Platform.KEY]);

		let stableDownloads = <div className='download-row'>
			{Xenon.createDownloadTile("secondary", "stable", "Xenon", Platform.LINUX, this.state.stable[Platform.LINUX.KEY])}
			{Xenon.createDownloadTile("secondary", "stable", "Xenon", Platform.MAC, this.state.stable[Platform.MAC.KEY])}
			{Xenon.createDownloadTile("secondary", "stable", "Xenon", Platform.WINDOWS, this.state.stable[Platform.WINDOWS.KEY])}
		</div>;

		let latestDownloads = <div className='download-row'>
			{Xenon.createDownloadTile("secondary", "latest", "Xenon", Platform.LINUX, this.state.latest[Platform.LINUX.KEY])}
			{Xenon.createDownloadTile("secondary", "latest", "Xenon", Platform.MAC, this.state.latest[Platform.MAC.KEY])}
			{Xenon.createDownloadTile("secondary", "latest", "Xenon", Platform.WINDOWS, this.state.latest[Platform.WINDOWS.KEY])}
		</div>;

		return (
			<div className='app'>

				<Header/>

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
							<a href='https://github.com/avereon/xenon' className='resource-tile' target="_blank">
								<FontAwesomeIcon icon={['fab', 'github']} size='4x'/>
								<span>GitHub</span>
							</a>
							<a href='https://travis-ci.org/avereon/xenon' className='resource-tile' target="_blank">
								<img alt='' style={{width: '4em', height: '4em'}} src='https://cdn.travis-ci.org/favicon-b4e438ec85b9ae88d26b49538bc4e5ce.png'/>
								<span>Travis-CI</span>
							</a>
						</div>
					</div>
				</div>

				<Footer/>
			</div>
		);
	}

}
