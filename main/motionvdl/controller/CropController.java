package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL cropping subcontroller
 * @author Joseph
 */
public class CropController extends Controller {
	
	// crop frame variables
	private int ax;   // top left point x axis
	private int ay;   // top left point y axis
	private int cfs;  // edge size
	private int limx; // x axis limit
	private int limy; // y axis limit
	private int click; // click counter
	//private boolean ready;    // ready-to-process flag
	//private boolean adjusted; // adjusted-by-user flag
	
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
		//ready = false;
		//adjusted = false;
		
		// debug trace
		Debug.trace(String.format("Created CropController '%s'", debugTitle));
	}
	
	
	/**
	 * Define the crop frame
	 * @param x The x axis of the coordinate
	 * @param y The y axis of the coordinate
	 */
	public void click(int x, int y) {
		
		// increment click counter
		click += 1;
		
		// debug trace
		Debug.trace(debugTitle+" recieved click "+click);
		
		// first click
		if (click == 1) {
			
			// use crop frame suggestion function
			ax = x;
			ay = y;
			cfs = (int) Math.min(Math.min(0.2*limx, limx-ax), Math.min(0.2*limy, limy-ay));
			
			// draw crop frame
			display.clearGeometry();
			display.drawRectangle(ax, ay, ax+cfs, ay+cfs);
			display.drawDiagonal(ax, ay);
			display.drawPoint(ax, ay);
		
		
		// second click
		} else if (click == 2) {
			
			// adjust crop frame if valid click
			if (ay != y) {
				
				// use crop frame adjustment function
				if (ay < y) {
					cfs = Math.min(y-ay, limx-ax);
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
				display.drawPoint(x, y);
				display.drawRectangle(ax, ay, ax+cfs, ay+cfs);
				
			// else warn
			} else {
				Debug.trace(debugTitle+" skipped click: invalid point");
				display.setMessage("*Invalid point*");
			}
			
			
		// third click
		} else if (click == 3){

			// reset crop frame
			click = 0;
			display.clearGeometry();
		}
	}
	
	
	/**
	 * Crop the video data and switch stage
	 */
	public void next() {
		
		// with defined crop frame
		if (click > 1) {
			
			// crop video and next stage
			buffer = buffer.crop(ax, ay, cfs, cfs);
			
			// next stage
			super.next();
		
		// with undefined crop frame
		} else {
			Debug.trace(debugTitle+" skipped next: undefined crop frame");
			display.setMessage("*Undefined crop frame*");
		}
	}
	
	
	/**
	 * Store video resolution then pass control
	 */
	public void pass(Video tempVideo) {
		
		// setup variables
		limy = tempVideo.height;
		limx = tempVideo.width;
		
		// pass control
		super.pass(tempVideo);
	}
}
