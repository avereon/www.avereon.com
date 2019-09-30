import React from 'react';
import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom'
import Home from "./Home";
import Mit from "./licenses/Mit"
import Xenon from "./product/Xenon";
import Header from "./Header";
import Footer from "./Footer";
import Copyright from "./Copyright";
import About from "./About";

export default class App extends React.Component {

	render() {
		return (
			<div className='app'>
				<Header/>
				<Router>
					<Switch>
						{/* First match wins */}
						<Route exact path='/licenses/mit' component={Mit}/>
						<Route exact path='/copyright' component={Copyright}/>
						<Route exact path='/xenon' component={Xenon}/>
						<Route exact path='/about' component={About}/>
						<Route exact path='/home' component={Home}/>
						<Route exact path='/' component={Xenon}/>
						<Redirect to='/'/>
					</Switch>
				</Router>
				<Footer/>
			</div>
		);
	}

}
