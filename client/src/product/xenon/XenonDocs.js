import React from "react";
import * as Icon from "../../Icon";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default class XenonDocs extends React.Component {

	render() {
		return (
			<div className='content'>

				<div className='product-title'>
					<img className="product-icon" alt="" src={Icon.XENON_LIGHT}/>
					<div className='product-name'>Xenon Documents</div>
				</div>

				<a href="/product/xenon/docs/user-guide"><FontAwesomeIcon icon={['fas', 'user']}/> User Guide</a>
				<a href="/product/xenon/docs/mods-guide"><FontAwesomeIcon icon={['fas', 'cubes']}/> Mod Developers Guide</a>

			</div>
		)
	}

}
