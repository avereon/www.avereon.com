package com.avereon.www.images;

import com.avereon.xenon.ProgramImage;
import javafx.scene.text.Font;

public abstract class BusinessCard extends ProgramImage {

	static final int LONG = 1050;

	static final int SHORT = 600;

	static final Font FONT = Font.loadFont( BusinessCard.class.getResourceAsStream( "/font/Sansation-Regular.ttf" ), 12 );

}
