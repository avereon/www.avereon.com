import React from "react";

export default class DocViewer extends React.Component {

	state = {
		docIndex: 'INDEX',
		docContent: 'CONTENT'
	};

	componentDidMount() {
		// Load the viewer index
		fetch(this.props.doc + "/index.html").then((response) => {
			response.text().then((text) => this.setState({docIndex: text}))
		});
		// Load the viewer content
		fetch(this.props.doc + "/content.html").then((response) => {
			response.text().then((text) => this.setState({docContent: text}))
		});
	}

	getDocIndex() {
		return {__html: this.state.docIndex}
	};


	getDocContent() {
		return {__html: this.state.docContent};
	}

	render() {
		return (
			<div className='content'>
				<div className='doc-view'>
					<div className='doc-index' dangerouslySetInnerHTML={this.getDocIndex()}/>
					<div className='doc-content' dangerouslySetInnerHTML={this.getDocContent()}/>
				</div>
			</div>
		)
	}

}
