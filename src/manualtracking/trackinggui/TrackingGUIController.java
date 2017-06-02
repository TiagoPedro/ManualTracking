/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking.trackinggui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

//import manualtracking.trackinginterface.TrackingInterface;
/**
 *
 * @author Tiago Pedro
 */
public class TrackingGUIController implements Initializable
{

    private TrackingEngine trackingEngine;
    private Image planImage;
    private String noteType;

    private enum MouseDraggingState
    {

        IDLE,
        EDITING_POINT,
        PANNING;
    }

    MouseDraggingState mouseDragState;

    // application point variables;
    private int upperLeftPlanImageX;
    private int upperLeftPlanImageY;
    private int newUpperLeftPlanImageX;
    private int newUpperLeftPlanImageY;
    private int pressedX;
    private int pressedY;
    private int totalTranslationLengthX;
    private int totalTranslationLengthY;
    private int[] selectedPoint;
    private double angle;

    @FXML
    private BorderPane appPane;

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox topBar;

    @FXML
    private VBox leftBar;

    @FXML
    private Button markButton;
    private boolean markButtonPressed;

    @FXML
    private Button textNoteButton;
    private boolean textNoteButtonPressed;

    @FXML
    private Button speechButton;

    @FXML
    private Button notesButton;

    @FXML
    private Button eventsButton;

    @FXML
    private Button visualAnglesButton;

    @FXML
    private Button blockButton;
    private boolean blockButtonPressed;

    @FXML
    private Button undoButton;
    
    @FXML
    private Button redoButton;
    
    @FXML
    private Button deleteButton;

