package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class MDSimulation {

    private final PriorityQueue<Event> events = new PriorityQueue<Event>();

    private final List<Particle> particles;
    private final double universeWidth;
    private final double universeHeight;
    private final double openingSize;
    private final double openingX;
    private final int maxIterations;


    public MDSimulation(Configuration config) {
        // Arraylist for faster iterations
        this.particles = new ArrayList<Particle>(config.getSampleSize());

        universeHeight = config.getUniverseHeight();
        universeWidth = config.getUniverseWidth();
        openingSize = config.getOpeningSize();
        openingX = universeWidth / 2;
        maxIterations = config.getMaxIterations();
    }


    public void run() {
        int i = 0;



        while(i < maxIterations) {

            Event nextEvent;
            // Get next valid event
            while(!events.isEmpty() && !events.poll().wasSuperveningEvent()) {
                 nextEvent = events.poll();
            }









            if (events.isEmpty()) throw new IllegalStateException("No events remaining!");
            i++;
        }
    }
}