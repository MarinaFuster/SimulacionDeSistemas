package com.itba.edu.ar;

import com.itba.edu.ar.config.AppConfig;
import com.itba.edu.ar.config.Boundary;
import com.itba.edu.ar.config.StaticConfig;
import com.itba.edu.ar.config.StaticConfigLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App 
{
    private static StaticConfig staticConfig;
    private static AppConfig appConfig;

    public static void main( String[] args )
    {
//        try  {
//            FileWriter fw = new FileWriter("timeDifferentNandM.txt");
//            PrintWriter pw = new PrintWriter(fw);
//                for (int i = 100; i <= 1000; i+= 100) {
//                    try {
//                        for (int j = 1; j < 1000; j++) {
//                            long time = timeDifferentCellSidesAndSampleSize(j,i);
//                            pw.printf("%d,%d,%d\n", j, i, time);
////                            System.out.printf("%d,%d,%d\n", j, i, time);
//                        }
//                    } catch (IllegalArgumentException ex) {
////                        System.out.println(ex.getMessage());
//                    }
//                }
//            fw.close();
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }

//        runConfigWithStdInput();
    }



    public static long timeDifferentCellSidesAndSampleSize(int M, int N) {
        appConfig = new AppConfig("./sample/");
        staticConfig = new StaticConfig(10, N, M, 20, 1, Boundary.CLOSED, new HashMap<>());
        addParticlesToStaticConfig(0.25);
        if ( !staticConfig.valid()) throw new IllegalArgumentException("Invalid static config parameters");
        long start = System.nanoTime();
        Simulation sim = new Simulation(staticConfig.getParticles());
        try {
            sim.run();
        } catch ( IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();

        }
        long end = System.nanoTime();
        return end - start;
    }

    public static void runConfigWithStdInput() {
        appConfig = new AppConfig("./sample/");
        appConfig.printInteractionsList = true;
        staticConfig = generateStaticConfig();
        addParticlesToStaticConfig();
        if ( !staticConfig.valid()) throw new IllegalArgumentException("Invalid static config parameters");
        try {
            StaticConfigLoader.save(staticConfig, "./sample/staticConfigOutput.txt");
        } catch ( IOException ex) {
            System.out.println(ex.getMessage());
        }


        Simulation sim = new Simulation(staticConfig.getParticles());

        try {
            sim.run();
        } catch ( IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void loadStaticConfig(String staticConfigPath) {
        //final String staticConfigPath = "./sample/staticInputTest.txt"; // TODO: User must input? Or change to default file
        try {
            staticConfig = StaticConfigLoader.load(staticConfigPath);

            System.out.println(staticConfig);
            // Do stuff
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static StaticConfig generateStaticConfig() {
        Scanner s = new Scanner(System.in);

        int timePartition = getInteger(s, "Insert time partition: ");
        int sampleSize = getInteger(s, "Insert sample size: ");
        double sideLength = getDouble(s, "Insert side length: ");
        int cellsPerSide = getInteger(s, "Insert cells per side (CIM): ");
        double interactionRadius = getDouble(s, "Insert interaction radius: ");
        Boundary boundaryMethod = getBoundaryMethod(s);
        Map<Integer, Particle> particlesMap = new HashMap<>();
        return new StaticConfig(timePartition, sampleSize, cellsPerSide, sideLength, interactionRadius, boundaryMethod, particlesMap);
    }

    public static void addParticlesToStaticConfig() {
        Scanner s = new Scanner(System.in);
        double particlesRadius = getDouble(s, "Insert radius for particles: ");
        addParticlesToStaticConfig(particlesRadius);
    }

    public static void addParticlesToStaticConfig(double particlesRadius) {
        int i = 0;
        while (staticConfig.getParticles().size() < staticConfig.getSampleSize()) {
            Point center = Point.getRandomPoint(staticConfig.getSideLength(), staticConfig.getSideLength());
            Particle newParticle = new Particle(particlesRadius, center);
            if (!particleOverlaps(newParticle, staticConfig.getParticles().values())) {
                staticConfig.getParticles().put(i++, newParticle);
            }
        }
    }

    public static StaticConfig getStaticConfig() {
        if (staticConfig == null) throw new RuntimeException("Attempted to get static config without initializing");
        return staticConfig;
    }

    public static AppConfig getAppConfig() {
        if (appConfig == null) throw new RuntimeException("Attempted to get app config without initializing");
        return appConfig;
    }

    public static double getDouble(Scanner s, String message) {
        System.out.println(message);
        return s.nextDouble();
    }
    public static int getInteger(Scanner s, String message) {
        System.out.println(message);
        return s.nextInt();
    }

    public static boolean particleOverlaps(Particle particle, Collection<Particle> particles) {
        return particles.stream().anyMatch((p) -> p.overlaps(particle));
    }

    public static Boundary getBoundaryMethod(Scanner s) {
        String str = "";
        while (!(str.toLowerCase().equals("y")) && (!str.toLowerCase().equals("n"))) {
            System.out.println("Use infinite boundaries? (y/n): ");
            str = s.next();
        }
        return str.equals("y") ? Boundary.INFINITE : Boundary.CLOSED;
    }
}
