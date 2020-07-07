import React from "react";
import * as Icon from "../Icon";

export default class Products extends React.Component {

	render() {
		return (
			<div className='content columns'>
				<div className='column product-status'>
					<h3>Products</h3>
					<ProductStatus ident='xenon' icon={Icon.XENON_LIGHT} name='Xenon'/>
					<ProductStatus ident='zenna' icon={Icon.ZENNA} name='Zenna'/>
					<h3>Libraries</h3>
					<ProductStatus ident='rossa' icon={Icon.ROSSA} name='Rossa'/>
					<ProductStatus ident='venza' icon={Icon.VENZA} name='Venza'/>
					<ProductStatus ident='zevra' icon={Icon.ZEVRA} name='Zevra'/>
					<h3>Maven Plugins</h3>
					<ProductStatus ident='cameo' icon={Icon.CAMEO} name='Cameo'/>
					<ProductStatus ident='curex' icon={Icon.CUREX} name='Curex'/>
				</div>
				<div className='column product-status'>
					<h3>Mods</h3>
					{/*<ProductStatus ident='amazo' icon={Icon.AMAZO} name='Amazo'/>*/}
					{/*<ProductStatus ident='arena' icon={Icon.ARENA} name='Arena'/>*/}
					<ProductStatus ident='aveon' icon={Icon.AVEON} name='Aveon'/>
					<ProductStatus ident='cart2' icon={Icon.CART2} name='Cartesia 2D'/>
					<ProductStatus ident='cart3' icon={Icon.CART3} name='Cartesia 3D'/>
					<ProductStatus ident='mazer' icon={Icon.MAZER} name='Mazer'/>
					<ProductStatus ident='recon' icon={Icon.RECON} name='Recon'/>
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
						<img alt={this.props.name + " Build Status"} src={"https://github.com/avereon/" + this.props.ident + "/workflows/Avereon%20" + this.props.name + "%20Continuous/badge.svg"}/>
					</a>
				</div>
			</div>
		)
	}

}
