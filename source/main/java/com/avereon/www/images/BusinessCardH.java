package com.avereon.www.images;

import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class BusinessCardH extends BusinessCard {

	public BusinessCardH() {
		setWidth( LONG );
		setHeight( SHORT );
	}

	@Override
	protected void render() {
		double scale = 0.6;
		double offset = scale * 0.1;
		move( offset, 1.5 * offset );
		zoom( scale, scale );
		draw( new WingDiscLargeIcon() );
		reset();

		setFont( FONT );
		setFillPaint( getIconDrawColor() );
		setTextAlign( TextAlignment.CENTER );
		fillText( "Avereon", 0.5 * scale + offset, 0.85, 0.15 );

		setFont( deriveFont( FONT, "Sans Serif" ) );
		setTextAlign( TextAlignment.LEFT );
		double vAnchor = 0.45;
		fillText( "Mark Soderquist", 0.7, vAnchor, 0.12 );
		fillText( "mark@avereon.com", 0.7, vAnchor + 0.1, 0.08 );
	}

	public static void main( String[] commands ) {
		proof( new BusinessCardH(), Color.web( "#e0e0e0" ) );
	}

}
