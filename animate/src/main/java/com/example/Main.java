package com.example;

import javax.swing.JFrame;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

class Main
{
    // this interface lets you create different animations without having to worry about anything but drawing
    public interface Animation {
        public void drawFrame(Graphics g, Dimension size, double frameTime, double deltaTime, int frameNumber);
    }

    public static class AnimationComponent extends Component {
        Animation animation;
        // keeps track of the time and frame
        int min = 0;
        double lat, lon;
        public List<Star> stars;
        Date date;
        // use this timer to run the animation
        Timer timer = new Timer();

        AnimationComponent(Animation animation, List<Star> stars, Date date, double lat, double lon) {
            this.animation = animation;
            this.stars = stars;
            this.date = date;
            this.lat = lat;
            this.lon = lon;
            // start the timer.
            // Its bets to do this only when the component is shown, but I don't
            // remember how to do this.  For now just start the animation after one second.
            // The 1000 / 30 will be 30 frames per second.
            timer.scheduleAtFixedRate(
                    new TimerTask() {
                        public void run() {
                            repaint();
                        }
                    }
                    , 1000, 1000 / 30);
        }

        // override the component paint method to update the time and call the animation
        public void paint(Graphics g) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE,1);
            date = calendar.getTime();
            int m, x, y;
            double alt, az;

            float fwidth = (float) (2.0 * 240.0);
            float fheight = (float) (2.0 * 180.0);
        

            g.drawOval((int) ((fwidth / 2.0) - 90.), (int) ((fheight / 2.0) - 90.), 180, 180);
            g.drawRect((int) ((fwidth / 2.0) - 5.), (int) ((fheight / 2.0) - 5.), 10, 10);
            for (Star star : stars) {
              star.precess((float) lat, (float) lon, date);
              // Right ascension (RA) and declination (Dec) are celestial coordinates
              // that range from 0–24 hours and +90°–-90°
        
              m = (int) (((star.getMAG() * -1.) + 8.0) * .3);
        
          
              alt = star.getALT();
              alt = (Math.PI / 2.) - alt;
              alt = (alt / (Math.PI / 2.0)) * 90.;
      
              if (alt > 0.) { // only draw stars above the horizon
                az = star.getAZ();
                x = (int) ((fwidth / 2.0) + (0.0 + alt) * Math.sin(az));
                y = (int) ((fheight / 2.0) + (0.0 + alt) * Math.cos(az));
                y = (int) fheight - y;
                x = (int) fwidth - x;
                if (star.getName().equals("Polaris")) { // make the northstar stand out
                  m = 5;
                  g.drawRect(x - m / 2, y - m / 2, m * 1, m * 1);
                } else {
                  g.drawOval(x - m / 2, y - m / 2, m * 1, m * 1);
                }
              }
            }   
        }

        // return the size we want the window to be 640x480
        public Dimension getPreferredSize() {
            return new Dimension(640, 480);
        }
    }

    // test animation to draw a sine wave
    public static class Planetarium implements Animation {
        public void drawFrame(Graphics g, Dimension size, double frameTime, double deltaTime, int frameNumber) {

        }
    }

    public static List<Star> readStars() {
    
        int limit_num_stars = 256;// 2048;
        Scanner fileName = null;
        try {
          fileName = new Scanner(new File("test.csv"));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        final Scanner finalFileName = fileName;
        
        List<Star> list = new ArrayList<Star>();

        String line1 = finalFileName.nextLine();
        for (int i = 0; i < limit_num_stars; i++) {
          String line = finalFileName.nextLine();
          String[] splitstr = line.split(",");
          list.add(new Star(splitstr[0], Float.parseFloat(splitstr[1]),
          Float.parseFloat(splitstr[2]),
          Float.parseFloat(splitstr[3])));
    }
    return list;
  }

    public static void main(String[] args) {
        // create a frame and add an animation component to it.
        JFrame frame = new JFrame("Animation");
        double latitude = 45.523062;
        double longitude = -122.676482; //-122
        Sky sky = new Sky(latitude, longitude);
        sky.stars = readStars();

        Date date = new Date();
        System.out.println(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        frame.add(new AnimationComponent(new Planetarium(), sky.stars, date, latitude, longitude));

        frame.pack();

        frame.show();

    }
}