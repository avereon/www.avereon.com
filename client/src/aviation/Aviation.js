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
                                    <th className='nowrap'>Weather</th>
                                    <th>Explanation</th>
                                </tr>
                                <tr>
                                    <td>April</td>
                                    <td className='nowrap'>Cold, Windy, Wet</td>
                                    <td>
                                        Temperatures are still cold and strong wind conditions are still possible as
                                        winter changes over to spring. There is still a possibility of snow, rain and
                                        wet flying fields.
                                    </td>
                                </tr>
                                <tr>
                                    <td>May</td>
                                    <td className='nowrap'>Cool, Windy, Wet</td>
                                    <td>
                                        Temperatures are still cool and string wind conditions are still possible as
                                        summer approaches. There is still a possibility of snow, rain and wet flying
                                        fields. This month generally has the most severe weather of the year.
                                    </td>
                                </tr>
                                <tr>
                                    <td>June</td>
                                    <td className='nowrap'>Warm, Breezy</td>
                                    <td>
                                        Temperatures start to warm and strong wind conditions are still possible. As
                                        the temperatures warm the winds usually subside resulting in favorable flight
                                        conditions. More daylight results in more flying hours.
                                    </td>
                                </tr>
                                <tr>
                                    <td>July</td>
                                    <td className='nowrap'>Hot, Breezy</td>
                                    <td>
                                        Temperatures can get pretty hot by this time of year and summer thunderstorms
                                        start to increase in the afternoons. More daylight provides more flying hours.
                                    </td>
                                </tr>
                                <tr>
                                    <td>August</td>
                                    <td className='nowrap'>Hot, Breezy</td>
                                    <td>
                                        Temperatures are still hot but start cooling a little. Summer thunderstorms
                                        are most prevalent this month. More daylight provides more flying hours.
                                    </td>
                                </tr>
                                <tr>
                                    <td>September</td>
                                    <td className='nowrap'>Warm, Breezy</td>
                                    <td>
                                        Temperatures are starting to cool but warm flying conditions are common. There
                                        is still a risk of thunderstorms but still plenty of daylight for evening
                                        flying.
                                    </td>
                                </tr>
                                <tr>
                                    <td>October</td>
                                    <td className='nowrap'>Cool, Breezy</td>
                                    <td>
                                        Temperatures continue to cool and breezy conditions pick up as fall starts.
                                        There is even the risk of rain and early snow. The days are getting shorter
                                        resulting in fewer flying hours.
                                    </td>
                                </tr>
                                <tr>
                                    <td>November</td>
                                    <td className='nowrap'>Cold, Windy</td>
                                    <td>
                                        Temperatures get cold and fall changes over to winter. Stronger wind conditions
                                        can be present and there is the possibility of rain and snow.
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>

                <h3><FontAwesomeIcon icon={['fas', 'battery-full']}/> AA Transmitter Batteries</h3>
                <div className='product-bar'>
                    <div className='body'>
                        <div>
                            Recently we learned some valuable lessons regarding rechargeable
                            batteries in our radio transmitters. Being budget minded, many of
                            us have the FlySky i6X transmitter that uses four AA size
                            batteries. The expectation is, of course, that each battery is 1.5
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
                                    <td>NiZn</td>
                                    <td>1.6</td>
                                    <td>Yes</td>
                                </tr>
                                <tr>
                                    <td colspan='100'>* The Lithium chemistry is regulated to 1.5 volts</td>
                                </tr>
                            </table>
                        </div>
                        <div>
                            We have since tested with both the Lithium and NiZn chemistries
                            and found slightly improved range and will probably continue to
                            use the NiZn type for now.
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
