package ar.edu.itba.ss.oscillator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class OscillatorSimulation {


    double time = 0;
    private final OscillatorConfiguration configuration;

    public OscillatorSimulation(OscillatorConfiguration configuration) {
        this.configuration = configuration;
    }

    public void run() {


        try {
            Files.deleteIfExists(Paths.get(getOutputFileName()));
        } catch (IOException e) {
            System.out.println("Unable to delete previously existing dynamic config");
        }

        OscillatorParticle oscillatorParticle = new OscillatorParticle(configuration.getStartX(), configuration.getStartV(),configuration.getParticleMass());
        oscillatorParticle.initializePreviousAcceleration(configuration.getDeltaT(), configuration.getSpringConstant(), configuration.getDamping());
        int iteration = 0;
        save(oscillatorParticle);
        while (time < configuration.getCutoffTime()) {
            oscillatorParticle.applyIntegrator(configuration.getIntegrator(),
                    configuration.getDeltaT(), configuration.getSpringConstant(), configuration.getDamping());
            time += configuration.getDeltaT();
            iteration++;

            if (iteration % configuration.getSaveFrequency() == 0) {
                save(oscillatorParticle);
            }
        }
    }

    private String getOutputFileName() {
        String outFolder = configuration.getOutFolder();
        if (outFolder.charAt(outFolder.length() - 1) != '/') {
            outFolder = outFolder + "/";
        }
        return String.format("%s%s_dynamic.xyz", outFolder, configuration.getName());
    }

    public void save(OscillatorParticle particle) {
        try (FileWriter fw = new FileWriter(getOutputFileName(), true);) {
            // x v radio
            String particleFormat = "%f\t%f\t0.01\n";
            PrintWriter pw = new PrintWriter(fw);

            pw.printf(Locale.US, "2\n\n"); // If we want to add extra data, they should go here between the \n
            pw.printf(Locale.US, particleFormat, 0D, 0D);
            pw.printf(Locale.US, particleFormat, particle.getX(), particle.getV());
        } catch (IOException ex) {
            System.out.println("Unable to save, reason: " + ex.getMessage());
        }
    }
}
