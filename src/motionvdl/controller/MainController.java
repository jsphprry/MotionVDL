package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * The MotionVDL main controller
 * @author Joseph
 */
public class MainController extends Controller {
	
	// components
	private Controller[] subControllers;
	
	/**
	 * Constructor for main controller instance
	 * @param md The main display
	 * @param v The subject video
	 */
	public MainController(Display md, Video v) {
		
		// setup components
		this.linkedController = null;
		this.display = md;
		this.video = v;
		
		// TODO setup
	}
	
	
	/**
	 * Frame click action
	 * @param x The x axis of the click
	 * @param y The y axis of the click
	 */
	public void point(int x, int y) {
		this.linkedController.point(x, y);
	}
	
	
	/**
	 * Process button action
	 */
	public void process() {
		this.linkedController.process();
	}
	
	
	/**
	 * Complete button action
	 */
	public void complete() {
		this.linkedController.complete();
	}
	
	
	/**
	 * Pass video to controller
	 */
	public void pass(Video video) {
		
		// TODO pass control to the next subcontroller
		throw new UnsupportedOperationException("Main controller pass is not implemented");
	}
}
