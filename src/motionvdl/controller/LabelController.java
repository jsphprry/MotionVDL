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
	private int[] framePoints;
	private Label label;
	
	/**
	 * Constructor for LabelController instance
	 * @param mc The main controller
	 * @param md The main display
	 * @param v The subject video
	 */
	public LabelController(MainController mc, Display md, Video v) {
		
		// setup components
		this.linkedController = mc;
		this.display = md;
		this.video = v;
		
		// setup variables
		this.frameIndex  = 0;
		this.framePoints = new int[this.video.getDepth()]; // default int value is 0 so no need to populate array
		this.label       = new Label(MAX_POINTS, this.video.getDepth());
	}
	
	
	/**
	 * Record the coordinates of a click in the point label
	 * @param x The x axis of the click event
	 * @param y The y axis of the click event
	 */
	@Override
	public void point(int x, int y) {
		
		// get the next frame-wise point index
		int pointIndex = this.framePoints[this.frameIndex];
		
		// if the frame label is incomplete
		if (pointIndex < MAX_POINTS) {

			// write point to label
			this.label.write(pointIndex, this.frameIndex, x, y);
			
			// update display
			this.display.setPoint(x, y);
			
			// increment frame-wise point counter
			this.framePoints[this.frameIndex] += 1;
		
		// otherwise go to next frame
		} else {
			this.frameUp();
		}
	}
	
	
	/**
	 * Undo the last frame click action
	 */
	@Override
	public void process() {
		
		// get the previous frame-wise point index
		int pointIndex = this.framePoints[this.frameIndex] - 1;
		
		// if the frame label is not empty
		if (pointIndex >= 0) {

			// delete point from label
			this.label.delete(pointIndex, this.frameIndex);
			
			// update display
			this.display.clearPoints();
			this.display.setPoints(this.label.getRow(this.frameIndex));
			
			// decrement frame-wise point counter
			this.framePoints[this.frameIndex] -= 1;
		
		// otherwise go to prev frame
		} else {
			this.frameDown();
		}
	}
	
	
	/**
	 * Check if the label is complete then either export the 
	 * labelled video or display a message describing the problem.
	 */
	@Override
	public void complete() {
		
		// check that the label is complete
		boolean ready = false;
		for (int i=0; i < this.framePoints.length; i++) {
			ready = (this.framePoints[i] == MAX_POINTS);
		}
		
		// if the label is complete export labelled video to file
		if (ready) {
			
			// encode
			boolean[] encodedVideo = this.video.export();
			boolean[] encodedLabel = this.label.export();
			
			// write to file
			// close the program
			throw new UnsupportedOperationException("Export to file is not implemented");
			
		
		// otherwise display a message
		} else {
			this.display.setMsg("Cannot finish because the label is incomplete.");
		}
	}
	
	
	/**
	 * Pass control to this controller
	 */
	public void pass(Video video) {
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL Labelling stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Display next frame up from current frame
	 */
	@Override
	public void frameUp() {
		
		// increment frameIndex
		this.frameIndex = Math.min(this.video.getDepth() - 1, frameIndex + 1);
		
		// update display
		this.display.clearPoints();
		this.display.setPoints(this.label.getRow(this.frameIndex));
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
		this.display.setPoints(this.label.getRow(this.frameIndex));
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
