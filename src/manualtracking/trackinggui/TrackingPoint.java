/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking.trackinggui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import manualtracking.trackinggui.note.NoteType;
import manualtracking.trackinggui.note.TrackingNote;

/**
 *
 * @author Tiago Pedro
 */
public class TrackingPoint extends Point
{
    private static int ID_COUNTER = 0;
    private boolean first;
    private int ID;
    private static final double RADIUS_OF_AREA_OF_INTERACTION = 4.5;
    private static final double POINT_RADIUS = 1.5;
    private static final double LINE_WIDTH = 0.5;
    private List<TrackingNote> listOfNotes;

    
    public TrackingPoint(int x, int y)
    {
        super(x, y);
        ID = ++ID_COUNTER;
        first = false;
        listOfNotes = new ArrayList<TrackingNote>();
    }
    
    public static double getRadiusOfAreaOfInteraction()
    {
        return RADIUS_OF_AREA_OF_INTERACTION;
    }
    
    public static double getPointRadius()
    {
        return POINT_RADIUS;
    }
    
    public static double getTrackingLineWidth()
    {
        return LINE_WIDTH;
    }

    public void addTrackingNote(NoteType noteType, Object input)
    {
        listOfNotes.add(TrackingNote.createTrackingNote(this, noteType, input));
    }
    
    public int getID()
    {
        return ID;
    }
}
