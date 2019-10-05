package com.avereon.www.images;

import javafx.scene.text.TextAlignment;

public class BusinessCardV extends BusinessCard {

	public BusinessCardV() {
		setWidth( SHORT );
		setHeight( LONG );
	}

	@Override
	protected void render() {
		double scale = 0.6;
		double offset = 0.5 - (scale * 0.5);

		move( offset, 0.5 * offset );
		zoom( scale, scale );
		draw( new WingDiscLargeIcon() );
		reset();

		setFont( FONT );
		setFillPaint( getIconDrawColor() );
		setTextAlign( TextAlignment.CENTER );
		fillText( "Avereon", 0.5 * scale + offset, 0.85, 0.15 );

		double fontHeight = 0.10;
		double vAnchor = 1.0 + 0.5 * offset;
		setFont( deriveFont( FONT, "Sans Serif" ) );
		setTextAlign( TextAlignment.CENTER );
		fillText( "Mark Soderquist", 0.5, vAnchor, fontHeight, 1 - 0.5*offset );
		fillText( "mark@avereon.com", 0.5, vAnchor + 0.1, 0.08, 1 - offset );
	}

	public static void main( String[] commands ) {
		proof( new BusinessCardV() );
	}

}
