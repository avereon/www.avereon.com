import React from 'react';
import * as Config from '../../Config';
import * as Icon from '../../Icon';
import * as Platform from '../../Platform';
import {library} from '@fortawesome/fontawesome-svg-core';
import {fas} from '@fortawesome/free-solid-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import '../../css/product.css';
import XenonProduct from "../xenon/XenonProduct";

library.add(fas, fab);

export default class AcornProduct extends React.Component {

    state = {
        stable: {
            linux: {},
            macosx: {},
            windows: {}
        },
        latest: {
            linux: {},
            macosx: {},
            windows: {}
        }
    };

    componentDidMount() {
        XenonProduct.productCards('acorn', (cards) => {
            this.setState(cards);
        });
    }

    render() {
        let stableDownload = <div className='download-row'>
            {XenonProduct.createDownloadTile('primary', 'stable', 'Acorn', this.state, Platform.CURRENT, 'product')}
        </div>;

        let latestDownload = <div className='download-row'>
            {XenonProduct.createDownloadTile('primary', 'latest', 'Acorn', this.state, Platform.CURRENT, 'product')}
        </div>;

        return (
            <div className='content'>
                <div className='product'>

                    <div className='product-title'>
                        <img className="product-icon" alt="" src={Icon.ACORN}/>
                        <div className='product-name'>Acorn</div>
                    </div>

                    <div className='product-content'>
                        Acorn is a simple system profiling tool. It will run
                        anywhere Java is supported. The tool is great for
                        getting a quick understanding how your system compares
                        with others.
                    </div>

                    {stableDownload}

                    {latestDownload}
                </div>

            </div>
        );
    }

}
