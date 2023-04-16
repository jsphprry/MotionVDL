package motionvdl.model.util;

/**
 * Immutable n-dimensional point with double precision
 * @author Joseph
 */
public class Point {

	// coordinate array
	private final double[] point;
	public final int shape;

	// instance constructor
	public Point(double ...coords) {
		this.point = coords;
		this.shape = coords.length;
	}


	/**
	 * Get x axis value
	 * @return The x axis value
	 */
	public double getX() {

		// no exception because constructor can never allow 0 args

		// return first element
		return this.point[0];
	}


	/**
	 * Get y axis value
	 * @return The y axis value
	 */
	public double getY() {

		// throw no-such-axis
		if (this.point.length < 2) throw new ArrayIndexOutOfBoundsException(shape+"d Point has no y axis");

		// return second element
		return this.point[1];
	}


	/**
	 * Get z axis value
	 * @return The z axis value
	 */
	public double getZ() {

		// throw no-such-axis
		if (this.point.length < 3) throw new ArrayIndexOutOfBoundsException(shape+"d Point has no z axis");

		// return third element
		return this.point[2];
	}


	/**
	 * Get arbitrary axis
	 * @param index The index of the axis returned
	 * @return The axis value
	 */
	public double getAxis(int index) {

		// throw no-such-axis
		if (index >= this.point.length) throw new ArrayIndexOutOfBoundsException(shape+"d Point has no axis "+index);

		// return indexed element
		return this.point[index];
	}
}
