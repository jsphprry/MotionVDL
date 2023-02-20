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
	private Video greyscaleVideo;
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
		if (this.greyscaleVideo == null) this.greyscaleVideo = this.video.greyScale();
		
		// toggle between greyscale and color video
		if (!this.greyscaleSet) {
			this.display.setFrame(this.greyscaleVideo.getFrame(this.frameIndex));
			this.greyscaleSet = true;
		} else {
			this.display.setFrame(this.video.getFrame(this.frameIndex));
			this.greyscaleSet = false;
		}
	}
	
	
	/**
	 * Pass the currently displayed video back to the main controller
	 */
	@Override
	public void complete() {
		
		// debug trace
		Debug.trace("Color controller recieved complete instruction");
		
		// if greyscale flag is true then set video to greyscale video
		if (this.greyscaleSet) this.video = this.greyscaleVideo;
		
		// call default complete
		super.complete();
	}
	
	
	/**
	 * Pass control to this controller
	 */
	protected void pass(Video video) {
		
		// debug trace
		Debug.trace("Color controller recieved pass instruction");
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL color processing stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
