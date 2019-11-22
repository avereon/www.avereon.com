import React from 'react';
import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom'
import Home from "./Home";
import Mit from "./licenses/Mit"
import XenonDownload from "./product/xenon/Download";
import XenonGettingStarted from "./product/xenon/GettingStarted";
import Header from "./Header";
import Footer from "./Footer";
import Legal from "./Legal";
import About from "./About";
import Products from "./product/Products";

const reload = () => window.location.reload();

export default class App extends React.Component {

	render() {
		return (
			<div className='app'>
				<Header/>
				<Router>
					<Switch>
						<Route path='/about' component={About}/>
						<Route path='/legal' component={Legal}/>
						<Route path='/product/*/javadoc' onEnter={reload}/>
						<Route path='/product/xenon/start' component={XenonGettingStarted}/>
						<Route path='/product/xenon' component={XenonDownload}/>
						<Route path='/product' component={Products}/>
						<Route path='/licenses/mit' component={Mit}/>
						<Route component={Home}/>
					</Switch>
				</Router>
				<Footer/>
			</div>
		);
	}

}
