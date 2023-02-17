package motionvdl.model;

import java.awt.Point;

/**
 * Video label consisting of height x width 2D integer points 
 * stored in an array of stacks, with one stack per video frame.
 * @author Joseph
 */
public class Label {
	
	// label metadata
	private final int width;
	private final int depth;
	private int[] pointCounts;
	
	// point buffer
	private Point[][] buffer;
	
	/**
	 * Constructor for Label instance
	 * @param numPoints	The number of points per frame
	 * @param numFrames The number of frames
	 */
	public Label(int numPoints, int numFrames) {
		
		// setup metadata
		this.width = numPoints;
		this.depth = numFrames;
		this.pointCounts = new int[this.depth]; // default int value is 0 so no need to populate array
		
		// setup buffer
		this.buffer = new Point[this.depth][this.width];
	}
	
	
	/**
	 * Get label width
	 * @return The label's width
	 */
	public int getWidth() {
		return this.width;
	}
	
	
	/**
	 * Get label height
	 * @return The label's height
	 */
	public int getDepth() {
		return this.depth;
	}
	
	
	/**
	 * Get an array of the points on a frame
	 * @param frameIndex The stack frame index
	 * @return Array of Points on the frame
	 */
	public Point[] getPoints(int frameIndex) {
		
		// get frame point count
		int pointCount = this.pointCounts[frameIndex];
		
		// setup array
		Point[] framePoints = new Point[pointCount];
		
		// populate array
		for (int i=0; i < pointCount; i++) {
			framePoints[i] = this.buffer[frameIndex][i];
		}
		
		return framePoints;
	}
	
	
	/**
	 * Pop point from frame stack
	 * @param frameIndex The stack frame index
	 * @return The most recent point
	 */
	public Point pop(int frameIndex) {
		
		// throw frame stack empty
		if (this.pointCounts[frameIndex] == 0) throw new ArrayIndexOutOfBoundsException("Frame stack "+frameIndex+" is empty");
		
		// get index from frame point count
		int currentIndex = this.pointCounts[frameIndex] - 1;
		
		// decrement frame point count
		this.pointCounts[frameIndex] -= 1;
		
		// return point at index
		return this.buffer[frameIndex][currentIndex];
	}
	
	/**
	 * Put point on frame stack
	 * @param frameIndex The stack frame index
	 * @param x The x axis of the point
	 * @param y The y axis of the point
	 */
	public void push(int frameIndex, int x, int y) {
		
		// throw frame stack full
		if (this.pointCounts[frameIndex] == this.width) throw new ArrayIndexOutOfBoundsException("Frame stack "+frameIndex+" is full");
		
		// get next index from frame point count
		int nextIndex = this.pointCounts[frameIndex];
		
		// increment frame point count
		this.pointCounts[frameIndex] += 1;
		
		// put point on frame stack
		this.buffer[frameIndex][nextIndex] = new Point(x,y);
	}
	
	
	/**
	 * Delete an entry from the label buffer
	 * @param frameIndex The stack frame index
	 */
	public void delete(int frameIndex) {
		
		// throw frame stack empty
		if (this.pointCounts[frameIndex] == 0) throw new ArrayIndexOutOfBoundsException("Frame stack "+frameIndex+" is empty");
		
		// decrement frame point count
		this.pointCounts[frameIndex] -= 1;
	}
	
	
	/**
	 * Check if all stack frames are full
	 * @return The isfull state
	 */
	public boolean checkFull() {
		
		// check if every stack frame is full
		boolean full = false;
		for (int i=0; i < this.depth; i++) {
			
			// is the stack frame full?
			full = (this.pointCounts[i] == this.width);
			
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
		
		// TODO implement label export
		throw new UnsupportedOperationException("Label export is unimplemented");
	}
}
