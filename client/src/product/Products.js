import React from "react";
import * as Icon from "../Icon";

export default class Products extends React.Component {

	render() {
		return (
			<div className='content'>
				<h1 className='title'>Products</h1>
				<div className='product-bar'>
					<a href='/product/xenon'><img className="product-icon" alt="" src={Icon.XENON}/></a>
					<div className='body'>
						<div>
							<a href='/product/xenon'>Xenon</a> is a simple application framework that provides services common
							to modern desktop applications. Specific functionality is implemented
							with downloadable mod packages.
						</div>
					</div>
				</div>

				<div className='product-bar'>
					<a href='https://github.com/avereon/zenna'><img className="product-icon" alt="" src={Icon.AVEREON}/></a>
					<div className='body'>
						<div><a href='https://github.com/avereon/zenna'>Zenna</a> is a simple update
							program that supports a simple command language to customize the
							update process. Initially developed for Xenon, Zenna is a standalone
							program and can be used with any product.
						</div>
					</div>
				</div>

				<div className='product-bar'>
					<a href='https://github.com/avereon/seenc'><img className="product-icon" alt="" src={Icon.AVEREON}/></a>
					<div className='body'>
						<div><a href='https://github.com/avereon/seenc'>Seenc</a> is a configurable
							command line tool to synchronized Git repositories with local clones.
							Seenc allows you to configure what repositories to synchronize,
							where to place the local clones and what branches to sync.
						</div>
					</div>
				</div>

				<div className='product-bar'>
					<a href='https://github.com/avereon/acorn'><img className="product-icon" alt="" src={Icon.AVEREON}/></a>
					<div className='body'>
						<div><a href='https://github.com/avereon/acorn'>Acorn</a> is a command line
							tool to score system performance in Java. The tool allows you to
							compare the performance on different Java Virtual Machines for
							different hardware.
						</div>
					</div>
				</div>
			</div>
		)
	}

}
