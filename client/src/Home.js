import React from "react";
import Icon from "./Icon";
import * as Image from "./Image";

export default class Home extends React.Component {

    render() {
        const imageSrc = Image.XENON_WELCOME_1x;
        const imageSet = Image.XENON_WELCOME_2x + " 2x, " + imageSrc + " 1x";
        const cartaImageSrc = Image.CARTESIA_JET_SAMPLE_1x;
        const cartaImageSet = Image.CARTESIA_JET_SAMPLE_2x + " 2x, " + cartaImageSrc + " 1x";

        return (
            <div className='content'>
                <div className='announcement-table'>

                    <div className='announcement-row'>

                        <div className='announcement-cell'>
                            <a href='/product/xenon'>
                                <img className="product-icon" src={Icon.XENON_LIGHT} alt="Product Icon"/>
                            </a>
                        </div>

                        <div className='announcement-cell'>
                            <div className='announcement-title'>
                                Xenon 1.9
                            </div>
                            <div className='announcement-body'>
                                <a href='/product/xenon'>Download now</a> to get the latest features:
                                <ul>
                                    <li>User interface improvements</li>
                                    <li>Performance improvements</li>
                                    <li>JDK 24 code enhancements</li>
                                    <li>Updated modules</li>
                                    <li>Bug fixes</li>
                                </ul>
                            </div>
                        </div>

                        <div className='announcement-cell'>
                            <a href='/product/xenon'>
                                <img className='screenshot-home'
                                     src={imageSrc} srcSet={imageSet}
                                     alt="Xenon Welcome Tool"
                                />
                            </a>
                        </div>
                    </div>

                    <div className='announcement-row'>
                        <div className='announcement-cell'>
                            <a href='/product/cartesia'>
                                <img className="product-icon" src={Icon.CARTA} alt="Product Icon"/>
                            </a>
                        </div>
                        <div className='announcement-cell'>
                            <div className='announcement-title'>
                                Cartesia 1.3
                            </div>
                            <div className='announcement-body'>
                                <a href='/product/cartesia'>Install now</a> to get the latest features:
                                <ul>
                                    <li>Layers - Unlimited layers</li>
                                    <li>Geometry - Box, line, circle, ellipse, arc, curve, path and text</li>
                                    <li>Snaps - Grid, nearest, midpoint, center</li>
                                    <li>Modifiers - Trim, extend, break</li>
                                    <li>Transforms - Move, copy, flip, mirror, rotate, scale, stretch</li>
                                    <li>Bug fixes</li>
                                </ul>
                            </div>
                        </div>

                        <div className='announcement-cell'>
                            <a href='/product/cartesia'>
                                <img className='screenshot-home'
                                    src={cartaImageSrc} srcSet={cartaImageSet}
                                    alt="Cartesia Design Tool"
                                />
                            </a>
                        </div>
                    </div>

                </div>
            </div>
        )
    }

}
