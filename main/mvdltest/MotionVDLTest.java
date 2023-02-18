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
		
		// setup video and controller
		Video noise = Video.noise(250,200,50);
		MainController controller = new MainController(new Display());
		
		// start the controller
		controller.start(noise);
		
		// crop stage
		controller.point(rand(49,100), rand(49,100)); // place 5 clicks on frame
		controller.point(rand(49,100), rand(49,100));
		controller.point(rand(49,100), rand(49,100));
		controller.point(rand(49,100), rand(49,100));
		controller.point(rand(49,100), rand(49,100));
		controller.process(); // commit crop
		controller.complete(); // next stage
		
		// scale stage
		controller.process(); // scale to target resolution
		controller.complete(); // next stage
		
		
		// greyscale stage
		controller.process(); // convert to greyscale
		controller.complete(); // next stage
		
		// labelling stage
		for (int i=0; i < 50; i++) { // on each frame
			for (int j=0; j < 11; j++) { // place 11 points
				controller.point(rand(49,100), rand(49,100)); 
			}
		}
		controller.process(); // undo the last point
		controller.point(rand(49,100), rand(49,100)); // place a point
		controller.complete(); // export files
	}
	
	
	/**
	 * Biased random number 
	 * @param bias The bias added to the random number
	 * @param range The maximum value of the randon number
	 * @return Biased random number
	 */
	private static int rand(int bias, int range) {
		return bias + (int) (Math.random() * range);
	}

}
