package motionvdl.display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import motionvdl.controller.MainController;
import motionvdl.model.data.Point;

/**
 * MotionVDL display component
 * @author Henri
 */
public class Display {

	public final int WIDTH;
	public final int HEIGHT;
	private double widthScaleFactor;
	private double heightScaleFactor;
	private MainController receiver;
	private final Stage primaryStage;
	private final Scene primaryScene;
	private final Pane primaryPane;
	private final Button processBut;
	private final Button nextBut;
	private final Button prevBut;
	private final ImageView imageView;
	private final Label nodeMessageLab;
	private final Label targetResLab;
	private final Label titleLab;
	private final List<Circle> points;
	private final List<Line> connectors;
	private final MenuBar menuBar;
	private final RadioButton radioBut;
	private final Slider sliderX;
	private final Slider sliderY;
	private final Slider sliderZoom;
	private final TextField resTextField;

	/**
	 * Display constructor.
	 * @param stage  Default Stage to be used by the Application
	 */
	public Display(Stage stage) {
		this.WIDTH = 675;
		this.HEIGHT = 500;
		this.widthScaleFactor = 1;
		this.heightScaleFactor = 1;
		this.primaryStage = stage;
		this.primaryPane = new Pane();
		this.primaryPane.setId("paneID");
		this.primaryScene = new Scene(this.primaryPane, this.WIDTH, this.HEIGHT);
		this.primaryScene.getStylesheets().add("motionvdl/display/res/mainStyle.css");

		// Title Label
		this.titleLab = new Label("Title");
		this.titleLab.setId("titleLabID");
		this.titleLab.setLayoutX(145);
		this.titleLab.setLayoutY(32);
		this.primaryPane.getChildren().add(this.titleLab);

		// ImageView to show current frame
		this.imageView = new ImageView();
		this.imageView.setId("cropImageViewID");
		this.imageView.setLayoutX(40);
		this.imageView.setLayoutY(65);
		this.imageView.setFitHeight(400);
		this.imageView.setFitWidth(400);
		this.imageView.setOnMouseClicked(
				event -> {
					// Left click - Pass to controller as point placement
					if (event.getButton() == MouseButton.PRIMARY) {
						receiver.click(event.getX() / this.imageView.getFitWidth(),
								event.getY() / this.imageView.getFitHeight());
					// Right click - Pass to controller as undo function
					} else if (event.getButton() == MouseButton.SECONDARY) {
						receiver.undo();
					}
				});
		this.primaryPane.getChildren().add(this.imageView);

		// X-axis directional crop Slider
		this.sliderX = new Slider();
		this.sliderX.setId("sliderID");
		this.sliderX.setLayoutX(40);
		this.sliderX.setLayoutY(475);
		this.sliderX.setMinWidth(400);
		this.sliderX.setMin(0);
		this.sliderX.setMax(0);
		this.sliderX.setTooltip(
				new Tooltip("Move X-Axis in frame.")
		);
		this.sliderX.valueProperty().addListener(
				event -> sliderChange("Horizontal")
		);

		// Y-axis directional crop Slider
		this.sliderY = new Slider();
		this.sliderY.setId("sliderID");
		this.sliderY.setOrientation(Orientation.VERTICAL);
		this.sliderY.setRotate(180);
		this.sliderY.setLayoutX(455);
		this.sliderY.setLayoutY(65);
		this.sliderY.setMinHeight(400);
		this.sliderY.setMin(0);
		this.sliderY.setMax(0);
		this.sliderY.setTooltip(
				new Tooltip("Move Y-Axis in frame.")
		);
		this.sliderY.valueProperty().addListener(
				event -> sliderChange("Vertical")
		);

		// Slider for changing zoom of cropping ViewPort
		this.sliderZoom = new Slider();
		this.sliderZoom.setId("sliderID");
		this.sliderZoom.setOrientation(Orientation.VERTICAL);
		this.sliderZoom.setLayoutX(15);
		this.sliderZoom.setLayoutY(65);
		this.sliderZoom.setMinHeight(400);
		this.sliderZoom.setMin(0);
		this.sliderZoom.setMax(0);
		this.sliderZoom.setTooltip(
				new Tooltip("Zoom into frame.")
		);
		this.sliderZoom.valueProperty().addListener(
				event -> sliderChange("Zoom")
		);

		// Radio button to toggle automatic mode
		this.radioBut = new RadioButton("Lock Min Res");
		this.radioBut.setId("radioID");
		this.radioBut.setLayoutX(500);
		this.radioBut.setLayoutY(100);
		this.radioBut.setMinSize(160, 50);
		this.radioBut.setTooltip(
				new Tooltip("Lock currently minimum specified res.")
		);
		this.primaryPane.getChildren().add(this.radioBut);

		// Button for processing
		this.processBut = new Button("Next stage");
		this.processBut.setId("buttonID");
		this.processBut.setLayoutX(480);
		this.processBut.setLayoutY(150);
		this.processBut.setMinSize(160,50);
		this.processBut.setTooltip(
				new Tooltip("Process and go to next stage?")
		);
		this.processBut.setOnAction(
				event -> receiver.complete()
		);
		this.primaryPane.getChildren().add(this.processBut);

		// Button for switching to previous frame
		this.prevBut = new Button("<-");
		this.prevBut.setId("buttonID");
		this.prevBut.setLayoutX(480);
		this.prevBut.setLayoutY(210);
		this.prevBut.setMinSize(78,50);
		this.prevBut.setTooltip(
				new Tooltip("Previous frame.")
		);
		this.prevBut.setOnAction(
				event -> receiver.setPrevFrame()
		);
		this.primaryPane.getChildren().add(this.prevBut);

		// Button for switching to next frame
		this.nextBut = new Button("->");
		this.nextBut.setId("buttonID");
		this.nextBut.setLayoutX(562);
		this.nextBut.setLayoutY(210);
		this.nextBut.setMinSize(78,50);
		this.nextBut.setTooltip(
				new Tooltip("Next frame.")
		);
		this.nextBut.setOnAction(
				event -> receiver.setNextFrame()
		);
		this.primaryPane.getChildren().add(this.nextBut);

		// Label to tell user what resTextField is for
		this.targetResLab = new Label("Target res:");
		this.targetResLab.setId("targetResLabID");
		this.targetResLab.setLayoutX(485);
		this.targetResLab.setLayoutY(275);
		this.primaryPane.getChildren().add(targetResLab);

		// TextField for specifying target resolution
		this.resTextField = new TextField();
		this.resTextField.setId("textFieldID");
		this.resTextField.setLayoutX(565);
		this.resTextField.setLayoutY(270);
		this.resTextField.setMinSize(5, 5);
		this.resTextField.setMaxWidth(70);
		this.resTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			// Check content of TextField is digits only, if not, disallow given input
			if (!newValue.matches("\\d*")) {
				this.resTextField.setText(newValue.replaceAll("\\D", ""));
			}
			// Make sure min res in TextField is kept in sync with crop frame
			if (!Objects.equals(this.resTextField.getText(), "") && getTarget() > this.sliderZoom.getValue() * widthScaleFactor) {
				this.resTextField.setText(Integer.toString((int) (this.sliderZoom.getValue() * widthScaleFactor)));
			}
		});
		this.primaryPane.getChildren().add(this.resTextField);

		// Message area Label
		this.nodeMessageLab = new Label();
		this.nodeMessageLab.setId("messageLabID");
		this.nodeMessageLab.setLayoutX(480);
		this.nodeMessageLab.setLayoutY(335);
		this.nodeMessageLab.setMaxWidth(160);
		this.nodeMessageLab.setWrapText(true);
		this.primaryPane.getChildren().add(this.nodeMessageLab);

		// Menu to allow for opening a file, and saving current labelling
		this.menuBar = new MenuBar();
		this.menuBar.setId("menuBarID");
		Menu fileMenu = new Menu("File");
		MenuItem open = new MenuItem("Open");
		open.setOnAction(event -> {
			// Show user their file system to allow file choice
			FileChooser fileChooser = new FileChooser();
			File fileChoice = fileChooser.showOpenDialog(this.primaryStage);
			if (fileChoice != null) {
				receiver.open(fileChoice.getPath());
			}
		});
		MenuItem save = new MenuItem("Save");
		save.setOnAction(event ->
				receiver.save()
		);
		MenuItem saveAs = new MenuItem("Save As");
		saveAs.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			File fileChoice = fileChooser.showOpenDialog(this.primaryStage);
			if (fileChoice != null) {
				receiver.saveAs(fileChoice.getPath());
			}
		});
		fileMenu.getItems().addAll(open, save, saveAs);
		this.menuBar.getMenus().add(fileMenu);
		this.menuBar.setMinWidth(this.WIDTH);
		this.primaryPane.getChildren().add(this.menuBar);

		// Points to be placed on the ImageView to visualise a click
		this.points = new ArrayList<>();

		// Lines to connect points during the labelling stage
		this.connectors = new ArrayList<>();

		// Details relating to the window itself
		this.primaryStage.setTitle("MotionVDL");
		this.primaryStage.getIcons().add(new Image("motionvdl/display/res/javaIcon.png"));
		this.primaryStage.setResizable(false);
		this.primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
		this.primaryStage.setScene(this.primaryScene);
		this.primaryPane.requestFocus();
		this.primaryStage.show();
	}

	/**
	 * Store a reference to the receiving controller.
	 * @param controller The controller reference
	 */
	public void sendTo(MainController controller) {
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
	 * @param nextNode Text to show the user
	 */
	public void setNodeMessage(String nextNode) {
		//TODO: Style this better
		this.nodeMessageLab.setText("Next node:\n" +  nextNode);
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
	 * Converts an array of AWT Colors to JavaFX Image, then sets
	 * the ImageView's Image property to display this frame.
	 * @param image AWT Image containing the current frame
	 */
	public void setFrame(java.awt.Image image) {
		Image FXImage = SwingFXUtils.toFXImage((BufferedImage) image, null);
		this.imageView.setImage(FXImage);

		// Upscale Image if required
		if (FXImage.getHeight() < this.imageView.getFitHeight() && FXImage.getWidth() < this.imageView.getFitWidth()) {
			this.imageView.setImage(upscaleFrame(FXImage));
		} else {
			this.widthScaleFactor = 1;
			this.heightScaleFactor = 1;
		}
	}

	/**
	 * Upscale a given Image when it is a lower base resolution than the ImageView.
	 * @param image Base Image to upscale
	 * @return An upscaled version of the same Image
	 */
	public Image upscaleFrame(Image image) {
		double scaleFactor = this.imageView.getFitHeight() / image.getHeight();
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		WritableImage scaledImage = new WritableImage((int) (width * scaleFactor), (int) (height * scaleFactor));
		PixelReader reader = image.getPixelReader();
		PixelWriter writer = scaledImage.getPixelWriter();

		// Loop through each pixel in the original image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int argb = reader.getArgb(x, y);
				// Loop through each scaled image pixel that corresponds to current original pixel
				for (int dy = 0; dy < scaleFactor; dy++) {
					for (int dx = 0; dx < scaleFactor; dx++) {
						writer.setArgb((int) (x * scaleFactor + dx), (int) (y * scaleFactor + dy), argb);
					}
				}
			}
		}

		this.widthScaleFactor = (double) width / scaledImage.getWidth();
		this.heightScaleFactor = (double) height / scaledImage.getHeight();
		return scaledImage;
	}

	/**
	 * Clear the ImageView.
	 */
	public void clearFrame() {
		this.imageView.setImage(null);
	}

	/**
	 * Sets the initial ViewPort based on the resolution of the Image.
	 */
	public void setViewPort() {
		// Set slider properties to default
		this.sliderX.setMin(0);
		this.sliderX.setMax(0);
		this.sliderY.setMin(0);
		this.sliderY.setMax(0);
		this.sliderZoom.setMin(0);
		this.sliderZoom.setMax(0);

		// Set initial required properties
		double imgWidth = this.imageView.getImage().getWidth();
		double imgHeight = this.imageView.getImage().getHeight();
		double maxSliderZoom = imgWidth;
		double sliderXMaxValue = 0.0;
		double sliderYMaxValue = 0.0;

		// Set required values with handling based on its orientation
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
		} else {
			// Square Image
			this.imageView.setViewport(new Rectangle2D(0, 0, imgWidth, imgHeight));
		}

		// Set common values
		this.sliderX.setMax(sliderXMaxValue);
		this.sliderX.setValue(this.sliderX.getMax() / 2);
		this.sliderY.setMax(sliderYMaxValue);
		this.sliderY.setValue(this.sliderY.getMax() / 2);
		this.sliderZoom.setMin(imgWidth * 0.1);
		this.sliderZoom.setMax(maxSliderZoom);
		this.sliderZoom.setValue(maxSliderZoom);

		// Set TextField value to be initially max possible res
		this.resTextField.setText(Integer.toString((int) (this.sliderZoom.getValue() * this.widthScaleFactor)));
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

				// Update the maximum values of the horizontal and vertical sliders
				this.sliderX.setMax(this.imageView.getImage().getWidth() - this.imageView.getViewport().getWidth());
				this.sliderY.setMax(this.imageView.getImage().getHeight() - this.imageView.getViewport().getHeight());

				// Adjust content of TextField accordingly to reflect zoom level change
				if (!this.resTextField.getText().equals("")) {
					if (!getRadio() || getTarget() >= this.sliderZoom.getValue() * this.widthScaleFactor) {
						this.resTextField.setText(Integer.toString((int) (this.sliderZoom.getValue() * this.widthScaleFactor)));
					}
				}
			}
		}
	}

	/**
	 * Take doubles between 0 and 1 and set the slider thumb positions relative to the given values.
	 * Primarily used for debugging and testing.
	 * @param x sliderX position
	 * @param y sliderY position
	 * @param z sliderZoom position
	 */
	public void setSliderValues(double x, double y, double z) {
		this.sliderZoom.setValue(z * this.sliderZoom.getMax());
		this.sliderX.setValue(x * this.sliderX.getMax());
		this.sliderY.setValue(y * this.sliderY.getMax());
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

		// Create new point and add it to point ArrayList
		this.points.add(new Circle());
		this.points.get(getPointNum()).setId("pointID");
		this.points.get(getPointNum()).setRadius(7);
		this.points.get(getPointNum()).setCenterX(x);
		this.points.get(getPointNum()).setCenterY(y);

		// Begin drawing connectors between points after second point is added
		if (getPointNum() > 0) drawConnector();

		// Add newly created point to ImageView
		this.primaryPane.getChildren().add(this.points.get(getPointNum()));
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
		int drawnPointNum = getPointNum() - 1;
		this.connectors.add(new Line());
		this.connectors.get(drawnPointNum).setId("lineID");

		// Set the start and end points of the Line
		BiConsumer<Integer, Integer> setPoints = (start, end) -> {
			this.connectors.get(drawnPointNum).setStartX(this.points.get(start).getCenterX());
			this.connectors.get(drawnPointNum).setStartY(this.points.get(start).getCenterY());
			this.connectors.get(drawnPointNum).setEndX(this.points.get(end).getCenterX());
			this.connectors.get(drawnPointNum).setEndY(this.points.get(end).getCenterY());
		};

		// Draw the Line based on which point is being placed
		switch (drawnPointNum) {
			default -> setPoints.accept(drawnPointNum, drawnPointNum + 1);

			// Special cases for points which don't originate from previous point
			case 3, 5 -> setPoints.accept(1, drawnPointNum + 1);
			case 8 -> setPoints.accept(6, drawnPointNum + 1);
		}
		this.primaryPane.getChildren().add(this.connectors.get(drawnPointNum));
	}

	/**
	 * Clear any Line and Circle objects currently on the Pane.
	 */
	public void clearGeometry() {
		this.primaryPane.getChildren().removeAll(this.points);
		this.primaryPane.getChildren().removeAll(this.connectors);
	}

	/**
	 * Change scene layout for preprocessing stage.
	 */
	public void alterForPreprocessing() {
		this.primaryPane.getChildren().addAll(this.sliderX, this.sliderY, this.sliderZoom);
		this.imageView.setId("cropImageViewID");
		this.radioBut.setText("Lock Min Res");
		this.radioBut.setTooltip(
				new Tooltip("Lock currently minimum specified res.")
		);
		this.radioBut.setSelected(false);
		this.primaryPane.requestFocus();
	}

	/**
	 * Change scene layout for labelling stage.
	 */
	public void alterForLabelling() {
		this.primaryPane.getChildren().removeAll(this.sliderX, this.sliderY, this.sliderZoom);
		this.imageView.setViewport(null);
		this.imageView.setId("labelImageViewID");
		this.radioBut.setText("Toggle Auto");
		this.radioBut.setTooltip(
				new Tooltip("Enable to automatically move to next\n" +
						"frame when all labels are placed.")
		);
		this.radioBut.setSelected(false);
		this.primaryPane.requestFocus();
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
		return this.radioBut.isSelected();
	}

	/**
	 * Returns the top-left x and y co-ordinates of the crop frame,
	 * as well as the width of crop frame.
	 * @return Current crop frame co-ordinates
	 */
	public int[] getCropFrame() {
		int x = (int) (this.imageView.getViewport().getMinX() * widthScaleFactor);
		int y = (int) (this.imageView.getViewport().getMinY() * heightScaleFactor);
		int z = (int) (this.imageView.getViewport().getWidth() * widthScaleFactor);
		return new int[]{x, y, z};
	}

	/**
	 * Close the currently open application instance.
	 */
	public void exit() {
		this.primaryStage.close();
	}
}
