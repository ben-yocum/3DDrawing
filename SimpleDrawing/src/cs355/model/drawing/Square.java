package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Add your square code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Square extends Shape {

	// The size of this Square.
	private double size;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param size the size of the new shape.
	 */
	public Square(Color color, Point2D.Double center, double size) {

		// Initialize the superclass.
		super(color, center);

		// Set the field.
		this.size = size;
	}

	/**
	 * Getter for this Square's size.
	 * @return the size as a double.
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Setter for this Square's size.
	 * @param size the new size.
	 */
	public void setSize(double size) {
		this.size = size;
	}

    public Point2D.Double getUpperLeft(){
        return new Point2D.Double(getCenter().getX() - size/2, getCenter().getY() - size/2);
    }

	/**
	 * Add your code to do an intersection test
	 * here. You shouldn't need the tolerance.
	 * @param pt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance) {
		AffineTransform w2o = new AffineTransform();
		w2o.rotate(-getRotation());
		w2o.translate(-center.getX(), -center.getY());
		Point2D.Double transPt = new Point2D.Double(pt.getX(), pt.getY());
		w2o.transform(pt, transPt);

		return transPt.getX() <= getSize() / 2 &&
				transPt.getX() >= -getSize() / 2 &&
				transPt.getY() <= getSize() / 2 &&
				transPt.getY() >=  -getSize() / 2;
	}

    @Override
    public Point2D.Double getPointAbove(double dist) {
        return new Point2D.Double(getCenter().getX(), getCenter().getY() - getSize() / 2 - dist);
    }

}
