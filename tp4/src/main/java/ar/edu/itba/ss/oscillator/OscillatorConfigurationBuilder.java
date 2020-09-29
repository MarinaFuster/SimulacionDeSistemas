package ar.edu.itba.ss.oscillator;

import ar.edu.itba.ss.Integrator;

public class OscillatorConfigurationBuilder {



    // Default values as shown in slide 35 of the presentation
    private double springConstant = Math.pow(10,4); // factor del elastico
    private double damping = 100; // factor de biscocidad

    private double particleMass = 70;
    private double startX = 1;
    private double startV = 0; // TODO: Check this value
    private double cutoffTime = 5;
    private double deltaT = 0.0001;
    private int saveFrequency = 5;
    private String name = "test";
    private String outFolder = "./output/";
    private Integrator integrator = Integrator.VERLET;

    public OscillatorConfiguration get() {
        return new OscillatorConfiguration(springConstant, damping, particleMass, startX, startV, cutoffTime, deltaT, saveFrequency, name, outFolder, integrator);
    }


    public OscillatorConfigurationBuilder springConstant(double springConstant) {
        this.springConstant = springConstant;
        return this;
    }

    public OscillatorConfigurationBuilder damping(double damping) {
        this.damping = damping;
        return this;
    }

    public OscillatorConfigurationBuilder particleMass(double particleMass) {
        this.particleMass = particleMass;
        return this;
    }

    public OscillatorConfigurationBuilder startX(double startX) {
        this.startX = startX;
        return this;
    }

    public OscillatorConfigurationBuilder startV(double startV) {
        this.startV = startV;
        return this;
    }

    public OscillatorConfigurationBuilder cutoffTime(double cutoffTime) {
        this.cutoffTime = cutoffTime;
        return this;
    }

    public OscillatorConfigurationBuilder deltaT(double deltaT) {
        this.deltaT = deltaT;
        return this;
    }

    public OscillatorConfigurationBuilder saveFrequency(int saveFrequency) {
        this.saveFrequency = saveFrequency;
        return this;
    }

    public OscillatorConfigurationBuilder name(String name) {
        this.name = name;
        return this;
    }

    public OscillatorConfigurationBuilder outFolder(String outFolder) {
        this.outFolder = outFolder;
        return this;
    }

    public OscillatorConfigurationBuilder integrator(Integrator integrator) {
        this.integrator = integrator;
        return this;
    }
}
