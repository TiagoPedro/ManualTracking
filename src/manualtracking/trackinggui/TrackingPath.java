/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking.trackinggui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import manualtracking.trackinggui.action.TrackingAction;
import manualtracking.trackinggui.note.NoteType;

/**
 *
 * @author Tiago Pedro
 */
public class TrackingPath
{
    protected List<TrackingPoint> pathPoints;
    
    public TrackingPath()
    {
        pathPoints = new LinkedList<>();
    }

    public List<int[]> getPoints()
    {
        List<int[]> coordinatesList = new LinkedList<>();
        
        for(TrackingPoint point : pathPoints)
        {
            coordinatesList.add(new int[]{point.x, point.y});
        }
        
        return coordinatesList;
    }
    
    public void clearPath()
    {
        pathPoints.clear();
    }
    
    public List<TrackingPoint> getTrackingPoints()
    {
        return pathPoints;
    }
    
    public void translatePoint(int[] selectedPoint, int translationInX, int translationInY)
    {
        for(TrackingPoint point : pathPoints)
        {
            if(point.x == selectedPoint[0] && point.y == selectedPoint[1])
            {
                point.x += translationInX;
                point.y += translationInY;
            }
        }
    }

    public void undoTranslation(TrackingPoint point, int[] originalPos)
    {
        if(pathPoints.contains(point))
	{
	    point.x = originalPos[0];
	    point.y = originalPos[1];
	}
    }
    
    public void redoTranslation(TrackingPoint point, int[] translatedPos)
    {
        if(pathPoints.contains(point))
	{
	    point.x = translatedPos[0];
	    point.y = translatedPos[1];
	}
    }
    
    public void addTrackingNoteToPoint(int[] selectedPoint, String noteType, Object input)
    {
        for(TrackingPoint point : pathPoints)
        {
            if(point.x == selectedPoint[0] && point.y == selectedPoint[1])
            {
                switch(noteType)
                {
                    case "TEXT_NOTE":
                        point.addTrackingNote(NoteType.TEXT_NOTE, input);
                        break;
                }
            }
        }
    }

    public void removeTrackingPoint(TrackingPoint point)
    {
	Iterator<TrackingPoint> iter = pathPoints.iterator();
	while (iter.hasNext())
	{
	    TrackingPoint p = iter.next();
	    if (p.getID() == point.getID())
	    {
		iter.remove();
	    }
	}
	
//        for(TrackingPoint p : pathPoints)
//        {
//            if(p.getID() == point.getID())
//            {
//                pathPoints.remove(p);
//            }
//        }
    }
}
