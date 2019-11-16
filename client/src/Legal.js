import React from "react";

export default class Legal extends React.Component {

	render() {
		return (
			<div className='content'>
				<h1>Legal Notices</h1>

				<div>
					<h4>Copyright</h4>
					<p>
						Copyright &copy; 2019 Avereon
					</p>

					<h4>Attribution</h4>
					<p>
						This web site uses the superb <a href='https://www.fontspace.com/bernd-montag/sansation'>Sansation</a> font
						created by Bernd Motag.
					</p>

					<h4>Trademarks</h4>
					<p>
						Java and OpenJDK are trademarks or registered trademarks of Oracle
						and/or its affiliates. Other names may be trademarks of their
						respective owners.
					</p>

					<h4>Licenses</h4>
					<p>
						All Avereon products are licensed under the <a href='/licenses/mit'>MIT License</a>.
					</p>
				</div>
			</div>
		)
	}

}