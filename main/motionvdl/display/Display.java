package motionvdl.display;

import java.awt.Color;
import java.awt.Point;
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

/**
 * MotionVDL display component
 * @author Henri
 */
public class Display {

	private final int WIDTH;
	private final int HEIGHT;
	private Controller receiver;
	private Stage primaryStage;
	private Scene primaryScene;
	private Pane primaryPane;
	private Button processBut;
	private Button nextBut;
	private Button prevBut;
	private ImageView imageView;
	private Label titleLab;
	private Label messageLab;
	private Line cropLine;
	private List<Circle> points;
	private List<Line> connectors;
	private RadioButton toggleAutoBut;
	private Rectangle opaqueSquare;
	private TextField widthTextField;
	private TextField heightTextField;

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
		this.titleLab.setId("labelID");
		this.titleLab.setLayoutX(6);
		this.titleLab.setLayoutY(6);
		this.primaryPane.getChildren().add(this.titleLab);

		// ImageView to show current frame
		this.imageView = new ImageView();
		this.imageView.setId("imageViewID");
		this.imageView.setLayoutX(15);
		this.imageView.setLayoutY(50);
		this.imageView.setFitHeight(400);
		this.imageView.setFitWidth(400);
		this.imageView.setPreserveRatio(false);
		this.imageView.setOnMouseClicked(
			event -> receiver.click((int) event.getX(), (int) event.getY())
		);
		this.primaryPane.getChildren().add(this.imageView);

		// Radio button to toggle automatic mode
		this.toggleAutoBut = new RadioButton("Toggle Auto");
		this.toggleAutoBut.setId("radioID");
		this.toggleAutoBut.setLayoutX(500);
		this.toggleAutoBut.setLayoutY(60);
		this.toggleAutoBut.setMinSize(160, 50);
		this.toggleAutoBut.setOnAction(
				event -> {
					if (this.toggleAutoBut.isSelected()){
						System.out.println("Radio button selected - enable auto");    // Controller reference here
					} else {
						System.out.println("Radio button deselected - disable auto"); // Controller reference here
					}
				});
		this.primaryPane.getChildren().add(this.toggleAutoBut);

		// Button for processing
		this.processBut = new Button("Process + Complete");
		this.processBut.setId("buttonID");
		this.processBut.setLayoutX(475);
		this.processBut.setLayoutY(120);
		this.processBut.setMinSize(160,50);
		this.processBut.setOnAction(
				event -> {
					receiver.process();
					receiver.complete();
				});
		this.primaryPane.getChildren().add(this.processBut);

		// Button for switching to previous frame
		this.prevBut = new Button("Previous");
		this.prevBut.setId("buttonID");
		this.prevBut.setLayoutX(475);
		this.prevBut.setLayoutY(180);
		this.prevBut.setMinSize(78,50);
		this.prevBut.setOnAction(
			event -> receiver.prevFrame()
		);
		this.primaryPane.getChildren().add(this.prevBut);

		// Button for switching to next frame
		this.nextBut = new Button("Next");
		this.nextBut.setId("buttonID");
		this.nextBut.setLayoutX(557);
		this.nextBut.setLayoutY(180);
		this.nextBut.setMinSize(78,50);
		this.nextBut.setOnAction(
			event -> receiver.nextFrame()
		);
		this.primaryPane.getChildren().add(this.nextBut);

		// Message area Label
		this.messageLab = new Label("Message area");
		this.messageLab.setId("labelID");
		this.messageLab.setMaxWidth(160);
		this.messageLab.setWrapText(true);
		this.messageLab.setLayoutX(475);
		this.messageLab.setLayoutY(300);
		this.primaryPane.getChildren().add(this.messageLab);

		// Points to be placed on the ImageView to visualise a click
		this.points = new ArrayList<>();

		// Lines to connect points during the labelling stage
		this.connectors = new ArrayList<>();

		// TextField for specifying resolution width
		this.widthTextField = new TextField();
		this.widthTextField.setId("textFieldID");
		this.widthTextField.setLayoutX(475);
		this.widthTextField.setLayoutY(240);
		this.widthTextField.setMinSize(5, 5);
		this.widthTextField.setMaxWidth(78);
		this.primaryPane.getChildren().add(this.widthTextField);

		// TextField for specifying resolution height
		this.heightTextField = new TextField();
		this.heightTextField.setId("textFieldID");
		this.heightTextField.setLayoutX(557);
		this.heightTextField.setLayoutY(240);
		this.heightTextField.setMinSize(5, 5);
		this.heightTextField.setMaxWidth(78);
		this.primaryPane.getChildren().add(this.heightTextField);

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
	 * @param x x co-ordinate of the user's click on the ImageView
	 * @param y y co-ordinate of the user's click on the ImageView
	 */
	public void drawPoint(int x, int y) {
		x += (int) this.imageView.getLayoutX();
		y += (int) this.imageView.getLayoutY();
		int pointNum = getPointNum();

		this.points.add(new Circle());
		this.points.get(pointNum).setId("pointID");
		this.points.get(pointNum).setRadius(7);
		this.points.get(pointNum).setCenterX(x);
		this.points.get(pointNum).setCenterY(y);
		if (pointNum > 0) {
			drawConnector();
		}
		this.primaryPane.getChildren().add(this.points.get(pointNum));
	}

	/**
	 * Draw multiple points on the ImageView.
	 * @param drawPoints An array of x and y co-ordinates for each point
	 */
	public void drawPoints(Point[] drawPoints) {
		for (Point point : drawPoints) {
			drawPoint(point.x, point.y);
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
	public void drawDiagonal(int ax, int ay) {
		int c = ay - ax;
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
	public void drawRectangle(int ax, int ay, int bx, int by) {
		ax += (int) this.imageView.getLayoutX();
		ay += (int) this.imageView.getLayoutY();
		bx += (int) this.imageView.getLayoutX();
		by += (int) this.imageView.getLayoutY();
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
	 * @return A Point containing the x and y properties for the resolution
	 */
	public Point getTarget() {
		try {
			int widthInt = Integer.parseInt(this.widthTextField.getText());
			int heightInt = Integer.parseInt(this.heightTextField.getText());
			System.out.println(widthInt + ", " + heightInt);
			return (new Point(widthInt, heightInt));
		} catch (NumberFormatException e) {
			throw new NumberFormatException();
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
}
