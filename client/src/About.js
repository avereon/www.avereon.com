import React from "react";
import * as Icon from "./Icon";

export default class About extends React.Component {

	render() {
		return (
			<div className='content product'>
				<h1>About Us</h1>
				<div className='product-bar'>
					<img className="product-icon" alt="" src={Icon.AVEREON}/>
					<div className='body'>
						<div>
							We are a small group of engineers, students, hobbyists and
							artists that create unique products for the work we do. We
							hope others will find our tools useful for their own work.
						</div>
					</div>
				</div>

				<h2>Icons</h2>

				<div className='product-bar'>
					<img className="product-icon" alt="" src={Icon.AVEREON}/>
					<div className='body'>
						<div>
							The Avereon wing icon is a stylized <a href='https://en.wikipedia.org/wiki/Flying_wing'>flying wing</a> with
							an advanced propulsion system, possibly a
							high-thrust <a href='https://en.wikipedia.org/wiki/Ion_thruster'>ion drive</a>.
							Several flying wing aircraft used for inspiration include
							the <a href='https://en.wikipedia.org/wiki/Northrop_Grumman_B-2_Spirit'>B-2 Spirt</a>,
							the <a href='https://en.wikipedia.org/wiki/Lockheed_F-117_Nighthawk'>F-117 Nighthawk</a> and
							the <a href='https://en.wikipedia.org/wiki/Northrop_N-9M'>Northrop N-9M</a>.
						</div>
					</div>
				</div>

				<div className='product-bar'>
					<img className="product-icon" alt="" src={Icon.XENON}/>
					<div className="body">
						<div>
							The Xenon X icon is a stylized <a href='https://en.wikipedia.org/wiki/Bipolar_nebula'>bipolar planetary nebula</a> surrounded
							by an accretion disk that causes the gas to stay in a double cone-like
							structure similar to the <a href='https://en.wikipedia.org/wiki/NGC_6302'>Butterfly Nebula</a> or
							the <a href='https://en.wikipedia.org/wiki/Egg_Nebula'>Egg Nebula</a>. It
							has also been considered to be a
							binary <a href='https://en.wikipedia.org/wiki/Herbig%E2%80%93Haro_object'>Herbig-Haro object</a> with
							the stars orbiting such that their axes are perpendicular to each other, even
							though no such object has been observed that we are aware of.
						</div>
					</div>
				</div>
			</div>
		)
	}

}