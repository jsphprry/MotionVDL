package motionvdl.display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
	private final List<Circle> points;
	private final List<Line> connectors;
	private final RadioButton toggleAutoBut;
	private final Slider sliderX;
	private final Slider sliderY;
	private final Slider sliderZoom;
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
		this.imageView.setLayoutX(40);
		this.imageView.setLayoutY(40);
		this.imageView.setFitHeight(400);
		this.imageView.setFitWidth(400);
		this.imageView.setOnMouseClicked(
				event -> {
					if (event.getButton() == MouseButton.PRIMARY) {
						receiver.click(event.getX() / this.imageView.getFitWidth(),
										event.getY() / this.imageView.getFitHeight());
					} else if (event.getButton() == MouseButton.SECONDARY) {
						System.out.println("UNDO FUNCTION");
					}
				});
		this.primaryPane.getChildren().add(this.imageView);

		// X-axis directional crop Slider
		this.sliderX = new Slider();
		this.sliderX.setId("sliderID");
		this.sliderX.setLayoutX(40);
		this.sliderX.setLayoutY(450);
		this.sliderX.setMinWidth(400);
		this.sliderX.setMin(0);
		this.sliderX.setMax(0);
		this.sliderX.valueProperty().addListener(
				event -> sliderChange("Horizontal")
		);
		this.primaryPane.getChildren().add(this.sliderX);

		// Y-axis directional crop Slider
		this.sliderY = new Slider();
		this.sliderY.setId("sliderID");
		this.sliderY.setOrientation(Orientation.VERTICAL);
		this.sliderY.setRotate(180);
		this.sliderY.setLayoutX(455);
		this.sliderY.setLayoutY(40);
		this.sliderY.setMinHeight(400);
		this.sliderY.setMin(0);
		this.sliderY.setMax(0);
		this.sliderY.valueProperty().addListener(
				event -> sliderChange("Vertical")
		);
		this.primaryPane.getChildren().add(this.sliderY);

		// Slider for changing zoom of cropping ViewPort
		this.sliderZoom = new Slider();
		this.sliderZoom.setId("sliderID");
		this.sliderZoom.setOrientation(Orientation.VERTICAL);
		this.sliderZoom.setLayoutX(15);
		this.sliderZoom.setLayoutY(40);
		this.sliderZoom.setMinHeight(400);
		this.sliderZoom.setMin(0);
		this.sliderZoom.setMax(0);
		this.sliderZoom.valueProperty().addListener(
				event -> sliderChange("Zoom")
		);
		this.primaryPane.getChildren().add(this.sliderZoom);

		// Radio button to toggle automatic mode
		this.toggleAutoBut = new RadioButton("Toggle Auto");
		this.toggleAutoBut.setId("radioID");
		this.toggleAutoBut.setLayoutX(500);
		this.toggleAutoBut.setLayoutY(40);
		this.toggleAutoBut.setMinSize(160, 50);
		this.toggleAutoBut.setTooltip(
				new Tooltip("Enable to automatically move to next\n" +
							"frame when all labels are placed.")
		);
		this.primaryPane.getChildren().add(this.toggleAutoBut);

		// Button for processing
		this.processBut = new Button("Next stage");
		this.processBut.setId("buttonID");
		this.processBut.setLayoutX(480);
		this.processBut.setLayoutY(90);
		this.processBut.setMinSize(160,50);
		this.processBut.setOnAction(
				event -> receiver.next()
		);
		this.primaryPane.getChildren().add(this.processBut);

		// Button for undoing an incorrect point placement
		this.undoBut = new Button("Undo");
		this.undoBut.setId("buttonID");
		this.undoBut.setLayoutX(480);
		this.undoBut.setLayoutY(150);
		this.undoBut.setMinSize(160, 50);
		this.undoBut.setOnAction(
				event -> receiver.undo()
		);
		this.primaryPane.getChildren().add(this.undoBut);

		// Button for switching to previous frame
		this.prevBut = new Button("<-");
		this.prevBut.setId("buttonID");
		this.prevBut.setLayoutX(480);
		this.prevBut.setLayoutY(210);
		this.prevBut.setMinSize(78,50);
		this.prevBut.setOnAction(
				event -> receiver.down()
		);
		this.primaryPane.getChildren().add(this.prevBut);

		// Button for switching to next frame
		this.nextBut = new Button("->");
		this.nextBut.setId("buttonID");
		this.nextBut.setLayoutX(562);
		this.nextBut.setLayoutY(210);
		this.nextBut.setMinSize(78,50);
		this.nextBut.setOnAction(
				event -> receiver.up()
		);
		this.primaryPane.getChildren().add(this.nextBut);

		// TextField for specifying target resolution
		this.resTextField = new TextField();
		this.resTextField.setId("textFieldID");
		this.resTextField.setLayoutX(525);
		this.resTextField.setLayoutY(270);
		this.resTextField.setMinSize(5, 5);
		this.resTextField.setMaxWidth(70);
		this.resTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				this.resTextField.setText(newValue.replaceAll("\\D", ""));
			}
			if (!Objects.equals(this.resTextField.getText(), "") && getTarget() > this.sliderZoom.getValue()) {
				this.resTextField.setText(Integer.toString((int) this.sliderZoom.getValue()));
			}
		});
		this.primaryPane.getChildren().add(this.resTextField);

		// Message area Label
		this.messageLab = new Label("Message area");
		this.messageLab.setId("messageLabID");
		this.messageLab.setLayoutX(480);
		this.messageLab.setLayoutY(310);
		this.messageLab.setMaxWidth(160);
		this.messageLab.setWrapText(true);
		this.primaryPane.getChildren().add(this.messageLab);

		// Points to be placed on the ImageView to visualise a click
		this.points = new ArrayList<>();

		// Lines to connect points during the labelling stage
		this.connectors = new ArrayList<>();

		// Details relating to window itself
		this.primaryStage.setTitle("MotionVDL");
		this.primaryStage.getIcons().add(new Image("motionvdl/display/images/javaIcon.png"));
		this.primaryStage.setResizable(false);
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
	 * Send the user a message, using a Label.
	 * @param string Text to show the user
	 */
	public void setMessage(String string) {
		this.messageLab.setText(string);
	}

	/**
	 * Send the user a warning, using an Alert.
	 * @param string Text to show the user
	 */
	public void sendAlert(String string) {
		Alert alert = new Alert(Alert.AlertType.WARNING, string, ButtonType.OK);
		alert.setTitle("Warning!");
		alert.setHeaderText(null);
		alert.setResizable(true);
		alert.getDialogPane().setPrefSize(250, 100);
		alert.showAndWait();
	}

	/**
	 * Convert an array of AWT Colors to JavaFX Image, then set
	 * the ImageView's Image property to display this frame.
	 * @param colorArray Array of colors, containing the current frame
	 */
	public void setFrame(Color[][] colorArray) {
		Function<Color, javafx.scene.paint.Color> convertColor = color ->
				javafx.scene.paint.Color.rgb(color.getRed(), color.getGreen(), color.getBlue());

		int height = colorArray.length;
		int width = colorArray[0].length;

		// Create JavaFX WritableImage and write pixel data
		WritableImage wImage = new WritableImage(width, height);
		PixelWriter pixelWriter = wImage.getPixelWriter();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = colorArray[y][x];
				pixelWriter.setColor(x, y, convertColor.apply(color));
			}
		}
		this.imageView.setImage(wImage);

		// Might need to be used - don't know yet

