import React from "react";

export default class CopyrightText extends React.Component {

	render() {
		return (
				<span>&copy; Avereon {new Date().getFullYear()}</span>
		)
	}

}
