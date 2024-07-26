import React from 'react';
import Icon from '../../Icon';
import Platform from '../../Platform';
import {library} from '@fortawesome/fontawesome-svg-core';
import {fas} from '@fortawesome/free-solid-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import '../../css/product.css';
import ProductPage from "../ProductPage";

library.add(fas, fab);

export default class Product extends React.Component {

    state = {
        stable: {
            linux: {}, macosx: {}, windows: {}
        }, latest: {
            linux: {}, macosx: {}, windows: {}
        }
    };

    componentDidMount() {
        ProductPage.productCards('carta', (cards) => {
            this.setState(cards);
        });
    }

    render() {
        let stableDownload = <div className='download-row'>
            {ProductPage.createDownloadTile('primary', 'stable', 'Weave', this.state, Platform.CURRENT, 'product')}
        </div>;

        let latestDownload = <div className='download-row'>
            {ProductPage.createDownloadTile('primary', 'latest', 'Weave', this.state, Platform.CURRENT, 'product')}
        </div>;

        return (<div className='content'>
            <div className='product'>

                <div className='product-title'>
                    <img className="product-icon" alt="" src={Icon.CARTA}/>
                    <div className='product-name'>Cartesia</div>
                </div>

                <div className='product-content'>
                    Cartesia is a computer aided design (CAD) mod for <a href='../xenon'>Xenon</a>.
                    Designed with simplicity
                    and user experience in mind, Cartesia offers a large set of simple commands
                    to construct your design. Cartesia provides common geometry, snaps,
                    modifiers and transforms as well as unlimited layers. Bring your vision
                    to life with Cartesia.
                </div>

                <div className='product-content'>
                    <h2>Installation</h2>
                    Cartesia is an extension to Xenon. To install Cartesia,
                    download and install <a href='../xenon'>Xenon</a> first.
                </div>

            </div>

        </div>);
    }

}
