import React from 'react';
import './App.css';

const ICON_URL="https://www.xeomar.com/download?channel=latest&artifact=mouse&category=product&type=icon";

function App() {
	return (
		<div className="App">
			<h1><img className="logo" alt="O" src={ICON_URL}/>Xeomar</h1>
			<p>Specialized software for specialized work</p>
		</div>
	);
}

export default App;
