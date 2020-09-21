package ar.edu.itba.ss;

public class Main {

    public static Configuration activeConfig;

    public static void main(String[] args) {
        activeConfig = createConfig();
        runSimulation();
    }

    public static Configuration createConfig() {
        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.name("test")
                .outFolder("./")
                .maxIterations(100)
                .sampleSize(200);

        return builder.get();
    }

    public static void runSimulation() {
        MDSimulation simulation = new MDSimulation(activeConfig);
        simulation.run();
    }
}
