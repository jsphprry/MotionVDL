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
	private final int capacity; // maximum stack capacity
	private final int length;   // the number of stacks
	
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
	 * Get an array of the points on a stack
	 * @param index The stack index
	 * @return Array of points on the frame
	 */
	public Point[] getPoints(int index) {
		
		// get stack size
		int pointCount = sizes[index];
		
		// debug trace
		Debug.trace("Found "+pointCount+" points on label stack "+index);
		
		// setup array of points
		Point[] framePoints = new Point[pointCount];
		for (int i=0; i < pointCount; i++) {
			framePoints[i] = buffer[index][i];
		}
		
		return framePoints;
	}
	
	
	/**
	 * Pop from a stack
	 * @param stack The stack index
	 * @return 2d point
	 */
	public Point pop(int stack) {
		
		// debug trace
		Debug.trace("Popped point from label stack "+stack);
		
		// throw empty stack
		if (sizes[stack] == 0) throw new ArrayIndexOutOfBoundsException("Error: Frame stack "+stack+" is empty");
		
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
		Debug.trace("Pushed point ("+x+","+y+") to label stack "+stack);
		
		// throw full stack
		if (sizes[stack] == capacity) throw new ArrayIndexOutOfBoundsException("Error: Frame stack "+stack+" is full");
		
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
		Debug.trace("Deleted point from label stack "+stack);
		
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
		boolean full = (sizes[stack] == capacity);
		
		// debug trace
		Debug.trace("Checking if label stack "+stack+" is full ("+full+")");
		
		return full;
	}
	
	
	/**
	 * Check if a stack is empty
	 * @param index The stack index
	 * @return The result
	 */
	public boolean stackEmpty(int index) {

		// determine result
		boolean empty = (sizes[index] == 0);
		
		// debug trace
		Debug.trace("Checking if label stack "+index+" is empty ("+empty+")");
		
		return empty;
	}
	
	
	/**
	 * Check if all stacks are full
	 * @return The result
	 */
	public boolean checkComplete() {
		
		// debug trace
		Debug.trace("Checking if the label is complete");
		
		// determine if every frame stack is full
		for (int i=0; i < length; i++) {
			if (stackEmpty(i)) return false;
		}
		
		return true;
	}
	
	
	/**
	 * Encode the label as a byte array
	 * @return The encoded label
	 */
	public byte[] encode() {
		
		// debug trace
		Debug.trace("Encoded label as byte sequence");
		
		// setup metadata
		int z = length;   // The depth of the label buffer
		int x = capacity; // The width of the label buffer
		
		// throw invalid size
		if (x > 255 || z > 255) throw new ArrayIndexOutOfBoundsException("The video buffer is too large to export");
		
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
