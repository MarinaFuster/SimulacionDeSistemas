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

            Event nextEvent = null;
            // Get next valid event
            while(!events.isEmpty() && !events.poll().wasSuperveningEvent()) {
                 nextEvent = events.poll();
            }
            assert nextEvent != null;
            double nextEventTime = nextEvent.getTime();

            // Advance all particles to time t along a straight line trajectory.
            for(Particle p : particles) {
                p.advanceStraight(nextEventTime);
            }

            // Update the velocities of the two colliding particles i and j according to the laws of elastic collision


            // Determine all future collisions that would occur involving either i or j, assuming all particles move in straight
            // line trajectories from time t onwards. Insert these events onto the priority queue.


            if (events.isEmpty()) throw new IllegalStateException("No events remaining!");
            i++;
        }
    }
}