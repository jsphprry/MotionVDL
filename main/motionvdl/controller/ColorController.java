package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL color processing subcontroller
 * @author Joseph
 */
public class ColorController extends Controller {
	
	// variables
	private Video tempVideo;
	private boolean greyscaleSet;
	
	/**
	 * Constructor for ColorController instance
	 * @param mainController The main controller
	 * @param display The display
	 */
	public ColorController(MainController mainController, Display display) {
		
		// debug trace
		Debug.trace("Created color controller");
		
		// setup components
		this.linkedController = mainController;
		this.display = display;
		this.video = null;
		
		// setup variables
		this.frameIndex = 0;
		this.greyscaleSet = false;
	}
	
	
	/**
	 * Toggle between greyscale and color video
	 */
	@Override
	public void process() {
		
		// debug trace
		Debug.trace("Color controller recieved process instruction");
		
		// if greyscale video does not exist create it
		if (this.tempVideo == null) this.tempVideo = this.video.greyScale();
		
		// swap videos
		Video temp = this.video;
		this.video = this.tempVideo;
		this.tempVideo = temp;
		this.greyscaleSet = !this.greyscaleSet;
		
		// update display
		this.display.setFrame(this.video.getFrame(this.frameIndex));
		
		// debug trace
		if (this.greyscaleSet) {
			Debug.trace("Color controller set to greyscale video");
		} else {
			Debug.trace("Color controller set to color video");
		}
	}
	
	
	/**
	 * Pass the currently displayed video back to the main controller
	 */
	@Override
	public void complete() {
		
		// debug trace
		Debug.trace("Color controller recieved complete instruction");
		
		// free tempVideo
		this.tempVideo = null;
		
		// call Controller complete
		super.complete();
	}
	
	
	/**
	 * Pass control to this controller
	 */
	@Override
	public void pass(Video video) {
		
		// debug trace
		Debug.trace("Color controller recieved pass instruction");
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL color processing stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
