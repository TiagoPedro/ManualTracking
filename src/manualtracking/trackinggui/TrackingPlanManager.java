/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking.trackinggui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import manualtracking.trackinggui.action.ActionManager;
import manualtracking.trackinggui.action.AddTrackingPoint;
import manualtracking.trackinggui.action.TranslateTrackingPoint;

/**
 *
 * @author Tiago Pedro
 */
public class TrackingPlanManager {
//    protected TrackingPoint[]     pointMatrix;
//    protected TrackingPoint     previousPoint;
//    protected TrackingPoint     currentPoint;

    protected TrackingPath activePath;
    private List<TrackingPath> paths;
    protected Image planImage;
    private ActionManager actionManager;

    private static TrackingPlanManager instance = null;

    private TrackingPlanManager() {
        instance = this;
        activePath = new TrackingPath();
        paths = new ArrayList<>();
        actionManager = new ActionManager();

//        planImage = new Image("file:D:\\Tiago Pedro\\Documents\\NetBeansProjects\\ManualTracking_touch\\src\\assets\\planExample1.jpg");
    }

    public static TrackingPlanManager getInstance() throws IOException {
        if (instance == null) {
            instance = new TrackingPlanManager();
        }

        return instance;
    }

    public List<int[]> getPoints()
    {
        return activePath.getPoints();
    }

    public Image getPlanImage() {
        return planImage;
    }

    public void setPlanImage(String filePath) {
        planImage = new Image("file:" + filePath);
    }

    public void createNewTrackingPointAction(int x, int y)
    {
        TrackingPoint p = new TrackingPoint(x, y);
        activePath.pathPoints.add(p);
        AddTrackingPoint action = actionManager.addTrackingPointAction(activePath, p);
    }

    public void translateTrackingPoint(int[] selectedPoint, int translationInX, int translationInY)
    {
	activePath.translatePoint(selectedPoint, translationInX, translationInY);
    }

    public void createTranslationAction(
	    int[] selectedPointCoordinates,
	    int totalTranslationLengthX, int totalTranslationLengthY)
    {
	TrackingPoint selectedPoint = null;
	for(TrackingPoint p : activePath.pathPoints)
	{
	    if(p.x == selectedPointCoordinates[0] &&
		p.y == selectedPointCoordinates[1])
	    {
		selectedPoint = p;
	    }
	}
        TranslateTrackingPoint action = actionManager.addTranslationAction(
		activePath, selectedPoint,
		totalTranslationLengthX, totalTranslationLengthY);
    }

    public void addTrackingNoteToPoint(int[] selectedPoint, String noteType, Object input)
    {
        activePath.addTrackingNoteToPoint(selectedPoint, noteType, input);
    }

    public void clearPoints()
    {
        activePath.clearPath();
        actionManager.clear();
    }

    public void undo()
    {
	actionManager.undoLastAction();
    }

    public void redo()
    {
	actionManager.redoNextAction();
    }
}
