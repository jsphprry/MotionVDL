package motionvdl.display;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * The Input View displays the Scene in which the user
 * will place points on each frame of the video.
 * @author Henri
 */
public class InputView {
    private static final Pane pane = new Pane();
    private static Stage primaryStage = null;
    private static Scene inputScene = null;
    private static final Label titleLab = new Label();
    private static final Label nextInputLab = new Label();
    private static final Button confirmBut = new Button();
    private static final ImageView imageView = new ImageView();
    private static Image imageToLabel = null;
    private static final Circle[] points = new Circle[10];
    private static int pointCount = 0;
    private static final String[] nextLocation = {
            "Head", "Chest", "L-Elbow", "L-Hand", "R-Elbow", "R-Hand", "L-Knee", "L-Foot", "R-Knee", "R-Foot", "Done"
    };

    /**
     * Initialise the nodes of this Scene.
     */
    public static void initialise(Stage stage) {
        // Title Label
        titleLab.setLayoutX(5);
        titleLab.setLayoutY(5);
        titleLab.setText("Input. \n" +
                "This page will be used for \n" +
                "placing points on each frame \n" +
                "of the video.");
        pane.getChildren().add(titleLab);

        // Label for indicating where to place the next point
        nextInputLab.setLayoutX(5);
        nextInputLab.setLayoutY(400);
        nextInputLab.setText("Please place point on: " + nextLocation[pointCount]);
        pane.getChildren().add(nextInputLab);

        // Button for confirming 10 points have been placed
        confirmBut.setLayoutX(420);
        confirmBut.setLayoutY(460);
        confirmBut.setMinSize(120, 60);
        confirmBut.setText("Confirm");
        confirmBut.setOnAction(
                actionEvent -> setFrame() );
        pane.getChildren().add(confirmBut);

        // ImageView for displaying the frame, and handling point placement
        imageToLabel = new Image("motionvdl/display/images/javaIcon.png");
        imageView.setLayoutX(280);
        imageView.setLayoutY(10);
        imageView.setImage(imageToLabel);
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(false);
        imageView.setOnMouseClicked(
                event -> setPoint((int) event.getX(), (int) event.getY()) );
        pane.getChildren().add(imageView);

        pane.setId("paneID");
        inputScene = new Scene(pane, 16*60, 9*60);
        inputScene.getStylesheets().add("motionvdl/display/styleSheets/inputStyle.css");
        primaryStage = stage;
    }

    /**
     * Sets a new frame, or sends the user back to the
     * welcome screen if there are no frames remaining.
     */
    public static void setFrame() {
        if (pointCount == 10) {
            removePoints();
            try {
                // Get new frame
                imageToLabel = new Image("motionvdl/display/images/javaIcon.png");
            } catch(Exception e) {
                // Do something if no frames remaining
                WelcomeView.changeScene();
            }
        } else {
            System.out.println("Not all points placed");
        }
    }

    /**
     * Sets a point on the ImageView where the user clicked.
     * @param x horizontal co-ordinate of the users click.
     * @param y vertical co-ordinate of the user's click.
     */
    public static void setPoint(int x, int y) {
        if (pointCount < 10) {
            points[pointCount] = new Circle(imageView.getLayoutX() + x, imageView.getLayoutY() + y, 7);
            points[pointCount].setId("pointID");
            pane.getChildren().add(points[pointCount]);

            System.out.println(x +", " + y);
            System.out.println("Point added: " + nextLocation[pointCount]);
            pointCount++;
            nextInputLab.setText("Please place point on: " + nextLocation[pointCount]);
        } else {
            System.out.println("All points placed!");
        }
    }

    /**
     * Reset all points on the ImageView.
     */
    public static void removePoints() {
        for (int i = 0; i < points.length; i++) {
            pane.getChildren().remove(points[i]);
            points[i] = null;
        }
        pointCount = 0;
        nextInputLab.setText("Please place point on: " + nextLocation[pointCount]);
    }

    /**
     * The method called to make this Scene active on the Stage.
     */
    public static void changeScene() {
        primaryStage.setScene(inputScene);
    }
}