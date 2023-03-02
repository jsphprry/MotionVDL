package motionvdl.model;

import java.awt.Point;

import motionvdl.Debug;

/**
 * Video frame label implemented as an array of stacks of 
 * 2d integer precision points with one stack per video frame
 * @author Joseph
 */
public class Label extends Encoding {
	
	// metadata
	public final int capacity; // maximum stack capacity
	public final int length;   // the number of stacks
	
	// point buffer
	private Point[][] buffer; // underlying array
	private int[] sizes;      // the sizes of each stack
	
	/**
	 * Constructor for Label instance
	 * @param capacity The individual stack capacity
	 * @param frames The stack count
	 */
	public Label(int capacity, int length) {
		
		// setup metadata
		this.capacity = capacity;
		this.length = length;
		
		// setup point buffer
		buffer = new Point[length][capacity];
		sizes = new int[length]; // default int value is 0 so no need to populate array
	}
	
	
	/**
	 * Return a point from a stack by index
	 * @param stack The stack index
	 * @param index The point index
	 * @return The point at the stack point index
	 */
	public Point getPoint(int stack, int index) {
		
		// throw out of bounds
		if (index > sizes[stack]) throw new ArrayIndexOutOfBoundsException("Index is larger than stack size");
		
		return buffer[stack][index];
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
	
	
	/**
	 * Return the size of a stack
	 * @param stack The stack index
	 * @return The size of the stack
	 */
	public int getSize(int stack) {
		return sizes[stack];
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
	 * Push to a stack
	 * @param stack The stack index
	 * @param x The x axis of the point
	 * @param y The y axis of the point
	 */
	public void push(int stack, int x, int y) {
		
		// debug trace
		Debug.trace(String.format("Label pushed point (%d,%d) to stack %d", x, y, stack));
		
		// throw full stack
		if (sizes[stack] == capacity) throw new ArrayIndexOutOfBoundsException("Error: Label stack "+stack+" is full");
		
		// increment stack size
		int index = sizes[stack];
		sizes[stack] += 1;
		
		// push to stack
		buffer[stack][index] = new Point(x,y);
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
	 * Check if a stack is full
	 * @param stack The stack index
	 * @return The result
	 */
	public boolean stackFull(int stack) {
		
		// determine result
		int size = sizes[stack];
		boolean full = (size == capacity);
		
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
		for (int i=0; i < length; i++) {
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
		int z = length;   // The depth of the label buffer
		int x = capacity; // The width of the label buffer
		
		// throw invalid size
		if (x > 255 || z > 255) throw new ArrayIndexOutOfBoundsException("The label buffer is too large to export");
		
		// encode metadata
		byte[] encoding = new byte[2 + 2*z*x];
		encoding[0] = (byte) z;
		encoding[1] = (byte) x;
		
		// encode each point in 2 bytes
		for (int i=0; i < z; i++) {
			for (int j=0; j < x; j++) {
				encoding[2 + i*x*2 + j*2 + 0] = (byte) buffer[i][j].getX();
				encoding[2 + i*x*2 + j*2 + 1] = (byte) buffer[i][j].getY();
			}
		}
		
		return encoding;
	}
}
