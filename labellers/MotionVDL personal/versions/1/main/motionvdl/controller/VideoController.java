package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.model.Encoding;

/**
 * MotionVDL video subcontroller
 * @author Joseph
 */
public class VideoController extends Controller {

	// variables
	private double cx;  // top-left norm x axis
	private double cy;  // top-left norm y axis
	private double cfs; // square crop frame edge size

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

		// setup flags
		ready = false;
		adjusted = false;

		// debug trace
		Debug.trace(String.format("Created VideoController '%s'", debugTitle));
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
		int limit = Encoding.MAX_DIMENSION;

		// determine validity
		boolean validCF = (ready && x+crop <= data.video.width && y+crop <= data.video.height);
		boolean validTR = (0 < target && target <= limit && target <= crop);

		// valid conditions
		if (validCF && validTR) {

			// process video and switch stage
			data = data.process(x, y, crop, target);
			super.complete();

		// invalid target resolution
		} else if (validCF && !validTR) {
			Debug.trace(String.format("%s ignored process: invalid target resolution. target=%d crop=%d limit=%d",debugTitle,target,crop,limit));

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
			//ax = ax;
			//ay = ay;
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
	}
}
