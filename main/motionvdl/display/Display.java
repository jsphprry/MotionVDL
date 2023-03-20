package motionvdl.display;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import motionvdl.controller.Controller;
import motionvdl.model.Point;

/**
 * MotionVDL display component
 * @author Henri
 */
public class Display {

	public final int WIDTH;
	public final int HEIGHT;
	private Controller receiver;
	private final Stage primaryStage;
	private final Scene primaryScene;
	private final Pane primaryPane;
	private final Button processBut;
	private final Button undoBut;
	private final Button nextBut;
	private final Button prevBut;
	private final ImageView imageView;
	private final Label titleLab;
	private final Label messageLab;
	private final Line cropLine;
	private final List<Circle> points;
	private final List<Line> connectors;
	private final RadioButton toggleAutoBut;
	private final Rectangle opaqueSquare;
	private final TextField resTextField;

	/**
	 * Display constructor.
	 * @param w      Width of the window
	 * @param h      Height of the window
	 * @param stage  Default Stage to be used by the Application
	 */
	public Display(int w, int h, Stage stage) {
		this.WIDTH = w;
		this.HEIGHT = h;

		this.primaryStage = stage;
		this.primaryPane = new Pane();
		this.primaryPane.setId("paneID");
		this.primaryScene = new Scene(this.primaryPane, this.WIDTH, this.HEIGHT);
		this.primaryScene.getStylesheets().add("motionvdl/display/styleSheets/mainStyle.css");

		// Title Label
		this.titleLab = new Label("Title");
		this.titleLab.setId("titleLabID");
		this.titleLab.setLayoutX(6);
		this.titleLab.setLayoutY(6);
		this.primaryPane.getChildren().add(this.titleLab);

		// ImageView to show current frame
		this.imageView = new ImageView();
		this.imageView.setId("imageViewID");
		this.imageView.setLayoutX(15);
		this.imageView.setLayoutY(40);
		this.imageView.setFitHeight(420);
		this.imageView.setFitWidth(420);
		this.imageView.setPreserveRatio(false);
		this.imageView.setOnMouseClicked(
				event -> receiver.click(event.getX() / this.imageView.getFitWidth(),
										event.getY() / this.imageView.getFitHeight())
		);
		this.primaryPane.getChildren().add(this.imageView);

		// Radio button to toggle automatic mode
		this.toggleAutoBut = new RadioButton("Toggle Auto");
		this.toggleAutoBut.setId("radioID");
		this.toggleAutoBut.setLayoutX(500);
		this.toggleAutoBut.setLayoutY(40);
		this.toggleAutoBut.setMinSize(160, 50);
		this.primaryPane.getChildren().add(this.toggleAutoBut);

		// Button for processing
		this.processBut = new Button("Next stage");
		this.processBut.setId("buttonID");
		this.processBut.setLayoutX(475);
		this.processBut.setLayoutY(90);
		this.processBut.setMinSize(160,50);
		this.processBut.setOnAction(
				event -> receiver.next()
		);
		this.primaryPane.getChildren().add(this.processBut);

		// Button for undoing an incorrect point placement
		this.undoBut = new Button("Undo");
		this.undoBut.setId("buttonID");
		this.undoBut.setLayoutX(475);
		this.undoBut.setLayoutY(150);
		this.undoBut.setMinSize(160, 50);
		this.undoBut.setOnAction(
				event -> receiver.undo()
		);
		this.primaryPane.getChildren().add(this.undoBut);

		// Button for switching to previous frame
		this.prevBut = new Button("<-");
		this.prevBut.setId("buttonID");
		this.prevBut.setLayoutX(475);
		this.prevBut.setLayoutY(210);
		this.prevBut.setMinSize(78,50);
		this.prevBut.setOnAction(
				event -> receiver.down()
		);
		this.primaryPane.getChildren().add(this.prevBut);

		// Button for switching to next frame
		this.nextBut = new Button("->");
		this.nextBut.setId("buttonID");
		this.nextBut.setLayoutX(557);
		this.nextBut.setLayoutY(210);
		this.nextBut.setMinSize(78,50);
		this.nextBut.setOnAction(
				event -> receiver.up()
		);
		this.primaryPane.getChildren().add(this.nextBut);

		// TextField for specifying target resolution
		this.resTextField = new TextField();
		this.resTextField.setId("textFieldID");
		this.resTextField.setLayoutX(520);
		this.resTextField.setLayoutY(270);
		this.resTextField.setMinSize(5, 5);
		this.resTextField.setMaxWidth(70);
		this.primaryPane.getChildren().add(this.resTextField);

		// Message area Label
		this.messageLab = new Label("Message area");
		this.messageLab.setId("messageLabID");
		this.messageLab.setLayoutX(475);
		this.messageLab.setLayoutY(310);
		this.messageLab.setMaxWidth(160);
		this.messageLab.setWrapText(true);
		this.primaryPane.getChildren().add(this.messageLab);

		// Points to be placed on the ImageView to visualise a click
		this.points = new ArrayList<>();

		// Lines to connect points during the labelling stage
		this.connectors = new ArrayList<>();

		// Line to visualise crop after first click during cropping stage
		this.cropLine = new Line();
		this.cropLine.setId("cropLineID");

		// Square to visualise crop after second click during cropping stage
		this.opaqueSquare = new Rectangle();
		this.opaqueSquare.setId("opaqueSquareID");

		// Details relating to window itself
		this.primaryStage.setTitle("MotionVDL");
		this.primaryStage.getIcons().add(new Image("motionvdl/display/images/javaIcon.png"));
		this.primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
		this.primaryStage.setScene(this.primaryScene);
		this.primaryStage.show();
	}

