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
	 * warning 
	 * This test currently causes the IDE to crash by creating
	 * a loop somewhere that the environemnt is not able to stop
	 */
	@Test
	void completeSequence() {
		
		
		// setup stage
		// setup video and controller
		Video noise = Video.noise(250,200,50);
		MainController controller = new MainController(new Display());
		
		// start the controller
		controller.start(noise);
		
		
		
		// crop stage
		// place 5 clicks on crop stage
		controller.point(rand(49,100), rand(49,100));
		controller.point(rand(49,100), rand(49,100));
		controller.point(rand(49,100), rand(49,100));
		controller.point(rand(49,100), rand(49,100));
		controller.point(rand(49,100), rand(49,100));
		
		// process and complete stage
		controller.process();
		controller.complete();
		
		
		
		// scale stage
		// scale to target resolution
		controller.process();
		
		// process and complete stage
		controller.process();
		controller.complete();
		
		
		
		// greyscale stage
		// process and complete stage
		controller.process();
		controller.complete();
		
		
		
		// labelling stage
		// complete the labelling
		for (int i=0; i < 50; i++) {
			for (int j=0; j < 11; j++) {
				controller.point(rand(49,100), rand(49,100));
			}
		}
		
		// process and complete stage
		controller.process();
		controller.complete();
	}
	
	
	// function rand() = RNG * range + bias
	private static int rand(int bias, int range) {
		return bias + (int) (Math.random() * range);
	}

}
