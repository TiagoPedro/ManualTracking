/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking.trackinggui.action;

import manualtracking.trackinggui.TrackingPath;
import manualtracking.trackinggui.TrackingPoint;

/**
 *
 * @author Tiago
 */
public class AddTrackingPoint extends TrackingAction
{
    private TrackingPoint point;
    private TrackingPath path;
    
    private int ID;
    
    public AddTrackingPoint(TrackingPath path, TrackingPoint point)
    {
        this.point = point;
        this.path = path;
        ID = TrackingAction.ID_COUNTER++;
    }
    
    @Override
    public void doAction()
    {
        path.getTrackingPoints().add(point);
    }

    @Override
    public void undoAction()
    {
        path.removeTrackingPoint(point);
    }

    @Override
    public int getID()
    {
        return ID;
    }
    
}
