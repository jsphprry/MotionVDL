package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL main controller
 * @author Joseph
 */
public class MainController extends Controller {
	
	// constants
	private final static int N_STAGES = 4;
	
	// components
	private Controller[] subcontrollers;
	
	// variables
	private int stage;
	
	/**
	 * Constructor for main controller instance
	 * @param md The main display
	 * @param v The subject video
	 */
	public MainController(Display md) {
		
		// setup subcontrollers
		this.subcontrollers = new Controller[N_STAGES];
		this.subcontrollers[0] = new CropController(this, md);
		this.subcontrollers[1] = new ScaleController(this, md);
		this.subcontrollers[2] = new GreyscaleController(this, md);
		this.subcontrollers[3] = new LabelController(this, md);
		
		// setup components
		this.linkedController = null;
		this.display = null;
		this.video = null;
		
		// setup variables
		this.stage = -1;
	}
	
	
	/**
	 * Start the controller sequence
	 */
	public void start(Video video) {
		
		// throw already-in-control case
		if (this.stage != -1) throw new IllegalStateException("The main controller has already been started");
		
		// pass control to self
		this.pass(video);
	}
	
	
	/*
	 * Get the main controller stage
	 */
	public int getStage() {
		return this.stage;
	}
	
	
	/**
	 * Frame click action
	 * @param x The x axis of the click
	 * @param y The y axis of the click
	 */
	@Override
	public void point(int x, int y) {
		
		// throw control-not-passed case
		if (this.stage == -1) throw new IllegalStateException("The main controller has not been passed control yet");
		
		// call subcontroller
		this.linkedController.point(x, y);
	}
	
	
	/**
	 * Process button action
	 */
	@Override
	public void process() {
		
		// throw control-not-passed case
		if (this.stage == -1) throw new IllegalStateException("The main controller has not been passed control yet");
		
		// call subcontroller
		this.linkedController.process();
	}
	
	
	/**
	 * Complete button action
	 */
	@Override
	public void complete() {
		
		// throw control-not-passed case
		if (this.stage == -1) throw new IllegalStateException("The main controller has not been passed control yet");
		
		// call subcontroller
		this.linkedController.complete();
	}
	
	
	/**
	 * Display next frame up from current frame
	 */
	@Override
	public void frameUp() {
		
		// throw control-not-passed case
		if (this.stage == -1) throw new IllegalStateException("The main controller has not been passed control yet");
		
		// call subcontroller
		this.linkedController.frameUp();
	}
	
	
	/**
	 * Display next frame down from current frame
	 */
	@Override
	public void frameDown() {
		
		// throw control-not-passed case
		if (this.stage == -1) throw new IllegalStateException("The main controller has not been passed control yet");
		
		// call subcontroller
		this.linkedController.frameDown();
	}
	
	
	/**
	 * Pass control to the next subcontroller
	 */
	@Override
	protected void pass(Video video) {
		
		// increment stage counter
		this.stage += 1;
		
		// set subcontroller
		this.linkedController = this.subcontrollers[this.stage];
		
		// pass control to subcontroller
		this.linkedController.pass(video);
	}
}