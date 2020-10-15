package ar.edu.itba.ss.mars;

public class Rocket extends Particle {
    private final  double creationTime = MarsSimulation.time;
    private double minTargetDistance = Double.MAX_VALUE;

    public Rocket(ParticleNames name, double x, double y, double vx, double vy, double mass, double radius, double visualizationRadius, double R, double G, double B) {
        super(name, x, y, vx, vy, mass, radius, visualizationRadius, R, G, B);
    }

    @Override
    public void applyChanges() {
        super.applyChanges();
        double distanceToTarget = this.getPosition().distance(MarsSimulation.targetPlanet.getPosition());
        minTargetDistance = Math.min(minTargetDistance, distanceToTarget);
        if (distanceToTarget <= MarsSimulation.targetPlanet.getRadius()) {
            MarsSimulation.crashed = true;
            System.out.println("crashed! start time: " + creationTime);
        }
    }

    public double getCreationTime() {
        return creationTime;
    }

    public double getMinTargetDistance() {
        return minTargetDistance;
    }
}
