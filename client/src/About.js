import React from "react";
import * as Icon from "./Icon";

export default class About extends React.Component {

	render() {
		let flow={float: 'left'};

		return (
			<div className='content'>
				<h1 className='title'>About Us</h1>
				<p>
					Avereon is a small organization of engineers, students, hobbyists and
					artists with the simple goal of creating unique products for the work
					they do.
				</p>

				<h1>Icons</h1>
				<p>
					<img className="product-icon" style={flow} alt="" src={Icon.AVEREON}/>
					The Avereon wing icon is a stylized <a href='https://en.wikipedia.org/wiki/Flying_wing'>flying wing</a> with
					an advanced	propulsion system, possibly a
					high-thrust <a href='https://en.wikipedia.org/wiki/Ion_thruster'>ion drive</a>.
					Several flying wing aircraft used for inspiration include
					the	<a href='https://en.wikipedia.org/wiki/Northrop_Grumman_B-2_Spirit'>B-2 Spirt</a>,
					the <a href='https://en.wikipedia.org/wiki/Lockheed_F-117_Nighthawk'>F-117 Nighthawk</a> and
					the <a href='https://en.wikipedia.org/wiki/Northrop_N-9M'>Northrop N-9M</a>.
				</p>
				<p>
					<img className="product-icon" style={flow} alt="" src={Icon.XENON}/>
					The Xenon X icon is a stylized <a href='https://en.wikipedia.org/wiki/Bipolar_nebula'>bipolar planetary nebula</a> surrounded
					by an accretion disk that causes the gas to stay in a double cone-like
					structure similar to the <a href='https://en.wikipedia.org/wiki/NGC_6302'>Butterfly Nebula</a> or
					the <a href='https://en.wikipedia.org/wiki/Egg_Nebula'>Egg Nebula</a>. It
					has also been considered to be a
					binary <a href='https://en.wikipedia.org/wiki/Herbig%E2%80%93Haro_object'>Herbig-Haro object</a> with
					the stars orbiting such that their axes are perpendicular to each other, even
					though no such object has been observed that we are aware of.
				</p>
			</div>
		)
	}

}