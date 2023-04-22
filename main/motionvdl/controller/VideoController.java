package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.model.data.LabeledVideo;

/**
 * MotionVDL video subcontroller
 * @author Joseph
 */
public class VideoController extends Controller {
	
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
	 * Default pass behaviour then setup display
	 * @param temp Reference to the labeled video
	 */
	public void pass(LabeledVideo temp) {
		super.pass(temp);
		display.alterForPreprocessing();
	}
	
	
	/**
	 * Export processed video and switch to next stage
	 */
	@Override
	public void complete() {
		
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
			data = data.getProcessed(cropX, cropY, cropRes, targetRes);
			
			// next stage
			super.complete();
		
		// skip and warn invalid target resolution
		} else {
			String message = String.format("Invalid target resolution [targetRes=%d, cropRes=%d]", targetRes, cropRes);
			Debug.trace(debugTitle+" ignored next: "+message);
			display.sendAlert(message);
		}
	}
}
