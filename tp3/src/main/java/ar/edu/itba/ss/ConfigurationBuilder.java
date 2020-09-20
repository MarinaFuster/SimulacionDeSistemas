package ar.edu.itba.ss;

public class ConfigurationBuilder {

    private double universeWidth = 0.24;
    private double universeHeight = 0.09;
    private double openingSize = 0.01;
    private int sampleSize;
    private int maxIterations;
    private String name;
    private String outFolder;

    public Configuration get() {
        return  new Configuration(universeWidth, universeHeight, openingSize, sampleSize, maxIterations, name, outFolder);
    }

    public ConfigurationBuilder maxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this;
    }

    public ConfigurationBuilder universeWidth(double universeWidth) {
        this.universeWidth = universeWidth;
        return this;
    }

    public ConfigurationBuilder universeHeight(double universeHeight) {
        this.universeHeight = universeHeight;
        return this;
    }

    public ConfigurationBuilder openingSize(double openingSize) {
        this.openingSize = openingSize;
        return this;
    }

    public ConfigurationBuilder sampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
        return this;
    }

    public ConfigurationBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ConfigurationBuilder outFolder(String outFolder) {
        this.outFolder = outFolder;
        return this;
    }
}