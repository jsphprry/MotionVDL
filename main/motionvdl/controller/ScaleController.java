package motionvdl.controller;

import java.awt.Point;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL scaling subcontroller
 * @author Joseph
 */
public class ScaleController extends Controller {
	
	// constants
	private static final int MAX_CFS = 255; // the maximum target resolution, frame should be square by this point
	
	/**
	 * Constructor for ScaleController instance
	 * @param mainController The main controller
	 * @param display The display
	 */
	public ScaleController(MainController mainController, Display display) {
		
		// debug trace
		Debug.trace("Created scale controller");
		
		// setup components
		this.linkedController = mainController;
		this.display = display;
		this.video = null;
		
		// setup variables
		this.frameIndex = 0;
	}
	
	
	/*
	 * Scale the video
	 */
	@Override
	public void process() {
		
		// debug trace
		Debug.trace("Scale controller recieved process instruction");
		
		// get the target resolution
		// placeholder
		int target = 100;//this.display.getTarget();
		
		// if the target is valid
		if (target < Math.min(this.video.getWidth(), MAX_CFS) && target > 0) {
			
			// scale the video
			this.video = this.video.downScale(target, target);
			
			// update the display
			this.display.setFrame(this.video.getFrame(this.frameIndex));
			
		// else report invalid target
		} else {
			Debug.trace("Error: Scale controller invalid target resolution");
			this.display.setMessage("Error: Invalid target resolution");
		}
	}
	
	
	/**
	 * Pass control to this controller
	 */
	@Override
	public void pass(Video video) {
		
		// debug trace
		Debug.trace("Scale controller recieved pass instruction");
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL Scaling stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
