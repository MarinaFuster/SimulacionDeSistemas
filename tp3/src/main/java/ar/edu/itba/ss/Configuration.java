package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Configuration {


    private final double universeWidth;
    private final double universeHeight;
    private final double openingSize;
    private final int sampleSize;
    private final int maxIterations;
    private final String name;
    private final String outFolder;
    private final  double initialVelocity;

    public Configuration(double universeWidth, double universeHeight, double openingSize, int sampleSize, int maxIterations, String name, String outFolder, double initialVelocity) {
        this.universeWidth = universeWidth;
        this.universeHeight = universeHeight;
        this.openingSize = openingSize;
        this.sampleSize = sampleSize;
        this.maxIterations = maxIterations;
        this.name = name;
        this.outFolder = outFolder;
        this.initialVelocity =initialVelocity;
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

    public double getInitialVelocity() {
        return initialVelocity;
    }

    @Override
    public String toString() {
        return String.format("%f\n%f\n%f\n%d\n%d\n%s\n%s\n%f\n", universeWidth, universeHeight, openingSize, sampleSize, maxIterations, name, outFolder, initialVelocity);
    }

    public void save() {

        String outF = this.outFolder;
        if (outF.charAt(outFolder.length() - 1) != '/') {
            outF = outFolder + "/";
        }
        String outPath =  String.format("%s%s_static.xyz", outF, name);

        try {
            Files.deleteIfExists(Paths.get(outPath));
        } catch (IOException e) {
            System.out.println("Unable to delete previously existing file");
            throw new RuntimeException();
        }
        try (FileWriter fw = new FileWriter(outPath, false)) {
            PrintWriter pw = new PrintWriter(fw);
            pw.printf("%s", this.toString());

        } catch (IOException ex) {
            System.out.println("Unable to save static file");
        }
    }
}
