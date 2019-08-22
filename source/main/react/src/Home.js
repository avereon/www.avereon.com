import React from "react";
import Header from "./Header";
import Footer from "./Footer";

export default class Home extends React.Component {

	render() {
		return (
			<div className='app'>
				<Header/>
				<div className='content'>
					<h1 className='title'>Avereon</h1>
				</div>
				<Footer/>
			</div>
		)
	}

}