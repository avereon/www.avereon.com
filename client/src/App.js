import React from 'react';
import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom'
import './css/index.css';
import './css/license.css';
import './css/product.css';
import './css/viewer.css';
import Home from "./Home";
import LicenseMit from "./licenses/Mit"
import Header from "./Header";
import Footer from "./Footer";
import Legal from "./Legal";
import About from "./About";
import Products from "./product/Products";
import DocViewer from "./DocViewer";
import AcornProduct from "./product/acorn/AcornProduct";
import XenonProduct from "./product/xenon/XenonProduct";
import XenonMods from "./product/xenon/XenonMods";
import XenonDocs from "./product/xenon/XenonDocs";
import NotFound from "./NotFound";
import Status from "./product/Status";
import SeencProduct from "./product/seenc/SeencProduct";
import Aviation from "./aviation/Aviation";

const reload = () => window.location.reload();

export default class App extends React.Component {

	render() {
		return (
			<div className='app'>
				<Header/>
				<Router>
					<Switch>
						{/* Supported routes */}
						<Route exact path='/product/acorn' component={AcornProduct}/>
						<Route exact path='/product/seenc' component={SeencProduct}/>
						<Route exact path='/product/xenon/docs/user-guide' render={(props) => <DocViewer {...props} doc='https://raw.githubusercontent.com/avereon/xenon/master/source/main/docs/manual/content.html' outline={3}/>}/>
						<Route exact path='/product/xenon/docs/mods-guide' render={(props) => <DocViewer {...props} doc='https://raw.githubusercontent.com/avereon/xenon/master/source/main/docs/mods/content.html' outline={3}/>}/>
						<Route exact path='/product/xenon/docs/tool-guide' render={(props) => <DocViewer {...props} doc='https://raw.githubusercontent.com/avereon/xenon/master/source/main/docs/tools/content.html' outline={3}/>}/>
						<Route exact path='/product/xenon/contribute' render={(props) => <DocViewer {...props} doc='https://raw.githubusercontent.com/avereon/xenon/master/source/main/docs/contribute/content.html' outline={3}/>}/>
						<Route exact path='/product/xenon/docs' component={XenonDocs}/>
						<Route exact path='/product/xenon/mods' component={XenonMods}/>
						<Route exact path='/product/xenon' component={XenonProduct}/>
						<Route exact path='/products' component={Products}/>
						<Route exact path='/aviation' component={Aviation}/>
						<Route exact path='/status' component={Status}/>
						<Route exact path='/license/mit' component={LicenseMit}/>
						<Route exact path='/legal' component={Legal}/>
						<Route exact path='/about' component={About}/>
						<Route exact path='/' component={Home}/>

						{/* API documentation routes */}
						<Route path='/product/acorn/docs/api' onEnter={reload}/>
						<Route path='/product/seenc/docs/api' onEnter={reload}/>
						<Route path='/product/xenon/docs/api' onEnter={reload}/>
						<Route path='/product/zenna/docs/api' onEnter={reload}/>
						<Route path='/product/zevra/docs/api' onEnter={reload}/>

						{/* Deprecated routes */}
						<Redirect exact path='/product/xenon/docs/manual' to={{ ...window.location, pathname: '/product/xenon/docs/user-guide' }}/>
						<Redirect exact path='/product/xenon/docs/mods' to={{ ...window.location, pathname: '/product/xenon/docs/mods-guide' }}/>
						<Redirect exact path='/rc' to={{ ...window.location, pathname: '/aviation' }}/>

						{/* Default route */}
						<Route component={NotFound}/>
					</Switch>
				</Router>
				<Footer/>
			</div>
		);
	}

}
