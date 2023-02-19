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
		
		// debug trace
		Debug.trace("Got number of point on frame label "+index);
		
		// get stack size
		int pointCount = this.pointCounts[index];
		
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
		Debug.trace("Popped point from frame label "+index);
		
		// throw empty stack
		if (this.pointCounts[index] == 0) throw new ArrayIndexOutOfBoundsException("Frame stack "+index+" is empty");
		
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
		Debug.trace("Pushed point ("+x+","+y+") to frame label "+index);
		
		// throw full stack
		if (this.pointCounts[index] == this.capacity) throw new ArrayIndexOutOfBoundsException("Frame stack "+index+" is full");
		
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
		Debug.trace("Deleted point from frame label "+index);
		
		// throw empty stack
		if (this.pointCounts[index] == 0) throw new ArrayIndexOutOfBoundsException("Frame stack "+index+" is empty");
		
		// decrement stack size
		this.pointCounts[index] -= 1;
	}
	
	
	/**
	 * Check if all stack frames are full
	 * @return The result
	 */
	public boolean checkFull() {
		
		// debug trace
		Debug.trace("Checking if the label is completely full");
		
		// check if every stack frame is full
		boolean full = false;
		for (int i=0; i < this.frames; i++) {
			
			// is the stack frame full?
			full = (this.pointCounts[i] == this.capacity);
			
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
		throw new UnsupportedOperationException("Label export is unimplemented");
	}
}
