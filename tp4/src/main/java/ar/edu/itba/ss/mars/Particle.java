package ar.edu.itba.ss.mars;

import ar.edu.itba.ss.Integrator;

import java.util.List;
import java.util.stream.Collectors;

public class Particle {

    private Vector2D position;
    private Vector2D speed;

    private final double mass;
    private final double radius;
    private final double visualizationRadius;


    // Previous values needed for beeman and predictor corrector
    // Beeman needs acceleration in t-deltaT
    private Vector2D previousAcceleration;

    // R G B
    private final double R,G,B;

    private ParticleNames name;

    public Particle(ParticleNames name, double x, double y, double vx, double vy, double mass, double radius, double visualizationRadius, double R, double G, double B) {
        this.position = new Vector2D(x,y);
        this.speed = new Vector2D(vx, vy);
        this.mass = mass;
        this.radius = radius;
        this.visualizationRadius = visualizationRadius;

        this.R = R;
        this.G = G;
        this.B = B;
    }


    // Returns 2d array of acting forces in x(0) and y(1)
    private Vector2D getActingForces(List<Particle> actingParticles) {
        return getActingForces(actingParticles, this.position, this.speed);
    }

        private Vector2D getActingForces(List<Particle> actingParticles, Vector2D position, Vector2D speed) {

            double totalForceX = 0;
            double totalForceY = 0;

            for (Particle p: actingParticles) {
                double f = Constants.GRAVITATIONAL_CONSTANT * mass * p.mass / position.distanceSquare(p.position);

                Vector2D e = p.position.sub(position).mul(1/position.distance(p.position));
                Vector2D projectedForces = e.mul(f);
                totalForceX += projectedForces.getX();
                totalForceY += projectedForces.getY();
            }

            return new Vector2D(totalForceX, totalForceY);
        }


    public void initializePreviousAcceleration(double deltaT, List<Particle> actingParticles) {
        Vector2D forces = getActingForces(actingParticles);
        Vector2D prevPosition = position.sub(speed.mul(deltaT)).add(forces.mul((1/2.0) * deltaT * deltaT));
        Vector2D prevSpeed = speed.sub(forces.mul(deltaT));
        previousAcceleration = getActingForces(actingParticles, prevPosition, prevSpeed).mul(1/mass);
    }

    private void applyVerlet(double deltaT, List<Particle> actingParticles) {
        Vector2D forces = getActingForces(actingParticles);
        speed = speed.add(forces.mul(deltaT/mass));
        position = position.add(speed.mul(deltaT)).add(forces.mul(deltaT*deltaT/(2*mass)));
    }

    public void applyIntegrator(Integrator integrator, double deltaT, List<Particle> actingParticles) {
        actingParticles = actingParticles.stream().filter(p-> p.name != name).collect(Collectors.toList());
        switch (integrator) {
            case VERLET:
                applyVerlet(deltaT, actingParticles);
                break;
            case BEEMAN:
//                applyBeeman(deltaT, actingParticles);
                break;
            case GEARPC:
//                applyGearPC(deltaT, actingParticles);
                break;
            default:
                throw new RuntimeException("Invalid Integrator");
        }
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getR() {
        return R;
    }

    public double getG() {
        return G;
    }

    public double getB() {
        return B;
    }

    public double getVisualizationRadius() {
        return visualizationRadius;
    }
}
