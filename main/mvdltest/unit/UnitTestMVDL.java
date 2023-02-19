package mvdltest.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import motionvdl.Debug;
import motionvdl.controller.ColorController;
import motionvdl.controller.Controller;
import motionvdl.controller.CropController;
import motionvdl.controller.LabelController;
import motionvdl.controller.ScaleController;
import motionvdl.display.Display;
import motionvdl.model.Video;
import mvdltest.util.Numbers;

class UnitTestMVDL {
	
	@BeforeEach
	void setUp() throws Exception {
		Debug.setup(true);
	}
	
	
	/**
	 * Test of standard sequence for crop controller
	 */
	@Test
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
		
		// complete stage, expect null pointer from pass to main controller
		try {
			controller.complete();
		} catch (NullPointerException e) {
			// do nothing
		}
		
	}
	
	
	/**
	 * Test of standard sequence for scale controller
	 */
	@Test
	public void testScale() {
		
		// setup components
		Video noise = Video.noise(250,200,50);
		Display display = new Display();
		Controller controller = new ScaleController(null, display);
		
		// start controller
		controller.pass(noise);
		
		// scale video
		controller.process();
		
		// complete stage, expect null pointer from pass to main controller
		try {
			controller.complete();
		} catch (NullPointerException e) {
			// do nothing
		}
		
	}
	
	
	/**
	 * Test of standard sequence for color controller
	 */
	@Test
	public void testColor() {
		
		// setup components
		Video noise = Video.noise(250,200,50);
		Display display = new Display();
		Controller controller = new ColorController(null, display);
		
		// start controller
		controller.pass(noise);
		
		// convert video
		controller.process();
		
		// complete stage, expect null pointer from main controller pass
		try {
			controller.complete();
		} catch (NullPointerException e) {
			// do nothing
		}
		
	}
	
	
	/**
	 * Test of standard sequence for label controller
	 */
	@Test
	public void testLabel() {
		
		// setup components
		Video noise = Video.noise(250,200,50);
		Display display = new Display();
		Controller controller = new LabelController(null, display);
		
		// start controller
		controller.pass(noise);
		
		// place points
		for (int i=0; i < 50; i++) {
			for (int j=0; j < 11; j++) {
				controller.point(Numbers.biasRand(49,100), Numbers.biasRand(49,100)); // on each frame place 11 points
			}
		}
		
		// complete stage, expect exception from unimplemented export
		try {
			controller.complete();
		} catch (UnsupportedOperationException e) {
			// do nothing
		}
		
	}
}