    @FXML
    private Button accountButton;

    
    public TrackingGUIController() throws IOException
    {
        try
        {
            this.trackingEngine = TrackingEngine.getInstance();
        } catch (IOException ex)
        {
            Logger.getLogger(TrackingGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        trackingEngine.applicationState = TrackingEngine.ApplicationState.IDLE;
        trackingEngine.previousApplicationState = null;
        mouseDragState = MouseDraggingState.IDLE;

        upperLeftPlanImageX = 0;
        upperLeftPlanImageY = 0;
        newUpperLeftPlanImageX = 0;
        newUpperLeftPlanImageY = 0;
        selectedPoint = new int[2];
    }

    private void paintNewPoint(List<int[]> pointsList)
    {
        int[] previousPoint = null;
        int[] currentPoint = pointsList.get(pointsList.size() - 1);
        currentPoint[0] += upperLeftPlanImageX;
        currentPoint[1] += upperLeftPlanImageY;

        if (trackingEngine.matrix.activePath.getTrackingPoints().size() > 1)
        {
            previousPoint = pointsList.get(pointsList.size() - 2);
            previousPoint[0] += upperLeftPlanImageX;
            previousPoint[1] += upperLeftPlanImageY;
            gc.strokeLine(
                    previousPoint[0],
                    previousPoint[1],
                    currentPoint[0],
                    currentPoint[1]);
        }

        gc.fillOval(
                currentPoint[0] - trackingEngine.getTrackingPointRadius(),
                currentPoint[1] - trackingEngine.getTrackingPointRadius(),
                trackingEngine.getTrackingPointRadius() * 2,
                trackingEngine.getTrackingPointRadius() * 2);
    }

    private void paintTrackingPath(List<int[]> pointsList)
    {
        int[] previousPoint = null;
        int[] currentPoint = null;

        for (int[] point : pointsList)
        {
            if (point != pointsList.get(0))
            {
                previousPoint = currentPoint;
            }

            currentPoint = point;

            gc.fillOval(
                    currentPoint[0] - trackingEngine.getTrackingPointRadius() + newUpperLeftPlanImageX,
                    currentPoint[1] - trackingEngine.getTrackingPointRadius() + newUpperLeftPlanImageY,
                    trackingEngine.getTrackingPointRadius() * 2,
                    trackingEngine.getTrackingPointRadius() * 2);

            if (point != pointsList.get(0))
            {
                gc.strokeLine(
                        previousPoint[0] + newUpperLeftPlanImageX,
                        previousPoint[1] + newUpperLeftPlanImageY,
                        currentPoint[0] + newUpperLeftPlanImageX,
                        currentPoint[1] + newUpperLeftPlanImageY);
            }
        }
    }

    private void invertIconColor(Button pressedButton, boolean pressedButtonState)
    {
        if (pressedButtonState)
        {
            if (pressedButton == blockButton)
            {
                pressedButton.setStyle("-fx-background-image: url('/assets/inverted_block_icon.png')");
            }
            else if (pressedButton == markButton)
            {
                pressedButton.setStyle("-fx-background-image: url('/assets/inverted_mark_icon.png')");
            }
            else if (pressedButton == textNoteButton)
            {
                pressedButton.setStyle("-fx-background-image: url('/assets/inverted_text_note_icon.png')");
            }
        }
        else
        {
            if (pressedButton == blockButton)
            {
                pressedButton.setStyle("-fx-background-image: url('/assets/white_block_icon.png')");
            }
            else if (pressedButton == markButton)
            {
                pressedButton.setStyle("-fx-background-image: url('/assets/white_mark_icon.png')");
            }
            else if (pressedButton == textNoteButton)
            {
                pressedButton.setStyle("-fx-background-image: url('/assets/white_text_note_icon.png')");
            }
        }
    }

    @FXML
    public void handleMouseClickOnPlan(MouseEvent event)
    {
        event.consume();

	// send points to plan matrix
	pressedX = (int) event.getX();
	pressedY = (int) event.getY();

	if (event.isStillSincePress())
	{
	    switch (trackingEngine.applicationState)
	    {
		case IDLE:
		    return;

		case MARKING:
		    trackingEngine.matrix.createNewTrackingPointAction(
			    pressedX - upperLeftPlanImageX,
			    pressedY - upperLeftPlanImageY);

		    // add offset and draw
		    paintNewPoint(trackingEngine.matrix.getPoints());
		    break;

		case BLOCKED_FOR_NOTE:
		    // check if any point is pressed over
		    for (int[] point : trackingEngine.matrix.getPoints())
		    {
			double dX = pressedX - point[0] - upperLeftPlanImageX;
			double dY = pressedY - point[1] - upperLeftPlanImageY;
			double distanceToPoint = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
			if (distanceToPoint < trackingEngine.getTrackingPointRadiusOfInfluence())
			{
			    noteType = "TEXT_NOTE";
			    TextInputDialog textDialog = new TextInputDialog();
			    textDialog.setTitle("Written Note");
			    textDialog.setContentText("Type your note:");
			    Optional<String> result = textDialog.showAndWait();
			    String input = "";
			    if (result.isPresent())
			    {
				input = result.get();
			    }

			    trackingEngine.matrix.addTrackingNoteToPoint(point, noteType, input);
			    textNoteButtonPressed = false;
			    invertIconColor(textNoteButton, textNoteButtonPressed);
//                            selectedPoint = point;
			    trackingEngine.applicationState = trackingEngine.previousApplicationState;
			    trackingEngine.previousApplicationState = null;
			    break;
			}
		    }
		    break;
	    }
	}
    }

    @FXML
    public void handleMouseClickOnBlock(MouseEvent event)
    {
        if (event.isStillSincePress())
        {
            switch (trackingEngine.applicationState)
            {
                case BLOCKED_FOR_EDITING:
                    trackingEngine.applicationState = TrackingEngine.ApplicationState.IDLE;
                    System.out.println("Marking");
                    break;

                case MARKING:
                    trackingEngine.applicationState = TrackingEngine.ApplicationState.BLOCKED_FOR_EDITING;
                    markButtonPressed = false;
                    invertIconColor(markButton, markButtonPressed);
                    break;

                case IDLE:
                    trackingEngine.applicationState = TrackingEngine.ApplicationState.BLOCKED_FOR_EDITING;
                    break;

                case BLOCKED_FOR_NOTE:
                    trackingEngine.applicationState = TrackingEngine.ApplicationState.BLOCKED_FOR_EDITING;
                    textNoteButtonPressed = false;
                    invertIconColor(textNoteButton, textNoteButtonPressed);
                    break;

                default:
                    break;
            }
        }
        blockButtonPressed = !blockButtonPressed;
        invertIconColor(blockButton, blockButtonPressed);
    }

    @FXML
    public void handleMouseClickOnMark(MouseEvent event)
    {
        switch (trackingEngine.applicationState)
        {
//            case IDLE:
//                trackingEngine.applicationState = TrackingEngine.ApplicationState.FIRST_MARKING;
//                break;
//
//            case MARKING:
//                trackingEngine.applicationState = TrackingEngine.ApplicationState.LAST_MARKING;
//                break;
//
//            case FIRST_MARKING:
//                trackingEngine.applicationState = TrackingEngine.ApplicationState.IDLE;
//                break;
//
//            case LAST_MARKING:
//                trackingEngine.applicationState = TrackingEngine.ApplicationState.MARKING;
//                break;

            case IDLE:
                trackingEngine.applicationState = TrackingEngine.ApplicationState.MARKING;
                break;

            case MARKING:
                trackingEngine.applicationState = TrackingEngine.ApplicationState.IDLE;
                break;

            case BLOCKED_FOR_EDITING:
                trackingEngine.applicationState = TrackingEngine.ApplicationState.MARKING;
                blockButtonPressed = false;
                invertIconColor(blockButton, blockButtonPressed);
                break;

            case BLOCKED_FOR_NOTE:
                trackingEngine.applicationState = TrackingEngine.ApplicationState.MARKING;
                textNoteButtonPressed = false;
                invertIconColor(textNoteButton, textNoteButtonPressed);
                break;

            default:
                break;
        }
        markButtonPressed = !markButtonPressed;
        invertIconColor(markButton, markButtonPressed);
    }

    @FXML
    public void handleMouseClickOnTextNote(MouseEvent event)
    {
        switch (trackingEngine.applicationState)
        {
            case BLOCKED_FOR_EDITING:
                trackingEngine.previousApplicationState = trackingEngine.applicationState;
                trackingEngine.applicationState = TrackingEngine.ApplicationState.BLOCKED_FOR_NOTE;
                break;

            case BLOCKED_FOR_NOTE:
                trackingEngine.applicationState = trackingEngine.previousApplicationState;
                break;

            case IDLE:
                trackingEngine.previousApplicationState = trackingEngine.applicationState;
                trackingEngine.applicationState = TrackingEngine.ApplicationState.BLOCKED_FOR_NOTE;
                break;

            case MARKING:
                trackingEngine.previousApplicationState = trackingEngine.applicationState;
                trackingEngine.applicationState = TrackingEngine.ApplicationState.BLOCKED_FOR_NOTE;
                break;

            default:
                break;
        }

        textNoteButtonPressed = !textNoteButtonPressed;
        invertIconColor(textNoteButton, textNoteButtonPressed);

//        if (trackingEngine.applicationState == TrackingEngine.ApplicationState.BLOCKED_FOR_EDITING)
//        {
//            return;
//        }
//        else if (trackingEngine.applicationState == TrackingEngine.ApplicationState.BLOCKED_FOR_NOTE)
//        {
//            trackingEngine.applicationState = trackingEngine.previousApplicationState;
//            trackingEngine.previousApplicationState = null;
//            textNoteButtonPressed = false;
//            invertIconColor(textNoteButton, textNoteButtonPressed);
//        } else
//        {
//            trackingEngine.previousApplicationState = trackingEngine.applicationState;
//            trackingEngine.applicationState = TrackingEngine.ApplicationState.BLOCKED_FOR_NOTE;
//            textNoteButtonPressed = true;
//            invertIconColor(textNoteButton, textNoteButtonPressed);
//        }
    }

    @FXML
    public void clearPaths()
    {
        gc.drawImage(planImage, upperLeftPlanImageX, upperLeftPlanImageY, 630, 670);
        trackingEngine.matrix.clearPoints();
    }

    @FXML
    public void handleMousePressedOnPlan(MouseEvent event)
    {
        pressedX = (int) event.getX();
        pressedY = (int) event.getY();

        // handling press on a point
        double dX;
        double dY;
        double distanceToPoint;
        if (trackingEngine.applicationState == TrackingEngine.ApplicationState.BLOCKED_FOR_EDITING)
        {
            // check if any point is pressed over
            for (int[] point : trackingEngine.matrix.getPoints())
            {
                dX = pressedX - point[0] - upperLeftPlanImageX;
                dY = pressedY - point[1] - upperLeftPlanImageY;
                distanceToPoint = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
                if (distanceToPoint < trackingEngine.getTrackingPointRadiusOfInfluence())
                {
                    mouseDragState = MouseDraggingState.EDITING_POINT;
                    selectedPoint = point;
                    break;
                }
            }
        }

        if (mouseDragState != MouseDraggingState.EDITING_POINT)
        {
            mouseDragState = MouseDraggingState.PANNING;
        }
        event.consume();
    }

    @FXML
    public void handleMouseDraggedOnPlan(MouseEvent event)
    {
        int translationLengthX = (int) event.getX() - pressedX;
        int translationLengthY = (int) event.getY() - pressedY;
        pressedX = (int) event.getX();
        pressedY = (int) event.getY();
        totalTranslationLengthX += translationLengthX;
        totalTranslationLengthY += translationLengthY;

        if (trackingEngine.applicationState == TrackingEngine.ApplicationState.BLOCKED_FOR_EDITING
                && mouseDragState == MouseDraggingState.EDITING_POINT)
        {
            trackingEngine.matrix.translateTrackingPoint(selectedPoint, translationLengthX, translationLengthY);

            selectedPoint[0] += translationLengthX;
            selectedPoint[1] += translationLengthY;
        } else
        {
            newUpperLeftPlanImageX += translationLengthX;
            newUpperLeftPlanImageY += translationLengthY;
        }
        update();
        event.consume();
    }

    @FXML
    public void handleMouseReleasedOnPlan(MouseEvent event)
    {
        if (mouseDragState == MouseDraggingState.EDITING_POINT)
        {
            trackingEngine.matrix.createTranslationAction(selectedPoint, totalTranslationLengthX, totalTranslationLengthY);
            totalTranslationLengthX = 0;
            totalTranslationLengthY = 0;
        }
        upperLeftPlanImageX = newUpperLeftPlanImageX;
        upperLeftPlanImageY = newUpperLeftPlanImageY;
        mouseDragState = MouseDraggingState.IDLE;
        event.consume();
    }

    @FXML
    public void handleMouseScrollDownOnPlan(ScrollEvent event)
    {
        final double SCALE_DELTA = 1.1;

        event.consume();

        if (event.getDeltaY() == 0)
        {
            return;
        }

        double scaleFactor
                = (event.getDeltaY() > 0)
                        ? SCALE_DELTA
                        : 1 / SCALE_DELTA;

        canvas.setScaleX(canvas.getScaleX() * scaleFactor);
        canvas.setScaleY(canvas.getScaleY() * scaleFactor);
    }

    @FXML
    public void handleZoomGestureOnPlan(ZoomEvent event
    )
    {
        if (event.getZoomFactor() == 0)
        {
            return;
        }

        canvas.setScaleX(canvas.getScaleX() * event.getZoomFactor());
        canvas.setScaleY(canvas.getScaleY() * event.getZoomFactor());

        event.consume();
    }

    @FXML
    public void handleRotateGestureOnPlan(RotateEvent event
    )
    {
        if (event.getTotalAngle() < 10 && event.getTotalAngle() > -10)
        {
            return;
        }

        canvas.setRotate(canvas.getRotate() + event.getAngle());

        event.consume();
    }

    @FXML
    public void quitApplication(MouseEvent event)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit Manual Tracking");
//        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Do you really wish to exit Manual Tracking?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Platform.exit();
        }
    }

    @FXML
    public void undo(MouseEvent event)
    {
        event.consume();
        trackingEngine.matrix.undo();
        update();
    }

    @FXML
    public void redo(MouseEvent event)
    {
        event.consume();
        trackingEngine.matrix.redo();
//        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        update();
    }

    public Canvas getCanvas()
    {
        return canvas;
    }

    public void setPlanImage(Image image)
    {
        planImage = image;
    }

    public void start()
    {
        markButtonPressed = false;
        blockButtonPressed = false;
        gc.drawImage(planImage, upperLeftPlanImageX, upperLeftPlanImageY, 630, 670);
    }

    private void update()
    {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(planImage,
                newUpperLeftPlanImageX,
                newUpperLeftPlanImageY,
                630, 670);
        paintTrackingPath(trackingEngine.matrix.getPoints());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.setStroke(Color.RED);
        gc.setLineDashes(8);
        gc.setLineDashOffset(0.0001);
        gc.setLineWidth(trackingEngine.getTrackingLineWidth());

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
