import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default class Aviation extends React.Component {

	render() {
		return (
			<div className='content product'>
				<h1>Aviation (Radio Control)</h1>

				<h3><FontAwesomeIcon icon={['fas', 'calendar']}/> 2021 Flying Season</h3>
				<div className='product-bar'>
					<div className='body'>
						<div>
							The 2021 flying season is quickly approaching even though its
							still a few months away. Here is a monthly calendar with general
							weather conditions and challenges:
							<table>
								<tr>
									<th>Month</th>
									<th>Weather</th>
									<th>Challenges</th>
								</tr>
								<tr>
									<td>April</td>
									<td>Cold, Windy, Wet</td>
									<td>Still cold and changing wind conditions as we get out of winter. Possible snow and rain.</td>
								</tr>
								<tr>
									<td>May</td>
									<td>Cool, Windy, Wet</td>
									<td>Still changing wind conditions as summer approaches. Rain and thunderstorms.</td>
								</tr>
								<tr>
									<td>June</td>
									<td>Warm, Breezy</td>
									<td>Still changing wind conditions as summer approaches.</td>
								</tr>
								<tr>
									<td>July</td>
									<td>Hot, Breezy</td>
									<td>Summer heat.</td>
								</tr>
								<tr>
									<td>August</td>
									<td>Hot</td>
									<td>Summer heat and thunderstorms.</td>
								</tr>
								<tr>
									<td>September</td>
									<td>Hot, Breezy</td>
									<td>Summer heat and thunderstorms.</td>
								</tr>
								<tr>
									<td>October</td>
									<td>Cool, Breezy</td>
									<td>Cooler temperatures and breezy conditions as fall starts.</td>
								</tr>
								<tr>
									<td>November</td>
									<td>Cold, Windy</td>
									<td>Colder temperatures, changing wind conditions and possible early snow.</td>
								</tr>
							</table>
						</div>
					</div>
				</div>

				<h3><FontAwesomeIcon icon={['fas', 'battery-full']}/> Batteries</h3>
				<div className='product-bar'>
					<div className='body'>
						<div>
							Recently we learned some valuable lessons regarding rechargeable
							batteries in our radio transmitters. Being budget minded many of
							us have the FlySky i6X transmitter that uses four AA size
							batteries. Of course the expectation is that each battery is 1.5
							volts. Some of us started using rechargeable NiMH batteries and
							found after some time that our transmitter was always showing that
							our battery level was at 60%, even with fully charged batteries.
						</div>
						<div>
							This is where we started to research the problem. We found that
							NiMH batteries only provide 1.2 volts of power when fully charged.
							We then researched other battery chemistries and found the
							following information.
							<table>
								<tr>
									<th>Battery Type</th>
									<th>Volts</th>
									<th>Rechargeable</th>
								</tr>
								<tr>
									<td>NiMH</td>
									<td>1.2</td>
									<td>Yes</td>
								</tr>
								<tr>
									<td>Alkaline</td>
									<td>1.5</td>
									<td>No</td>
								</tr>
								<tr>
									<td>Lithium*</td>
									<td>1.5</td>
									<td>Yes</td>
								</tr>
								<tr>
									<td>NiZN</td>
									<td>1.6</td>
									<td>Yes</td>
								</tr>
								<tr colspan='3'>
									<td>* The Lithium chemistry is regulated to 1.5 volts</td>
								</tr>
							</table>
						</div>
						<div>
							We have since tested with both the Lithium and NiZN chemistries
							and found slightly improved range and will probably continue to
							use the NiZN type for now.
						</div>
						<div>
							Note that there are other transmitter vendors that also support
							the use of Lithium Polymer batteries and we invite you to research
							these options for your use.
						</div>
						<div>2021-01-09 - Mark Soderquist</div>
					</div>
				</div>

				<h3><FontAwesomeIcon icon={['fas', 'user']}/> About Us</h3>
				<div className='product-bar'>
					<div className='body'>
						<div>
							We are a small group of engineers, professionals and hobbyists
							that have come together for one purpose, to learn through the use
							of radio control aircraft.
						</div>
					</div>
				</div>

			</div>
		)
	}

}