	/**
	 * Store a reference to the receiving controller.
	 * @param controller The controller reference
	 */
	public void sendTo(Controller controller) {
		this.receiver = controller;
	}

	/**
	 * Set the title of the Scene.
	 * @param string Text to be set as title
	 */
	public void setTitle(String string) {
		this.titleLab.setText(string);
	}

	/**
	 * Convert an array of AWT Colors to JavaFX Image, then set
	 * the ImageView's Image property to display this frame.
	 * @param colorArray Array of colors, containing the current frame
	 */
	public void setFrame(Color[][] colorArray) {
		// Convert color array to awt.BufferedImage
		int width = colorArray.length;
		int height = colorArray[0].length;
		BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color color = colorArray[x][y];
				int rgb = color.getRGB();
				bImage.setRGB(x, y, rgb);
			}
		}

		// Copy pixel data from BufferedImage to byte array
		width = bImage.getWidth();
		height = bImage.getHeight();
		byte[] buffer = new byte[width * height * 4];
		int[] pixels = bImage.getRGB(0, 0, width, height, null, 0, width);
		for (int i = 0; i < pixels.length; i++) {
			buffer[i * 4 + 3] = (byte) ((pixels[i] >> 24) & 0xFF);
			buffer[i * 4 + 2] = (byte) ((pixels[i] >> 16) & 0xFF);
			buffer[i * 4 + 1] = (byte) ((pixels[i] >> 8) & 0xFF);
			buffer[i * 4] = (byte) ((pixels[i]) & 0xFF);
		}

		// Create JavaFX WritableImage and write pixel data
		WritableImage wImage = new WritableImage(width, height);
		PixelWriter pixelWriter = wImage.getPixelWriter();
		PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
		pixelWriter.setPixels(0, 0, width, height, pixelFormat, buffer, 0, width * 4);

		this.imageView.setImage(wImage);
	}

	/**
	 * Send the user a message, using a Label.
	 * @param string Text to show the user
	 */
	public void setMessage(String string) {
		this.messageLab.setText(string);
	}


	/**
	 * Draw a point on the ImageView, using a Circle object.
	 * @param x Normalised x co-ordinate of the user's click on the ImageView
	 * @param y Normalised y co-ordinate of the user's click on the ImageView
	 */
	public void drawPoint(double x, double y) {
		// Convert normalised co-ordinates to relative
		x = x * this.imageView.getFitWidth() + this.imageView.getLayoutX();
		y = y * this.imageView.getFitHeight() + this.imageView.getLayoutY();

		int pointNum = getPointNum();
		this.points.add(new Circle());
		this.points.get(pointNum).setId("pointID");
		this.points.get(pointNum).setRadius(7);
		this.points.get(pointNum).setCenterX(x);
		this.points.get(pointNum).setCenterY(y);
		if (pointNum > 0) {             //
			drawConnector();            // Ideally should be moved to LabelController
		}                               //
		this.primaryPane.getChildren().add(this.points.get(pointNum));
	}

	/**
	 * Draw multiple points on the ImageView.
	 * @param drawPoints An array of x and y co-ordinates for each point
	 */
	public void drawPoints(Point[] drawPoints) {
		for (Point point : drawPoints) {
			drawPoint(point.getX(), point.getY());
		}
	}

	/**
	 * Draw a Line object which connects two points.
	 */
	public void drawConnector() {
		int pointNum = getPointNum() - 1;
		this.connectors.add(new Line());
		this.connectors.get(pointNum).setId("lineID");
		switch (pointNum) {
			default -> {
				this.connectors.get(pointNum).setStartX(this.points.get(pointNum).getCenterX());
				this.connectors.get(pointNum).setStartY(this.points.get(pointNum).getCenterY());
				this.connectors.get(pointNum).setEndX(this.points.get(pointNum + 1).getCenterX());
				this.connectors.get(pointNum).setEndY(this.points.get(pointNum + 1).getCenterY());
			}

			// Special cases for points not connected to previous point
			case 3, 5 -> {
				this.connectors.get(pointNum).setStartX(this.points.get(1).getCenterX());
				this.connectors.get(pointNum).setStartY(this.points.get(1).getCenterY());
				this.connectors.get(pointNum).setEndX(this.points.get(pointNum + 1).getCenterX());
				this.connectors.get(pointNum).setEndY(this.points.get(pointNum + 1).getCenterY());
			}
			case 8 -> {
				this.connectors.get(pointNum).setStartX(this.points.get(6).getCenterX());
				this.connectors.get(pointNum).setStartY(this.points.get(6).getCenterY());
				this.connectors.get(pointNum).setEndX(this.points.get(pointNum + 1).getCenterX());
				this.connectors.get(pointNum).setEndY(this.points.get(pointNum + 1).getCenterY());
			}
		}
		this.primaryPane.getChildren().add(this.connectors.get(pointNum));
	}

	/**
	 * Draw a diagonal Line object to visualise cropping process.
	 * @param ax x co-ordinate of the user's click on the ImageView
	 * @param ay y co-ordinate of the user's click on the ImageView
	 */
	public void drawDiagonal(double ax, double ay) {
		// Convert normalised co-ordinates to relative
		ax = ax * this.imageView.getFitWidth();
		ay = ay * this.imageView.getFitHeight();

		double c = ay - ax;
		if (ax > ay) {
			this.cropLine.setStartX(this.imageView.getLayoutX() - c);
			this.cropLine.setStartY(this.imageView.getLayoutY());
			this.cropLine.setEndX(this.imageView.getLayoutX() + this.imageView.getFitWidth());
			this.cropLine.setEndY(this.imageView.getLayoutY() + this.imageView.getFitHeight() + c);
		} else if (ax < ay) {
			this.cropLine.setStartX(this.imageView.getLayoutX());
			this.cropLine.setStartY(this.imageView.getLayoutY() + c);
			this.cropLine.setEndX(this.imageView.getLayoutX() + this.imageView.getFitWidth() - c);
			this.cropLine.setEndY(this.imageView.getLayoutY() + this.imageView.getFitHeight());
		} else {
			this.cropLine.setStartX(this.imageView.getLayoutX());
			this.cropLine.setStartY(this.imageView.getLayoutY());
			this.cropLine.setEndX(this.imageView.getLayoutX() + this.imageView.getFitWidth());
			this.cropLine.setEndY(this.imageView.getLayoutY() + this.imageView.getFitHeight());
		}
		this.primaryPane.getChildren().add(this.cropLine);
	}

	/**
	 * Draw an opaque Rectangle object to visualise cropping process.
	 * @param ax x co-ordinate of the top-left corner of the rectangle
	 * @param ay y co-ordinate of the top-left corner of the rectangle
	 * @param bx x co-ordinate of the bottom-right corner of the rectangle
	 * @param by y co-ordinate of the bottom-right corner of the rectangle
	 */
	public void drawRectangle(double ax, double ay, double bx, double by) {
		// Convert normalised co-ordinates to relative
		ax = ax * this.imageView.getFitWidth() + this.imageView.getLayoutX();
		ay = ay * this.imageView.getFitHeight() + this.imageView.getLayoutY();
		bx = bx * this.imageView.getFitWidth() + this.imageView.getLayoutX();
		by = by * this.imageView.getFitHeight() + this.imageView.getLayoutY();

		this.opaqueSquare.setLayoutX(ax);
		this.opaqueSquare.setLayoutY(ay);
		this.opaqueSquare.setWidth(bx - ax);
		this.opaqueSquare.setHeight(by - ay);
		this.primaryPane.getChildren().add(opaqueSquare);
	}

	/**
	 * Clear all Lines, Circles, and Rectangle objects from the Pane.
	 */
	public void clearGeometry() {
		// Even if these nodes haven't yet been added to the Pane, this will still work
		this.primaryPane.getChildren().remove(this.cropLine);
		this.primaryPane.getChildren().remove(this.opaqueSquare);
		this.primaryPane.getChildren().removeAll(this.points);
		this.primaryPane.getChildren().removeAll(this.connectors);
	}

	/**
	 * Process a user's input for target resolution, and return only if valid.
	 * @return An int specifying the target resolution
	 */
	public int getTarget() {
		try {
			return Integer.parseInt(this.resTextField.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException(e + "");
		}
	}

	/**
	 * Use a stream to find how many instances of Circle
	 * objects are actively on the Pane.
	 * @return The number of points actively on the Pane
	 */
	public int getPointNum() {
		return (int) this.primaryPane
				.getChildren()
				.stream()
				.filter(node -> node instanceof Circle)
				.count();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getRadio() {
		return this.toggleAutoBut.isSelected();
	}
}
