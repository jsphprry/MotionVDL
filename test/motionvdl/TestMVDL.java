package motionvdl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.stage.Stage;
import motionvdl.controller.MainController;
import motionvdl.display.Display;
import motionvdl.model.Video;

public class TestMVDL {
	
	@BeforeEach
	void setUp() throws Exception {
		Debug.setup(true);
	}
	
	
	/**
	 * Example of start to finish instruction 
	 * sequence for a fully integrated system
	 * @author Joseph
	 */
	@Test
	public void standardTest() {
		
		// constants
		int N_FRAMES = 10;
		
		// setup display and controller
		Display display = new Display(500, 350, new Stage());
		MainController controller = new MainController(display);
		display.sendTo(controller);
		
		// start main controller with video noise
		controller.pass(Video.noise(250,200,N_FRAMES));
		
		// crop stage
		controller.click(100,100); // ready
		controller.click(200,200); // ready and adjusted
		controller.process();      // crop video
		controller.complete();     // next stage
		
		// scale stage
		controller.process();  // scale video
		controller.complete(); // next stage
		
		// color stage
		controller.process();  // convert to greyscale
		controller.process();  // back to color
		controller.process();  // back to greyscale
		controller.complete(); // next stage
		
		// labelling stage
		for (int i=0; i < N_FRAMES; i++) for (int j=0; j < 11; j++) controller.click(j,j); // on each frame place 11 points
		controller.complete(); // complete program
	}
}
