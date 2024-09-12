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
                    Designed with simplicity and user experience in mind, Cartesia offers a large set of simple commands
                    to construct your design. Cartesia provides common geometry, snaps,
                    modifiers and transforms as well as unlimited layers.
                </div>

                <div className='product-content'>
                    To install Cartesia, download and install the latest version
                    of <a href='/product/xenon'>Xenon</a>. Once Xenon is installed, start Xenon and
                    open the Settings tool, navigate to Mods and install the Cartesia mod. The
                    Cartesia mod will be enabled after installing.
                </div>

                <div className='product-content'>
                    The <a href='/product/cartesia/docs/user-guide'>Cartesia User Guide</a> is available online and
                    provides all the necessary information needed to get started. Documentation for Cartesia commands
                    is also available in the Xenon application with the Cartesia mod installed.
                </div>

            </div>

        </div>);
    }

}
