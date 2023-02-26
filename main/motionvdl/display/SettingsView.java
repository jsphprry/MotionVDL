package motionvdl.display;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * The Settings View displays a settings page,
 * used for adjusting frame settings.
 * @author Henri
 */
public class SettingsView {
    private static final Pane pane = new Pane();
    private static Stage primaryStage = null;
    private static Scene settingsScene = null;
    private static final Label titleLab = new Label();
    private static final Button inputSceneBut = new Button();
    private static final Button sendResBut = new Button();
    private static final Button resetCropBut = new Button();
    private static final Button showCropBut = new Button();
    private static final ImageView imageView = new ImageView();
    private static final TextField widthTextField = new TextField();
    private static final TextField heightTextField = new TextField();
    private static final Line[] cropLine = new Line[4];
    private static Line diagonalLine = new Line();
    private static String cropState = "Not";
    private static Rectangle opaqueSquare;

    /**
     * Initialise the nodes of this Scene.
     */
    public static void initialise(Stage stage) {
        // Title Label
        titleLab.setLayoutX(5);
        titleLab.setLayoutY(5);
        titleLab.setText("Settings. \n" +
                "This page can be used to \n" +
                "adjust settings of the video \n" +
                "to be used (e.g. cropping).");
        pane.getChildren().add(titleLab);

        // ImageView to show frame example
        imageView.setLayoutX(280);
        imageView.setLayoutY(10);
        imageView.setImage(new Image("motionvdl/display/images/javaIcon.png")); // Set image to first frame as reference
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(false);
        imageView.setOnMouseClicked(
                event -> drawCrop((int) event.getX(), (int) event.getY()) );
        pane.getChildren().add(imageView);

        // Button for resetting a given crop
        resetCropBut.setLayoutX(730);
        resetCropBut.setLayoutY(350);
        resetCropBut.setMinSize(120, 60);
        resetCropBut.setText("Reset Crop");
        resetCropBut.setOnAction(
                actionEvent -> resetCrop() );
        pane.getChildren().add(resetCropBut);

        // Button for visualising a given crop
        showCropBut.setLayoutX(730);
        showCropBut.setLayoutY(425);
        showCropBut.setMinSize(120, 60);
        showCropBut.setText("Show crop");
        showCropBut.setOnAction(
                actionEvent -> showCrop() );
        pane.getChildren().add(showCropBut);

        // Button for switching to InputView
        inputSceneBut.setLayoutX(420);
        inputSceneBut.setLayoutY(460);
        inputSceneBut.setMinSize(120, 60);
        inputSceneBut.setText("Confirm Settings");
        inputSceneBut.setOnAction(
                actionEvent -> InputView.changeScene() );
        pane.getChildren().add(inputSceneBut);

        // TextField for specifying resolution width
        widthTextField.setLayoutX(720);
        widthTextField.setLayoutY(150);
        widthTextField.setMinSize(5, 5);
        widthTextField.setMaxWidth(60);
        pane.getChildren().add(widthTextField);

        // TextField for specifying resolution height
        heightTextField.setLayoutX(795);
        heightTextField.setLayoutY(150);
        heightTextField.setMinSize(5, 5);
        heightTextField.setMaxWidth(60);
        pane.getChildren().add(heightTextField);

        // Button for confirming and checking values in TextFields above
        sendResBut.setLayoutX(730);
        sendResBut.setLayoutY(220);
        sendResBut.setMinSize(120, 60);
        sendResBut.setText("Confirm Values");
        sendResBut.setOnAction(
                actionEvent -> sendTargetRes() );
        pane.getChildren().add(sendResBut);

        pane.setId("paneID");
        settingsScene = new Scene(pane, 16*60, 9*60);
        settingsScene.getStylesheets().add("motionvdl/display/styleSheets/settingsStyle.css");
        primaryStage = stage;
    }

    /**
     * Send the target resolution.
     */
    public static void sendTargetRes() {
        // Get target resolution from two text fields and send to Display
        try {
            int widthInt = Integer.parseInt(widthTextField.getText());
            int heightInt = Integer.parseInt(heightTextField.getText());
            System.out.println(widthInt + ", " + heightInt);
        } catch (NumberFormatException e) {
            System.out.println("Not valid - " + e);
        }
    }

