package motionvdl.model;

import java.awt.Point;

/**
 * Video label consisting of height x width 2D integer-precision points
 * @author Joseph
 */
public class Label {
	
	// label metadata
	private int nodes;
	private int frames;
	
	// nodes buffer of 2D points
	private Point[][] nodeBuffer;
	
	/**
	 * Constructor for Label instance
	 * @param width	 The number of nodes per frame
	 * @param height The number of frames
	 */
	public Label(int nodes, int frames) {
		
		// setup metadata
		this.nodes = nodes;
		this.frames = frames;
		
		// setup node buffer
		this.nodeBuffer = new Point[this.nodes][this.frames];
	}
	
	
	/**
	 * Get label width
	 * @return The label's width
	 */
	public int getNodeCount() {
		return this.nodes;
	}
	
	
	/**
	 * Get label height
	 * @return The label's height
	 */
	public int getFrameCount() {
		return this.frames;
	}
	
	
	/**
	 * Write a 2D Point to the label buffer at a specific index
	 * @param nodeIndex The index of the node
	 * @param frameIndex The index of the frame
	 * @param x The x axis of the point
	 * @param y The y axis of the point
	 */
	public void write(int nodeIndex, int frameIndex, int x, int y) {
		this.nodeBuffer[frameIndex][nodeIndex] = new Point(x,y);
	}
	
	
	/**
	 * Read a 2D Point from the label buffer
	 * @param w The index of the node
	 * @param h The index of the frame
	 * @return The point at the provided index
	 */
	public Point read(int nodeIndex, int frameIndex) {
		
		// throw empty record case
		if (this.nodeBuffer[frameIndex][nodeIndex] == null) throw new ArrayIndexOutOfBoundsException("No entry at index ["+frameIndex+"]["+nodeIndex+"]");
		
		// return point at index
		return this.nodeBuffer[frameIndex][nodeIndex];
	}
	
	
	/**
	 * Delete an entry from the label buffer
	 * @param w The index of the node
	 * @param h The index of the frame
	 */
	public void delete(int nodeIndex, int frameIndex) {
		this.nodeBuffer[frameIndex][nodeIndex] = null;
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
