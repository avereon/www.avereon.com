import React from 'react';
import Icon from '../../Icon';
import Platform from '../../Platform';
import Config from '../../Config';
import {library} from '@fortawesome/fontawesome-svg-core';
import {fas} from '@fortawesome/free-solid-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import '../../css/product.css';
import ProductPage from "../ProductPage";

library.add(fas, fab);

// function productCards(product, success, failure) {
// 	const url = Config.DOWNLOAD_URL + "/product/cards/" + product;
// 	return fetch(url)
// 		.then((response) => response.status === 200 ? response.json() : {})
// 		.then((card) => success(card))
// 		.catch(failure)
// }

export default class Product extends React.Component {

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
		ProductPage.productCards("xenon", (cards) => {
			this.setState(cards);
		});
	}

	render() {
		let stableDownload = <div className='download-row'>
			{ProductPage.createDownloadTile("primary", "stable", "Xenon", this.state, Platform.CURRENT)}
		</div>;

		let stableDownloads = <div className='download-row'>
			{ProductPage.createDownloadTile("secondary", "stable", "Xenon", this.state, Platform.LINUX)}
			{ProductPage.createDownloadTile("secondary", "stable", "Xenon", this.state, Platform.MACOS)}
			{ProductPage.createDownloadTile("secondary", "stable", "Xenon", this.state, Platform.WINDOWS)}
		</div>;

		let latestDownloads = <div className='download-row'>
			{ProductPage.createDownloadTile("secondary", "latest", "Xenon", this.state, Platform.LINUX)}
			{ProductPage.createDownloadTile("secondary", "latest", "Xenon", this.state, Platform.MACOS)}
			{ProductPage.createDownloadTile("secondary", "latest", "Xenon", this.state, Platform.WINDOWS)}
		</div>;

		return (
			<div className='content'>
				<div className='product'>

					<div className='product-title'>
						<img className="product-icon" alt="" src={Icon.XENON_LIGHT}/>
						<div className='product-name'>Xenon</div>
					</div>

					<div className='product-content'>
						Xenon is an application framework that provides common services for
						modern desktop applications. Specific functionality is implemented
						with downloadable packages called mods. Users are encouraged to
						discover and utilize the mods that best suit their needs.
					</div>

					{stableDownload}

					<div className='resource-row'>
						<div className='resource-tile'>
							<div><a href='/product/xenon/docs/user-guide'><FontAwesomeIcon icon={['fas', 'user']}/> User Guide</a></div>
							<div><a href='/product/xenon/modules'><FontAwesomeIcon icon={['fas', 'cubes']}/> Mods</a></div>
							<div><a href='/product/xenon/docs/screenshots'><FontAwesomeIcon icon={['fas', 'desktop']}/> Screenshots</a></div>
						</div>
						<div className='resource-tile'>
							<div><a href='/product/xenon/docs/mods-guide'><FontAwesomeIcon icon={['fas', 'file']}/> Building Mods</a></div>
							<div><a href='/product/xenon/docs/tool-guide'><FontAwesomeIcon icon={['fas', 'file']}/> Building Tools</a></div>
							<div><a href={Config.ROOT_URL + '/product/xenon/docs/api/index.html'}><FontAwesomeIcon icon={['fas', 'tools']}/> Xenon API</a></div>
							<div><a href='https://github.com/avereon/xenon' target="_blank" rel="noopener noreferrer"><FontAwesomeIcon icon={['fab', 'github']}/> Source Code</a></div>
						</div>
					</div>

					<h3>Other Platforms</h3>
					{stableDownloads}
					<h3>Development Builds</h3>
					{latestDownloads}

				</div>
			</div>
		);
	}

}
