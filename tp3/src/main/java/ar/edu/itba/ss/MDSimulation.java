package ar.edu.itba.ss;

import ar.edu.itba.ss.events.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MDSimulation {

    private final PriorityQueue<Event> events = new PriorityQueue<>();

    private final List<Particle> particles;
    private final List<Particle> particlesWithCorners;
    private final double universeWidth;
    private final double universeHeight;
    private final double openingSize;
    private final double openingX;
    private final int maxIterations;
    private final int sampleSize;
    private double systemTime;
    private final Configuration configuration;

    public MDSimulation(Configuration config) {
        // Arraylist for faster iterations
        this.particles = new ArrayList<>(config.getSampleSize());
        this.particlesWithCorners = new ArrayList<>(config.getSampleSize() + 2);

        universeHeight = config.getUniverseHeight();
        universeWidth = config.getUniverseWidth();
        openingSize = config.getOpeningSize();
        openingX = universeWidth / 2;
        maxIterations = config.getMaxIterations();
        sampleSize = config.getSampleSize();
        configuration = config;
        systemTime = 0D;
    }


    public void run() {
        initializeParticles();
        addAllFutureColitions(particles);
        try {
            Files.deleteIfExists(Paths.get(getOutputFileName()));
        } catch (IOException e) {
            System.out.println("Unable to delete previously existing dynamic config");
        }
        save();
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
                p.advanceStraight(nextEventTime-systemTime);
            }
            systemTime = nextEventTime;

            // Update the velocities of the two colliding particles i and j according to the laws of elastic collision
            nextEvent.applyBounce();

            // Determine all future collisions that would occur involving either i or j, assuming all particles move in straight
            // line trajectories from time t onwards. Insert these events onto the priority queue.
            addAllFutureColitions(nextEvent.getParticles());

            if (events.isEmpty()) throw new IllegalStateException("No events remaining!");
            if ( i % Constants.SAVE_FREQUENCY == 0) {
                save();
            }
            i++;

        }
        System.out.println("All done!");
    }



    public void addAllFutureColitions(List<Particle> toAddCollisionsList) {
        double collisionXDeltaT, collisionYDeltaT;
        for (Particle p : toAddCollisionsList) {

            // Get deltaT for both collisions
            collisionXDeltaT = p.collidesX();
            collisionYDeltaT = p.collidesY();

            // If both occur at same time, we add a diagonal event
            if (collisionXDeltaT == collisionYDeltaT && collisionXDeltaT >= 0D) {
                events.offer(new WallDiagonalEvent(systemTime+collisionXDeltaT, p));
            } else {
                if (collisionXDeltaT >= 0) { // If collision X can happen, we add one
                    events.offer(new WallXEvent(systemTime+collisionXDeltaT, p));
                }
                if ( collisionYDeltaT >= 0) { // If collision Y can happen, we add one
                    events.offer(new WallYEvent(systemTime+collisionYDeltaT, p));
                }
            }

           // Add collisions against other particles
            List<Particle> particlesCopy = new ArrayList<>(particles.size()+2);

            for (Particle otherP : particlesWithCorners) {
                // In order to repeat a.crash(b) and b.crash(a), we only add it if the ID is lower
                if(p.getId() < otherP.getId()) {
                  double collidesDeltaT = p.collides(otherP);
                  if (collidesDeltaT >= 0) {
                    events.add(new ParticleEvent(collidesDeltaT+systemTime, p, otherP));
                  }
                }
            }
        }
    }


    // Here we generate our N particles, including their initial position and speed:
    // - They must be on the left side of the opening
    // - They have a total initial velocity of Constants.INITIAL_VELOCITY
    // - They must not be overlapped! (check overlap formula from TP1)
    public void initializeParticles() {
        for (int i = 0; i < sampleSize; i++) {
            particles.add(i, getParticle(i));
            particlesWithCorners.add(i, particles.get(i));
        }

        Particle corner1 = new Particle(sampleSize, universeWidth / 2, universeHeight / 2 + openingSize / 2, 0, 0);
        Particle corner2 = new Particle(sampleSize + 1, universeWidth / 2, universeHeight / 2 - openingSize / 2, 0, 0);
        corner1.setRadius(0D);
        corner2.setRadius(0D);
        particlesWithCorners.add(corner1.getId(), corner1);
        particlesWithCorners.add(corner2.getId(), corner2);
    }

    public Particle getParticle(int id) {
        double speedAngle = Math.random() * (2 * Math.PI);
        double vy = Math.cos(speedAngle) * Constants.INITIAL_VELOCITY;
        double vx = Math.sin(speedAngle) * Constants.INITIAL_VELOCITY;
        assert (Math.sqrt(vx*vx+vy*vy) - Constants.INITIAL_VELOCITY) < Constants.EPSILON;

        double y;
        double x;
        boolean overlaps;;
        do {
            y = Math.random() * (universeHeight - 2 * Constants.PARTICLE_RADIUS) + Constants.PARTICLE_RADIUS;
            x = Math.random() * (openingX - 2 * Constants.PARTICLE_RADIUS) + Constants.PARTICLE_RADIUS;
            overlaps = false;
            for (Particle p : particles) {
                if (p.overlaps(x, y)) {
                    overlaps = true;
                    break;
                }
            }
        } while(overlaps);
        return new Particle(id, x, y, vx, vy);
    }

    public void save(){
        try (FileWriter fw = new FileWriter(getOutputFileName(), true);) {

            String particleFormat = "%d\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\n";
            PrintWriter pw = new PrintWriter(fw);
            double wallParticlesDiameter = 2 * Constants.WALLPARTICLESRADIUS;
            long amountOfParticlesX = Math.round(universeWidth / wallParticlesDiameter);
            long amountOfParticlesY = Math.round(universeHeight / wallParticlesDiameter);
            long amountOfParticlesOpening = Math.round(((universeHeight - openingSize) / wallParticlesDiameter)  / 2);
            long totalParticles = amountOfParticlesX * 2 + amountOfParticlesY * 2 + 2 * amountOfParticlesOpening + sampleSize;
            pw.printf("%d\n\n", totalParticles);
            for (Particle p : particles) {
                // TODO: Check if we add RGB here
                double color = p.getId() / (double)sampleSize;
                pw.printf(particleFormat, p.getId(), p.getX(), p.getY(), p.getVx(), p.getVy(), p.getMass(), p.getRadius(), color, 1 - color, 1 - color);
            }

            // Pared de abajo y arriba
            for (int i = 0; i < amountOfParticlesX; i++) {
                pw.printf(particleFormat, -1, i * wallParticlesDiameter, 0D, 0D, 0D, 0D, Constants.WALLPARTICLESRADIUS, 1D, 1D, 1D);
                pw.printf(particleFormat, -1, i * wallParticlesDiameter, universeHeight, 0D, 0D, 0D, Constants.WALLPARTICLESRADIUS, 1D, 1D, 1D);
            }

            // Paredes derecha izq
            for (int i = 0; i < amountOfParticlesY; i++) {
                pw.printf(particleFormat, -1, 0D, i * wallParticlesDiameter, 0D, 0D, 0D, Constants.WALLPARTICLESRADIUS, 1D, 1D, 1D);
                pw.printf(particleFormat, -1, universeWidth, i * wallParticlesDiameter, 0D, 0D, 0D, Constants.WALLPARTICLESRADIUS, 1D, 1D, 1D);
            }

            // Paredes abertura
            for (int i = 1; i <= amountOfParticlesOpening; i++) {
                pw.printf(particleFormat, -1, universeWidth / 2, i * wallParticlesDiameter, 0D, 0D, 0D, Constants.WALLPARTICLESRADIUS, 1D, 1D, 1D);
                pw.printf(particleFormat, -1, universeWidth / 2, universeHeight - i * wallParticlesDiameter, 0D, 0D, 0D, Constants.WALLPARTICLESRADIUS, 1D, 1D, 1D);
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