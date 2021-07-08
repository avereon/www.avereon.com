import React from "react";
import Icon from "../Icon";

export default class Products extends React.Component {

	render() {
		return (
			<div className='content columns'>
				<div className='column product-status'>
					<h3>Products</h3>
					<ProductStatus ident='xenon' icon={Icon.XENON_LIGHT} name='Xenon'/>
					<ProductStatus ident='weave' icon={Icon.WEAVE} name='Weave'/>
					<ProductStatus ident='acorn-cli' icon={Icon.ACORN} name='Acorn CLI'/>
					<ProductStatus ident='seenc' icon={Icon.SEENC} name='Seenc'/>
				</div>
				<div className='column product-status'>
					<h3>Xenon Mods</h3>
					<ProductStatus ident='acorn' icon={Icon.ACORN} name='Acorn'/>
					{/*<ProductStatus ident='amazo' icon={Icon.AMAZO} name='Amazo'/>*/}
					{/*<ProductStatus ident='arena' icon={Icon.ARENA} name='Arena'/>*/}
					<ProductStatus ident='aveon' icon={Icon.AVEON} name='Aveon'/>
					<ProductStatus ident='carta' icon={Icon.CARTA} name='Cartesia'/>
					<ProductStatus ident='mazer' icon={Icon.MAZER} name='Mazer'/>
					<ProductStatus ident='recon' icon={Icon.RECON} name='Recon'/>
				</div>
				<div className='column product-status'>
					<h3>Libraries</h3>
					<ProductStatus ident='curve' icon={Icon.CURVE} name='Curve'/>
					<ProductStatus ident='zarra' icon={Icon.ZARRA} name='Zarra'/>
					<ProductStatus ident='zenna' icon={Icon.ZENNA} name='Zenna'/>
					<ProductStatus ident='zerra' icon={Icon.ZERRA} name='Zerra'/>
					<ProductStatus ident='zevra' icon={Icon.ZEVRA} name='Zevra'/>
					<h3>Maven Plugins</h3>
					<ProductStatus ident='cameo' icon={Icon.CAMEO} name='Cameo'/>
					<ProductStatus ident='curex' icon={Icon.CUREX} name='Curex'/>
				</div>
			</div>
		)
	}

}

class ProductStatus extends React.Component {

	render() {
		return (
			<div className='product-bar'>
				<a href={"https://github.com/avereon/" + this.props.ident}>
					<img className="product-icon" alt="" src={this.props.icon}/>
				</a>
				<div className='body'>
					<h6>{this.props.name}</h6>
					<a href={'https://github.com/avereon/' + this.props.ident + '/actions'}>
						<img alt={this.props.name + " Build Status"} src={"https://github.com/avereon/" + this.props.ident + "/workflows/CI/badge.svg"}/>
					</a>
				</div>
			</div>
		)
	}

}
