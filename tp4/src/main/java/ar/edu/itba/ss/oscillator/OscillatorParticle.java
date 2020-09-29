package ar.edu.itba.ss.oscillator;

import ar.edu.itba.ss.Integrator;

public class OscillatorParticle {

    private double x;
    private double v;
    private double mass;

    // Previous values needed for beeman and predictor corrector
    // Beeman needs acceleration in t-deltaT
    private double previousAcceleration;


    public OscillatorParticle(double x, double v, double mass) {
        this.x = x;
        this.v = v;
        this.mass = mass;
    }




    private double getActingForces(double elasticConstant, double damping) {
        return getActingForces(elasticConstant, damping, x, v);
    }

    private double getActingForces(double elasticConstant, double damping, double x, double v) {
        return -1 * x * elasticConstant - (damping * v);
    }

    public void initializePreviousAcceleration(double deltaT, double elasticConstant, double damping) {
        double forces = getActingForces(elasticConstant, damping);
        double prevX = x - v * deltaT + (1/2.0) * deltaT * deltaT * forces;
        double prevV = v - forces * deltaT;
        double prevF = getActingForces(elasticConstant, damping, prevX, prevV);
        previousAcceleration = prevF / mass;
    }

    private void applyVerlet(double deltaT, double elasticConstant, double damping) {
        double forces = getActingForces(elasticConstant, damping);
        v = v + (deltaT / mass) * forces;
        x = x + deltaT * v + deltaT * deltaT * forces / (2 * mass);
    }


    private void applyBeeman(double deltaT, double elasticConstant, double damping) {
        double forces = getActingForces(elasticConstant, damping);
        double a = forces / mass;
        double deltaT2 = deltaT * deltaT;
        x = x + v * deltaT + (2/3.0) * a * deltaT2 - (1/6.0)  * previousAcceleration * deltaT2;
        double predV = v + (3/2.0) * a * deltaT - (1/2.0) * previousAcceleration * deltaT;
        double newForces = getActingForces(elasticConstant, damping, x, predV);
        double newA = newForces / mass;
        v = v + (1/3.0) * newA * deltaT + (5/6.0) * a * deltaT - (1/6.0) * previousAcceleration * deltaT;
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
