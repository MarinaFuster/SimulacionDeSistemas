package ar.edu.itba.ss.mars;

import ar.edu.itba.ss.Integrator;

public class MarsConfigurationBuilder {


    private double cutoffTime = 5;
    private double deltaT = 1;
    private int saveFrequency = 5;
    private String name = "test";
    private String outFolder = "./output/";
    private Integrator integrator = Integrator.VERLET;


    public MarsConfiguration get() {
        return new MarsConfiguration(cutoffTime, deltaT, saveFrequency, name, outFolder, integrator);
    }

    public MarsConfigurationBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MarsConfigurationBuilder outFolder(String outFolder) {
        this.outFolder = outFolder;
        return this;
    }

    public MarsConfigurationBuilder integrator(Integrator integrator) {
        this.integrator = integrator;
        return this;
    }

    public MarsConfigurationBuilder deltaT(double deltaT) {
        this.deltaT = deltaT;
        return this;
    }

    public MarsConfigurationBuilder cutoffTime(double cutoffTime) {
        this.cutoffTime = cutoffTime;
        return this;
    }

    public MarsConfigurationBuilder saveFrequency(int saveFrequency) {
        this.saveFrequency = saveFrequency;
        return this;
    }
}
