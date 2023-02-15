package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * Abstract superclass defining default behaviour for subcontrollers
 * @author Joseph
 */
public abstract class Subcontroller {
	
	// components
	protected MainController mainController;
	protected Display mainDisplay;
	protected Video localVideo;
	
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
		
		// Pass the video back to the main controller
		this.mainController.nextController(this.localVideo);
	}
	
	
	/**
	 * Display next frame up from current frame
	 */
	public void frameUp() {
		
		// increment frameIndex
		this.frameIndex = Math.max(0,Math.min(this.localVideo.getDepth()-1, frameIndex + 1));
		
		// update display
		this.mainDisplay.setFrame(this.localVideo.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Display next frame down from current frame
	 */
	public void frameDown() {
		
		// decrement frameIndex
		this.frameIndex = Math.max(0,Math.min(this.localVideo.getDepth()-1, frameIndex - 1));
		
		// update display
		this.mainDisplay.setFrame(this.localVideo.getFrame(this.frameIndex));
	}
}
