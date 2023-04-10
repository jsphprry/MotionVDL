package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL cropping subcontroller
 * @author Joseph
 */
public class VideoController extends Controller {
	
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
		
		// debug trace
		Debug.trace(String.format("Created CropController '%s'", debugTitle));
	}
	
	
	/**
	 * Define the crop frame
	 * @param x The normalised x-axis of the click event
	 * @param y The normalised y-axis of the click event
	 */
	/*public void click(double x, double y) {
		
		// increment click counter
		click += 1;
		
		// debug trace
		Debug.trace(String.format("%s received click%d (%.2f,%.2f)",debugTitle, click, x, y));
		
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
	}*/
	
	
	/**
	 * Crop scale and color the video data then switch stage
	 */
	public void next() {
		
		// debug trace
		Debug.trace(debugTitle + " received next");
		
		// get target res and scale crop frame
		int targetRes = display.getTarget();
		int[] cfd = display.getCropFrame();
		int cropX = cfd[0];
		int cropY = cfd[1];
		int cropRes = cfd[2];
		
		// proceed if crop frame and target resolution are valid
		boolean validTR = (0 < targetRes && targetRes <= cropRes);
		if (validTR) {
			
			// crop scale and color video
			video = video.squareCrop(cropX, cropY, cropRes).downScale(targetRes, targetRes).greyScale();
			
			// duplicate code rather than super call. Done so that debug
			// trace is correctly ordered ('received next' before 'crop video')
			// export and free video buffer
			video.export(outputTitle);
			Video temp = video;
			video = null;
			
			// pass temporary video to the linked controller
			linkedController.pass(temp);
			//// end of duplicate
		
		// skip and warn invalid target resolution
		} else {
			String message = String.format("Invalid target resolution [targetRes=%d, cropRes=%d]", targetRes, cropRes);
			Debug.trace(debugTitle+" ignored next: "+message);
			display.sendAlert(message);
		}
	}
}
