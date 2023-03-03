package motionvdl.display;

import java.awt.Color;
import java.awt.Point;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
	private Circle[] points;
	private Line diagonalLine;
	private Line[] lines;
	private Rectangle opaqueSquare;
	
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

		// ImageView to show current frame
		this.imageView = new ImageView();
		this.imageView.setLayoutX(15);
		this.imageView.setLayoutY(50);
		this.imageView.setImage(new Image("motionvdl/display/images/javaIcon.png")); // Set image to frame
		this.imageView.setFitHeight(400);
		this.imageView.setFitWidth(400);
		this.imageView.setPreserveRatio(false);
		this.imageView.setOnMouseClicked(
				event -> System.out.println(event)  /* Controller Reference Here */ );
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

		// Points to be placed on the ImageView to visualise a click
		this.points = new Circle[11];
		for (int i = 0; i < this.points.length; i++) {
			this.points[i] = new Circle();
			this.points[i].setId("pointID");
			this.points[i].setRadius(7);
		}

		// Lines to connect points during the labelling stage
		this.lines = new Line[10];
		for (int i = 0; i < this.lines.length; i++) {
			this.lines[i] = new Line();
			this.lines[i].setId("lineID");
		}

		// Line to visualise crop after first click during cropping stage
		this.diagonalLine = new Line();

		// Square to visualise crop after second click during cropping stage
		this.opaqueSquare = new Rectangle();
		this.opaqueSquare.setId("opaqueSquare");

		// Details relating to window itself
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
		this.receiver = controller;
	}
	
	public void setTitle(String string) {
		this.titleLab.setText(string);
	}
	
	public void setFrame(Color[][] frame) {
		// TODO: JavaFX ImageView requires type Image, so must convert from Color[][] to Image to display on screen
		// this.imageView.setImage(frame);
		
	}
	
	public void setMessage(String string) {
		this.messageLab.setText(string);
	}

	public void drawPoint(int x, int y) {
		x += (int) this.imageView.getLayoutX();
		y += (int) this.imageView.getLayoutY();
		// TODO: Replace 0 with the number based on how many points have been placed - passed as parameter?
		this.points[0].setCenterX(x);
		this.points[0].setCenterY(y);
		this.primaryPane.getChildren().add(this.points[0]);
	}
	
	public void drawPoints(Point[] points) {
		// TODO: Draw multiple points on the display
		
	}
	
	public void drawDiagonal(int ax, int ay) {
		ax += (int) this.imageView.getLayoutX();
		ay += (int) this.imageView.getLayoutY();
		this.diagonalLine.setStartX(ax);
		this.diagonalLine.setStartY(ay);
		this.diagonalLine.setEndX(ax);
		this.diagonalLine.setEndY(ay);

		// Ensure line is within the bounds of the ImageView
		while(this.diagonalLine.getStartX() != this.imageView.getLayoutX()
				&& this.diagonalLine.getStartY() != this.imageView.getLayoutY()) {
			this.diagonalLine.setStartX(this.diagonalLine.getStartX()-1);
			this.diagonalLine.setStartY(this.diagonalLine.getStartY()-1);
		}
		while(this.diagonalLine.getEndX() != (this.imageView.getLayoutX() + this.imageView.getFitWidth())
				&& this.diagonalLine.getEndY() != (this.imageView.getLayoutY() + this.imageView.getFitHeight())) {
			this.diagonalLine.setEndX(this.diagonalLine.getEndX()+1);
			this.diagonalLine.setEndY(this.diagonalLine.getEndY()+1);
		}
		this.primaryPane.getChildren().add(this.diagonalLine);
	}
	
	public void drawRectangle(int ax, int ay, int bx, int by) {
		ax += (int) this.imageView.getLayoutX();
		ay += (int) this.imageView.getLayoutY();
		this.opaqueSquare.setLayoutX(ax);
		this.opaqueSquare.setLayoutY(ay);
		this.opaqueSquare.setWidth(bx);
		this.opaqueSquare.setHeight(by);
		this.primaryPane.getChildren().add(opaqueSquare);
	}
	
	public void clearGeometry() {
		// Even if these nodes haven't yet been added to the Pane, this will still work
		this.primaryPane.getChildren().remove(this.diagonalLine);
		this.primaryPane.getChildren().remove(this.opaqueSquare);
		this.primaryPane.getChildren().removeAll(this.points);
		this.primaryPane.getChildren().removeAll(this.lines);
	}


	public Point getTarget() {
		// TODO Auto-generated method stub
		return null;
	}
}
