package com.avereon.www.images;

import com.avereon.rossa.icon.WingDiscLargeIcon;
import com.avereon.venza.image.Proof;
import javafx.scene.paint.Color;
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
		render( new WingDiscLargeIcon() );
		reset();

		setFont( FONT );
		setFillPaint( Color.BLACK );
		setTextAlign( TextAlignment.CENTER );
		fillText( "Avereon", 0.5 * scale + offset, 0.85, 0.15 );

		double fontHeight = 0.10;
		double vAnchor = 1.0 + 0.5 * offset;
		setFont( DEFAULT_FONT );
		setTextAlign( TextAlignment.CENTER );
		fillText( "Mark Soderquist", 0.5, vAnchor, fontHeight, 1 - 0.5*offset );
		fillText( "mark@avereon.com", 0.5, vAnchor + 0.1, 0.08, 1 - offset );
	}

	public static void main( String[] commands ) {
		Proof.proof( new BusinessCardV() );
	}

}
