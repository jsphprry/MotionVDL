package motionvdl.model.util;

import motionvdl.Debug;
import motionvdl.model.Encoding;

/**
 * Array of limited capacity stacks of
 * 2d normalised double precision points
 * @author joseph
 */
public class Label extends Encoding {

	// metadata
	public final int length;
	public final int capacity;
	private int[] sizes;

	// label buffer
	private Point[][] buffer;

	/**
	 * Construct empty label
	 * @param length Label length
	 * @param capacity Stack capacity
	 */
	public Label(int length, int capacity) {

		// debug trace
		Debug.trace("Created empty label");

		// setup metadata
		this.length = length;
		this.capacity = capacity;
		this.sizes = new int[length];

		// setup buffer
		buffer = new Point[length][capacity];
	}

	/**
	 * Construct label from byte sequence
	 * @param encoding Byte sequence
	 * @throws IllegalArgumentException Malformed byte sequence
	 */
	public Label(byte[] encoding) throws IllegalArgumentException {

		// debug trace
		Debug.trace("Created Label from byte sequence");

		try {

			// read metadata
			int l = encoding[0] & 0xFF;
			int c = encoding[1] & 0xFF;
			int[] s = new int[l];
			for (int i=0; i < l; i++) {
				s[i] = encoding[2+i] & 0xFF;
			}
			int msize = 2+l;

			// setup label
			length = l;
			capacity = c;
			sizes = s;
			buffer = new Point[l][c];

			// decode into buffer
			int offset = 0;
			for (int i=0; i < l; i++) {
				for (int j=0; j < s[i]; j++) {
					double x = (encoding[msize + offset + 0] & 0xFF) / 255.0;
					double y = (encoding[msize + offset + 1] & 0xFF) / 255.0;
					offset += 2;
					buffer[i][j] = new Point(x,y);
				}
			}

		} catch (Exception e) {
			throw new IllegalArgumentException("Label error: problem decoding bytes");
		}
	}


	/**
	 * Get the size of a stack
	 * @param stack Stack index
	 */
	public int getSize(int stack) {
		return sizes[stack];
	}


	/**
	 * Get the points on a frame stack as array
	 * @param stack Stack index
	 * @return Array of points on frame label
	 */
	public Point[] getPoints(int stack) {

		// get stack size
		int size = sizes[stack];

		// setup array of points
		Point[] points = new Point[size];
		for (int i=0; i < size; i++) {
			points[i] = buffer[stack][i];
		}

		return points;
	}


	/**
	 * Add point to a stack
	 * @param stack Stack index
	 * @param x Normalised x axis
	 * @param y Normalised y axis
	 */
	public void push(int stack, double x, double y) {

		// throw exceptions
		if (x > 1.0 || x < 0.0 || y > 1.0 || y < 0.0) throw new IllegalArgumentException(String.format("Label error: illegal point '(%.2f,%.2f)'",x,y));
		if (sizes[stack] == capacity) throw new ArrayIndexOutOfBoundsException(String.format("Label error: stack %d is full", stack));

		// increment stack size
		int index = sizes[stack];
		sizes[stack] += 1;

		// push to stack
		buffer[stack][index] = new Point(x,y);

		// debug trace
		Debug.trace(String.format("Label: pushed point (%.2f, %.2f) to stack %d",x,y,stack));
	}


	/**
	 * Pop point from a stack
	 * @param stack Stack index
	 * @return Normalised 2d Point
	 */
	public Point pop(int stack) {

		// throw empty stack
		if (sizes[stack] == 0) throw new ArrayIndexOutOfBoundsException(String.format("Label error: stack %d is empty", stack));

		// decrement stack size
		int index = sizes[stack] - 1;
		sizes[stack] -= 1;

		// debug trace
		Debug.trace(String.format("Label: popped point (%.2f, %.2f) from stack %d",
				buffer[stack][sizes[stack]].getX(),
				buffer[stack][sizes[stack]].getY(),
				stack));

		// return point at index
		return buffer[stack][index];
	}


	/**
	 * Check if all stacks are full
	 * @return Result
	 */
	public boolean checkFull() {

		// determine if every stack is full
		for (int i=0; i < length; i++) {
			if (!checkFull(i)) {
				Debug.trace("Label: label is incomplete");
				return false;
			}
		}

		// return result
		Debug.trace("Label: label is complete");
		return true;
	}


	/**
	 * Check if a stack is full
	 * @param stack Stack index
	 * @return Result
	 */
	public boolean checkFull(int stack) {

		// determine result
		int size = sizes[stack];
		boolean result = (size == capacity);

		// return result
		Debug.trace(String.format("Label: stack %d is %sfull %d/%d", stack, (result ? "" : "not "), size, capacity));
		return result;
	}


	/**
	 * Check if a stack is empty
	 * @param stack Stack index
	 * @return Result
	 */
	public boolean checkEmpty(int stack) {

		// determine result
		int size = sizes[stack];
		boolean result = (size == 0);

		// return result
		Debug.trace(String.format("Label: frame label %d is %sempty %d/%d", stack, (result ? "" : "not "), size, capacity));
		return result;
	}


	/**
	 * Encode as byte sequence
	 * @return Byte sequence
	 */
	@Override
	public byte[] encode() {

		// debug trace
		Debug.trace("Label: encoded as byte sequence");

		// setup metadata
		int l = length;
		int c = capacity;
		int msize = 2+l; // metadata volume
		int lsize = 0;   // label volume
		for (int i=0; i < l; i++) {lsize += sizes[i]*2;}

		// throw invalid shape
		if (l > MAX_DIMENSION || c > MAX_DIMENSION) throw new ArrayIndexOutOfBoundsException("Label error: buffer dimensions are too large to export");

		// encode metadata
		byte[] encoding = new byte[msize+lsize];
		encoding[0] = (byte) l;
		encoding[1] = (byte) c;
		for (int i=0; i < l; i++) {
			encoding[2+i] = (byte) sizes[i];
		}

		// encode label buffer
		int offset = 0;
		for (int i=0; i < l; i++) {
			for (int j=0; j < sizes[i]; j++) {
				encoding[msize + offset + 0] = (byte) (buffer[i][j].getX() * 255);
				encoding[msize + offset + 1] = (byte) (buffer[i][j].getY() * 255);
				offset += 2;
			}
		}

		// return byte encoding
		return encoding;
	}
}
