import React from 'react';
import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom'
import Home from "./Home";
import Mit from "./licenses/Mit"
import XenonDownload from "./product/xenon/Download";
import Header from "./Header";
import Footer from "./Footer";
import Legal from "./Legal";
import About from "./About";
import Products from "./product/Products";
import DocViewer from "./product/DocViewer";

import './css/index.css';
import './css/license.css';
import './css/product.css';
import './css/viewer.css';
import XenonMods from "./product/xenon/Mods";

const reload = () => window.location.reload();

export default class App extends React.Component {

	render() {
		return (
			<div className='app'>
				<Header/>
				<Router>
					<Switch>
						<Route exact path='/product/xenon/docs/manual' render={(props) => <DocViewer {...props} doc='/docs/xenon/manual'/>}/>
						<Route exact path='/product/xenon/mods' component={XenonMods}/>
						<Route exact path='/product/xenon' component={XenonDownload}/>
						<Route exact path='/product' component={Products}/>
						<Route exact path='/license/mit' component={Mit}/>
						<Route exact path='/legal' component={Legal}/>
						<Route exact path='/about' component={About}/>
						<Route exact path='/' component={Home}/>

						<Route path='/product/zevra/docs/api' onEnter={reload}/>
						<Route path='/product/zenna/docs/api' onEnter={reload}/>
						<Route path='/product/xenon/docs/api' onEnter={reload}/>

						<Redirect to='/'/>
					</Switch>
				</Router>
				<Footer/>
			</div>
		);
	}

}
