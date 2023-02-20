package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * Superclass defining default controller behaviour
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
		
		// debug trace
		Debug.trace("Called Controller point instruction");
		
		// by default do nothing with the frame click action
	}
	
	
	/**
	 * Process button action
	 */
	public void process() {
		
		// debug trace
		Debug.trace("Called Controller process instruction");
		
		// by default do nothing with the process button action
	}
	
	
	/**
	 * Complete button action
	 */
	public void complete() {
		
		// debug trace
		Debug.trace("Called Controller complete instruction");
		
		// move video to temporary variable and unset video
		Video tempVideo = this.video;
		this.video = null;
		
		// pass the video back to the linked controller
		this.linkedController.pass(tempVideo);
	}
	
	
	/**
	 * Display next frame up from current frame
	 */
	public void frameUp() {
		
		// debug trace
		Debug.trace("Called Controller frameUp instruction");
		
		// increment frameIndex
		this.frameIndex = Math.min(this.video.getFrames() - 1, frameIndex + 1);
		
		// update display
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Display next frame down from current frame
	 */
	public void frameDown() {
		
		// debug trace
		Debug.trace("Called Controller frameDown instruction");
		
		// decrement frameIndex
		this.frameIndex = Math.max(0, frameIndex - 1);
		
		// update display
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Pass control to this controller
	 */
	protected void pass(Video video) {
		
		// debug trace
		Debug.trace("Called Controller pass instruction");
		
		// by default just set video
		this.video = video;
	}
}
