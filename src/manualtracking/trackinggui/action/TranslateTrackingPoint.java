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
public class TranslateTrackingPoint extends TrackingAction
{

    private int[] originalPos;
    private int[] translatedPos;
    private TrackingPoint point;
    private TrackingPath path;

    private int ID;

    public TranslateTrackingPoint(
	    TrackingPath path, TrackingPoint point,
	    int translationInX, int translationInY)
    {
	this.point = point;
	this.translatedPos = new int[2];
	this.originalPos = new int[2];
	this.translatedPos[0] = point.x;
	this.translatedPos[1] = point.y;
	this.originalPos[0] = translatedPos[0] - translationInX;
	this.originalPos[1] = translatedPos[1] - translationInY;
	this.path = path;
	ID = TrackingAction.ID_COUNTER++;
    }

    @Override
    public void doAction()
    {
	path.redoTranslation(point, translatedPos);
    }

    @Override
    public void undoAction()
    {
	path.undoTranslation(point, originalPos);
    }

    @Override
    public int getID()
    {
	return ID;
    }

}
