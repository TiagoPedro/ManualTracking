/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking.trackinggui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

/**
 *
 * @author Tiago Pedro
 */
public class TrackingEngine
{

    private TrackingGUIController controller;
    protected final TrackingPlanManager matrix;
    private FXMLLoader loader;
    private BorderPane root;
    protected final Scene scene;

    // application states
    protected enum ApplicationState
    {
        IDLE,
        BLOCKED_FOR_EDITING,
        BLOCKED_FOR_NOTE,
        MARKING;
    }

    protected ApplicationState applicationState;
    protected ApplicationState previousApplicationState;

    private static TrackingEngine instance = null;

    private TrackingEngine() throws IOException
    {
        instance = this;

        matrix = TrackingPlanManager.getInstance();
        loadFXML();
        scene = new Scene(root);

        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose plan file");
        File file = chooser.showOpenDialog(scene.getWindow());
        matrix.setPlanImage(file.getAbsolutePath());

        controller = (TrackingGUIController) loader.getController();
        controller.setPlanImage(matrix.getPlanImage());
    }

    private void loadFXML() throws IOException
    {
        URL location = getClass().getResource("TrackingGUI.fxml");
        loader = new FXMLLoader();
        loader.setLocation(location);
        root = (BorderPane) loader.load(location.openStream());
    }

    public Scene getScene()
    {
        return scene;
    }

    public TrackingPlanManager getMatrix()
    {
        return matrix;
    }

    public void init()
    {
        controller.start();
    }

    public double getTrackingPointRadiusOfInfluence()
    {
        return TrackingPoint.getRadiusOfAreaOfInteraction();
    }

    public double getTrackingPointRadius()
    {
        return TrackingPoint.getPointRadius();
    }

    public double getTrackingLineWidth()
    {
        return TrackingPoint.getTrackingLineWidth();
    }

    public void addTrackingNoteToPoint(int[] selectedPoint, String noteType, Object input) throws IOException
    {
        TrackingPlanManager.getInstance().addTrackingNoteToPoint(selectedPoint, noteType, input);
    }
    
    public ApplicationState getApplicationState()
    {
        return applicationState;
    }
    
    public void setApplicationState(ApplicationState applicationState)
    {
        this.applicationState = applicationState;
    }

    public static TrackingEngine getInstance() throws IOException
    {
        if (instance == null)
        {
            instance = new TrackingEngine();
        }

        return instance;
    }
}
