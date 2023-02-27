package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL cropping subcontroller
 * @author Joseph
 */
public class CropController extends Controller {
	
	// crop frame origin coordinate
	private int ax;
	private int ay;
	
	// frame limits
	private int vx;
	private int vy;
	
	// crop frame size
	private int cfs;
	
	// state flags
	private boolean ready;
	private boolean adjusted;
	
	/**
	 * Constructor for CropController instance
	 * @param mainController The main controller
	 * @param display The display
	 */
	public CropController(MainController mainController, Display display) {
		
		// debug trace
		Debug.trace("Created crop controller");
		
		// setup components
		this.linkedController = mainController;
		this.display = display;
		this.video = null;
		
		// setup variables
		this.frameIndex = 0;
		ready = false;
		adjusted = false;
	}
	
	
	/**
	 * Set the crop frame coordinates
	 * @param x The x axis of the coordinate
	 * @param y The y axis of the coordinate
	 */
	@Override
	public void point(int x, int y) {
		
		// debug trace
		Debug.trace("Crop controller recieved point instruction");
		
		
		// first click
		if (!ready && !adjusted) {
			
			// debug trace
			Debug.trace("Crop controller ready");
			
			// define crop frame
			ax = x;
			ay = y;
			cfs = (int) Math.min(Math.min(vx-ax, 0.2*vx), Math.min(vy-ay, 0.2*vy));
			
			// set state flags
			ready = true;
			adjusted = false;
			
			// update display
			display.clearGeometry();
			display.drawRectangle(ax, ay, ax+cfs, ay+cfs);
			display.drawDiagonal(ax, ay);
			display.drawPoint(ax, ay);
		
		
		// second click
		} else if (ready && !adjusted) {
			
			// debug trace
			Debug.trace("Crop controller ready and adjusted");
			
			// define crop frame
			// no handling for y coord because of the assumption that clicks cannot come from outside the frame
			if (ay < y) {
				cfs = Math.min(y-ay, vx-ax);
			} else if (ay > y) {
				cfs = Math.min(ay-y, ax);
				ax = ax-cfs;
				ay = ay-cfs;
			} else {
				display.setMessage("Crop frame cannot have zero size");
			}
			
			// set state flags
			ready = true;
			adjusted = true;
			
			// update display
			display.clearGeometry();
			display.drawRectangle(ax, ay, ax+cfs, ay+cfs);
			display.drawDiagonal(ax, ay);
			display.drawPoint(ax, ay);
			
			
		// third click
		} else {
			
			// debug trace
			Debug.trace("Crop controller clear frame");
			
			// set state flags
			ready = false;
			adjusted = false;
			
			// update display
			display.clearGeometry();
		}
	}
	
	
	/*
	 * Crop the video around the crop frame
	 */
	@Override
	public void process() {
		
		// debug trace
		Debug.trace("Crop controller recieved process instruction");

		// crop video
		if (ready) {
			
			// crop video
			video = video.crop(ax, ay, cfs, cfs);
			
			// update display
			display.clearGeometry();
			display.setFrame(video.getFrame(frameIndex));
		
		// report undefined crop frame 
		} else {
			Debug.trace("Error: Crop controller crop frame is undefined");
			display.setMessage("Error: Cannot crop when crop frame is undefined");
		}
	}
	
	
	/**
	 * Pass control to this controller
	 */
	@Override
	public void pass(Video video) {
		
		// debug trace
		Debug.trace("Crop controller recieved pass instruction");
		
		// set the video
		this.video = video;
		vy = video.getHeight();
		vx = video.getWidth();
		
		// update display
		display.setTitle("MotionVDL Cropping stage");
		display.setFrame(video.getFrame(frameIndex));
	}
}
