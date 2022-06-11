import React from 'react';
import Icon from '../../Icon';
import Platform from '../../Platform';
import {library} from '@fortawesome/fontawesome-svg-core';
import {fas} from '@fortawesome/free-solid-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import '../../css/product.css';
import ProductPage from "../ProductPage";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Config from "../../Config";

library.add(fas, fab);

export default class Product extends React.Component {

    state = {
        stable: {
            any: {},
            linux: {},
            macosx: {},
            windows: {}
        },
        latest: {
            any: {},
            linux: {},
            macosx: {},
            windows: {}
        }
    };

    componentDidMount() {
        ProductPage.productCards('acorn-cli', (cards) => {
            this.setState(cards);
        });
    }

    render() {
        let stableDownload = <div className='download-row'>
            {ProductPage.createDownloadTile('primary', 'stable', 'Acorn', this.state, Platform.ANY, 'product')}
        </div>;

        let latestDownload = <div className='download-row'>
            {ProductPage.createDownloadTile('primary', 'latest', 'Acorn', this.state, Platform.ANY, 'product')}
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

                    <div className='resource-row'>
                        <div className='resource-tile'>
                            <div><a href={Config.ROOT_URL + '/product/acorn/docs/api/index.html'}><FontAwesomeIcon icon={['fas', 'tools']}/> Acorn API</a></div>
                        </div>
                        <div className='resource-tile'>
                            <div><a href='https://github.com/avereon/acorn' target="_blank" rel="noopener noreferrer"><FontAwesomeIcon icon={['fab', 'github']}/> Source Code</a></div>
                        </div>
                    </div>

                    {latestDownload}
                </div>

            </div>
        );
    }

}
