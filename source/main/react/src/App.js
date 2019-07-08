import React from 'react';
import './App.css';

const ICON_URL="https://www.xeomar.com/download?channel=latest&artifact=xeomar&category=product&type=icon";

function App() {
	return (
		<div className="App">
			<h1><img className="logo" alt="O" src={ICON_URL}/>Xeomar</h1>
			<p>Specialized products for specialized work</p>
			<a href={'https://www.xeomar.com/download/stable/v2/xenon/linux/install/jar'}>Xenon Installer - Stable Release</a>
			<a href={'https://www.xeomar.com/download/latest/v2/xenon/linux/install/jar'}>Xenon Installer - Nightly Release</a>
		</div>
	);
}

export default App;
