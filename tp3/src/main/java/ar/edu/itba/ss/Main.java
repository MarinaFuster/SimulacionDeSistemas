package ar.edu.itba.ss;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static Configuration activeConfig;


    public static void main(String[] args) {
        ConfigurationBuilder builder = createConfig();

        double[] testInitialVelocities = {0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09};
        int iterations = 10;

        int totalIterations = testInitialVelocities.length * iterations;
        long acumTimes = 0;
        int finishedIterations = 0;

        for (double v : testInitialVelocities) {
            for (int i = 0; i < iterations; i++) {
                System.out.printf("Running for initial velocity: %.2f: ", v);
                long startTime = System.currentTimeMillis();
                builder.initialVelocity(v);

                activeConfig = builder.get();
                runSimulation();
                long elapsedTime = System.currentTimeMillis() - startTime;

//                Calculos de tiempo de ejecucion
                acumTimes += elapsedTime;
                finishedIterations += 1;
                long averageTime = acumTimes / finishedIterations;
                int remainingIterations = totalIterations - finishedIterations;
                long ETA = remainingIterations * averageTime;
                Duration diff = Duration.ofMillis(ETA);
                LocalTime fTime = LocalTime.ofNanoOfDay(diff.toNanos());
                DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_TIME;
                String output = fTime.format(df);
                System.out.printf("Finished in %d seconds\n", (elapsedTime / 1000));
                System.out.println("Expected remaining time: " + output);
            }
        }

    }

    public static ConfigurationBuilder createConfig() {
        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.name("test")
                .outFolder("./output/")
                .maxIterations(150)
                .sampleSize(150)
                .measurePressure(true); // measurePressure para que calcule la presion

        return builder;
    }

    public static void runSimulation() {
        activeConfig.save();
        MDSimulation simulation = new MDSimulation(activeConfig);
        simulation.run();
    }
}
