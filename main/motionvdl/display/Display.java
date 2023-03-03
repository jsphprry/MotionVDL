package motionvdl.display;

import java.awt.Color;
import java.awt.Point;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import motionvdl.controller.Controller;

public class Display {
	
	//private Label title;
	//private Frame videoFrame;
	//private Label message;
	
	private int height;
	private int width;
	private Controller receiver;

	private Stage primaryStage;
	private Scene primaryScene;
	private Pane primaryPane;
	private ImageView imageView;
	private Label titleLab;
	private Label messageLab;
	private Button processBut;
	private Button completeBut;
	private Button nextBut;
	private Button prevBut;
	
	public Display(int h, int w, Stage stage) {
		this.height = h;
		this.width = w;
		
		//this.videoFrame = new Frame();

		this.primaryStage = stage;
		this.primaryPane = new Pane();
		this.primaryPane.setId("paneID");
		this.primaryScene = new Scene(this.primaryPane, this.height, this.width);
		this.primaryScene.getStylesheets().add("motionvdl/display/styleSheets/mainStyle.css");

		// Title Label
		this.titleLab = new Label();
		this.titleLab.setLayoutX(6);
		this.titleLab.setLayoutY(6);
		this.titleLab.setText("Title");
		this.primaryPane.getChildren().add(this.titleLab);

		// ImageView to show frame
		this.imageView = new ImageView();
		this.imageView.setLayoutX(15);
		this.imageView.setLayoutY(50);
		this.imageView.setImage(new Image("motionvdl/display/images/javaIcon.png")); // Set image to frame
		this.imageView.setFitHeight(400);
		this.imageView.setFitWidth(400);
		this.imageView.setPreserveRatio(false);
		this.imageView.setOnMouseClicked(
				event -> System.out.println(event) /* Controller Reference Here */ );
		this.primaryPane.getChildren().add(imageView);

		// Button for processing
		this.processBut = new Button();
		this.processBut.setLayoutX(475);
		this.processBut.setLayoutY(60);
		this.processBut.setMinSize(160,50);
		this.processBut.setText("Process");
		this.processBut.setOnAction(
				actionEvent -> System.out.println(actionEvent) /* Controller Reference Here */ );
		this.primaryPane.getChildren().add(this.processBut);

		// Button for completing
		this.completeBut = new Button();
		this.completeBut.setLayoutX(475);
		this.completeBut.setLayoutY(120);
		this.completeBut.setMinSize(160,50);
		this.completeBut.setText("Complete");
		this.completeBut.setOnAction(
				actionEvent -> System.out.println(actionEvent) /* Controller Reference Here */ );
		this.primaryPane.getChildren().add(this.completeBut);

		// Button for switching to previous frame
		this.prevBut = new Button();
		this.prevBut.setLayoutX(475);
		this.prevBut.setLayoutY(180);
		this.prevBut.setMinSize(78,50);
		this.prevBut.setText("Previous");
		this.prevBut.setOnAction(
				actionEvent -> System.out.println(actionEvent) /* Controller Reference Here */ );
		this.primaryPane.getChildren().add(this.prevBut);

		// Button for switching to next frame
		this.nextBut = new Button();
		this.nextBut.setLayoutX(557);
		this.nextBut.setLayoutY(180);
		this.nextBut.setMinSize(78,50);
		this.nextBut.setText("Next");
		this.nextBut.setOnAction(
				actionEvent -> System.out.println(actionEvent) /* Controller Reference Here */ );
		this.primaryPane.getChildren().add(this.nextBut);

		// Message area Label
		this.messageLab = new Label();
		this.messageLab.setLayoutX(475);
		this.messageLab.setLayoutY(280);
		this.messageLab.setText("Message area");
		this.primaryPane.getChildren().add(this.messageLab);

		this.primaryStage.setTitle("MotionVDL");
		this.primaryStage.getIcons().add(new Image("motionvdl/display/images/javaIcon.png"));
		this.primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
		this.primaryStage.setScene(this.primaryScene);
		this.primaryStage.show();
	}
	
	
	/**
	 * Store a reference to the receiving controller
	 * @param controller The controller reference
	 */
	public void sendTo(Controller controller) {
		receiver = controller;
	}
	
	public void setTitle(String string) {
		// TODO Auto-generated method stub
		//this.title.set(string);
		
	}
	
	public void setFrame(Color[][] frame) {
		// TODO Auto-generated method stub
		
		//this.videoFrame.setImage(frame);
		
	}
	
	public void setMessage(String string) {
		// TODO Auto-generated method stub
		//this.message.set(string);
		
	}
	
	

	public void drawPoint(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawPoints(Point[] points) {
		// TODO Draw multiple points on the display - commit test
		
	}
	
	public void drawDiagonal(int ax, int ay) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawRectangle(int ax, int ay, int bx, int by) {
		// TODO Auto-generated method stub
		
	}
	
	public void clearGeometry() {
		// TODO Auto-generated method stub
		
	}
	
	

	public Point getTarget() {
		// TODO Auto-generated method stub
		return null;
	}
}
