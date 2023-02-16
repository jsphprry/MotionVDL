package motionvdl.model;

import java.awt.Point;

/**
 * Video label consisting of height x width 2D integer-precision points
 * @author Joseph
 */
public class Label {
	
	// label metadata
	private final int width;
	private final int depth;
	
	// buffer of 2D points
	private Point[][] pointBuffer;
	
	/**
	 * Constructor for Label instance
	 * @param width	 The number of points per frame
	 * @param height The number of frames
	 */
	public Label(int numPoints, int numFrames) {
		
		// setup metadata
		this.width = numPoints;
		this.depth = numFrames;
		
		// setup point buffer
		this.pointBuffer = new Point[this.depth][this.width];
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
	 * Get a specific row of points
	 * @param index Index of the relevant frame
	 * @return The associated row
	 */
	public Point[] getRow(int index) {
		return this.pointBuffer[index];
	}
	
	/**
	 * Write a 2D Point to the label buffer at a specific index
	 * @param pointIndex The index of the point
	 * @param frameIndex The index of the frame
	 * @param x The x axis of the point
	 * @param y The y axis of the point
	 */
	public void write(int pointIndex, int frameIndex, int x, int y) {
		this.pointBuffer[frameIndex][pointIndex] = new Point(x,y);
	}
	
	
	/**
	 * Read a 2D Point from the label buffer
	 * @param w The index of the point
	 * @param h The index of the frame
	 * @return The point at the provided index
	 */
	public Point read(int pointIndex, int frameIndex) {
		
		// throw empty record case
		if (this.pointBuffer[frameIndex][pointIndex] == null) throw new ArrayIndexOutOfBoundsException("No entry at index ["+frameIndex+"]["+pointIndex+"]");
		
		// return point at index
		return this.pointBuffer[frameIndex][pointIndex];
	}
	
	
	/**
	 * Delete an entry from the label buffer
	 * @param w The index of the point
	 * @param h The index of the frame
	 */
	public void delete(int pointIndex, int frameIndex) {
		this.pointBuffer[frameIndex][pointIndex] = null;
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
