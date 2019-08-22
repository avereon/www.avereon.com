import React from "react";
import * as Icon from "./Icon";

export default class Header extends React.Component {

	render() {
		return (
			<div className='header'>
				<img className='logo' alt="" src={Icon.AVEREON}/>
				<div className='title'>Avereon</div>
			</div>
		)
	}

}