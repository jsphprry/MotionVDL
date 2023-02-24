package motionvdl.display;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.*;

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
    private static final Circle[] points = new Circle[11];
    private static int pointCount = 0;
    private static final Point[] pointLoc = new Point[11];
    private static final Line[] connectors = new Line[10];
    private static final String[] nextLocation = {
            "Head", "Chest", "L-Elbow", "L-Hand", "R-Elbow", "R-Hand", "Torso", "L-Knee", "L-Foot", "R-Knee", "R-Foot", "Done"
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
                event -> setPointAndLine((int) event.getX(), (int) event.getY()) );
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
        if (pointCount == 11) {
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
     * Sets a point and a connection to the previous point
     * on the ImageView where the user clicked.
     * @param x horizontal co-ordinate of the users click.
     * @param y vertical co-ordinate of the user's click.
     */
    public static void setPointAndLine(int x, int y) {
        if (pointCount < 11) {
            pointLoc[pointCount] = new Point((int) imageView.getLayoutX() + x, (int) imageView.getLayoutY() + y);
            points[pointCount] = new Circle(pointLoc[pointCount].x, pointLoc[pointCount].y, 7);
            points[pointCount].setId("pointID");
            pane.getChildren().add(points[pointCount]);

            int temp = pointCount - 1;
            switch (pointCount) {
                // Connect point to previous point
                default -> connectors[temp] =
                        new Line(pointLoc[temp].x, pointLoc[temp].y, pointLoc[temp+1].x, pointLoc[temp+1].y);

                // Special cases for points not connected to previous point
                case 0 -> {} // Do nothing
                case 4 -> connectors[3] =
                        new Line(pointLoc[1].x, pointLoc[1].y, pointLoc[4].x, pointLoc[4].y);
                case 6 -> connectors[5] =
                        new Line(pointLoc[1].x, pointLoc[1].y, pointLoc[6].x, pointLoc[6].y);
                case 9 -> connectors[8] =
                        new Line(pointLoc[6].x, pointLoc[6].y, pointLoc[9].x, pointLoc[9].y);
            }
            if (pointCount != 0 ) {
                connectors[temp].setId("connectorID");
                pane.getChildren().add(connectors[temp]);
            }

            System.out.println(x +", " + y);
            System.out.println("Point added: " + nextLocation[pointCount]);
            pointCount++;
            nextInputLab.setText("Please place point on: " + nextLocation[pointCount]);
        } else {
            System.out.println("All points placed!");
        }
    }

    /**
     * Reset all points and connectors on the ImageView.
     */
    public static void removePoints() {
        for (int i = 0; i < points.length; i++) {
            pane.getChildren().remove(points[i]);
            points[i] = null;
        }
        for (int i = 0; i < connectors.length; i++) {
            pane.getChildren().remove(connectors[i]);
            connectors[i] = null;
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