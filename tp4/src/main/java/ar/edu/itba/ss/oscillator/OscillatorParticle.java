package ar.edu.itba.ss.oscillator;

import ar.edu.itba.ss.Integrator;

public class OscillatorParticle {

    private double x;
    private double v;
    private double mass;

    // Previous values needed for beeman and predictor corrector
    // Beeman needs acceleration in t-deltaT
    // TODO: Find initial value for previousAcceleration
    private double previousAcceleration;




    public OscillatorParticle(double x, double v, double mass) {
        this.x = x;
        this.v = v;
        this.mass = mass;
    }


    public double getActingForces(double elasticConstant, double damping) {
        return -1 * x * elasticConstant - (damping * v);
    }

    private void applyVerlet(double deltaT, double elasticConstant, double damping) {
        double forces = getActingForces(elasticConstant, damping);
        v = v + (deltaT / mass) * forces;
        x = x + deltaT * v + deltaT * deltaT * forces / (2 * mass);
    }

    private void applyBeeman(double deltaT, double elasticConstant, double damping) {
        double forces = getActingForces(elasticConstant, damping);
        double a = forces / mass;
        double vt = v;
        double deltaT2 = deltaT * deltaT;
        x = x + v * deltaT + (2/3.0) * a * deltaT2 - (1/6.0)  * previousAcceleration * deltaT2;
        v = vt + (3/2.0) * a * deltaT - (1/2.0) * previousAcceleration * deltaT;
        double newForces = getActingForces(elasticConstant, damping);
        double newA = newForces / mass;
        v = vt + (1/3.0) * newA * deltaT + (5/6.0) * a * deltaT - (1/6.0) * previousAcceleration * deltaT;
        previousAcceleration = a;
    }

    private void applyGearPC(double deltaT, double elasticConstant, double damping) {


        // TODO


    }

    public void applyIntegrator(Integrator integrator, double deltaT, double elasticConstant, double damping) {
        switch (integrator) {
            case VERLET:
                applyVerlet(deltaT, elasticConstant, damping);
                break;
            case BEEMAN:
                applyBeeman(deltaT, elasticConstant, damping);
                break;
            case GEARPC:
                applyGearPC(deltaT, elasticConstant, damping);
                break;
            default:
                throw new RuntimeException("Invalid Integrator");
        }
    }



    public double getX() {
        return x;
    }

    public double getV() {
        return v;
    }
}