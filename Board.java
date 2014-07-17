package NewLand;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import java.util.ArrayList;

public class Board extends JPanel implements Runnable {
    private final int DELAY = 10;

    private int width;
    private int height;
    private int zoom;
    private Map map;
    private Rover rover;
    private Thread animator;

    public Board(int width, int height, int zoom) {
        // Save parameters
        this.width = width;
        this.height = height;
        this.zoom = zoom;

        // Generate new Map
    	map = new Map(this.width, this.height);
        rover = new Rover((int)(this.width / 2), (int)(this.height / 2), map);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g = drawMap(g);
        g.setColor(Color.black);
        g.drawString("Energy: " + Double.toString(rover.getEnergy()), 0, (height * zoom) + 10);
        g.drawString("Trips: " + Integer.toString(rover.getTrips()), 100, (height * zoom) + 10);
        g.drawString("Discovered: " + Integer.toString(rover.getDiscovered()) + "( " + Double.toString(rover.getDiscoveredAverage()) +  ") - " +  Integer.toString(rover.getDiscoveredTotal()) + " (" + Double.toString(rover.getDiscoveredTotal() * 100.0 / (width * height)) + "%)", 200, (height * zoom) + 10);
    }

    private void cycle() {
        rover.move();
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            cycle();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    public Graphics drawMap(Graphics g){
    	for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                switch (map.getDiscovered(i, j)) {
                    case -1:
                        // Undicovered terrain
                        g.setColor(Color.BLACK);
                        break;
                    case 101:
                        // HQ
                        g.setColor(Color.WHITE);
                        break;
                    default:
                        Color aux = new Color((int)(map.getDiscovered(i, j) * 255 / 100), 255 - (int)(map.getDiscovered(i, j) * 255 / 100), 0);
                        g.setColor(aux);
                        break;
                }
                g.fillRect((i * zoom), (j * zoom), zoom, zoom);
            }
        }

        // Draw Rovers
        g.setColor(Color.YELLOW);
        g.fillRect((rover.getX() * zoom), (rover.getY() * zoom), zoom, zoom);

        return g;
    }
}