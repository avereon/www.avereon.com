import React from "react";
import Icon from "../../Icon";
import {ScreenshotTile} from "../../Screenshot";

export default class Screenshots extends React.Component {

    render() {
        return (
            <div className='content'>
                <div className='product-title'>
                    <img className="product-icon" alt="" src={Icon.XENON_LIGHT}/>
                    <div className='product-name'>Xenon Screenshots</div>
                </div>

                <div className='columns'>
                    <div className='column'>
                        <ScreenshotTile title='Welcome Tool' path='welcome-tool'/>
                        <ScreenshotTile title='About Tool' path='about-tool'/>
                        <ScreenshotTile title='Settings Tool' path='settings/settings-tool-general'/>
                    </div>
                    <div className='column'>
                        <ScreenshotTile title='Default Workarea' path='default-workarea'/>
                        <ScreenshotTile title='Installed Modules' path='settings/settings-tool-modules'/>
                        <ScreenshotTile title='Module Markets' path='settings/settings-tool-modules-sources'/>
                    </div>
                </div>
            </div>
        )
    }

}
