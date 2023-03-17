package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL cropping subcontroller
 * @author Joseph
 */
public class VideoController extends Controller {
	
	// variables
	private double ax;  // top-left norm x axis
	private double ay;  // top-left norm y axis
	private double cfs; // square crop frame edge size
	private int click;  // click counter
	
	/**
	 * Construct crop controller
	 * @param mainController The main controller
	 * @param mainDisplay The main display
	 */
	public VideoController(MainController mainController, Display mainDisplay) {
		
		// setup titles
		displayTitle = "Video preprocessing";
		debugTitle = "Video controller";
		outputTitle = "video";
		
		// setup components
		linkedController = mainController;
		display = mainDisplay;
		
		// setup variables
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
		Debug.trace(String.format("%s recieved click%d (%f.2f,%f.2f)",debugTitle, click, x, y));
		
		// first click suggests frame
		if (click == 1) {
			
			// use crop frame suggestion function
			ax = x;
			ay = y;
			cfs = Math.min(Math.min(0.2, 1.0-x), Math.min(0.2, 1.0-y));
			
			// draw crop frame
			display.drawRectangle(ax, ay, ax+cfs, ay+cfs);
			display.drawDiagonal(ax, ay);
			display.drawPoint(ax, ay);
			
		
		// second click adjusts frame
		} else if (click == 2) {
			
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
			
			
		// third click resets frame
		} else {
			click = 0;
			display.clearGeometry();
		}
	}
	
	
	/**
	 * Crop scale and color the video data then switch stage
	 */
	public void next() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved next");
		
		// get target resolution and color flag
		int target = 50;//display.getTarget();
		boolean grey = display.getRadio();
		
		// proceed if crop frame and target resolution are valid
		boolean validCF = (0 < click && click <= 2);
		boolean validTR = (0 < target && target <= cfs);
		if (validCF && validTR) {
			
			// crop scale and color video
			video = video.crop(ax, ay, ax+cfs, ay+cfs).downScale(target, target);
			if (grey) video = video.greyScale();
			
			//// duplicate code rather than super call. Done so that debug 
			//// trace is correctly ordered ('recieved next' before 'crop video')
			// export and free video buffer
			video.export(outputTitle);
			Video temp = video;
			video = null;
			
			// pass temporary video to the linked controller
			linkedController.pass(temp);
			//// end of duplicate
		
		// skip and warn if invalid target resolution
		} else if (validCF && !validTR) {
			Debug.trace(debugTitle+" no action, invalid target resolution");
			display.setMessage("*Invalid target resolution*");
		
		// skip and warn if invalid crop frame
		} else {
			Debug.trace(debugTitle+" no action, undefined crop frame");
			display.setMessage("*Undefined crop frame*");
		}
	}
}
