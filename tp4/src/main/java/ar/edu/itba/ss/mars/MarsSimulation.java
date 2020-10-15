package ar.edu.itba.ss.mars;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MarsSimulation {

    private final MarsConfiguration configuration;
    public static double time = 0;
    public static Particle mars, earth, sun, jupiter;
    public static Particle targetPlanet;
    public static boolean crashed = false;

    public MarsSimulation(MarsConfiguration configuration) {
        this.configuration = configuration;
    }


    public void run() {

        try {
            Files.deleteIfExists(Paths.get(getOutputFileName()));
            Files.deleteIfExists(Paths.get(getRocketOutputFileName()));
        } catch (IOException e) {
            System.out.println("Unable to delete previously existing dynamic config");
        }

        // Initialize Particles
        // First doing this for just sun and earth
        List<Particle> particles = new LinkedList<>();



        earth   = createPlanet(Constants.EarthConstants.class);
        mars    = createPlanet(Constants.MarsConstants.class);
        jupiter = createPlanet(Constants.JupiterConstants.class);
        sun     = createPlanet(Constants.SunConstants.class);
        particles.add(earth);
        particles.add(mars);
        particles.add(sun);
        particles.add(jupiter);

        targetPlanet = jupiter;


        boolean stopOnCrash = false;
        int iteration = 0;
        double secondsInHour = 60*60;
        double secondsInDay = 60*60*24;
        double secondsInAYear = 3.154 * Math.pow(10,7);


        // Estos datos son para deltaT = 50 para jugar un poco
//        double rocketStart = 3;
//        double rocketEnd =   0;
//        double rocketFrequency = 1;
//        stopOnCrash = false;

        // Estos datos son para deltaT = 50 para que haya 1 solo cohete en la fecha de la distancia minima
        double rocketStart = 1.7736E7;
        double rocketEnd =   1.7736E7;
        double rocketFrequency = 1;
        stopOnCrash = true;

//         Estos datos son para deltaT = 50 para el segundo grafico de dincaia vs tiempo de lanzamiento
//        double rocketStart = secondsInDay * 203;
//        double rocketEnd = secondsInDay * 207;
//        double rocketFrequency = 60;
//        stopOnCrash = false;

        // Estos datos son para mandar un cohete por dia durante toda la simulacion
        // (runtime ~1 hora y mediaporque
//        double rocketStart = 0;
//        double rocketEnd = configuration.getCutoffTime();
//        double rocketFrequency = (secondsInDay) / configuration.getDeltaT();

        // To optimize mars calculation distance
        List<Particle> planets = new ArrayList<>(particles);
        List<Rocket> rockets = new ArrayList<>(Math.max((int)((rocketEnd - rocketStart) / rocketFrequency + 1), 0));


        save(particles);

        // Calculate R(t+1) and V(t+1)
        while(time <= configuration.getCutoffTime()) {
            if (stopOnCrash && crashed) {
                System.out.println("Stopping on crash");
                break;
            }
            if (time >= rocketStart && time <= rocketEnd && iteration % rocketFrequency == 0) {
                Particle r = createRocket(earth);
                particles.add(r);
                rockets.add((Rocket)r);
            }

            particles.parallelStream().forEach(p -> {
                if(p.getParticleName() != ParticleNames.SUN) {
                    p.applyIntegrator(configuration.getIntegrator(), configuration.getDeltaT(), particles);
                }
            }) ;

            // Apply the previously  calculated speeds and positions
            planets.stream().forEach(Particle::applyChanges);
            rockets.parallelStream().forEach(Particle::applyChanges);


            time += configuration.getDeltaT();
            iteration++;

            if (iteration % configuration.getSaveFrequency() == 0) {
                save(particles);
            }
        }




        save(particles);
        saveRocketsData(particles);
    }

    private String getOutputFileName() {
        String outFolder = configuration.getOutFolder();
        if (outFolder.charAt(outFolder.length() - 1) != '/') {
            outFolder = outFolder + "/";
        }
        return String.format("%s%s_dynamic.xyz", outFolder, configuration.getName());
    }

    private String getRocketOutputFileName() {
        String outFolder = configuration.getOutFolder();
        if (outFolder.charAt(outFolder.length() - 1) != '/') {
            outFolder = outFolder + "/";
        }
        return String.format("%s%s_rockets.xyz", outFolder, configuration.getName());
    }

    public void save(List<Particle> particles) {
        try (FileWriter fw = new FileWriter(getOutputFileName(), true)) {

            // Format: x y VX VY radio R G B
            PrintWriter pw = new PrintWriter(fw);

            // If we want to add extra data, they should go here between the \n as to not affect the ovito output
            pw.printf(Locale.US, "%d\n%f\t%d\n", particles.size(), time, (int)(time / (60*60*24)));

            for (Particle p : particles) {
                pw.printf(Locale.US, particleStr(p).toString());
            }

        } catch (IOException ex) {
            System.out.println("Unable to save, reason: " + ex.getMessage());
        }
    }

    public void saveRocketsData(List<Particle> particles) {
        try (FileWriter fw = new FileWriter(getRocketOutputFileName(), true)) {

            // Format: x y VX VY radio R G B
            PrintWriter pw = new PrintWriter(fw);

            // If we want to add extra data, they should go here between the \n as to not affect the ovito output
            pw.printf(Locale.US, "%d\n%f\n", particles.size(), time);

            for (Particle p : particles.stream().filter(p-> p.getParticleName() == ParticleNames.ROCKET).collect(Collectors.toList())) {
                Rocket r = (Rocket) p;
                StringBuilder str = new StringBuilder();
                str.append(r.getCreationTime()).append("\t").append(r.getMinTargetDistance()).append("\t").append((int)(r.getCreationTime() / (60*60*24))).append("\n");
                pw.printf(Locale.US, str.toString());
            }

        } catch (IOException ex) {
            System.out.println("Unable to save, reason: " + ex.getMessage());
        }

    }

    public Particle createPlanet(Class constantsClass) {
        try {
            double mass = constantsClass.getField("MASS").getDouble(null);
            double radius = constantsClass.getField("RADIUS").getDouble(null);
            double visualization_radius = constantsClass.getField("VISUALIZATION_RADIUS").getDouble(null);
            double x = constantsClass.getField("STARTX").getDouble(null);
            double y = constantsClass.getField("STARTY").getDouble(null);
            double vx = constantsClass.getField("STARTVX").getDouble(null);
            double vy = constantsClass.getField("STARTVY").getDouble(null);
            ParticleNames name = (ParticleNames) constantsClass.getField("name").get(null);
            double r = constantsClass.getField("r").getDouble(null);
            double g = constantsClass.getField("g").getDouble(null);
            double b = constantsClass.getField("b").getDouble(null);

            System.out.println("name " + name + ". mass: " + mass);

            return new Particle(name, x, y, vx, vy, mass, radius, visualization_radius, r, g, b);
        } catch (NoSuchFieldException | IllegalStateException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }

    public Particle createRocket(Particle earth) {
        double xt = earth.getPosition().getX();
        double yt = earth.getPosition().getY();
        double alpha = Math.atan2(yt, xt); // radians
        double beta = (Math.PI / 2) - alpha;
        double rocketV0 = Constants.RocketConstants.rocketVelocity;

        double totalDistance = Constants.RocketConstants.spaceStationDistance
                + Constants.EarthConstants.RADIUS
                + Math.sqrt(xt * xt + yt * yt);

        double xRocket = totalDistance * Math.cos(alpha);
        double yRocket = totalDistance * Math.sin(alpha);

        double vxt = earth.getSpeed().getX();
        double vyt = earth.getSpeed().getY();

        double totalVelocity = rocketV0
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

        return new Rocket(ParticleNames.ROCKET, xRocket, yRocket, vxRocket, vyRocket,
                Constants.RocketConstants.rocketMass,
                Constants.RocketConstants.RADIUS,
                Constants.RocketConstants.VISUALIZATION_RADIUS,
                1,1,1
        );
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
