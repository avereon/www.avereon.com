package com.avereon.www.images;

import com.avereon.venza.image.RenderedImage;
import javafx.scene.text.Font;

public abstract class BusinessCard extends RenderedImage {

	static final int LONG = 1050;

	static final int SHORT = 600;

	static final Font DEFAULT_FONT = Font.getDefault();

	static final Font FONT = Font.loadFont( BusinessCard.class.getResourceAsStream( "/font/sansation/Sansation-Regular.ttf" ), 12 );

}
