import React from 'react';
import * as Icon from '../../Icon';
import * as Platform from '../../Platform';
import {library} from '@fortawesome/fontawesome-svg-core';
import {fas} from '@fortawesome/free-solid-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import '../../css/product.css';
import ProductPage from "../ProductPage";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

library.add(fas, fab);

export default class SeencProduct extends React.Component {

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
        ProductPage.productCards('seenc', (cards) => {
            this.setState(cards);
        });
    }

    render() {
        let stableDownload = <div className='download-row'>
            {ProductPage.createDownloadTile('primary', 'stable', 'Seenc', this.state, Platform.CURRENT, 'product')}
        </div>;

        let latestDownload = <div className='download-row'>
            {ProductPage.createDownloadTile('primary', 'latest', 'Seenc', this.state, Platform.CURRENT, 'product')}
        </div>;

        return (
            <div className='content'>
                <div className='product'>

                    <div className='product-title'>
                        <img className="product-icon" alt="" src={Icon.SEENC}/>
                        <div className='product-name'>Seenc</div>
                    </div>

                    <div className='product-content'>
                        Seenc is a simple system profiling tool. It will run
                        anywhere Java is supported. The tool is great for
                        getting a quick understanding how your system compares
                        with others.
                    </div>

                    {stableDownload}

                    <div className='resource-row'>
                        <div className='resource-tile'>
                            <div><a href='/product/seenc/docs/api/index.html'><FontAwesomeIcon icon={['fas', 'tools']}/> Seenc API</a></div>
                        </div>
                        <div className='resource-tile'>
                            <div><a href='https://github.com/avereon/seenc' target="_blank" rel="noopener noreferrer"><FontAwesomeIcon icon={['fab', 'github']}/> Source Code</a></div>
                        </div>
                    </div>

                    {latestDownload}
                </div>

            </div>
        );
    }

}
