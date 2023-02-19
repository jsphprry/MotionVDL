package mvdltest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import motionvdl.Debug;
import motionvdl.controller.Controller;
import motionvdl.controller.CropController;
import motionvdl.controller.MainController;
import motionvdl.display.Display;
import motionvdl.model.Video;

class MotionVDLTest {
	
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
		controller.point(biasRand(49,100), biasRand(49,100)); // place first point
		controller.point(biasRand(49,100), biasRand(49,100)); // place second point
		controller.point(biasRand(49,100), biasRand(49,100)); // clear points
		controller.point(biasRand(49,100), biasRand(49,100)); // place first point
		controller.point(biasRand(49,100), biasRand(49,100)); // place second point
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
		for (int i=0; i < 50; i++) {
			for (int j=0; j < 11; j++) {
				controller.point(biasRand(49,100), biasRand(49,100)); // on each frame place 11 points
			}
		}
		controller.process(); // undo the last point
		controller.complete(); // try to export files, should be blocked because of the incomplete label
		controller.point(biasRand(49,100), biasRand(49,100)); // place a point
		controller.complete(); // export files
		
	}
	
	
	/**
	 * Biased random number 
	 * @param bias The bias added to the random number
	 * @param range The maximum value of the randon number
	 * @return Biased random number
	 */
	private static int biasRand(int bias, int range) {
		return bias + (int) (Math.random() * range);
	}
	
	
	/**
	 * Test for Crop controller
	 */
	//@Test
	public void testCrop() {
		
		// setup components
		Video noise = Video.noise(250,200,50);
		Display display = new Display();
		Controller controller = new CropController(null, display);
		
		// start controller
		controller.pass(noise);
		
		// place points
		controller.point(100, 100);
		controller.point(150, 150);
		
		// crop video
		controller.process();
		
		// complete stage, expect null pointer from main controller pass
		try {
			controller.complete();
		} catch (NullPointerException e) {
			// do nothing
		}
		
	}

}
