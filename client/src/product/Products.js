import React from "react";
import Icon from "../Icon";

export default class Products extends React.Component {

    render() {
        return (
            <div className='content'>
                <div className='product'>

                    <h1 className='title'>Products</h1>

                    <div className='product-bar'>
                        <a href='/product/xenon'><img className="product-icon" alt="" src={Icon.XENON_LIGHT}/></a>
                        <div className='body'>
                            <div>
                                <a href='/product/xenon'>Xenon</a> is a simple application framework that provides
                                services common
                                to modern desktop applications. Specific functionality is implemented
                                with downloadable module packages.
                            </div>
                        </div>
                    </div>

                    <div className='product-bar'>
                        <a href='/product/cartesia'><img className="product-icon" alt="" src={Icon.CARTA}/></a>
                        <div className='body'>
                            <div>
                                <a href='/product/cartesia'>Cartesia</a> is a computer aided design (CAD) module for
                                Xenon. Designed with simplicity and user experience in mind, Cartesia offers a large set
                                of simple commands to construct your design. Cartesia provides common geometry, snaps,
                                modifiers and transforms as well as unlimited layers. Bring your vision to life with
                                Cartesia.
                            </div>
                        </div>
                    </div>

                    {/*<div className='product-bar'>*/}
                    {/*	<a href='/product/acorn'><img className="product-icon" alt="" src={Icon.ACORN}/></a>*/}
                    {/*	<div className='body'>*/}
                    {/*		<div><a href='/product/acorn'>Acorn</a> is a command line*/}
                    {/*			tool to score system performance in Java. The tool allows you to*/}
                    {/*			compare the performance on different Java Virtual Machines for*/}
                    {/*			different hardware.*/}
                    {/*		</div>*/}
                    {/*	</div>*/}
                    {/*</div>*/}

                    {/*<div className='product-bar'>*/}
                    {/*	<a href='/product/weave'><img className="product-icon" alt="" src={Icon.WEAVE}/></a>*/}
                    {/*	<div className='body'>*/}
                    {/*		<div><a href='https://github.com/avereon/weave'>Weave</a> is a simple update*/}
                    {/*			program that supports a simple command language to customize the*/}
                    {/*			update process. Initially developed for Xenon, Weave is a standalone*/}
                    {/*			program and can be used with any product.*/}
                    {/*		</div>*/}
                    {/*	</div>*/}
                    {/*</div>*/}

                    {/*<div className='product-bar'>*/}
                    {/*	<a href='/product/seenc'><img className="product-icon" alt="" src={Icon.SEENC}/></a>*/}
                    {/*	<div className='body'>*/}
                    {/*		<div><a href='/product/seenc'>Seenc</a> is a configurable*/}
                    {/*			command line tool to synchronized Git repositories with local clones.*/}
                    {/*			Seenc allows you to configure what repositories to synchronize,*/}
                    {/*			where to place the local clones and what branches to sync.*/}
                    {/*		</div>*/}
                    {/*	</div>*/}
                    {/*</div>*/}
                </div>
            </div>
        )
    }

}
