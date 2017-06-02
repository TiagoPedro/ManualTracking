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
public class TextNote extends TrackingNote
{
    private String note;
       
    protected TextNote(TrackingPoint parent, String input)
    {
        this.note = input;
        this.parentPoint = parent;
    }
    
    @Override
    public void setNote(Object input)
    {
        this.note = (String) input;
    }

    @Override
    public String getNote()
    {
        return note;
    }
    
}
