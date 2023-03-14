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
		controller.undo();      // crop video
		controller.next();     // next stage
		
		// scale stage
		controller.undo();  // scale video
		controller.next(); // next stage
		
		// color stage
		controller.undo();  // convert to greyscale
		controller.undo();  // back to color
		controller.undo();  // back to greyscale
		controller.next(); // next stage
		
		// labelling stage
		for (int i=0; i < N_FRAMES; i++) for (int j=0; j < 11; j++) controller.click(j,j); // on each frame place 11 points
		controller.next(); // complete program
	}
}
