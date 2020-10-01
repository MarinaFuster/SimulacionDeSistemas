package ar.edu.itba.ss.oscillator;

import ar.edu.itba.ss.Integrator;

public class OscillatorParticle {

    private double x;
    private double v;
    private double mass;
    private double currentTime;
    private double A;

    // Previous values needed for beeman and predictor corrector
    // Beeman needs acceleration in t-deltaT
    private double previousAcceleration;
    private double previousRVector[];


    public OscillatorParticle(double x, double v, double mass) {
        this.x = x;
        this.v = v;
        this.mass = mass;
        this.currentTime = 0;
        this.A = x;
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
        if(this.previousRVector == null) {
            initializeRVector(elasticConstant, damping);
        }

        // multiplier
        double matrix[][] = multiplier(deltaT);

        // initialize predicted r
        double RPredictedVector[] = new double[6];

        // calculates predicted r
        for(int i=0; i<6; i++) {
            for(int j=0; j<6; j++) {
                RPredictedVector[i] += matrix[i][j]*previousRVector[j];
            }
        }

        double r2tdeltaT = getActingForces(elasticConstant, damping, RPredictedVector[0], RPredictedVector[1])/mass;
        double r2ptdeltaT = RPredictedVector[2];
        double deltaA =r2tdeltaT - r2ptdeltaT; // TODO: check if this should be abs

        // correction
        double deltaR2 = deltaA * deltaT * deltaT / 2;

        // alphas to correct predicted values
        double c[] = {3/16.0, 251/360.0, 1, 11/18.0, 1/6.0, 1/60.0};

        // updates x and v
        x = RPredictedVector[0] + c[0]*deltaR2 * 0 / Math.pow(deltaT, 0);
        v = RPredictedVector[1] + c[1]*deltaR2 * 1 / Math.pow(deltaT, 1);

        // apply corrections and save them
        for(int i=0; i<6; i++) {
            previousRVector[i] = RPredictedVector[i] + c[i]*deltaR2 * i / Math.pow(deltaT, i);
        }
    }

    private void initializeRVector(double elasticConstant, double damping) {
        this.previousRVector = new double[6];
        this.previousRVector[0] = x;
        this.previousRVector[1] = v;
        this.previousRVector[2] = getActingForces(elasticConstant, damping)/mass;
        this.previousRVector[3] = 0;
        this.previousRVector[4] = 0;
        this.previousRVector[5] = 0;
    }

    private double[][] multiplier(double deltaT) {
        double matrix[][] = {
                {1, deltaT, Math.pow(deltaT,2)/2, Math.pow(deltaT,3)/6, Math.pow(deltaT,4)/24, Math.pow(deltaT,5)/120},
                {0, 1, deltaT, Math.pow(deltaT,2)/2, Math.pow(deltaT,3)/6, Math.pow(deltaT,4)/24},
                {0, 0, 1, deltaT, Math.pow(deltaT,2)/2, Math.pow(deltaT,3)/6},
                {0, 0, 0, 1, deltaT, Math.pow(deltaT,2)/2},
                {0, 0, 0, 0, 1, deltaT},
                {0, 0, 0, 0, 0, 1}};
        return matrix;
    }

    private void applyAnalytic(double deltaT, double elasticConstant, double damping) {
        double t = currentTime + deltaT;
        x = A * Math.exp(-1 * damping / (2*mass) * t) *
                Math.cos(Math.pow(elasticConstant/mass - damping*damping/(4*mass*mass), 0.5)*t);
        currentTime = t;
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
            case ANALYTIC:
                applyAnalytic(deltaT, elasticConstant, damping);
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
