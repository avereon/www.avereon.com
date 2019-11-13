package com.avereon.www.images;

import com.avereon.xenon.ProgramIcon;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

/**
 * Use <a href="http://www.pic2icon.com/">Pic2Icon</a> to convert to Windows icon.
 */
public class WingDiscIcon extends ProgramIcon {

	double POINT_RADIUS;

	double DISC_RADIUS;

	double zx;

	double zy;

	double yx;

	double yy;

	double xx;

	double xy;

	double wx;

	double wy;

	double vx;

	double vy;

	private double bx;

	private double by;

	private double dx;

	private double dy;

	private double fx;

	private double fy;

	private double frontStartAngleDeg;

	private double frontSpanAngleDeg;

	private double leftArcStartAngleDeg;

	private double leftArcSpanAngleDeg;

	private double backArcStartAngleDeg;

	private double backArcSpanAngleDeg;

	private double rightArcStartAngleDeg;

	private double rightArcSpanAngleDeg;

	// Gray 400
	private Color wingColor = Color.web( "#BDBDBD" );

	// Gray 300
	private Color wingHighlight = Color.web( "#E0E0E0" );

	// Teal 300
	private Color discColor = Color.web( "#4DB6AC" );

	// Teal 50
	private Color discHighlight = Color.web( "#E0F2F1" );

	public WingDiscIcon() {
		POINT_RADIUS = g( 2 );
		DISC_RADIUS = g( 5 );
		zx = g( 16 );
		zy = g( 7 );
		yx = g( 7 );
		yy = g( 25 );
		xx = g( 16 );
		xy = g( 22 );
		wx = g( 25 );
		wy = g( 25 );
		vx = xx;
		vy = xy;
	}

	@Override
	protected void render() {
		calculateNumbers();

		// Use these numbers to control the gradient across the wing
		double goB = g( 3 );

		double leftLeNormal = (zx - yx) / (yy - zy);
		double leftLeX = 0.5 * (yx + zx);
		double leftLeY = 0.5 * (yy + zy);
		double leftGbX = leftLeX - POINT_RADIUS;
		double leftGbY = leftLeY - POINT_RADIUS * leftLeNormal;
		double leftGeX = leftLeX + goB;
		double leftGeY = leftLeY + goB * leftLeNormal;

		double rightLeNormal = (wx - zx) / (wy - zy);
		double rightLeX = 0.5 * (wx + zx);
		double rightLeY = 0.5 * (wy + zy);
		double rightGbX = rightLeX + POINT_RADIUS;
		double rightGbY = rightLeY - POINT_RADIUS * rightLeNormal;
		double rightGeX = rightLeX - goB;
		double rightGeY = rightLeY + goB * rightLeNormal;

		setFillPaint( radialPaint( vx, vy - DISC_RADIUS, 2 * DISC_RADIUS, new Stop( 0.5, discHighlight ), new Stop( 1, discColor ) ) );
		fillCenteredOval( vx, vy, DISC_RADIUS, DISC_RADIUS );
		drawCenteredOval( vx, vy, DISC_RADIUS, DISC_RADIUS );

		Stop[] stops = new Stop[]{ new Stop( 0, wingColor ), new Stop( 0.6, wingColor ), new Stop( 1, wingHighlight ) };

		// Left wing
		getGraphicsContext2D().save();
		clip( 0, 0, 0.5, 1 );
		arrow();
		//setFillPaint( linearPaint( leftGbX, leftGbY, leftGeX, leftGeY, stops ) );
		setFillPaint( linearPaint( 0, 0, 0.5,0, stops));
		fill();
		getGraphicsContext2D().restore();

		// Right wing
		getGraphicsContext2D().save();
		clip( 0.5, 0, 1, 1 );
		arrow();
		//setFillPaint( linearPaint( rightGbX, rightGbY, rightGeX, rightGeY, stops ) );
		setFillPaint( linearPaint( 1, 0, 0.5,0, stops));
		fill();
		getGraphicsContext2D().restore();

		// Outline
		arrow();
		draw();
	}

	private void arrow() {
		startPath();
		addArc( zx, zy, POINT_RADIUS, POINT_RADIUS, frontStartAngleDeg, frontSpanAngleDeg );
		lineTo( bx, by );
		addArc( yx, yy, POINT_RADIUS, POINT_RADIUS, leftArcStartAngleDeg, leftArcSpanAngleDeg );
		lineTo( dx, dy );
		addArc( xx, xy, POINT_RADIUS, POINT_RADIUS, backArcStartAngleDeg, backArcSpanAngleDeg );
		lineTo( fx, fy );
		addArc( wx, wy, POINT_RADIUS, POINT_RADIUS, rightArcStartAngleDeg, rightArcSpanAngleDeg );
		closePath();
	}

	private void clip( double x, double y, double w, double h ) {
		startPath();
		addRect( x, y, w, h );
		closePath();
		clip();
	}

	private void calculateNumbers() {
		double frontTangent = (yy - zy) / (zx - yx);
		double frontNormal = 1 / frontTangent;
		double frontNormalAngle = Math.atan( frontNormal );
		frontStartAngleDeg = frontNormalAngle * (DEGREES_PER_RADIAN);
		frontSpanAngleDeg = 180 - (2 * frontStartAngleDeg);

		double hyp = distance( xx, xy, yx, yy );
		double opp = 2 * POINT_RADIUS;
		double adj = Math.sqrt( Math.pow( hyp, 2 ) - Math.pow( opp, 2 ) );

		double alpha = Math.atan2( xy - yy, xx - yx );
		double beta = Math.atan2( adj, opp );

		double backTangentAngle = alpha + beta;
		double backNormalAngle = Math.PI / 2 - backTangentAngle;

		leftArcStartAngleDeg = 180 - frontStartAngleDeg;
		leftArcSpanAngleDeg = 90 + frontStartAngleDeg + backNormalAngle * DEGREES_PER_RADIAN;

		backArcStartAngleDeg = (Math.PI - backTangentAngle) * DEGREES_PER_RADIAN;
		backArcSpanAngleDeg = -backNormalAngle * 2 * DEGREES_PER_RADIAN;

		rightArcStartAngleDeg = 180 + backTangentAngle * DEGREES_PER_RADIAN;
		rightArcSpanAngleDeg = 90 + frontStartAngleDeg + backNormalAngle * DEGREES_PER_RADIAN;

		bx = yx - Math.cos( frontNormalAngle ) * POINT_RADIUS;
		by = yy - Math.sin( frontNormalAngle ) * POINT_RADIUS;

		dx = xx - Math.cos( backTangentAngle ) * POINT_RADIUS;
		dy = xy - Math.sin( backTangentAngle ) * POINT_RADIUS;

		fx = wx - Math.cos( backTangentAngle ) * POINT_RADIUS;
		fy = wy + Math.sin( backTangentAngle ) * POINT_RADIUS;
	}

	public static void main( String[] commands ) {
		proof( new WingDiscIcon() );
	}

}
