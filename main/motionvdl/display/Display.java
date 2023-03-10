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
	private Button completeBut;
	private Button nextBut;
	private Button prevBut;
	private ImageView imageView;
	private Label titleLab;
	private Label messageLab;
	private Line diagonalLine;
	private List<Circle> points;
	private List<Line> connectors;
	private Rectangle opaqueSquare;
	private TextField widthTextField;
	private TextField heightTextField;
	
	// Joseph 230305. the instantiation of stage would be best encapsulated 
	// inside the constructor here, since it seems that all 
	// instantiations of Display pass stage as new Stage()
	// 
	// Joseph 230305. actually it seems that MotionVDL.start() passes 
	// stage to Display as an argument, but I can't find a use of
	// start in the code so I'm not sure where that argument comes 
	// from, maybe you could explain this to me next meeting.

	// Henri 230305. Reconsidering, I will reprogram MotionVDL class
	// to account for a new display instead of default - makes
	// things easier to handle in that class also

	// Henri 230306. I have decided it would be more memory efficient to
	// keep the auto-generated default stage as the one that is sent.
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
		this.titleLab.setLayoutX(6);
		this.titleLab.setLayoutY(6);
		this.primaryPane.getChildren().add(this.titleLab);

		// ImageView to show current frame
		this.imageView = new ImageView();
		this.imageView.setLayoutX(15);
		this.imageView.setLayoutY(50);
		//this.imageView.setImage(new Image("motionvdl/display/images/javaIcon.png")); // Set image to frame
		this.imageView.setFitHeight(400);
		this.imageView.setFitWidth(400);
		this.imageView.setPreserveRatio(false);
		this.imageView.setOnMouseClicked(
			event -> receiver.click((int)event.getX(), (int)event.getY())
		);
		this.primaryPane.getChildren().add(imageView);

		// Button for processing
		this.processBut = new Button("Process");
		this.processBut.setLayoutX(475);
		this.processBut.setLayoutY(60);
		this.processBut.setMinSize(160,50);
		this.processBut.setOnAction(
			event -> receiver.process()
		);
		this.primaryPane.getChildren().add(this.processBut);

		// Button for completing
		this.completeBut = new Button("Complete");
		this.completeBut.setLayoutX(475);
		this.completeBut.setLayoutY(120);
		this.completeBut.setMinSize(160,50);
		this.completeBut.setOnAction(
			event -> receiver.complete()
		);
		this.primaryPane.getChildren().add(this.completeBut);

		// Button for switching to previous frame
		this.prevBut = new Button("Previous");
		this.prevBut.setLayoutX(475);
		this.prevBut.setLayoutY(180);
		this.prevBut.setMinSize(78,50);
		this.prevBut.setOnAction(
			event -> receiver.prevFrame()
		);
		this.primaryPane.getChildren().add(this.prevBut);

		// Button for switching to next frame
		this.nextBut = new Button("Next");
		this.nextBut.setLayoutX(557);
		this.nextBut.setLayoutY(180);
		this.nextBut.setMinSize(78,50);
		this.nextBut.setOnAction(
			event -> receiver.nextFrame()
		);
		this.primaryPane.getChildren().add(this.nextBut);

		// Message area Label
		this.messageLab = new Label("Message area");
		this.messageLab.setLayoutX(475);
		this.messageLab.setLayoutY(300);
		this.primaryPane.getChildren().add(this.messageLab);

		// Points to be placed on the ImageView to visualise a click
		this.points = new ArrayList<>();

		// Lines to connect points during the labelling stage
		this.connectors = new ArrayList<>();

		// TextField for specifying resolution width
		this.widthTextField = new TextField();
		this.widthTextField.setLayoutX(475);
		this.widthTextField.setLayoutY(240);
		this.widthTextField.setMinSize(5, 5);
		this.widthTextField.setMaxWidth(78);
		this.primaryPane.getChildren().add(this.widthTextField);

		// TextField for specifying resolution height
		this.heightTextField = new TextField();
		this.heightTextField.setLayoutX(557);
		this.heightTextField.setLayoutY(240);
		this.heightTextField.setMinSize(5, 5);
		this.heightTextField.setMaxWidth(78);
		this.primaryPane.getChildren().add(this.heightTextField);

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
	
	// Joseph 230307. I've tried running this with a noise video, at the moment I get two errors:
	// * error from javafx.scene.paint.Color about value ranges, awt.color uses 0-255 range and 
	//   javafx.scene.paint.Color uses 0.0-1.0 range. This is ok we just need to normalise the 
	//   colorArray color channels before converting to javafx.scene.paint.Color
	// * 'IndexOutOfBoundsException: 300, 0' from line 211 I haven't looked into this one 
	//   yet but it's probably just something to do with the argument order in the WritableImage 
	//   constructor.

	// Henri 230309. I've rewritten it to instead keep everything in Swing until
	// an awt Image has been created, which is then converted to a JavaFX image.
	// Hopefully this will work?
	public void setFrame(Color[][] colorArray) {
//		WritableImage frame = new WritableImage(colorArray.length, colorArray[0].length);
//		PixelWriter writer = frame.getPixelWriter();
//		for (int i = 0; i < colorArray.length; i++) {
//			for (int j = 0; j < colorArray[i].length; j++) {
//				// Convert each java.awt.Color object to a javafx.scene.paint.Color object
//				javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.color(
//						colorArray[i][j].getRed(),
//						colorArray[i][j].getGreen(),
//						colorArray[i][j].getBlue());
//
//				// Set each pixel's color
//				writer.setColor(j, i, fxColor);
//			}
//		}
//		this.imageView.setImage(frame);

		// Convert color array to awt.BufferImage
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

		width = bImage.getWidth();
		height = bImage.getHeight();
		byte[] buffer = new byte[width * height * 4];

		// Copy pixel data from BufferedImage to byte array
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
	
	public void setMessage(String string) {
		this.messageLab.setText(string);
	}

	public void drawPoint(int x, int y) {
		x += (int) this.imageView.getLayoutX();
		y += (int) this.imageView.getLayoutY();
		int pointNum = getPointNum();
		
		// Joseph 230305. getChildren() returns a type implementing ObservableList, which itself implements List
		// List has the method size() which returns the number of elements in the list so you could try 
		// size() to get the next index for the points array if you create a temporary variable to hold 
		// the list to call size() on

		// Henri 230305. Very clever - I hadn't even considered that and I probably never would have - this
		// makes it so much easier to draw the points and connectors, as I've done below, the
		// pointNum variable counts only the number of Circle objects which are currently on
		// screen, which is much better than storing a counter variable
		
		// Joseph 230306. The controller uses drawPoint in scenarios that do not always need a 
		// wireframe, for instance in the crop stage when defining the crop frame. because 
		// of this I think It might be best if we have seperate methods for drawing points 
		// and connectors
		
		// Henri 230306. I don't think the crop controller needs to place points, does it?
		// As it's already drawing other visual elements instead (diagonal line then square)
		
		// Joseph 230307. In the case of point B, (second click) where it's vertical axis is 
		// mapped to the unit square, it might be more intuitive for a user to see a point, but 
		// honestly that's pretty subjective. I would probably say that from program-design-wise, 
		// it's usually best to seperate different functionalities like these to make the code 
		// more modular

		// Henri 230307. Ok I see what you mean, now this method will simply create a new Circle
		// object any time it's called. The checks that there are a certain number of points being
		// placed, which I've commented out below, will need to be in the Controllers, for which
		// I've created a new method called getPointNum() which will return the number of points
		// currently placed on screen

//		if (pointNum < 11) {
		this.points.add(new Circle());
		this.points.get(pointNum).setId("pointID");
		this.points.get(pointNum).setRadius(7);
		this.points.get(pointNum).setCenterX(x);
		this.points.get(pointNum).setCenterY(y);
		if (pointNum > 0) {
			drawConnector();
		}
		this.primaryPane.getChildren().add(this.points.get(pointNum));
//			if (pointNum > 0) {
//				drawConnector(pointNum - 1);
//			}
//		} else {
//			setMessage("All points placed!");
//		}
	}

	public void drawPoints(Point[] drawPoints) {
		for (Point point : drawPoints) {
			drawPoint(point.x, point.y);
		}
	}

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
	
	public void drawDiagonal(int ax, int ay) {
		ax += (int) this.imageView.getLayoutX();
		ay += (int) this.imageView.getLayoutY();
		this.diagonalLine.setStartX(ax);
		this.diagonalLine.setStartY(ay);
		this.diagonalLine.setEndX(ax);
		this.diagonalLine.setEndY(ay);
		
		// Ensure line is within the bounds of the ImageView
		
		// Joseph 230306. This handling might not be neccesary 
		// if we implement with the assumption that clicks 
		// cannot come from invalid positions
		
		// Henri 230306. This isn't handling for the click location - it
		// is to ensure the line gets drawn only within the bounds of the
		// ImageView, by evenly increasing the line length until it touches
		// the bounds of the ImageView
		
		// Joseph 230307. Ah I understand sorry for misunderstanding.
		// In that case, I think there might be a more efficient way to do 
		// this using a non-linear function, let's discuss it next meeting.
		
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
		try {
			int widthInt = Integer.parseInt(this.widthTextField.getText());
			int heightInt = Integer.parseInt(this.heightTextField.getText());
			System.out.println(widthInt + ", " + heightInt);
			return (new Point(widthInt, heightInt));
		} catch (NumberFormatException e) {
			throw new NumberFormatException();
		}
	}

	public int getPointNum() {
		return  (int) this.primaryPane
				.getChildren()
				.stream()
				.filter(node -> node instanceof Circle)
				.count();
	}

}