//		double scaleFactor;
//		if (wImage.getHeight() < imageView.getFitHeight() && wImage.getWidth() < imageView.getFitWidth()) {
//			// Upscale Image
//			scaleFactor = this.imageView.getFitHeight() / wImage.getHeight();
//			width = (int) wImage.getWidth();
//			height = (int) wImage.getHeight();
//			WritableImage scaledWImage = new WritableImage((int) (width * scaleFactor), (int) (height * scaleFactor));
//			PixelReader reader = wImage.getPixelReader();
//			PixelWriter writer = scaledWImage.getPixelWriter();
//
//			for (int y = 0; y < height; y++) {
//				for (int x = 0; x < width; x++) {
//					int argb = reader.getArgb(x, y);
//					for (int dy = 0; dy < scaleFactor; dy++) {
//						for (int dx = 0; dx < scaleFactor; dx++) {
//							writer.setArgb((int) (x * scaleFactor + dx), (int) (y * scaleFactor + dy), argb);
//						}
//					}
//				}
//			}
//			this.imageView.setImage(scaledWImage);}
//		} else if (wImage.getHeight() > imageView.getFitHeight() && wImage.getWidth() > imageView.getFitWidth()) {
//			// Downscale image
//			this.imageView.setImage(wImage);
//		} else {
//			this.imageView.setImage(wImage);
//		}
	}

	/**
	 * Sets the initial ViewPort based on the resolution of the Image
	 */
	public void setViewPort() {
		this.sliderX.setMin(0);
		this.sliderX.setMax(0);
		this.sliderY.setMin(0);
		this.sliderY.setMax(0);
		this.sliderZoom.setMin(0);
		this.sliderZoom.setMax(0);

		double imgWidth = this.imageView.getImage().getWidth();
		double imgHeight = this.imageView.getImage().getHeight();
		double maxSliderZoom;
		double sliderXMaxValue = 0.0;
		double sliderYMaxValue = 0.0;

		// Set the viewport of the image based on its orientation
		if (imgWidth > imgHeight) {
			// Landscape Image
			this.imageView.setViewport(new Rectangle2D((imgWidth / 2) - (imgHeight / 2), 0, imgHeight, imgHeight));
			sliderXMaxValue = imgWidth - this.imageView.getViewport().getWidth();
			sliderYMaxValue = 0.0;
			maxSliderZoom = imgHeight;
		} else if (imgWidth < imgHeight) {
			// Portrait Image
			this.imageView.setViewport(new Rectangle2D(0, (imgHeight / 2) - (imgWidth / 2), imgWidth, imgWidth));
			sliderXMaxValue = 0.0;
			sliderYMaxValue = imgHeight - this.imageView.getViewport().getHeight();
			maxSliderZoom = imgWidth;
		} else {
			// Square Image
			this.imageView.setViewport(new Rectangle2D(0, 0, imgWidth, imgHeight));
			maxSliderZoom = imgWidth;
		}
		this.sliderX.setMax(sliderXMaxValue);
		this.sliderX.setValue(this.sliderX.getMax() / 2);
		this.sliderY.setMax(sliderYMaxValue);
		this.sliderY.setValue(this.sliderY.getMax() / 2);
		this.sliderZoom.setMin(imgWidth * 0.1);
		this.sliderZoom.setMax(maxSliderZoom);
		this.sliderZoom.setValue(maxSliderZoom);

		this.resTextField.setText(Integer.toString((int) this.sliderZoom.getValue()));

	}

	/**
	 * Changes the current state of the ViewPort based on a Slider's movement.
	 * @param slider Which slider's value has changed
	 */
	public void sliderChange(String slider) {
		switch (slider) {
			case "Horizontal" -> this.imageView.setViewport(
					new Rectangle2D(this.sliderX.getValue(),
							this.imageView.getViewport().getMinY(),
							this.imageView.getViewport().getWidth(),
							this.imageView.getViewport().getHeight()));

			case "Vertical" -> this.imageView.setViewport(
					new Rectangle2D(this.imageView.getViewport().getMinX(),
							this.sliderY.getValue(),
							this.imageView.getViewport().getWidth(),
							this.imageView.getViewport().getHeight()));

			case "Zoom" -> { this.imageView.setViewport(
					new Rectangle2D(this.sliderX.getValue(),
							this.sliderY.getValue(),
							this.sliderZoom.getValue(),
							this.sliderZoom.getValue()));
				this.sliderX.setMax(this.imageView.getImage().getWidth() - this.imageView.getViewport().getWidth());
				this.sliderY.setMax(this.imageView.getImage().getHeight() - this.imageView.getViewport().getHeight());
				if (!Objects.equals(this.resTextField.getText(), "") && getTarget() >= sliderZoom.getValue()) {
					this.resTextField.setText(Integer.toString((int) this.sliderZoom.getValue()));
				}
			}
		}
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
		if (pointNum > 0) drawConnector();
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
		BiConsumer<Integer, Integer> setPoints = (start, end) -> {
			this.connectors.get(pointNum).setStartX(this.points.get(start).getCenterX());
			this.connectors.get(pointNum).setStartY(this.points.get(start).getCenterY());
			this.connectors.get(pointNum).setEndX(this.points.get(end).getCenterX());
			this.connectors.get(pointNum).setEndY(this.points.get(end).getCenterY());
		};
		switch (pointNum) {
			default -> setPoints.accept(pointNum, pointNum + 1);
			case 3, 5 -> setPoints.accept(1, pointNum + 1);
			case 8 -> setPoints.accept(6, pointNum + 1);
		}
		this.primaryPane.getChildren().add(this.connectors.get(pointNum));
	}

	/**
	 * Clear all Lines, Circles, and Rectangle objects from the Pane.
	 */
	public void clearGeometry() {
		// Even if these nodes haven't yet been added to the Pane, this will still work
		this.primaryPane.getChildren().removeAll(this.points);
		this.primaryPane.getChildren().removeAll(this.connectors);
	}

	/**
	 * Change scene layout for labelling stage.
	 */
	public void alterForLabelling() {
		this.primaryPane.getChildren().removeAll(sliderX, sliderY, sliderZoom);
		this.imageView.setViewport(null);
	}

	/**
	 * Process a user's input for target resolution.
	 * @return An int specifying the target resolution
	 */
	public int getTarget() {
		return Integer.parseInt(this.resTextField.getText());
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
	 * If the radio button is selected, returns true. If not, returns false.
	 * @return Current state of auto radio button
	 */
	public boolean getRadio() {
		return this.toggleAutoBut.isSelected();
	}

	/**
	 * Returns the top-left x and y co-ordinates of the crop frame,
	 * as well as the width of crop frame.
	 * @return Current crop frame co-ordinates
	 */
	public int[] getCropFrame() {
		int x = (int) (this.imageView.getViewport().getMinX());
		int y = (int) (this.imageView.getViewport().getMinY());
		int z = (int) (this.imageView.getViewport().getWidth());
		return new int[]{x, y, z};
	}
}
