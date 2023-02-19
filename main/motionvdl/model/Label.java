package motionvdl.model;

import java.awt.Point;

import motionvdl.Debug;

/**
 * Video label implemented as multistack of 2d integer precision points
 * @author Joseph
 */
public class Label {
	
	// metadata
	private final int capacity;
	private final int frames;
	private int[] pointCounts;
	
	// point buffer
	private Point[][] buffer;
	
	/**
	 * Constructor for Label instance
	 * @param capacity	The individual stack capacity
	 * @param frames The stack count
	 */
	public Label(int capacity, int frames) {
		
		// setup metadata
		this.capacity = capacity;
		this.frames = frames;
		this.pointCounts = new int[this.frames]; // default int value is 0 so no need to populate array
		
		// setup buffer
		this.buffer = new Point[this.frames][this.capacity];
	}
	
	
	/**
	 * Get individual stack capacity
	 * @return The individual stack capacity
	 */
	public int getCapacity() {
		return this.capacity;
	}
	
	
	/**
	 * Get stack count
	 * @return The label frames
	 */
	public int getFrames() {
		return this.frames;
	}
	
	
	/**
	 * Get an array of the points on a stack
	 * @param index The stack index
	 * @return Array of points on the frame
	 */
	public Point[] getPoints(int index) {
		
		// get stack size
		int pointCount = this.pointCounts[index];
		
		// debug trace
		Debug.trace("Found "+pointCount+" points on label stack "+index);
		
		// setup array
		Point[] framePoints = new Point[pointCount];
		
		// populate array
		for (int i=0; i < pointCount; i++) {
			framePoints[i] = this.buffer[index][i];
		}
		
		return framePoints;
	}
	
	
	/**
	 * Pop from a stack
	 * @param index The stack index
	 * @return 2d point
	 */
	public Point pop(int index) {
		
		// debug trace
		Debug.trace("Popped point from label stack "+index);
		
		// throw empty stack
		if (this.pointCounts[index] == 0) throw new ArrayIndexOutOfBoundsException("Error: Frame stack "+index+" is empty");
		
		// get current index from stack size
		int currentIndex = this.pointCounts[index] - 1;
		
		// decrement frame point count
		this.pointCounts[index] -= 1;
		
		// return point at index
		return this.buffer[index][currentIndex];
	}
	
	/**
	 * Push to a stack
	 * @param index The stack index
	 * @param x The x axis of the point
	 * @param y The y axis of the point
	 */
	public void push(int index, int x, int y) {
		
		// debug trace
		Debug.trace("Pushed point ("+x+","+y+") to label stack "+index);
		
		// throw full stack
		if (this.pointCounts[index] == this.capacity) throw new ArrayIndexOutOfBoundsException("Error: Frame stack "+index+" is full");
		
		// get next index from stack size
		int nextIndex = this.pointCounts[index];
		
		// increment stack size
		this.pointCounts[index] += 1;
		
		// push to stack
		this.buffer[index][nextIndex] = new Point(x,y);
	}
	
	
	/**
	 * Delete from a stack
	 * @param index The stack index
	 */
	public void delete(int index) {
		
		// debug trace
		Debug.trace("Deleted point from label stack "+index);
		
		// throw empty stack
		if (this.pointCounts[index] == 0) throw new ArrayIndexOutOfBoundsException("Error: Frame stack "+index+" is empty");
		
		// decrement stack size
		this.pointCounts[index] -= 1;
	}
	
	
	/**
	 * Check if a stack is full
	 * @param index The stack index
	 * @return The result
	 */
	public boolean frameFull(int index) {
		
		boolean full = (this.pointCounts[index] == this.capacity);
		
		// debug trace
		Debug.trace("Checking if label stack "+index+" is full ("+full+")");
		
		return full;
	}
	
	
	/**
	 * Check if a stack is empty
	 * @param index The stack index
	 * @return The result
	 */
	public boolean frameEmpty(int index) {
		
		boolean empty = (this.pointCounts[index] == 0);
		
		// debug trace
		Debug.trace("Checking if label stack "+index+" is empty ("+empty+")");
		
		return empty;
	}
	
	/**
	 * Check if all stacks are full
	 * @return The result
	 */
	public boolean full() {
		
		// debug trace
		Debug.trace("Checking if all label stacks are full");
		
		// check if every stack frame is full
		boolean full = false;
		for (int i=0; i < this.frames; i++) {
			
			// is the current stack full?
			full = frameFull(i);
			
			// break if not
			if (!full) break;
		}
		
		return full;
	}
	
	
	/**
	 * Export the label
	 * @return The exported label
	 */
	public boolean[] export() {
		
		// debug trace
		Debug.trace("Exported label");
		
		// TODO implement label export
		throw new UnsupportedOperationException("Error: Label export is unimplemented");
	}
}
