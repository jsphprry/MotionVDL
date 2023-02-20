package mvdltest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import motionvdl.Debug;
import motionvdl.controller.MainController;
import motionvdl.display.Display;
import motionvdl.model.Video;
import mvdltest.util.Numbers;

public class IntegrationTestMVDL {
	
	@BeforeEach
	void setUp() throws Exception {
		Debug.setup(true);
	}
	
	
	/**
	 * Example of start to finish instruction 
	 * sequence for a fully integrated system
	 */
	@Test
	public void fullProgram() {
		
		// setup components
		Video noise = Video.noise(250,200,50);
		Display display = new Display();
		MainController controller = new MainController(display);
		
		// start the main controller
		controller.start(noise);
		
		// crop stage
		controller.point(Numbers.biasRand(49,100), Numbers.biasRand(49,100)); // place first point
		controller.point(Numbers.biasRand(49,100), Numbers.biasRand(49,100)); // place second point
		controller.point(Numbers.biasRand(49,100), Numbers.biasRand(49,100)); // clear points
		controller.point(Numbers.biasRand(49,100), Numbers.biasRand(49,100)); // place first point
		controller.point(Numbers.biasRand(49,100), Numbers.biasRand(49,100)); // place second point
		controller.process(); // commit crop
		controller.complete(); // next stage
		
		// scale stage
		controller.process(); // scale to target resolution
		controller.complete(); // next stage
		
		// color stage
		controller.process(); // convert to greyscale
		controller.process(); // back to color
		controller.process(); // back to greyscale
		controller.complete(); // next stage
		
		// labelling stage
		controller.process(); // try to undo a point on the first frame when it's empty
		for (int i=0; i < 50; i++) {
			for (int j=0; j < 11; j++) {
				controller.point(Numbers.biasRand(49,100), Numbers.biasRand(49,100)); // on each frame place 11 points
			}
		}
		controller.process(); // undo the last point
		controller.complete(); // try to export files, should be blocked because of the incomplete label
		controller.point(Numbers.biasRand(49,100), Numbers.biasRand(49,100)); // place a point
		controller.point(Numbers.biasRand(49,100), Numbers.biasRand(49,100)); // try placing a 12th point on the last frame
		
		// try to export files, expect unsupported exception until implemented
		try {
			controller.complete();
			Assertions.fail(); // fail if there is no exception from controller
		} catch (UnsupportedOperationException e) {
			Debug.trace(e.getMessage());
		}
	}
}
