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
	public void process() {
		
		// debug trace
		Debug.trace("Scale controller recieved process instruction");
		
		// get the target resolution
		Point target = this.display.getTarget();
		int targetX = (int) target.getX();
		int targetY = (int) target.getY();
		
		// if the target is valid
		try {
			
			// scale the video
			this.video = this.video.downScale(targetX, targetY);
			
			// update the display
			this.display.setFrame(this.video.getFrame(this.frameIndex));

		// if the target is invalid
		} catch (IllegalArgumentException e) {
			this.display.setMessage(e.getMessage());
		}
	}
	
	
	/**
	 * Pass control to this controller
	 */
	protected void pass(Video video) {
		
		// debug trace
		Debug.trace("Scale controller recieved pass instruction");
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL Scaling stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
