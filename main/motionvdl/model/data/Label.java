package motionvdl.model.data;

import motionvdl.Debug;

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

		// setup metadata
		this.length = length;
		this.capacity = capacity;
		this.sizes = new int[length];

		// setup buffer
		buffer = new Point[length][capacity];

		// debug trace
		Debug.trace(String.format("Created empty (%d,%d) label",length,capacity));
	}

	/**
	 * Construct label from byte sequence
	 * @param encoding Byte sequence
	 * @throws IllegalArgumentException Malformed byte sequence
	 */
	public Label(byte[] encoding) throws IllegalArgumentException {
		try {
			
			// read metadata
			int b0 = encoding[0] & 0xFF;
			int b1 = encoding[1] & 0xFF;
			int b2 = encoding[2] & 0xFF;
			
			// decode metadata
			length   = b0*256+b1;
			capacity = b2;
			sizes    = new int[length];
			for (int i=0; i < length; i++) {
				sizes[i] = encoding[3+i] & 0xFF;
			}
			
			// decode buffer
			buffer = new Point[length][capacity];
			int offset = 3+length;
			for (int i=0; i < length; i++) {
				for (int j=0; j < sizes[i]; j++) {
					double x = (encoding[offset + 0] & 0xFF) / 255.0;
					double y = (encoding[offset + 1] & 0xFF) / 255.0;
					buffer[i][j] = new Point(x,y);
					offset += 2;
				}
			}
			
			// debug trace
			Debug.trace(String.format("Created (%d,%d) label from byte sequence", length, capacity));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Label error: problem decoding bytes. "+e.getMessage());
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
	public byte[] getEncoding() {
		
		// debug trace
		Debug.trace("Label: encoded as byte sequence");
		
		// throw invalid shape
		if (length   > SIXTEEN_BIT_LIMIT || 
			capacity > EIGHT_BIT_LIMIT
		) throw new ArrayIndexOutOfBoundsException("Label error: buffer dimensions exceed value limits");
		
		// determine volume
		int m0size = 3;                  // shape metadata volume
		int m1size = length;             // stack metadata volume
		int bsize = 0;                   // buffer volume
		for (int i=0; i < length; i++) {
			bsize += sizes[i]*2;
		}
		byte[] encoding = new byte[m0size+m1size+bsize]; // total volume
		
		// encode metadata
		encoding[0] = (byte) Math.floor(length / 256);
		encoding[1] = (byte) (length % 256);
		encoding[2] = (byte) capacity;
		for (int i=0; i < length; i++) {
			encoding[m0size+i] = (byte) sizes[i];
		}
		
		// encode buffer
		int offset = m0size+m1size;
		for (int i=0; i < length; i++) {
			for (int j=0; j < sizes[i]; j++) {
				encoding[offset + 0] = (byte) (buffer[i][j].getX() * 255);
				encoding[offset + 1] = (byte) (buffer[i][j].getY() * 255);
				offset += 2;
			}
		}
		
		// return byte encoding
		return encoding;
	}
	
	
	/**
	 * Translate all points in the label by the difference between the given point and the first point on the given stack
	 * @param stack Stack index
	 * @param x Normalised x coordinate of point
	 * @param y Normalised y coordinate of point
	 * @throws IllegalStateException Given stack contains no points or resulting translation puts any point outside bounds
	 */
	public void calibrate(int stack, double x, double y) throws IllegalStateException {
		
		// if the stack has no first point throw an error
		if (sizes[stack] == 0) throw new IllegalStateException("Label error: no points on stack, cannot calibrate");
		
		// calculate the translation vector based on the difference
		// between the provided (x,y) coord and the coord of the first
		// point on the stack. will throw error if the stack has no points
		Point anchor = buffer[stack][0];
		double tx = x - anchor.getX();
		double ty = y - anchor.getY();
		
		// debug trace
		Debug.trace(String.format("Label: calibrating (%.2f,%.2f)",tx,ty));
		
		// translate each point
		Point[][] workBuffer = new Point[length][capacity];
		for (int i=0; i < length; i++) {
			for (int j=0; j < sizes[i]; j++) {
				double px = buffer[i][j].getX() + tx;
				double py = buffer[i][j].getY() + ty;
				workBuffer[i][j] = new Point(px,py);
				
				// if the translated point is out of bounds throw an error
				if (px < 0 || px > 1 || 
					py < 0 || py > 1
				) throw new IllegalStateException("Label error: calibration puts point outside bounds, aborting");
			}
		}
		
		// overwrite buffer
		buffer = workBuffer;
	}
}