    /**
     * Visualise the crop bounds on the frame.
     *  @param x horizontal co-ordinate of the users click.
     *  @param y vertical co-ordinate of the user's click.
     */
    public static void drawCrop(int x, int y) {
        // Get position of the click on the ImageView
        x = (int) imageView.getLayoutX() + x;
        y = (int) imageView.getLayoutY() + y;

        // If no corner crop point has been placed
        if (cropState.equals("Not")) {

            // Create visual crop lines for the top-left corner
            cropLine[0] = new Line(x, y, x, y+70);
            cropLine[0].setId("cropLine");
            cropLine[1] = new Line(x, y, x+70, y);
            cropLine[1].setId("cropLine");
            pane.getChildren().addAll(cropLine[0], cropLine[1]);

            // Create diagonal line and ensure it is within the bounds of the ImageView
            diagonalLine = new Line(x, y, x, y);
            while(diagonalLine.getStartX() != imageView.getLayoutX()
                    && diagonalLine.getStartY() != imageView.getLayoutY()) {
                diagonalLine.setStartX(diagonalLine.getStartX()-1);
                diagonalLine.setStartY(diagonalLine.getStartY()-1);
            }
            while(diagonalLine.getEndX() != (imageView.getLayoutX() + imageView.getFitWidth())
                    && diagonalLine.getEndY() != (imageView.getLayoutY() + imageView.getFitHeight())) {
                diagonalLine.setEndX(diagonalLine.getEndX()+1);
                diagonalLine.setEndY(diagonalLine.getEndY()+1);
            }
            pane.getChildren().add(diagonalLine);

            // Change crop state and initialise the square's top-left corner
            cropState = "Drawn";
            opaqueSquare = new Rectangle();
            opaqueSquare.setId("opaqueSquare");
            opaqueSquare.setLayoutX(x);
            opaqueSquare.setLayoutY(y);

        // If a corner crop point has been placed
        } else if (cropState.equals("Drawn")) {

            // Set the bounds of the square and ensure it is within the bounds of the ImageView
            opaqueSquare.setWidth(x - opaqueSquare.getLayoutX());
            opaqueSquare.setHeight(y - opaqueSquare.getLayoutY());
            boolean inBounds = true;
            if (opaqueSquare.getHeight() > 0) {
                while(opaqueSquare.getWidth() != opaqueSquare.getHeight()) {
                    if ((opaqueSquare.getWidth() + opaqueSquare.getLayoutX())
                            >= (imageView.getLayoutX() + imageView.getFitWidth())) {
                        inBounds = false;
                        opaqueSquare = null;
                        break;
                    }
                    if (opaqueSquare.getWidth() < opaqueSquare.getHeight()) {
                        opaqueSquare.setWidth(opaqueSquare.getWidth() + 1);
                    } else if (opaqueSquare.getWidth() > opaqueSquare.getHeight()) {
                        opaqueSquare.setWidth(opaqueSquare.getWidth() - 1);
                    }
                }
                if (inBounds) {

                    // Create two new visual crop lines for the bottom-right corner
                    int cropLineOriginX = (int) opaqueSquare.getLayoutX() + (int) opaqueSquare.getWidth();
                    int cropLineOriginY = (int) opaqueSquare.getLayoutY() + (int) opaqueSquare.getHeight();
                    cropLine[2] = new Line(cropLineOriginX, cropLineOriginY, cropLineOriginX, cropLineOriginY-70);
                    cropLine[2].setId("cropLine");
                    cropLine[3] = new Line(cropLineOriginX, cropLineOriginY, cropLineOriginX-70, cropLineOriginY);
                    cropLine[3].setId("cropLine");
                    pane.getChildren().addAll(cropLine[2], cropLine[3]);

                    // Add the crop square and remove the diagonal line as it is no longer needed
                    pane.getChildren().add(opaqueSquare);
                    pane.getChildren().remove(diagonalLine);
                    diagonalLine = null;
                    cropState = "Cropped";
                } else {
                    System.out.println("Not a valid crop point!");
                }
            } else {
                System.out.println("Not a valid crop point!");
            }
        }
    }

    /**
     * Once the user has made a crop frame, this method can
     * be invoked to show the new cropped frame.
     */
    public static void showCrop() {
        if (cropState.equals("Cropped")) {
            pane.getChildren().remove(opaqueSquare);
            pane.getChildren().removeAll(cropLine);

            // Calculations need to be done to consider the image's actual dimensions
            PixelReader reader = imageView.getImage().getPixelReader();
            float resX = (float) (opaqueSquare.getLayoutX() - imageView.getLayoutX()) / (float) imageView.getFitWidth();
            float resY = (float) (opaqueSquare.getLayoutY() - imageView.getLayoutY()) / (float) imageView.getFitHeight();
            int x = (int) (imageView.getImage().getWidth() * resX);
            int y = (int) (imageView.getImage().getHeight() * resY);
            float resWidth = (float) opaqueSquare.getWidth() / (float) imageView.getFitWidth();
            float resHeight = (float) opaqueSquare.getHeight() / (float) imageView.getFitHeight();
            int width = (int) ((float) imageView.getImage().getWidth() * resWidth);
            int height = (int) ((float) imageView.getImage().getHeight() * resHeight);
            WritableImage newImage = new WritableImage(reader, x, y, width, height);
            imageView.setImage(newImage);
        }
    }

    /**
     * Reset the crop bounds to be redone.
     */
    public static void resetCrop() {
        if (!cropState.equals("Not")) {
            if (cropState.equals("Drawn")) {
                pane.getChildren().remove(diagonalLine);
                diagonalLine = null;
            } else if (cropState.equals("Cropped")) {
                pane.getChildren().remove(opaqueSquare);
                opaqueSquare = null;
            }
            pane.getChildren().removeAll(cropLine);
            Arrays.fill(cropLine, null);
            imageView.setImage(new Image("motionvdl/display/images/javaIcon.png"));
            cropState = "Not";
        }
    }

    /**
     * The method called to make this Scene active on the Stage.
     */
    public static void changeScene() {
        primaryStage.setScene(settingsScene);
    }
}
