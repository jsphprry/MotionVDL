package motionvdl.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import motionvdl.Debug;
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
		
		// debug trace
		Debug.trace("Created label controller");
		
		// setup components
		this.linkedController = mainController;
		this.display = display;
		this.video = null;
		
		// setup variables
		this.frameIndex = 0;
	}
	
	
	/**
	 * Record a point
	 * @param x The x axis of the click event
	 * @param y The y axis of the click event
	 */
	@Override
	public void point(int x, int y) {
		
		// debug trace
		Debug.trace("Label controller recieved point instruction");

		// if the frame label is not full
		if (!this.label.frameFull(this.frameIndex)) {
			
			// record point
			this.label.push(this.frameIndex, x, y);
			
			// update display
			this.display.drawPoint(x, y);
			
			// if the frame label is now full display next frame
			if (this.label.frameFull(this.frameIndex)) this.frameUp();
		
		// if the frame label is full
		} else {
			
			// debug trace
			Debug.trace("Label controller ignores point instruction");
		}
	}
	
	
	/**
	 * Delete last point on frame
	 */
	@Override
	public void process() {
		
		// debug trace
		Debug.trace("Label controller recieved process instruction");

		// if the frame label is not empty
		if (!this.label.frameEmpty(this.frameIndex)) {
			
			// remove point
			this.label.delete(this.frameIndex);
			
			// update display
			this.display.clearGeometry();
			this.display.drawPoints(this.label.getPoints(this.frameIndex));
		
		// if the frame label is empty
		} else {
			this.frameDown();
		}
	}
	
	
	/**
	 * Check if the label is complete then either export the 
	 * labelled video or display a message describing the problem.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Override
	public void complete() {
		
		// debug trace
		Debug.trace("Label controller recieved complete instruction");
		
		// if the label is full export the labelled video
		if (this.label.full()) {
			
			// debug trace
			Debug.trace("Label is completely full");
			
			// encode video and label
			byte[] encodedVideo = this.video.encode();
			byte[] encodedLabel = this.label.encode();
			
			// write to file
			try {
				
				// debug trace
				Debug.trace("Exported video.mvdl, label.mvdl");
				
				// write bytes to output streams
				FileOutputStream videoStream = new FileOutputStream("video.mvdl");
				FileOutputStream labelStream = new FileOutputStream("label.mvdl");
				videoStream.write(encodedVideo);
				labelStream.write(encodedLabel);
				videoStream.close();
				labelStream.close();
			
			// report error
			} catch (Exception e) {
				Debug.trace("Error: "+e.getMessage());
				this.display.setMessage("Error: Problem encountered when writing to file");
			}
			
		// report error
		} else {
			Debug.trace("Error: Label is not completely full");
			this.display.setMessage("Error: The label must be full to export to file");
		}
	}
	
	
	/**
	 * Display next frame up from current frame
	 */
	@Override
	public void frameUp() {
		
		// debug trace
		Debug.trace("Label controller recieved frameUp instruction");
		
		// increment frameIndex
		this.frameIndex = Math.min(this.video.getFrames() - 1, frameIndex + 1);
		
		// update display
		this.display.clearGeometry();
		this.display.drawPoints(this.label.getPoints(this.frameIndex));
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Display next frame down from current frame
	 */
	@Override
	public void frameDown() {
		
		// debug trace
		Debug.trace("Label controller recieved frameDown instruction");
		
		// decrement frameIndex
		this.frameIndex = Math.max(0, frameIndex - 1);
		
		// update display
		this.display.clearGeometry();
		this.display.drawPoints(this.label.getPoints(this.frameIndex));
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Pass control to this controller
	 */
	@Override
	public void pass(Video video) {
		
		// debug trace
		Debug.trace("Label controller recieved pass instruction");
		
		// set the video and label
		this.video = video;
		this.label = new Label(MAX_POINTS, this.video.getFrames());
		
		// update display
		this.display.setTitle("MotionVDL Labelling stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
