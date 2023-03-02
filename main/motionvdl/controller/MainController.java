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
	private final static int N_STAGES = 4;
	
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
		subcontrollers[0] = new CropController(this, mainDisplay);
		subcontrollers[1] = new ScaleController(this, mainDisplay);
		subcontrollers[2] = new ColorController(this, mainDisplay);
		subcontrollers[3] = new LabelController(this, mainDisplay);
		
		// setup variables
		stage = -1;
		
		// debug trace
		Debug.trace(String.format("Created MainController '%s'", debugTitle));
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 * @param x The x axis of the click
	 * @param y The y axis of the click
	 */
	@Override
	public void click(int x, int y) {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved click instruction");
		
		// call subcontroller
		linkedController.click(x, y);
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void process() {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved process instruction");
		
		// call subcontroller
		linkedController.process();
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 */
	public void complete() {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved complete instruction");
		
		// call subcontroller
		linkedController.complete();
	}
	
	
	/**
	 * Switch to next subcontroller
	 */
	public void pass(Video tempVideo) {
		
		// debug trace
		Debug.trace(debugTitle+" recieved pass instruction");

		// pass control to next subcontroller
		stage += 1;
		linkedController = subcontrollers[stage];
		linkedController.pass(tempVideo);
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void nextFrame() {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved nextFrame instruction");
		
		// call subcontroller
		linkedController.nextFrame();
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void prevFrame() {
		
		// throw control-not-passed case
		if (stage == -1) throw new IllegalStateException(debugTitle+" has not been passed control yet");
		
		// debug trace
		Debug.trace(debugTitle+" recieved prevFrame instruction");
		
		// call subcontroller
		linkedController.prevFrame();
	}
}
