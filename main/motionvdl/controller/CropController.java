package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL cropping subcontroller
 * @author Joseph
 */
public class CropController extends Controller {
	
	// variables
	private double ax;   // top-left norm x axis
	private double ay;   // top-left norm y axis
	private double cfs;  // crop frame edge size
	private int click; // click counter
	
	/**
	 * Construct crop controller
	 * @param mainController The main controller
	 * @param mainDisplay The main display
	 */
	public CropController(MainController mainController, Display mainDisplay) {
		
		// setup titles
		displayTitle = "Cropping stage";
		debugTitle = "Crop controller";
		outputTitle = "videoS1";
		
		// setup components
		linkedController = mainController;
		display = mainDisplay;
		
		// setup crop frame variables
		click = 0;
		
		// debug trace
		Debug.trace(String.format("Created CropController '%s'", debugTitle));
	}
	
	
	/**
	 * Define the crop frame
	 * @param x The normalised x axis of the click event
	 * @param y The normalised y axis of the click event
	 */
	public void click(double x, double y) {
		
		// increment click counter
		click += 1;
		
		// debug trace
		Debug.trace(debugTitle+" recieved click"+click+" "+x+" "+y);
		
		// first click suggests frame
		if (click == 1) {
			
			// use crop frame suggestion function
			ax = x;
			ay = y;
			cfs = Math.min(Math.min(0.2, 1.0-ax), Math.min(0.2, 1.0-ay));
			
			// draw crop frame
			display.drawRectangle(ax, ay, ax+cfs, ay+cfs);
			display.drawDiagonal(ax, ay);
			display.drawPoint(ax, ay);
		
		
		// second click adjusts frame
		} else if (click == 2) {
			
			// if adjustment is valid
			if (ay != y) {
				
				// use crop frame adjustment function
				if (ay < y) {
					cfs = Math.min(y-ay, 1.0-ax);
					//ax = ax;
					//ay = ay;
				} else {
					cfs = Math.min(ay-y, ax);
					ax -= cfs;
					ay -= cfs;
				}
				
				// draw crop frame
				display.clearGeometry();
				display.drawDiagonal(ax, ay);
				display.drawPoint(ax, ay);
				display.drawPoint(ax+cfs, ay+cfs);
				display.drawRectangle(ax, ay, ax+cfs, ay+cfs);
				
			// else warn
			} else {
				Debug.trace(debugTitle+" skipped click: invalid point");
				display.setMessage("*Invalid point*");
			}
			
			
		// third click resets
		} else {
			click = 0;
			display.clearGeometry();
		}
	}
	
	
	/**
	 * Crop the video data and switch stage
	 */
	public void next() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved next");
		
		// with defined crop frame
		if (click == 1 || click == 2) {
			
			// crop video and next stage
			buffer = buffer.crop(ax, ay, ax+cfs, ay+cfs);
			
			// duplicate code. so that debug trace can be ordered correctly ('recieved' before 'crop')
			// export and free video buffer
			buffer.export(outputTitle);
			Video temp = buffer;
			buffer = null;
			
			// pass temporary video to the linked controller
			linkedController.pass(temp);
		
		// with undefined crop frame
		} else {
			Debug.trace(debugTitle+" skipped next: undefined crop frame");
			display.setMessage("*Undefined crop frame*");
		}
	}
}
