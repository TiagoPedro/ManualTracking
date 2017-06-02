/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking.trackinggui.note;

import manualtracking.trackinggui.TrackingPoint;

/**
 *
 * @author Tiago Pedro
 */
public abstract class TrackingNote
{
    protected TrackingPoint parentPoint;
    
    protected TrackingNote() {}
    
    public static TrackingNote createTrackingNote(TrackingPoint parent, NoteType type, Object input)
    {
        switch(type)
        {
            case TEXT_NOTE:
                return new TextNote(parent, (String) input);
        }
        return null;
    }
    
    public abstract void setNote(Object input);
    
    public abstract Object getNote();
}
