import React from "react";

export default class DocViewer extends React.Component {

	loadDocIndex() {
		return {__html: 'This Section Header Could be a bit longer but what if it is really long bit longer but what if it is really long bit longer but what if it is really long<ul><li>Item 1</li><ul><li>Item 1.1</li></ul></ul>'};
	}

	loadDocContent() {
		return {__html: '<h2>Title</h2><p>This is some content that should be displayed. If it is long enough it should be wrapped. All HTML elements should be respected.</p>'};
	}

	render() {
		return (
			<div className='content'>
				<div className='doc-view'>
					<div className='doc-index' dangerouslySetInnerHTML={this.loadDocIndex()}/>
					<div className='doc-content' dangerouslySetInnerHTML={this.loadDocContent()}/>
				</div>
			</div>
		)
	}

}
