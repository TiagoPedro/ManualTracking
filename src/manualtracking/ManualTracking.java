/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manualtracking;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import manualtracking.trackinggui.TrackingEngine;

/**
 *
 * @author Tiago Pedro
 */
public class ManualTracking extends Application
{
    private TrackingEngine trackingGUI;
    private Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception
    {
        trackingGUI = TrackingEngine.getInstance();
        
        this.stage = stage;
        this.stage.setFullScreen(true);
        this.stage.setTitle("Manual Tracking");
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/manual_tracking_icon.png")));
        this.stage.setScene(trackingGUI.getScene());
        trackingGUI.init();
        this.stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
