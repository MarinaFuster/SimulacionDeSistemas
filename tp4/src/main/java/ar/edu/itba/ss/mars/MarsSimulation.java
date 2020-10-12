package ar.edu.itba.ss.mars;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MarsSimulation {

    private final MarsConfiguration configuration;
    private double time = 0;


    public MarsSimulation(MarsConfiguration configuration) {
        this.configuration = configuration;
    }


    public void run() {

        try {
            Files.deleteIfExists(Paths.get(getOutputFileName()));
        } catch (IOException e) {
            System.out.println("Unable to delete previously existing dynamic config");
        }

        // Initialize Particles
        // First doing this for just sun and earth
        List<Particle> particles = new LinkedList<>();
        Particle earth = new Particle(
                ParticleNames.EARTH,
                Constants.EarthConstants.STARTX,
                Constants.EarthConstants.STARTY,
                Constants.EarthConstants.STARTVX,
                Constants.EarthConstants.STARTVY,
                Constants.EarthConstants.MASS,
                Constants.EarthConstants.RADIUS,
                Constants.EarthConstants.VISUALIZATION_RADIUS,
                0,1,0);

        Particle mars = new Particle(
                ParticleNames.MARS,
                Constants.MarsConstants.STARTX,
                Constants.MarsConstants.STARTY,
                Constants.MarsConstants.STARTVX,
                Constants.MarsConstants.STARTVY,
                Constants.MarsConstants.MASS,
                Constants.MarsConstants.RADIUS,
                Constants.MarsConstants.VISUALIZATION_RADIUS,
                1,0,0);

        Particle sun = new Particle(ParticleNames.SUN,
                0,0,0,0,
                Constants.SunConstants.MASS,
                Constants.SunConstants.RADIUS,
                Constants.SunConstants.VISUALIZATION_RADIUS,
                1, 1, 0);

        particles.add(earth);
        particles.add(mars);
        particles.add(sun);

        int iteration = 0;


        particles.add(createRocket(sun, earth));

        save(particles);

//
        double secondsInDay = 60*60*24;
        double secondsInAYear = 3.154 * Math.pow(10,7);
        double rocketStart = secondsInAYear * 2 - secondsInDay * 80;
        double rocketEnd = secondsInAYear * 2 + secondsInDay * 100;
        double rocketFrequency = secondsInDay;


            // Calculate R(t+1) and V(t+1)
        while(time <= configuration.getCutoffTime()) {

            if (time >= rocketStart && time <= rocketEnd && time % rocketFrequency == 0) {
                particles.add(createRocket(sun, earth));
            }

            for (Particle p : particles) {
                if(p.getParticleName() != ParticleNames.SUN) {
                    p.applyIntegrator(configuration.getIntegrator(), configuration.getDeltaT(), particles);
                }
            }

            // Apply the previously  calculated speeds and positions
            for (Particle p : particles) {
                p.applyChanges();
            }

            time += configuration.getDeltaT();
            iteration++;

            if (iteration % configuration.getSaveFrequency() == 0) {
                save(particles);
            }
        }

        save(particles);
    }

    private String getOutputFileName() {
        String outFolder = configuration.getOutFolder();
        if (outFolder.charAt(outFolder.length() - 1) != '/') {
            outFolder = outFolder + "/";
        }
        return String.format("%s%s_dynamic.xyz", outFolder, configuration.getName());
    }

    public void save(List<Particle> particles) {
        try (FileWriter fw = new FileWriter(getOutputFileName(), true);) {

            // Format: x y VX VY radio R G B
            String particleFormat = "%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\n"; // TODO: check changing this to str builder
            PrintWriter pw = new PrintWriter(fw);

            // If we want to add extra data, they should go here between the \n as to not affect the ovito output
            pw.printf(Locale.US, "%d\n%f\n", particles.size(), time);

            for (Particle p : particles) {
                pw.printf(Locale.US, particleStr(p).toString());
            }

        } catch (IOException ex) {
            System.out.println("Unable to save, reason: " + ex.getMessage());
        }
    }

    public Particle createRocket(Particle sun, Particle earth) {
        double xt = earth.getPosition().getX();
        double yt = earth.getPosition().getY();
        double alpha = Math.atan2(yt, xt); // radians
        double beta = (Math.PI / 2) - alpha;

        double totalDistance = Constants.RocketConstants.spaceStationDistance
                + Constants.EarthConstants.RADIUS
                + Math.sqrt(xt * xt + yt * yt);

        double xRocket = totalDistance * Math.cos(alpha);
        double yRocket = totalDistance * Math.sin(alpha);

        double vxt = earth.getSpeed().getX();
        double vyt = earth.getSpeed().getY();

        double earthV = Math.sqrt(vxt * vxt + vyt * vyt);
        double totalVelocity = Constants.RocketConstants.rocketVelocity
                + Constants.RocketConstants.spaceStationVelocity;

        double vxRocket = totalVelocity * Math.cos(beta);
        double vyRocket = totalVelocity * Math.sin(beta);
        xRocket = Math.signum(xt) * Math.abs(xRocket);
        yRocket = Math.signum(yt) * Math.abs(yRocket);
        vxRocket = Math.signum(vxt) * Math.abs(vxRocket);
        vyRocket = Math.signum(vyt) * Math.abs(vyRocket);

        
        vxRocket += vxt;
        vyRocket += vyt;
//        System.out.println("Before");
//        System.out.printf("x: %f - y: %f - vx: %f - vy: %f\n", xRocket, yRocket, vxRocket, vyRocket);


//        System.out.println("New rocket");
//        System.out.printf("x: %f - y: %f - vx: %f - vy: %f\n", xRocket, yRocket, vxRocket, vyRocket);
//
//        System.out.println("Tierra");
//        System.out.printf("x: %f - y: %f - vx: %f - vy: %f\n", xt, yt, vxt, vyt);

        Particle rocket = new Particle(ParticleNames.ROCKET, xRocket, yRocket, vxRocket, vyRocket,
                Constants.RocketConstants.rocketMass,
                Constants.RocketConstants.RADIUS,
                Constants.RocketConstants.VISUALIZATION_RADIUS,
                0, 0, 0.5
        );
        return rocket;

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
        str.append(p.getVisualizationRadius());
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
