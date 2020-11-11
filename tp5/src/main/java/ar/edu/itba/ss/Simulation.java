package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Simulation {

    // Static so it is accessible by the pedestrians methods just in case
    public static Config config;
    public static double time = 0;
    public static Particle goal;
    public static List<Obstacle> obstacles;



    public Simulation(Config config) {
        Simulation.config = config;
        goal = new Particle(new Vector2D(config.getGoalX(),
                config.getGoalY()), new Vector2D(0,0), config.getPedestrianRadius(), 0,0, 1);
    }

    public void run() {

        try {
            Files.deleteIfExists(Paths.get(getOutputFileName()));
        } catch (IOException e) {
            System.out.println("Unable to delete previously existing dynamic config");
        }



        Pedestrian pedestrian = new Pedestrian(new Vector2D(config.getStartX(), config.getStartY()), config.getPedestrianRadius());
        obstacles = initializeObstacles();

        int iteration = 0;

        // Save initial state
        save(obstacles, pedestrian);

        while(time < config.getCutoffTime() && !pedestrian.reachedObjective) {
            // Advance obstacles


            pedestrian.advance(config.getDeltaT());
            for (Obstacle obstacle: obstacles) {
                obstacle.advance(config.getDeltaT());
            }


            time += config.getDeltaT();
            iteration++;

            if (iteration % config.getSaveFrequency() == 0) {
                save(obstacles, pedestrian);
                for (Obstacle o : obstacles) {
                    o.setG(1);
                }
            }

        }
        System.out.println("Max velocity: " + pedestrian.maxVelocity);

    }

    public List<Obstacle> initializeObstacles() {
        List<Obstacle> obstacles = new LinkedList<>();

        // Two ways to add obstacles - either start adding them and make sure they don't "collide" on the same lane
        // Or add them one by one, and make sure there is a min distance between their X positions so we avoid lane
        // overlapping. I will do the 2nd option unless instructed otherwise
        // The first obstacle X will be hardcoded from the config, and from there on they will be randomized
        double obstacleX = config.getPedestrianToFirstObstacleDistance();

        while (obstacleX < (config.getGoalX() - config.getLastObstacleToGoalDistance())) {
            double speedDirection = Math.random() > 0.5? 1 : -1;
            Vector2D initialSpeed = new Vector2D(0, config.getObstacleSpeed() * speedDirection);
            Vector2D initialPosition = new Vector2D(obstacleX,
                    Math.random() * (config.getMaxY() - 2 * config.getObstacleRadius()) + config.getObstacleRadius());

            obstacles.add(new Obstacle(initialPosition, initialSpeed, config.getObstacleRadius()));
            obstacleX = getNextObstacleX(obstacleX);
        }
        return obstacles;
    }

    public double getNextObstacleX(double lastObstacleX ) {
        double r = config.getObstacleRadius();
        return lastObstacleX + Math.random() * (r) + (3 * r);
    }

    public void save(List<Obstacle> obstacles, Pedestrian pedestrian) {
        try (FileWriter fw = new FileWriter(getOutputFileName(), true)) {

            // Format: x y VX VY radio R G B
            PrintWriter pw = new PrintWriter(fw);

            // If we want to add extra data, they should go here between the \n as to not affect the ovito output
            pw.printf(Locale.US, "%d\n%f\n", obstacles.size() + 2, time);

            // Write Pedestrian and target


            pw.printf(Locale.US, particleStr(pedestrian).toString());
            pw.printf(Locale.US, particleStr(goal).toString());
            for (Obstacle obstacle : obstacles) {
                pw.printf(Locale.US, particleStr(obstacle).toString());
            }
        } catch (IOException ex) {
            System.out.println("Unable to save, reason: " + ex.getMessage());
        }
    }

    private String getOutputFileName() {
        String outFolder = config.getOutFolder();
        if (outFolder.charAt(outFolder.length() - 1) != '/') {
            outFolder = outFolder + "/";
        }
        return String.format("%s%s_dynamic.xyz", outFolder, config.getName());
    }

    private StringBuilder particleStr(Particle p) {
        StringBuilder str = new StringBuilder();
        str.append(p.getPosition().getX());
        str.append("\t");
        str.append(p.getPosition().getY());
        str.append("\t");
        str.append(p.getSpeed().getX());
        str.append("\t");
        str.append(p.getSpeed().getY());
        str.append("\t");
        str.append(p.getRadius());
        str.append("\t");
        str.append(p.getR());
        str.append("\t");
        str.append(p.getG());
        str.append("\t");
        str.append(p.getB());
        str.append("\n");
        return str;
    }

}
