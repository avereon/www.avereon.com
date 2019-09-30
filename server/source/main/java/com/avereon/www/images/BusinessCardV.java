package com.avereon.www.images;

public class BusinessCardV extends BusinessCard {

	public BusinessCardV() {
		setWidth( SHORT );
		setHeight( LONG );
	}

	@Override
	protected void render() {
		double scale = 0.8;
		double offset = 0.5 - (scale * 0.5);
		move( offset, 0.5*offset );
		zoom( scale, scale );
		draw( new WingDiscLargeIcon() );
		reset();
	}

	public static void main( String[] commands ) {
		proof( new BusinessCardV() );
	}

}
