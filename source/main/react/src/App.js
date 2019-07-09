import React from 'react';
import './App.css';
import {Platform} from "./Platform";

//const ICON_URL="https://www.xeomar.com/download?channel=latest&artifact=xeomar&category=product&type=icon";
const ICON_URL="https://www.xeomar.com/download/latest/v2/xeomar/product/icon";

function App() {

	return (
		<div className="App">
			<h1><img className="logo" alt="" src={ICON_URL}/>Xeomar</h1>
			<p>Specialized products for specialized work</p>
			<a href={'https://www.xeomar.com/download/stable/v2/xenon/' + Platform.PLATFORM +'/install/jar'}>Xenon Installer - Stable Release</a>
			<a href={'https://www.xeomar.com/download/latest/v2/xenon/' + Platform.PLATFORM +'/install/jar'}>Xenon Installer - Nightly Release</a>
		</div>
	);
}

export default App;
