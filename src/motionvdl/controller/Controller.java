package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * Abstract superclass defining default controller behaviour
 * @author Joseph
 */
public abstract class Controller {
	
	// components
	protected Controller linkedController;
	protected Display display;
	protected Video video;
	
	// variables
	protected int frameIndex;
	
	
	/**
	 * Frame click action
	 * @param x The x axis of the click
	 * @param y The y axis of the click
	 */
	public void point(int x, int y) {
		// by default do nothing with the frame click action
	}
	
	
	/**
	 * Process button action
	 */
	public void process() {
		// by default do nothing with the process button action
	}
	
	
	/**
	 * Complete button action
	 */
	public void complete() {
		
		// Pass the video back to the linked controller
		this.linkedController.pass(this.video);
	}
	
	
	/**
	 * Pass control to this controller
	 */
	abstract public void pass(Video video);
	
	
	/**
	 * Display next frame up from current frame
	 */
	public void frameUp() {
		
		// increment frameIndex
		this.frameIndex = Math.min(this.video.getDepth() - 1, frameIndex + 1);
		
		// update display
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Display next frame down from current frame
	 */
	public void frameDown() {
		
		// decrement frameIndex
		this.frameIndex = Math.max(0, frameIndex - 1);
		
		// update display
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
