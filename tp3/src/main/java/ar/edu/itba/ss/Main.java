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
                .outFolder("./output/")
                .maxIterations(150)
                .sampleSize(30);

        return builder.get();
    }

    public static void runSimulation() {
        activeConfig.save();
        MDSimulation simulation = new MDSimulation(activeConfig);
        simulation.run();
    }
}
