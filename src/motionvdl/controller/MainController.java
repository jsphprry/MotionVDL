package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Label;
import motionvdl.model.Video;

/**
 * The MotionVDL main controller
 * @author Joseph
 */
public class MainController {
	
	// components
	private Subcontroller[] subcontrollers;
	private Subcontroller controller;
	private Display mainDisplay;
	private Video localVideo;
	
	/**
	 * Constructor for main controller instance
	 * @param md
	 * @param lv
	 */
	public MainController(Display md, Video lv) {
		
		// setup components
		this.mainDisplay = md;
		this.localVideo = lv;
		
		// TODO other setup
		
		// start program
		this.start();
	}
	
	
	public void start() {
		// TODO start program
	}
	
	public void nextController(Video video) {
		// TODO transfer control between subcontrollers and continue program
		
	}
	
	public void finalStage(Video video, Label label) {
		// TODO export video and label to file
		
	}
}
