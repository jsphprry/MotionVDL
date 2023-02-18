package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Label;
import motionvdl.model.Video;

/**
 * MotionVDL labelling subcontroller
 * @author Joseph
 */
public class LabelController extends Controller {
	
	// constants
	private final static int MAX_POINTS = 11;
	
	// variables
	private Label label;
	
	/**
	 * Constructor for LabelController instance
	 * @param mainController The main controller
	 * @param display The main display
	 */
	public LabelController(MainController mainController, Display display) {
		
		// setup components
		this.linkedController = mainController;
		this.display = display;
		this.video = null;
		
		// setup variables
		this.frameIndex = 0;
		this.label = new Label(MAX_POINTS, this.video.getFrameCount());
	}
	
	
	/**
	 * Record a point
	 * @param x The x axis of the click event
	 * @param y The y axis of the click event
	 */
	@Override
	public void point(int x, int y) {
		
		// if the frame label is incomplete
		try {
			
			// record point
			this.label.push(this.frameIndex, x, y);
			
			// update display
			this.display.setPoint(x, y);
			
		// otherwise go to next frame
		} catch(ArrayIndexOutOfBoundsException e) {
			this.frameUp();
		}
	}
	
	
	/**
	 * Delete last point on frame
	 */
	@Override
	public void process() {
		
		// if the frame label is not empty
		try {
			
			// remove point
			this.label.delete(this.frameIndex);
			
			// update display
			this.display.clearPoints();
			this.display.setPoints(this.label.getPoints(this.frameIndex));
			
		// otherwise go to next frame
		} catch(ArrayIndexOutOfBoundsException e) {
			this.frameDown();
		}
	}
	
	
	/**
	 * Check if the label is complete then either export the 
	 * labelled video or display a message describing the problem.
	 */
	@Override
	public void complete() {
		
		// if the label is full export the labelled video
		if (this.label.checkFull()) {
			
			// encode
			boolean[] encodedVideo = this.video.export();
			boolean[] encodedLabel = this.label.export();
			
			// write to file
			// close the program
			throw new UnsupportedOperationException("LabelController complete method is unimplemented");
			
		
		// otherwise display a message
		} else {
			this.display.setMessage("The label must be full to export to file");
		}
	}
	
	
	/**
	 * Display next frame up from current frame
	 */
	@Override
	public void frameUp() {
		
		// increment frameIndex
		this.frameIndex = Math.min(this.video.getFrameCount() - 1, frameIndex + 1);
		
		// update display
		this.display.clearPoints();
		this.display.setPoints(this.label.getPoints(this.frameIndex));
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Display next frame down from current frame
	 */
	@Override
	public void frameDown() {
		
		// decrement frameIndex
		this.frameIndex = Math.max(0, frameIndex - 1);
		
		// update display
		this.display.clearPoints();
		this.display.setPoints(this.label.getPoints(this.frameIndex));
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Pass control to this controller
	 */
	protected void pass(Video video) {
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL Labelling stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
