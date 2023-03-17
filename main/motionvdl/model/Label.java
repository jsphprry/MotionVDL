package motionvdl.model;

import motionvdl.Debug;

/**
 * Video frame label implemented as an array of stacks of 
 * normalised double precision 2D points, where 0<x<1 and 0<y<1
 * @author Joseph
 */
public class Label extends Encoding {
	
	// metadata
	public final int size;          // the number of stacks
	public final int stackCapacity; // maximum stack capacity
	
	// point buffer
	private Point[][] buffer; // underlying array
	private int[] sizes;      // the sizes of each stack
	
	/**
	 * Constructor for Label instance
	 * @param size The number of stacks
	 * @param stackCapacity The individual stack capacity
	 */
	public Label(int size, int stackCapacity) {
		
		// setup metadata
		this.size = size;
		this.stackCapacity = stackCapacity;
		
		// setup point buffer
		buffer = new Point[size][stackCapacity];
		sizes = new int[size]; // default int value is 0 so no need to populate array
	}
	
	
	/**
	 * Push to a stack
	 * @param stack The stack index
	 * @param x The normalised x axis of the point
	 * @param y The normalised y axis of the point
	 */
	public void push(int stack, double x, double y) {
		
		// debug trace
		Debug.trace(String.format("Label pushed point (%d,%d) to stack %d", x, y, stack));
		
		// throw invalid point and full stack
		if (x > 1.0 || x < 0.0 || y > 1.0 || y < 0.0) throw new IllegalArgumentException("Error: Invalid point");
		if (sizes[stack] == stackCapacity) throw new ArrayIndexOutOfBoundsException("Error: Stack "+stack+" is full");
		
		// increment stack size
		int index = sizes[stack];
		sizes[stack] += 1;
		
		// push to stack
		buffer[stack][index] = new Point(x,y);
	}
	
	
	/**
	 * Pop from a stack
	 * @param stack The stack index
	 * @return 2d point
	 */
	public Point pop(int stack) {
		
		// debug trace
		Debug.trace("Label popped point from stack "+stack);
		
		// throw empty stack
		if (sizes[stack] == 0) throw new ArrayIndexOutOfBoundsException("Error: Label stack "+stack+" is empty");
		
		// decrement stack size
		int index = sizes[stack] - 1;
		sizes[stack] -= 1;
		
		// return point at index
		return buffer[stack][index];
	}
	
	
	/**
	 * Delete from a stack
	 * @param stack The stack index
	 */
	public void delete(int stack) {
		
		// debug trace
		Debug.trace("Label deleted point from stack "+stack);
		
		// throw empty stack
		if (sizes[stack] == 0) throw new ArrayIndexOutOfBoundsException("Error: Frame stack "+stack+" is empty");
		
		// decrement stack size
		sizes[stack] -= 1;
	}
	
	
	/**
	 * Get an array of the points on a stack
	 * @param stack The stack index
	 * @return Array of points on the frame
	 */
	public Point[] getPoints(int stack) {
		
		// get stack size
		int size = sizes[stack];
		
		// setup array of points
		Point[] framePoints = new Point[size];
		for (int i=0; i < size; i++) {
			framePoints[i] = buffer[stack][i];
		}
		
		return framePoints;
	}
	
	
//	/**
//	 * Return a point from a stack by index
//	 * @param stack The stack index
//	 * @param index The point index
//	 * @return The point at the stack-point index
//	 */
//	public Point getPoint(int stack, int index) {
//		
//		// throw out of bounds
//		if (index > sizes[stack]) throw new ArrayIndexOutOfBoundsException("Index is larger than stack size");
//		
//		return buffer[stack][index];
//	}
//	
//	
//	/**
//	 * Return the size of a stack
//	 * @param stack The stack index
//	 * @return The size of the stack
//	 */
//	public int getSize(int stack) {
//		return sizes[stack];
//	}
	
	
	/**
	 * Check if a stack is full
	 * @param stack The stack index
	 * @return The result
	 */
	public boolean stackFull(int stack) {
		
		// determine result
		int size = sizes[stack];
		boolean full = (size == stackCapacity);
		
		// debug trace
		Debug.trace(String.format("Label stack "+stack+" is %sfull (%d points)", ((full) ? "" : "not "), size));
		
		return full;
	}
	
	
	/**
	 * Check if a stack is empty
	 * @param stack The stack index
	 * @return The result
	 */
	public boolean stackEmpty(int stack) {

		// determine result
		int size = sizes[stack];
		boolean empty = (size == 0);
		
		// debug trace
		Debug.trace(String.format("Label stack "+stack+" is %sempty (%d points)", ((empty) ? "" : "not "), size));
		
		return empty;
	}
	
	
	/**
	 * Check if all stacks are full
	 * @return The result
	 */
	public boolean checkComplete() {
		
		// determine if every stack is full
		for (int i=0; i < size; i++) {
			if (!stackFull(i)) {
				Debug.trace("Label is incomplete");
				return false;
			}
		}

		Debug.trace("Label is complete");
		return true;
	}
	
	
	/**
	 * Encode the label as a byte array
	 * @return The encoded label
	 */
	public byte[] encode() {
		
		// debug trace
		Debug.trace("Label encoded as byte sequence");
		
		// setup metadata
		int h = size;          // The height of the label buffer
		int w = stackCapacity; // The width of the label buffer
		
		// throw invalid size
		if (w > 255 || h > 255) throw new ArrayIndexOutOfBoundsException("The label buffer is too large to export");
		
		// encode metadata
		byte[] encoding = new byte[2 + 2*h*w];
		encoding[0] = (byte) h;
		encoding[1] = (byte) w;
		
		// encode each point scaled to 0-255 in 2 bytes
		for (int i=0; i < h; i++) {
			for (int j=0; j < w; j++) {
				encoding[2 + i*w*2 + j*2 + 0] = (byte) (buffer[i][j].getX() * 255);
				encoding[2 + i*w*2 + j*2 + 1] = (byte) (buffer[i][j].getY() * 255);
			}
		}
		
		return encoding;
	}
}
