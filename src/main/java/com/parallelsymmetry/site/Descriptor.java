package com.parallelsymmetry.site;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Descriptor {

	private Node node;

	private List<String> paths;

	public Descriptor() throws SAXException, IOException, ParserConfigurationException {}

	public Descriptor( Node node ) {
		this.node = node;
	}

	public Descriptor( String uri ) throws SAXException, IOException, ParserConfigurationException {
		if( uri == null ) return;
		node = loadXmlDocument( uri );
	}

	public Descriptor( Reader reader ) throws SAXException, IOException, ParserConfigurationException {
		if( reader == null ) return;
		node = loadXmlDocument( reader );
	}

	public Document getDocument() {
		return ( node instanceof Document ) ? (Document)node : node.getOwnerDocument();
	}

	public Node getNode() {
		return node;
	}

	public List<String> getPaths() {
		if( paths == null ) paths = listPaths( node );
		return paths;
	}

	public Node getNode( String path ) {
		return getNode( this.node, path );
	}

	public Node[] getNodes( String path ) {
		return getNodes( this.node, path );
	}

	public String getValue( String path ) {
		return getValue( this.node, path );
	}

	public String getValue( String path, String defaultValue ) {
		return getValue( this.node, path, defaultValue );
	}

	/**
	 * Get an array of all the values that have the same path.
	 * 
	 * @param path
	 * @return An array of values with the same path.
	 */
	public String[] getValues( String path ) {
		return getValues( this.node, path );
	}

	public static Node getNode( Node node, String path ) {
		if( node == null || isEmpty( path ) ) return null;

		Node value = null;
		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			value = (Node)xpath.evaluate( path, node, XPathConstants.NODE );
		} catch( XPathExpressionException exception ) {
			// Intentionally ignore exception.
		}

		return value;
	}

	public static Node[] getNodes( Node node, String path ) {
		if( node == null || isEmpty( path ) ) return null;

		NodeList nodes = null;
		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			nodes = (NodeList)xpath.evaluate( path, node, XPathConstants.NODESET );
		} catch( XPathExpressionException exception ) {
			// Intentionally ignore exception.
		}
		if( nodes == null ) return null;

		ArrayList<Node> values = new ArrayList<Node>();
		int count = nodes.getLength();
		for( int index = 0; index < count; index++ ) {
			values.add( nodes.item( index ) );
		}

		return values.toArray( new Node[values.size()] );
	}

	public static String getValue( Node node, String path ) {
		if( node == null || isEmpty( path ) ) return null;

		String value = null;
		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			value = (String)xpath.evaluate( path, node, XPathConstants.STRING );
		} catch( XPathExpressionException exception ) {
			// Intentionally ignore exception.
		}

		if( isEmpty( value ) ) return null;

		return value;
	}

	public static String getValue( Node node, String path, String defaultValue ) {
		String value = getValue( node, path );
		if( value == null ) return defaultValue;
		return value;
	}

	/**
	 * Get an array of all the values in the node that have the same path.
	 * 
	 * @param path
	 * @return An array of values with the same path.
	 */
	public static String[] getValues( Node node, String path ) {
		if( node == null || isEmpty( path ) ) return null;

		NodeList nodes = null;
		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			nodes = (NodeList)xpath.evaluate( path, node, XPathConstants.NODESET );
		} catch( XPathExpressionException exception ) {
			// Intentionally ignore exception.
		}
		if( nodes == null ) return null;

		ArrayList<String> values = new ArrayList<String>();
		int count = nodes.getLength();
		for( int index = 0; index < count; index++ ) {
			Node item = nodes.item( index );
			values.add( item.getTextContent() );
		}

		return values.toArray( new String[values.size()] );
	}

	public static String getAttribute( Node node, String name ) {
		Node attribute = node.getAttributes().getNamedItem( name );
		return attribute == null ? null : attribute.getNodeValue();
	}

	private List<String> listPaths( Node parent ) {
		List<String> paths = new ArrayList<String>();
		if( parent == null ) return paths;

		Node node = null;
		NodeList list = parent.getChildNodes();
		int count = list.getLength();
		for( int index = 0; index < count; index++ ) {
			node = list.item( index );
			if( node instanceof Text ) {
				if( isEmpty( node.getTextContent() ) ) continue;
				paths.add( getPath( node ) );
			}
			paths.addAll( listPaths( node ) );
		}

		return paths;
	}

	private String getPath( Node node ) {
		StringBuilder builder = new StringBuilder();
		Node parent = node.getParentNode();
		while( parent != this.node ) {
			builder.insert( 0, parent.getNodeName() );
			builder.insert( 0, "/" );
			parent = parent.getParentNode();
		}
		return builder.toString();
	}

	private Document loadXmlDocument( String uri ) throws SAXException, IOException, ParserConfigurationException {
		if( uri == null ) return null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = factory.newDocumentBuilder().parse( uri );
		document.getDocumentElement().normalize();
		return document;
	}

	private Document loadXmlDocument( Reader reader ) throws SAXException, IOException, ParserConfigurationException {
		if( reader == null ) return null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = factory.newDocumentBuilder().parse( new InputSource( reader ) );
		document.getDocumentElement().normalize();
		return document;
	}

	private static boolean isEmpty( String string ) {
		if( string == null ) return true;
		if( string.trim().length() == 0 ) return true;
		return false;
	}

}
