package mvdltest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import motionvdl.controller.MainController;
import motionvdl.display.Display;
import motionvdl.model.Video;

class MotionVDLTest {
	
	@BeforeEach
	void setUp() throws Exception {
	}
	
	
	/**
	 * WARNING
	 * This test currently causes the IDE to crash by creating
	 * a loop somewhere that the environemnt is not able to stop
	 * 
	 * Example of start to finish controller instruction seqeunce 
	 * for a fully integrated system
	 */
	@Test
	void fullProgram() {
		
		// setup components
		Video noise = Video.noise(250,200,50);
		Display display = new Display();
		MainController controller = new MainController(display);
		
		// start the main controller
		controller.start(noise);
		
		// crop stage
		controller.point(biasRand(49,100), biasRand(49,100)); // place 5 clicks on frame
		controller.point(biasRand(49,100), biasRand(49,100));
		controller.point(biasRand(49,100), biasRand(49,100));
		controller.point(biasRand(49,100), biasRand(49,100));
		controller.point(biasRand(49,100), biasRand(49,100));
		controller.process(); // commit crop
		controller.complete(); // next stage
		
		// scale stage
		controller.process(); // scale to target resolution
		controller.complete(); // next stage
		
		
		// color stage
		controller.process(); // convert to greyscale
		controller.complete(); // next stage
		
		// labelling stage
		for (int i=0; i < 50; i++) { // on each frame
			for (int j=0; j < 11; j++) { // place 11 points
				controller.point(biasRand(49,100), biasRand(49,100)); 
			}
		}
		controller.process(); // undo the last point
		controller.complete(); // try to export files, should be blocked by incomplete label
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

}
