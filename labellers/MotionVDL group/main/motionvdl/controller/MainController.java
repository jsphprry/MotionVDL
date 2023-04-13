package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.FileSystem;
import motionvdl.model.data.LabeledVideo;
import motionvdl.model.data.Video;

/**
 * MotionVDL main controller
 * @author Joseph
 */
public class MainController extends Controller {

	// subcontrollers
	private Controller[] subcontroller; // stage controllers
	private int controllerIndex;        // index of current subcontroller

	/**
	 * Construct main controller
	 * @param d Pointer to display
	 */
	public MainController(Display d) {

		// setup titles
		debugTitle = "MC";

		// setup components
		display = d;
		display.sendTo(this);
		// 230410 Joseph. Needs equivelant JavaFX instructions
		//display.setVisible(true);

		// setup subcontrollers
		controllerIndex = -1;
		subcontroller = new Controller[] {
			new VideoController(this),   // video stage controller
			new LabelController(this)};  // label stage controller

		// debug trace
		Debug.trace(String.format("Created MainController '%s'", debugTitle));
	}


	/**
	 * Pass video loaded from file system location to relevant subcontroller
	 * @param location Filesystem location
	 */
	public void open(String location) {
		
		// debug trace
		Debug.trace(String.format("%s open ->", debugTitle));
		
		try {
			
			// get file extension
			String[] pattern = location.split("\\.");
			String extension = (pattern.length > 0) ? pattern[pattern.length-1] : "";
			
			// decode mvdl files
			if (extension.equals("mvdl")) {
				
				// pass to label controller
				controllerIndex = 1;
				linkedController = subcontroller[controllerIndex];
				linkedController.pass(new LabeledVideo(FileSystem.readBytes(location)));
			
			// otherwise open as image directory
			} else {
				
				// pass to video controller
				controllerIndex = 0;
				linkedController = subcontroller[controllerIndex];
				linkedController.pass(new LabeledVideo(new Video(FileSystem.readImages(location))));
			}
			
		// trace error and pass noise to video controller
		} catch (Exception  e) {
			e.printStackTrace();
			Debug.trace(e.getMessage());
			controllerIndex = 0;
			linkedController = subcontroller[controllerIndex];
			linkedController.pass(new LabeledVideo(Video.noise(100, 100, 10)));
		}
	}
	
	
	/**
	 * Save data to default filesystem location
	 */
	public void save() {
		saveAs(outputFile);
	}
	
	
	/**
	 * Save data to given filesystem location
	 * @param location Filesystem location
	 */
	public void saveAs(String location) {
		
		// debug trace
		Debug.trace(String.format("%s save ->", debugTitle));
		
		// export data
		try {
			if (controllerIndex > -1) linkedController.data.export(location);
		} catch (Exception e) {
			e.printStackTrace();
			Debug.trace(e.getMessage());
		}
	}


	/**
	 * Switch to next subcontroller
	 * @param temp Temporary pointer to labelled video data
	 */
	@Override
	public void pass(LabeledVideo temp) {

		// debug trace
		Debug.trace(String.format("%s pass ->", debugTitle));

		// switch to next subcontroller
		controllerIndex += 1;
		linkedController = subcontroller[controllerIndex];
		linkedController.pass(temp);
	}


	/**
	 * Pass instruction to subcontroller
	 * @param x Normalised x coordinate
	 * @param y Normalised y coordinate
	 */
	@Override
	public void click(double x, double y) {

		// debug trace
		Debug.trace(String.format("%s click ->",debugTitle));

		// call subcontroller
		if (controllerIndex > -1) linkedController.click(x, y);
	}


	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void undo() {

		// debug trace
		Debug.trace(String.format("%s undo ->",debugTitle));

		// call subcontroller
		if (controllerIndex > -1) linkedController.undo();
	}
	
	
	/**
	 * Pass instruction to subcontroller
	 * @param x Normalised x coordinate
	 * @param y Normalised y coordinate
	 */
	public void calibrate(double x, double y) {

		// debug trace
		Debug.trace(String.format("%s calibrate ->", debugTitle));

		// call subcontroller
		if (controllerIndex > -1) linkedController.calibrate(x,y);
	}


	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void complete() {

		// debug trace
		Debug.trace(String.format("%s complete ->", debugTitle));

		// call subcontroller
		if (controllerIndex > -1) linkedController.complete();
	}


	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void setFrame(int index) {
		
		// debug trace
		Debug.trace(String.format("%s setFrame ->", debugTitle));
		
		// call subcontroller
		if (controllerIndex > -1) linkedController.setFrame(index);
	}


	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void setNextFrame() {

		// debug trace
		Debug.trace(String.format("%s setNextFrame ->", debugTitle));

		// call subcontroller
		if (controllerIndex > -1) linkedController.setNextFrame();
	}


	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void setPrevFrame() {

		// debug trace
		Debug.trace(String.format("%s setPrevFrame ->", debugTitle));

		// call subcontroller
		if (controllerIndex > -1) linkedController.setPrevFrame();
	}


	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void setMinFrame() {

		// debug trace
		Debug.trace(String.format("%s setMinFrame ->", debugTitle));

		// call subcontroller
		if (controllerIndex > -1) linkedController.setMinFrame();
	}


	/**
	 * Pass instruction to subcontroller
	 */
	@Override
	public void setMaxFrame() {

		// debug trace
		Debug.trace(String.format("%s prevFrame ->", debugTitle));

		// call subcontroller
		if (controllerIndex > -1) linkedController.setMaxFrame();
	}
}
