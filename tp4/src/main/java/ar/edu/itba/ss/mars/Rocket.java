package ar.edu.itba.ss.mars;

public class Rocket extends Particle {
    private final  double creationTime = MarsSimulation.time;
    private double minMarsDistance = Double.MAX_VALUE;

    public Rocket(ParticleNames name, double x, double y, double vx, double vy, double mass, double radius, double visualizationRadius, double R, double G, double B) {
        super(name, x, y, vx, vy, mass, radius, visualizationRadius, R, G, B);
    }

    @Override
    public void applyChanges() {
        super.applyChanges();
        double distanceToMars = this.getPosition().distance(MarsSimulation.mars.getPosition());
        minMarsDistance = Math.min(minMarsDistance, distanceToMars);
    }

    public double getCreationTime() {
        return creationTime;
    }

    public double getMinMarsDistance() {
        return minMarsDistance;
    }
}
