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
                        <ScreenshotTile title='Welcome Tool' path='welcome-tool'>
                            The first time the program is started the Welcome
                            tool is shown. The welcome tool gives quick access
                            to some general program resources.
                        </ScreenshotTile>
                        <ScreenshotTile title='About Tool' path='about-tool'>
                            The About tool shows program information and has
                            several pages of information of varying detail.
                        </ScreenshotTile>
                        <ScreenshotTile title='Settings Tool' path='settings/settings-tool-general'>
                            The Settings tool give access to all the program
                            and tool settings. Modules can add settings as
                            they are added as well.
                        </ScreenshotTile>
                    </div>
                    <div className='column'>
                        <ScreenshotTile title='Default Workarea' path='default-workarea'>
                            This is the default workarea. Note that it is
                            basically empty, leaving the space for the user to
                            fill with their work.
                        </ScreenshotTile>
                        <ScreenshotTile title='Installed Modules' path='settings/settings-tool-modules'>
                            This tool shows the installed modules and allows
                            the user to enable, disable and remove modules.
                        </ScreenshotTile>
                        <ScreenshotTile title='Module Markets' path='settings/settings-tool-modules-sources'>
                            This tool shows the module sources. Modules sources
                            can be added by the user to allow custom modules to
                            be added.
                        </ScreenshotTile>
                    </div>
                </div>
            </div>
        )
    }

}
