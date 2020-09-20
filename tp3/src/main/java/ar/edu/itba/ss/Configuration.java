package ar.edu.itba.ss;

public class Configuration {


    private final double universeWidth;
    private final double universeHeight;
    private final double openingSize;
    private final int sampleSize;
    private final int maxIterations;
    private final String name;
    private final String outFolder;

    public Configuration(double universeWidth, double universeHeight, double openingSize, int sampleSize, int maxIterations, String name, String outFolder) {
        this.universeWidth = universeWidth;
        this.universeHeight = universeHeight;
        this.openingSize = openingSize;
        this.sampleSize = sampleSize;
        this.maxIterations = maxIterations;
        this.name = name;
        this.outFolder = outFolder;
    }

    public double getUniverseWidth() {
        return universeWidth;
    }

    public double getUniverseHeight() {
        return universeHeight;
    }

    public double getOpeningSize() {
        return openingSize;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public String getName() {
        return name;
    }

    public String getOutFolder() {
        return outFolder;
    }
}
