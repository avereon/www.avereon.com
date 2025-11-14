import React from "react";
import Icon from "../../Icon";

export default class Modules extends React.Component {

	render() {
		return (
			<div className='content'>
				<div className='product'>

					<div className='product-title'>
						<img className="product-icon" alt="" src={Icon.XENON_LIGHT}/>
						<div className='product-name'>Xenon Mods</div>
					</div>

					<div className='product-bar'>
						<a href='/product/xenon'><img className="product-icon" alt="" src={Icon.XENON}/></a>
						<div className='body'>
							<div>
								<a href='/product/xenon'>Xenon</a> is an application framework
								that provides common services for modern desktop applications.
								Specific functionality is implemented with downloadable packages
								called modules. Users are encouraged to discover and utilize the
								modules that best suit their needs.
							</div>
						</div>
					</div>

					<div className='product-bar'>
						<a href='https://github.com/avereon/acorn'><img className="product-icon" alt="" src={Icon.ACORN}/></a>
						<div className='body'>
							<div><a href='https://github.com/avereon/acorn'>Acorn</a> provides
								a tool to score system performance in Java. The tool computes
								simple metrics for the current system and displays a score to
								compare the performance on different Java Virtual Machines for
								different hardware.
							</div>
						</div>
					</div>

					<div className='product-bar'>
						<a href='https://github.com/avereon/carta'><img className="product-icon" alt="" src={Icon.CARTA}/></a>
						<div className='body'>
							<div><a href='https://github.com/avereon/carta'>Cartesia</a> provides
								basic 2D and 3D computer aided design tools. Generally
								designed for hobbyists and crafters to have simple and
								inexpensive computer aided design tools available.
							</div>
						</div>
					</div>

					<div className='product-bar'>
						<a href='https://github.com/avereon/recon'><img className="product-icon" alt="" src={Icon.RECON}/></a>
						<div className='body'>
							<div><a href='https://github.com/avereon/recon'>Recon</a> provides
								a tool to monitor access to network devices from the current
								system. This is helpful for tracking down simple network
								connectivity and accessibility issues.
							</div>
						</div>
					</div>

					{/*<div className='product-bar'>*/}
					{/*	<a href='/product/xenon'><img className="product-icon" alt="" src={Icon.XENON}/></a>*/}
					{/*	<div className='body'>*/}
					{/*		<div>*/}
					{/*			<a href='/product/xenon'>Xenon</a> is a simple application framework that provides services common*/}
					{/*			to modern desktop applications. Specific functionality is implemented*/}
					{/*			with downloadable module packages.*/}
					{/*		</div>*/}
					{/*	</div>*/}
					{/*</div>*/}

					{/*<div className='product-bar'>*/}
					{/*	<a href='https://github.com/avereon/zenna'><img className="product-icon" alt="" src={Icon.AVEREON}/></a>*/}
					{/*	<div className='body'>*/}
					{/*		<div><a href='https://github.com/avereon/zenna'>Zenna</a> is a simple update*/}
					{/*			program that supports a simple command language to customize the*/}
					{/*			update process. Initially developed for Xenon, Zenna is a standalone*/}
					{/*			program and can be used with any product.*/}
					{/*		</div>*/}
					{/*	</div>*/}
					{/*</div>*/}

					{/*<div className='product-bar'>*/}
					{/*	<a href='https://github.com/avereon/seenc'><img className="product-icon" alt="" src={Icon.AVEREON}/></a>*/}
					{/*	<div className='body'>*/}
					{/*		<div><a href='https://github.com/avereon/seenc'>Seenc</a> is a configurable*/}
					{/*			command line tool to synchronized Git repositories with local clones.*/}
					{/*			Seenc allows you to configure what repositories to synchronize,*/}
					{/*			where to place the local clones and what branches to sync.*/}
					{/*		</div>*/}
					{/*	</div>*/}
					{/*</div>*/}

				</div>

				More Coming Soon!
			</div>
		)
	}

}
