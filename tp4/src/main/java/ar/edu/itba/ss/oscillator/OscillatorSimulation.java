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
        // save(oscillatorParticle);
        while (time < configuration.getCutoffTime()) {
            if (iteration % configuration.getSaveFrequency() == 0) {
                save(oscillatorParticle);
            }

            oscillatorParticle.applyIntegrator(configuration.getIntegrator(),
                    configuration.getDeltaT(), configuration.getSpringConstant(), configuration.getDamping());
            time += configuration.getDeltaT();
            iteration++;
        }
    }

    private String getOutputFileName() {
        String outFolder = configuration.getOutFolder();
        if (outFolder.charAt(outFolder.length() - 1) != '/') {
            outFolder = outFolder + "/";
        }
        return String.format("%s%s_%d_dynamic.xyz", outFolder, configuration.getName(), getExp(configuration.getDeltaT()));
    }

    private int getExp(double deltaT) {
        int i=0;
        while(deltaT < 1) {
            deltaT = deltaT*10;
            i++;
        }
        return i;
    }

    public void save(OscillatorParticle particle) {
        try (FileWriter fw = new FileWriter(getOutputFileName(), true);) {
            // x v radio
            String particleFormat = "%f\t%f\t0.01\n";

            StringBuilder particleLine = new StringBuilder();
            particleLine.append(particle.getX());
            particleLine.append("\t");
            particleLine.append(particle.getV());
            particleLine.append("\t");
            particleLine.append(0.01);
            particleLine.append("\n");

            PrintWriter pw = new PrintWriter(fw);

            pw.printf(Locale.US, "2\n%f\n", time); // If we want to add extra data, they should go here between the \n
            pw.printf(Locale.US, particleFormat, 0D, 0D);
            pw.printf(Locale.US, particleLine.toString());
        } catch (IOException ex) {
            System.out.println("Unable to save, reason: " + ex.getMessage());
        }
    }
}
