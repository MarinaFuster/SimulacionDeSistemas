package ar.edu.itba.ss;

public class Main {
    public static void main(String[] args) {
        runSimulation(getConfig());
    }


    public static Configuration getConfig() {
        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.name("test")
                .outFolder("./")
                .maxIterations(100)
                .sampleSize(200);

        return builder.get();
    }

    public static void runSimulation(Configuration config) {
        MDSimulation simulation = new MDSimulation(config);
        simulation.run();
    }
}
