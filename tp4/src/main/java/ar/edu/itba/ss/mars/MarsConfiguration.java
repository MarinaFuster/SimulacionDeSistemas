package ar.edu.itba.ss.mars;

import ar.edu.itba.ss.Integrator;

public class MarsConfiguration {


    private final double cutoffTime;
    private final double deltaT;
    private final int saveFrequency;
    private final String name;
    private final String outFolder;
    private final Integrator integrator;

    public MarsConfiguration(double cutoffTime, double deltaT, int saveFrequency, String name, String outFolder, Integrator integrator) {
        this.cutoffTime = cutoffTime;
        this.deltaT = deltaT;
        this.saveFrequency = saveFrequency;
        this.name = name;
        this.outFolder = outFolder;
        this.integrator = integrator;
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
