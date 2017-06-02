/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking.trackinggui.action;

/**
 *
 * @author Tiago
 */
public abstract class TrackingAction
{
    protected static int ID_COUNTER = 0;
    
    public abstract void doAction();
    
    public abstract void undoAction();
    
    public abstract int getID();
}
