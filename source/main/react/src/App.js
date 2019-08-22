import React from 'react';
import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom'
import Home from "./Home";
import Xenon from "./product/Xenon";

export default class App extends React.Component {

	render() {
		return (
			<Router>
				<Switch>
					/* First match wins */
					<Route exact path='/xenon' component={Xenon}/>
					<Route exact path='/home' component={Home}/>
					<Route exact path='/' component={Xenon}/>
					<Redirect to='/'/>
				</Switch>
			</Router>
		);
	}

}
