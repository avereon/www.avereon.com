import React from 'react';
import * as Config from "../Config";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default class ProductPage extends React.Component {

    static productCards(product, success, failure) {
        const url = Config.DOWNLOAD_URL + "/product/cards/" + product;
        return fetch(url)
            .then((response) => response.status === 200 ? response.json() : {})
            .then((card) => success(card))
            .catch(failure)
    }

    static createDownloadTile(type, category, product, store, platform, pack = 'install') {
        const card = store[category][platform.KEY];

        const artifact = card.artifact;
        const version = card.version;
        // TODO Clean this up after the 1.3 release
        const ext = version > '1.2' ? platform.INSTALLER_EXT : 'jar';

        let style = 'download ' + type;
        const platformSize = type === 'primary' ? '3x' : '2x';
        const platformIcon = type === 'primary' ? 'download' : ['fab', platform.ICON];
        if (version === undefined) {
            return <div className={style + ' disabled'}>
                <div className='download-layout'>
                    <FontAwesomeIcon className="download-icon" icon={platformIcon} size={platformSize}/>
                    <div className='download-metadata'>
                        <div className='title'>{product} for {platform.NAME}</div>
                        <div>unavailable</div>
                    </div>
                </div>
            </div>
        } else {
            return <a className={style + " " + category} href={Config.DOWNLOAD_URL + '/' + category + '/' + artifact + '/' + platform.KEY + '/' + pack + '/' + ext}>
                <div className='download-layout'>
                    <FontAwesomeIcon className='download-icon' icon={platformIcon} size={platformSize}/>
                    <div className='download-metadata'>
                        <div className='title'>{product} for {platform.NAME}</div>
                        <div>{version}</div>
                    </div>
                </div>
            </a>
        }
    }

}
