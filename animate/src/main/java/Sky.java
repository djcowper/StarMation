

import java.util.List;
import java.awt.Graphics;
import javax.swing.Timer;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.Date;
import java.util.Calendar;

public class Sky {
    // the list of stars
    public List<Star> stars;
    // keep the location of the observer
    public double latitude;
    public double longitude;
    // keep the date and time
    public Date date;

    public Sky(double lat, double lon) {// , Date date) {
        // super("Sky");
        int width = (2 * 240); // 24 "hours" across the sky
        int height = (2 * 180); // +/- 90 degrees
        this.latitude = lat;
        this.longitude = lon;
        this.date = new Date();
        // setSize(width, height);
        // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    
        // Timer timer = new Timer(10, e -> {
        //   // Clear the canvas
        //   getGraphics().clearRect(0, 0, getWidth(), getHeight());
        //   // Draw a circle on the canvas
        //   getGraphics().drawOval(100, 100, 50, 50);
        //   // Refresh the canvas
        //   repaint();
        // });
    
        }
}
