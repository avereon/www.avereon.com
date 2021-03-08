import * as Config from "./Config";
import React from "react";
import {Link} from "react-router-dom";

function X1(name) {
    return Config.IMAGE_ROOT + "/screenshots/" + name + ".png";
}

function X2(name) {
    return Config.IMAGE_ROOT + "/screenshots/" + name + "@2x.png";
}

export default class Screenshot extends React.Component {
    /* The original screenshots are 800x500 */
    width = 80 * 10
    height = 50 * 10

    render() {
        let title = this.props.location.state.title
        let path = this.props.location.state.path
        let link = this.props.location.state.returnLink

        return (
            <div className='content'>
                <h1>{title}</h1>
                <Link className='screenshot-tile' to={link}>
                    <img width={this.width} height={this.height} srcSet={X2(path) + " 2x, " + X1(path) + " 1x"} src={X1(path)} alt={title}/>
                </Link>
            </div>
        )
    }
}

export class ScreenshotTile extends React.Component {
    width = 80 * 2
    height = 50 * 2

    render() {
        let title = this.props.title
        let path = this.props.path
        let returnLink = window.location.pathname
        return (
            <div className='content'>
                <h6>{title}</h6>
                <Link className='screenshot-tile' to={{pathname: '/product/screenshot', state: {title, path, returnLink}}}>
                    <img width={this.width} height={this.height} srcSet={X2(path) + " 2x, " + X1(path) + " 1x"} src={X1(path)} alt={title}/>
                </Link>
            </div>
        )
    }
}
