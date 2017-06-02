/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking.trackinggui.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import manualtracking.trackinggui.TrackingPath;
import manualtracking.trackinggui.TrackingPoint;

/**
 *
 * @author Tiago
 */
public class ActionManager
{
    private List<TrackingAction> trackingActions;
    private int currentActionID;
    
    public ActionManager()
    {
        trackingActions = new ArrayList<>();
        currentActionID = 0;
    }
    
    public AddTrackingPoint addTrackingPointAction(
	    TrackingPath path, TrackingPoint point)
    {
        Iterator<TrackingAction> iter = trackingActions.iterator();
        while(iter.hasNext())
        {
            TrackingAction a = iter.next();
            if(a.getID() > currentActionID)
            {
                iter.remove();
                TrackingAction.ID_COUNTER = currentActionID;
            }
        }
        
        AddTrackingPoint action = new AddTrackingPoint(path, point);
        trackingActions.add(action);
        if(trackingActions.size() > 50)
        {
            trackingActions.remove(0);
        }
        currentActionID = action.getID();

        System.out.println(currentActionID);
        return action;
    }
    
    public TranslateTrackingPoint addTranslationAction(
	    TrackingPath path, TrackingPoint point,
	    int translationInX, int translationInY)
    {
	Iterator<TrackingAction> iter = trackingActions.iterator();
	while (iter.hasNext())
	{
	    TrackingAction a = iter.next();
	    if (a.getID() > currentActionID)
	    {
		iter.remove();
		TrackingAction.ID_COUNTER = currentActionID;
	    }
	}
	TranslateTrackingPoint action = new TranslateTrackingPoint(
		path, point, translationInX, translationInY);
	trackingActions.add(action);
	if (trackingActions.size() > 50)
	{
	    trackingActions.remove(0);
	}
	currentActionID = action.getID();

	System.out.println(currentActionID);
	return action;
    }

    public void undoLastAction()
    {
        if(!trackingActions.isEmpty())
        {
            if (currentActionID >= trackingActions.get(0).getID())
            {
                for (TrackingAction a : trackingActions)
                {
                    if (a.getID() == currentActionID)
                    {
                        a.undoAction();
                        currentActionID--;
                    }
                }
            }
            else
            {
                System.out.println("Can't undo any more actions");
            }     
        }
    }
    
    public void redoNextAction()
    {
	if (!trackingActions.isEmpty())
	{
	    if (currentActionID != trackingActions.get(trackingActions.size() - 1).getID())
	    {
		currentActionID++;
		for (TrackingAction a : trackingActions)
		{
		    if (a.getID() == currentActionID)
		    {
			a.doAction();
		    }
		}
	    }
	    else
	    {
		System.out.println("Can't redo any more actions.");
	    }
	}
    }

    public void clear()
    {
        trackingActions.clear();
        currentActionID = 0;
    }
}
