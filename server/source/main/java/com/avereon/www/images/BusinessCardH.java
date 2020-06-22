package com.avereon.www.images;

import com.avereon.rossa.icon.WingDiscLargeIcon;
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
		double aspectRatio = (double)LONG / (double)SHORT;
		move( offset, 1.5 * offset );
		zoom( scale, scale );
		render( new WingDiscLargeIcon() );
		reset();

		setFont( FONT );
		setFillPaint( Color.BLACK );
		setTextAlign( TextAlignment.CENTER );
		fillText( "Avereon", 0.5 * scale + offset, 0.85, 0.15 );

		double fontHeight = 0.12;
		double vAnchor = 2 * offset + fontHeight;
		setFont( DEFAULT_FONT );
		setTextAlign( TextAlignment.LEFT );
		fillText( "Mark Soderquist", 0.7, vAnchor, fontHeight, (aspectRatio - 0.7 - offset) );
		fillText( "mark@avereon.com", 0.7, vAnchor + 0.1, 0.08 );
	}

	public static void main( String[] commands ) {
		proof( new BusinessCardH(), Color.web( "#e0e0e0" ) );
	}

}
