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
        ProductPage.productCards('weave', (cards) => {
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

        return (
            <div className='content'>
                <div className='product'>

                    <div className='product-title'>
                        <img className="product-icon" alt="" src={Icon.SEENC}/>
                        <div className='product-name'>Weave</div>
                    </div>

                    <div className='product-content'>
                        Weave is a simple update utility to apply updates. This
                        program is commonly used with other programs to
                        automate the update process. It has pre-defined tasks
                        that can be specified via stdin or file. If any task
                        requires elevated privileges the user is prompted for
                        proper credentials. If specified, it has a simple UI
                        that can be used to provide user feedback regarding the
                        progress.
                    </div>

                    {stableDownload}

                    <div className='resource-row'>
                        <div className='resource-tile'>
                            <div><a href={Config.ROOT_URL + '/product/weave/docs/api/index.html'}><FontAwesomeIcon icon={['fas', 'tools']}/> Weave API</a></div>
                        </div>
                        <div className='resource-tile'>
                            <div><a href='https://github.com/avereon/weave' target="_blank" rel="noopener noreferrer"><FontAwesomeIcon icon={['fab', 'github']}/> Source Code</a></div>
                        </div>
                    </div>

                    {latestDownload}
                </div>

            </div>
        );
    }

}
