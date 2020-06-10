import React from 'react';
import * as Icon from './Icon';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default class NotFound extends React.Component {

	render() {
		return (
			<div className='content'>
				<h1><FontAwesomeIcon icon={['fas', 'frown']}/> Link Not Found</h1>

				<div className='product-bar'>
					<div className='body'>
						<p>
							The URL you requested is not found on this site! This might be due
							to a bad external link, a bad internal link or simply a typing
							error in the navigation bar. You might find these links helpful to
							get you back on track:
						</p>
						<p>
							<a href='/'><h3><img style={{height: '1em'}} alt="" src={Icon.AVEREON_LIGHT}/> Avereon Home Page</h3></a>
							<a href='/product/xenon'><h3><img style={{height: '1em'}} alt="" src={Icon.XENON_LIGHT}/> Avereon Xenon</h3></a>
							<a href='/products'><h3><img style={{height: '1em'}} alt="" src={Icon.AVEREON_LIGHT}/> Other Products</h3></a>
						</p>
					</div>
				</div>

			</div>
		)
	}

}
