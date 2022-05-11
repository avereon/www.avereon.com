import React, {useEffect} from 'react';
import {BrowserRouter as Router, Route, Routes, useNavigate, useParams} from 'react-router-dom'
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
import Screenshot from "./Screenshot";
import DocViewer from "./DocViewer";
import AcornProduct from "./product/acorn/Product";
import XenonProduct from "./product/xenon/Product";
import XenonMods from "./product/xenon/Mods";
import XenonDocs from "./product/xenon/XenonDocs";
import XenonScreenshots from "./product/xenon/Screenshots";
import NotFound from "./NotFound";
import Status from "./product/Status";
import SeencProduct from "./product/seenc/Product";
import WeaveProduct from "./product/weave/Product";

const reload = () => window.location.reload();

function Redirect({to}) {
	let navigate = useNavigate();
	useEffect(() => {
		navigate(to);
	});
	return null;
}

export default function App() {

	return (
		<div className='app'>
			<Header/>
			<Router>
				<Routes>
					{/* Supported routes */}
					<Route exact path='/product/acorn' element={<AcornProduct/>}/>
					<Route exact path='/product/weave' element={<WeaveProduct/>}/>
					<Route exact path='/product/seenc' element={<SeencProduct/>}/>
					<Route exact path='/product/xenon/docs/user-guide' element={<DocViewer {...useParams()} doc='https://raw.githubusercontent.com/avereon/xenon/master/source/main/docs/manual/content.html' outline={3}/>}/>
					<Route exact path='/product/xenon/docs/mods-guide' element={<DocViewer {...useParams()} doc='https://raw.githubusercontent.com/avereon/xenon/master/source/main/docs/mods/content.html' outline={3}/>}/>
					<Route exact path='/product/xenon/docs/tool-guide' element={<DocViewer {...useParams()} doc='https://raw.githubusercontent.com/avereon/xenon/master/source/main/docs/tools/content.html' outline={3}/>}/>
					<Route exact path='/product/xenon/contribute' render={<DocViewer {...useParams()} doc='https://raw.githubusercontent.com/avereon/xenon/master/source/main/docs/contribute/content.html' outline={3}/>}/>
					<Route exact path='/product/xenon/docs/screenshots' element={<XenonScreenshots/>}/>
					<Route exact path='/product/xenon/docs' element={<XenonDocs/>}/>
					<Route exact path='/product/xenon/mods' element={<XenonMods/>}/>
					<Route exact path='/product/xenon' element={<XenonProduct/>}/>
					<Route exact path='/product/screenshot' element={<Screenshot/>}/>
					<Route exact path='/products' element={<Products/>}/>
					<Route exact path='/status' element={<Status/>}/>
					<Route exact path='/license/mit' element={<LicenseMit/>}/>
					<Route exact path='/legal' element={<Legal/>}/>
					<Route exact path='/about' element={<About/>}/>
					<Route exact path='/' element={<Home/>}/>

					{/* API documentation routes */}
					<Route path='/product/acorn/docs/api' onEnter={reload}/>
					<Route path='/product/seenc/docs/api' onEnter={reload}/>
					<Route path='/product/xenon/docs/api' onEnter={reload}/>
					<Route path='/product/zenna/docs/api' onEnter={reload}/>
					<Route path='/product/zevra/docs/api' onEnter={reload}/>

					{/* Deprecated routes */}
					<Route path='/product/xenon/docs/manual' render={() => <Redirect to='/product/xenon/docs/user-guide'/>}/>
					<Route path='/product/xenon/docs/mods' render={() => <Redirect to='/product/xenon/docs/mods-guide'/>}/>
					<Route path='/rc' render={() => <Redirect to='/aviation'/>}/>

					{/* Default route */}
					<Route element={<NotFound/>}/>
				</Routes>
			</Router>
			<Footer/>
		</div>
	);

}
