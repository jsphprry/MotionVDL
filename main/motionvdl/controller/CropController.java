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
	private boolean ready;    // ready-to-process flag
	private boolean adjusted; // adjusted-by-user flag
	
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
		ready = false;
		adjusted = false;
		
		// debug trace
		Debug.trace(String.format("Created CropController '%s'", debugTitle));
	}
	
	
	/**
	 * Set the crop frame coordinates
	 * @param x The x axis of the coordinate
	 * @param y The y axis of the coordinate
	 */
	@Override
	public void click(int x, int y) {
		
		// debug trace
		Debug.trace(debugTitle+" recieved click instruction");
		
		// first click
		if (!ready && !adjusted) {
			
			// suggest crop frame
			ax = x;
			ay = y;
			cfs = (int) Math.min(Math.min(0.2*limx, limx-ax), Math.min(0.2*limy, limy-ay));
			
			// set flags
			Debug.trace(debugTitle+" set ready");
			ready = true;
			adjusted = false;
			
			// draw crop frame
			display.clearGeometry();
			display.drawRectangle(ax, ay, ax+cfs, ay+cfs);
			display.drawDiagonal(ax, ay);
			display.drawPoint(ax, ay);
		
		
		// second click
		} else if (ready && !adjusted) {
			
			// adjust crop frame 
			// if the second click is valid
			if (ay != y) {
				
				// if the second click is below the first
				if (ay < y) {
					cfs = Math.min(y-ay, limx-ax);
				
				// if the second click is above the first
				} else {
					cfs = Math.min(ay-y, ax);
					ax -= cfs;
					ay -= cfs;
				}
				
				// set flags
				Debug.trace(debugTitle+" set ready and adjusted");
				ready = true;
				adjusted = true;
				
				// draw crop frame
				display.clearGeometry();
				display.drawDiagonal(ax, ay);
				display.drawPoint(ax, ay);
				display.drawPoint(ax+cfs, ay+cfs);
				display.drawRectangle(ax, ay, ax+cfs, ay+cfs);
			
			// else warn
			} else {
				Debug.trace(debugTitle+" ignored click instruction 'crop frame cannot have zero size'");
				display.setMessage("Warning! Crop frame cannot have zero size");
			}
			
			
		// third click
		} else {
			
			// reset flags
			Debug.trace(debugTitle+" set not ready");
			ready = false;
			adjusted = false;
			
			// clear display
			display.clearGeometry();
		}
	}
	
	
	/**
	 * Crop the video to the crop frame
	 */
	@Override
	public void process() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved process instruction");
		
		// if the crop frame is ready
		if (ready) {
			
			// crop buffered video
			buffer = buffer.crop(ax, ay, cfs, cfs);
			
			// update display
			display.clearGeometry();
			display.setFrame(buffer.getFrame(frameIndex));
		
		// else warn
		} else {
			Debug.trace(debugTitle+" ignored process instruction");
			display.setMessage("Warning! Not ready to crop");
		}
	}
	
	
	/**
	 * Pass control to this controller
	 */
	public void pass(Video tempVideo) {
		
		// setup variables
		limy = tempVideo.height;
		limx = tempVideo.width;
		
		super.pass(tempVideo);
	}
}
