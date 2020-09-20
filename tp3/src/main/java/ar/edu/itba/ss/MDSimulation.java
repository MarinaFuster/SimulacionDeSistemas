package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
    private final int sampleSize;
    private final Configuration configuration;

    public MDSimulation(Configuration config) {
        // Arraylist for faster iterations
        this.particles = new ArrayList<Particle>(config.getSampleSize());

        universeHeight = config.getUniverseHeight();
        universeWidth = config.getUniverseWidth();
        openingSize = config.getOpeningSize();
        openingX = universeWidth / 2;
        maxIterations = config.getMaxIterations();
        sampleSize = config.getSampleSize();
        configuration = config;
    }


    public void run() {

        initializeParticles();
        addAllFutureColitions();


        int i = 0;
        while(i < maxIterations) {

            Event nextEvent = null;
            // Get next valid event
            while(!events.isEmpty() && (nextEvent = events.poll()).wasSuperveningEvent()) {
                // Do nothing
            }

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



    public void addAllFutureColitions() {
        double t;
        for (Particle p : particles) {
          // Add collisions against walls
            t = p.collidesX();
            if (t >= 0) {
                events.add(new WallEvent(t, p));
            }

            t = p.collidesY();
            if ( t >= 0) {
                events.add(new WallEvent(t, p));
            }


          // Add collisions againsts other particles
          for (Particle p2 : particles) {
            // In order to repeat a.crash(b) and b.crash(a), we only add it if the id is lower
            // TODO: Check if this is correct
              if(p.getId() < p2.getId()) {
                  t = p.collides(p2);
                  if (t >= 0) {
                      events.add(new ParticleEvent(p.collides(p2), p, p2));
                  }
              }
          }
        }
    }


    // Here we generate our N particles, incluiding their initial position and speed:
    // - They must be on the left side of the opening
    // - They have a total initial velocity of Constants.INITIAL_VELOCITY
    // - They must not be overlapped! (check overlap formula from TP1)
    public void initializeParticles() {
        for (int i = 0; i < sampleSize; i++) {
            particles.set(i, getParticle(i));
        }
    }

    public Particle getParticle(int id) {
        // TODO: Get new Particle correctly
        return new Particle(id, 0, 0, 0, 0);
    }

    public void save(){
        try (FileWriter fw = new FileWriter(getOutputFileName(), true);) {
            PrintWriter pw = new PrintWriter(fw);
            pw.printf("%d\n\n", sampleSize);
            for (Particle p : particles) {
                // TODO: Check if we add RGB here
                pw.printf("%f\t%f\t%f\t%f\t%f\t%f\n", p.getX(), p.getY(), p.getVx(), p.getVy(), p.getMass(), p.getRadius());
            }
        } catch (IOException ex) {
            System.out.println("Unable to save output: " + ex.getMessage());
        }
    }

    private String getOutputFileName() {
        String outFolder = configuration.getOutFolder();
        if (outFolder.charAt(outFolder.length() - 1) != '/') {
            outFolder = outFolder + "/";
        }
        return String.format("%s%s_dynamic.xyz", outFolder, configuration.getName());
    }


}