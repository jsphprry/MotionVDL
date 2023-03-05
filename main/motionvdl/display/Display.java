package motionvdl.display;

import java.awt.Color;
import java.awt.Point;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import motionvdl.controller.Controller;

/**
 * MotionVDL display component
 * @author Henri
 */
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
	private Line[] connectors;
	private Rectangle opaqueSquare;
	
	// Joseph - the instantiation of stage would be best encapsulated 
	// inside the constructor here, since it seems that all 
	// instantiations of Display pass stage as new Stage()
	// 
	// Joseph - actually it seems that MotionVDL.start() passes 
	// stage to Display as an argument, but I can't find a use of
	// start in the code so I'm not sure where that argument comes 
	// from, maybe you could explain this to me next meeting.

	// Henri - Reconsidering, I will reprogram MotionVDL class
	// to account for a new display instead of default - makes
	// things easier to handle in that class also
	public Display(int h, int w) {
		this.height = h;
		this.width = w;

		this.primaryStage = new Stage();
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
				event -> drawPoint((int) event.getX(), (int) event.getY())  /* Controller Reference Here */ );
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
		this.connectors = new Line[10];
		for (int i = 0; i < this.connectors.length; i++) {
			this.connectors[i] = new Line();
			this.connectors[i].setId("lineID");
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
	
	public void setFrame(Color[][] colorArray) {
		// TODO: Might work, need an example to make sure
		WritableImage frame = new WritableImage(colorArray.length, colorArray[0].length);
		PixelWriter writer = frame.getPixelWriter();
		for (int i = 0; i < colorArray.length; i++) {
			for (int j = 0; j < colorArray[i].length; j++) {
				// Convert each java.awt.Color object to a javafx.scene.paint.Color object
				javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.color(
						colorArray[i][j].getRed(),
						colorArray[i][j].getGreen(),
						colorArray[i][j].getBlue() );

				// Set each pixel's color
				writer.setColor(j, i, fxColor);
			}
		}
		this.imageView.setImage(frame);
	}
	
	public void setMessage(String string) {
		this.messageLab.setText(string);
	}

	public void drawPoint(int x, int y) {
		x += (int) this.imageView.getLayoutX();
		y += (int) this.imageView.getLayoutY();
		
		// Joseph - getChildren() returns a type implementing ObservableList, which itself implements List
		// List has the method size() which returns the number of elements in the list so you could try 
		// size() to get the next index for the points array if you create a temporary variable to hold 
		// the list to call size() on

		// Very clever - I hadn't even considered that and I probably never would have - this
		// makes it so much easier to draw the points and connectors, as I've done below, the
		// pointNum variable counts only the number of Circle objects which are currently on
		// screen, which is much better than storing a counter variable

		int pointNum = (int) this.primaryPane
				.getChildren()
				.stream()
				.filter(node -> node instanceof Circle)
				.count();

		if (pointNum < 11) {
			this.points[pointNum].setCenterX(x);
			this.points[pointNum].setCenterY(y);
			this.primaryPane.getChildren().add(this.points[pointNum]);
			if (pointNum > 0) {
				drawConnector(pointNum);
			}
		} else {
			setMessage("All points placed!");
		}
	}

	public void drawConnector(int pointNum) {
		pointNum--;  // Decrement as there will always be one less connector than point
		switch (pointNum) {
			default -> {
				connectors[pointNum].setStartX(this.points[pointNum].getCenterX());
				connectors[pointNum].setStartY(this.points[pointNum].getCenterY());
				connectors[pointNum].setEndX(this.points[pointNum + 1].getCenterX());
				connectors[pointNum].setEndY(this.points[pointNum + 1].getCenterY());
			}

			// Special cases for points not connected to previous point
			case 3, 5 -> {
				connectors[pointNum].setStartX(this.points[1].getCenterX());
				connectors[pointNum].setStartY(this.points[1].getCenterY());
				connectors[pointNum].setEndX(this.points[pointNum + 1].getCenterX());
				connectors[pointNum].setEndY(this.points[pointNum + 1].getCenterY());
			}
			case 8 -> {
				connectors[pointNum].setStartX(this.points[6].getCenterX());
				connectors[pointNum].setStartY(this.points[6].getCenterY());
				connectors[pointNum].setEndX(this.points[pointNum + 1].getCenterX());
				connectors[pointNum].setEndY(this.points[pointNum + 1].getCenterY());
			}
		}
		this.primaryPane.getChildren().add(this.connectors[pointNum]);
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
		this.primaryPane.getChildren().removeAll(this.connectors);
	}


	public Point getTarget() {
		// TODO Auto-generated method stub
		return null;
	}
}
