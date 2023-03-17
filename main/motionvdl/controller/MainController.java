package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL main controller
 * @author Joseph
 */
public class MainController extends Controller {
	
	// constants
	private final static int N_STAGES = 2;
	
	// components
	private Controller[] subcontrollers;
	
	// variables
	private int stage;
	
	/**
	 * Construct main controller
	 * @param mainDisplay The main display
	 */
	public MainController(Display mainDisplay) {
		
		// setup titles
		debugTitle = "Main controller";
		
		// setup components
		subcontrollers = new Controller[N_STAGES];
		subcontrollers[0] = new VideoController(this, mainDisplay);
		subcontrollers[1] = new LabelController(this, mainDisplay);
		
		// setup variables
		stage = -1;
		
		// debug trace
		Debug.trace(String.format("Created MainController '%s'", debugTitle));
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 * @param x The normalised x axis of the click event
	 * @param y The normalised y axis of the click event
	 */
	public void click(double x, double y) {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved click");
		
		// call subcontroller
		linkedController.click(x, y);
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 */
	public void up() {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved up");
		
		// call subcontroller
		linkedController.up();
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 */
	public void down() {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved down");
		
		// call subcontroller
		linkedController.down();
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 */
	public void undo() {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved undo");
		
		// call subcontroller
		linkedController.undo();
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 */
	public void next() {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved next");
		
		// call subcontroller
		linkedController.next();
	}
	
	
	/**
	 * Switch to next subcontroller
	 */
	public void pass(Video tempVideo) {
		
		// debug trace
		Debug.trace(debugTitle+" recieved pass");

		// pass control to next subcontroller
		stage += 1;
		linkedController = subcontrollers[stage];
		linkedController.pass(tempVideo);
	}
}
