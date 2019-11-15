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

export default class App extends React.Component {

	render() {
		return (
			<div className='app'>
				<Header/>
				<Router>
					<Switch>
						<Route exact path='/' component={Home}/>
						<Route exact path='/home' component={Home}/>
						<Route exact path='/about' component={About}/>
						<Route exact path='/legal' component={Legal}/>
						<Route exact path='/product' component={Products}/>
						<Route exact path='/product/xenon' component={XenonDownload}/>
						<Route exact path='/product/xenon/start' component={XenonGettingStarted}/>
						<Route exact path='/licenses/mit' component={Mit}/>
						<Redirect to='/'/>
					</Switch>
				</Router>
				<Footer/>
			</div>
		);
	}

}
