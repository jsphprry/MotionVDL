package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.model.data.Encoding;
import motionvdl.model.data.LabeledVideo;

/**
 * MotionVDL video subcontroller
 * @author Joseph
 */
public class VideoController extends Controller {
	
	// variables
	private double cx;  // top-left crop frame x coord
	private double cy;  // top-left crop frame y coord
	private double cfs; // crop frame square edge size
	
	// flags
	private boolean ready;    // crop-frame ready flag
	private boolean adjusted; // crop-frame adjusted flag
	
	/**
	 * Construct crop controller
	 * @param mc Pointer to main controller
	 */
	public VideoController(MainController mc) {
		
		// setup titles
		displayTitle = "Video processing";
		debugTitle = "VSC";
		
		// setup components
		linkedController = mc;
		display = linkedController.display;
		
		// debug trace
		Debug.trace(String.format("Created VideoController '%s'", debugTitle));
	}
	
	
	/**
	 * Default behaviour then show target input
	 */
	public void pass(LabeledVideo temp) {
		
		// default
		super.pass(temp);
		
		// setup variables
		cx = 0.0;
		cy = 0.0;
		cfs = 1.0;
		
		// setup flags
		ready = false;
		adjusted = false;
		
		// setup display
		display.showTarget();
	}
	
	
	/**
	 * Define the crop frame
	 * @param x Normalised x coordinate
	 * @param y Normalised y coordinate
	 */
	@Override
	public void click(double x, double y) {

		// debug trace
		Debug.trace(String.format("%s click ->",debugTitle));

		// action branch
		if (!ready && !adjusted) {
			suggest(x,y);
		} else if (ready && !adjusted) {
			adjust(x,y);
		} else {
			reset();
		}
	}
	
	
	/**
	 * Undo last click action
	 */
	@Override
	public void undo() {

		// debug trace
		Debug.trace(String.format("%s undo ->",debugTitle));

		// action branch
		if (adjusted) {
			suggest(cx,cy);
		} else {
			reset();
		}
	}
	
	
	/**
	 * Center the crop frame on the given point
	 * @param x Normalised x coordinate
	 * @param y Normalised y coordinate
	 */
	public void calibrate(double x, double y) {
		
		// debug trace
		Debug.trace(String.format("%s calibrate ->",debugTitle));
		
		if (ready) {
			
			// update crop frame
			cx = Math.max(0.0,Math.min(1.0-cfs,x-0.5*cfs));
			cy = Math.max(0.0,Math.min(1.0-cfs,y-0.5*cfs));
			adjust(cx+cfs,cy+cfs);
		}
	}
	
	
	/**
	 * Export processed video and switch to next stage
	 */
	@Override
	public void complete() {
		
		// debug trace
		Debug.trace(String.format("%s process ->",debugTitle));
		
		// scale crop frame values
		int scale = data.video.width;
		int x = (int) (cx*scale);
		int y = (int) (cy*scale);
		int crop = (int) (cfs*scale);
		
		// get target and target limit
		int target = display.getTarget();
		int limit = Encoding.SIXTEEN_BIT_LIMIT;
		
		// determine validity
		boolean validCF = (ready && x+crop <= data.video.width && y+crop <= data.video.height);
		boolean validTR = (0 < target && target <= limit && target <= crop);
		
		// valid conditions
		if (validCF && validTR) {
			
			// process video and switch stage
			display.hideTarget();
			data = data.getProcessed(x, y, crop, target);
			super.complete();
			
		// invalid target resolution
		} else if (validCF && !validTR) {
			Debug.trace(String.format("%s ignored process: invalid target resolution. crop=%d target=%d",debugTitle,crop,target));
			
		// invalid crop frame
		} else {
			Debug.trace(String.format("%s ignored process: invalid crop frame",debugTitle));
		}
	}
	
	
	/**
	 * Define the crop frame with the suggestion function
	 * @param x Normalised click event x axis
	 * @param y Normalised click event y axis
	 */
	private void suggest(double x, double y) {

		// debug trace
		Debug.trace(String.format("%s suggest (%.2f,%.2f)",debugTitle, x, y));

		// suggestion function
		cx = x;
		cy = y;
		cfs = Math.min(Math.min(0.25, 1.0-x), Math.min(0.25, 1.0-y));

		// set flags
		ready = true;
		adjusted = false;

		// draw crop frame
		display.clearGeometry();
		display.drawRectangle(cx, cy, cx+cfs, cy+cfs);
		display.drawDiagonalLeft(cx, cy);
		display.drawPoint(cx, cy);
		display.setTarget((int) (cfs*data.video.width));
	}
	
	
	/**
	 * Define the crop frame with the adjustement function
	 * @param x Normalised click event x axis
	 * @param y Normalised click event y axis
	 */
	private void adjust(double x, double y) {

		// debug trace
		Debug.trace(String.format("%s adjust (%.2f,%.2f)",debugTitle, x, y));

		// adjustment function
		if (cy < y) {
			cfs = Math.min(y-cy, 1.0-cx);
			//cx = cx;
			//cy = cy;
		} else {
			cfs = Math.min(cy-y, cx);
			cx -= cfs;
			cy -= cfs;
		}

		// set flags
		ready = true;
		adjusted = true;

		// draw crop frame
		display.clearGeometry();
		display.drawDiagonalLeft(cx, cy);
		display.drawDiagonalRight(cx+0.5*cfs, cy+0.5*cfs);
		display.drawRectangle(cx, cy, cx+cfs, cy+cfs);
		display.setTarget((int) (cfs*data.video.width));
	}
	
	
	/**
	 * Reset crop frame
	 */
	private void reset() {

		// debug trace
		Debug.trace(String.format("%s reset",debugTitle));

		// set flags
		ready = false;
		adjusted = false;

		// clear display
		display.clearGeometry();
		display.setTarget(data.video.width);
	}
}
