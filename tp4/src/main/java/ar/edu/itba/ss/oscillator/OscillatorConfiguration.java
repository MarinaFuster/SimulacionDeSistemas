package ar.edu.itba.ss.oscillator;

import ar.edu.itba.ss.Integrator;

public class OscillatorConfiguration {


    private final double springConstant; // factor del elastico
    private final double damping; // factor de biscocidad

    private final double particleMass;
    private final double startX;
    private final double startV;
    private final double cutoffTime;
    private final double deltaT;
    private final int saveFrequency;
    private final String name;
    private final String outFolder;
    private final Integrator integrator;

    public OscillatorConfiguration(double springConstant, double damping, double particleMass, double startX, double startV, double cutoffTime, double deltaT, int saveFrequency, String name, String outFolder, Integrator integrator) {
        this.springConstant = springConstant;
        this.damping = damping;
        this.particleMass = particleMass;
        this.startX = startX;
        this.startV = startV;
        this.cutoffTime = cutoffTime;
        this.deltaT = deltaT;
        this.saveFrequency = saveFrequency;
        this.name = name;
        this.outFolder = outFolder;
        this.integrator = integrator;
    }

    public double getSpringConstant() {
        return springConstant;
    }

    public double getDamping() {
        return damping;
    }

    public double getParticleMass() {
        return particleMass;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartV() {
        return startV;
    }

    public double getCutoffTime() {
        return cutoffTime;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public int getSaveFrequency() {
        return saveFrequency;
    }

    public String getName() {
        return name;
    }

    public String getOutFolder() {
        return outFolder;
    }

    public Integrator getIntegrator() {
        return integrator;
    }
}
