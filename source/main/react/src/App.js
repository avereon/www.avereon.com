import React from 'react';
import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom'
import Home from "./Home";
import Mit from "./licenses/Mit"
import Xenon from "./product/Xenon";
import Header from "./Header";
import Footer from "./Footer";

export default class App extends React.Component {

	render() {
		return (
			<div className='app'>
				<Header/>
				<Router>
					<Switch>
						{/* First match wins */}
						<Route exact path='/licenses/mit' component={Mit}/>
						<Route exact path='/xenon' component={Xenon}/>
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
