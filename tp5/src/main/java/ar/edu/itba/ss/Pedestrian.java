package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Pedestrian extends Particle{

    private double mass = 60;
    public Pedestrian(Vector2D position, double radius) {
        super(position, new Vector2D(0,0), radius, 1, 1, 1);
    }

    public double maxVelocity = 0;
    public boolean reachedObjective = false;



    public void advance(double deltaT) {

        List<Vector2D> actingForces = new LinkedList<>();
        actingForces.add(getGoalForce());
        actingForces.addAll(getWallForces());

        Vector2D forcesSum = new Vector2D(0,0);

        for (Vector2D force : actingForces) {
            forcesSum = forcesSum.add(force);
        }

        double uMax = Simulation.config.getMaxVelocity();
        Vector2D vDes = getSpeed().add(forcesSum.mul(deltaT / mass));
        if (vDes.module() > uMax) {
            vDes = vDes.mul(uMax/ vDes.module());
        }
        PriorityQueue<Collision> futureCollisions = getFutureCollisions(vDes);

        Vector2D obstaclesForces = new Vector2D(0,0);

        int i = 0;

        int[] weights = new int[Simulation.config.getCollisionsToKeep()];
        // TODO: Esto puede esta rmal
        for (int j = 0; j < weights.length; j++) {
            weights[i] = Simulation.config.getCollisionsToKeep() - i;
        }
        while ((!futureCollisions.isEmpty()) && (i < Simulation.config.getCollisionsToKeep())) {
            Collision c = futureCollisions.poll();
            c.getParticle().setG(0);
            assert c != null;
            obstaclesForces = obstaclesForces.add(getPedestrianForce(c, vDes).mul(weights[i]));
            i++;
        }


        Vector2D totalForces = forcesSum.add(obstaclesForces);

        // Verlet - Euler modificado
        setSpeed(getSpeed().add(totalForces.mul(deltaT/mass)));
        if (getSpeed().module() > uMax) {
            setSpeed(getSpeed().mul(uMax/ getSpeed().module()));
        }
        setPosition(getPosition().add(getSpeed().mul(deltaT)).add(totalForces.mul(deltaT*deltaT/(2*mass))));

        Vector2D goalPosition = Simulation.goal.getPosition();
        if (Math.abs(getPosition().getX() - goalPosition.getX()) <= getRadius()) {
            if (Math.abs(getPosition().getY() - goalPosition.getY()) <= 3*getRadius()) {
                reachedObjective = true;
            }
        }
        maxVelocity = Math.max(maxVelocity, getSpeed().module());
    }

    private PriorityQueue<Collision> getFutureCollisions(Vector2D vDes) {
        PriorityQueue<Collision> futureCollisions = new PriorityQueue<>();

        for (Particle p : Simulation.obstacles) {

            /*
            modulo(r - vt) = raiz((rx- vxt ) ^2  + (ry - vyt ) ^2  )

            (ro + rj)^2 =(rx- vxt ) ^2  + (ry - vyt ) ^2

            (ro + rj) ^2 = (rx)^2 - 2 rx vxt + vx^2 t^2 + ry^2 - 2 ry vy t + vy^2  t^2

            0 = t^2 (vx ^2 + vy^2) + t (-2 rx vx - 2 ry vy) + t^0 (rx^2 + ry^2 - (ro + rj) ^2)

             */
            double ro = Simulation.config.getSafePedestrianDistance();
            Vector2D r = p.getPosition().sub(getPosition());
            Vector2D v = vDes.sub(p.getSpeed());
            double a = v.getX() * v.getX() + v.getY() * v.getY();
            double b = ((-2) * r.getX() * v.getX()) - (2 * r.getY() * v.getY());
            double c = r.getX() * r.getX() + r.getY() * r.getY() - Math.pow(ro + p.getRadius(), 2);
            double result = b*b - 4 * a * c;
            // Dos t
            if (result > 0) {
                double t1 = (-b + Math.pow(result, 0.5)) / (2.0 *a);
                double t2 = (-b - Math.pow(result, 0.5)) / (2.0 *a);

                if (t1 <= 0 && t2 <= 0) {
                    // Do nothing
                } else if (t1>= 0 && t2 >= 0) {
                    double collisionTime = Math.min(t1, t2);
                    if (collisionTime <= Simulation.config.getAnticipationTime()) {
                        futureCollisions.add(new Collision(p, collisionTime));
                    }
                } else {
                    futureCollisions.add(new Collision(p, 0));
                }
            } else {
                //If the equation has no solution or a single solution, then no collision takes place
            }
        }

        return futureCollisions;
    }

    public Vector2D getGoalForce() {

        double tau = Simulation.config.getIdealAccelerationTime();
        double upref = Simulation.config.getDesiredVelocity();
        Particle goal = Simulation.goal;
        Vector2D n = goal.getPosition().sub(getPosition()).mul(1/ getPosition().distance(goal.getPosition()));
        // F = (u *N - V) * 1/tau
        return n.mul(upref).sub(getSpeed()).mul(1/tau);
    }



    public List<Vector2D> getWallForces() {
        // We must compute collisions with top and bottom wall


        double ds = Simulation.config.getSafeWallDistance();

        List<Vector2D> forces = new ArrayList<>(2);

        Vector2D nUpper = new Vector2D(0, 1);
        Vector2D nLower = new Vector2D(0, -1);
        Vector2D nLeft = new Vector2D(-1, 0);
        Vector2D nRight = new Vector2D(1, 0);

        double diLower = getPosition().getY();
        double diUpper = Simulation.config.getMaxY() - getPosition().getY();


        double diLeft  = getPosition().getX();
        double diRight = Simulation.goal.getPosition().getX() + 5 - getPosition().getX();
        addWallForce(ds, forces, nUpper, diUpper);
        addWallForce(ds, forces, nLower, diLower);
        addWallForce(ds, forces, nLeft, diLeft);
        addWallForce(ds, forces, nRight, diRight);
        return forces;
    }

    private void addWallForce(double ds, List<Vector2D> forces, Vector2D n, double di) {
        if (di - getRadius() < ds) {
            double magnitude = ds + getRadius() - di / Math.pow(di - getRadius(), Simulation.config.getWallSteepness());
            forces.add(n.mul(magnitude));
        } else {
            forces.add(new Vector2D(0,0));
        }

    }

    private Vector2D getPedestrianForce(Collision collision, Vector2D vDes) {
        Particle o = collision.getParticle();
        Vector2D ci = getPosition().add(vDes.mul(collision.getTime()));
        Vector2D cj = o.getPosition().add(o.getSpeed().mul(collision.getTime()));
        Vector2D n = ci.sub(cj).mul(1/(ci.distance(cj)));


        double forceMultiplier = 3;
        double D = ci.distance(getPosition()) + ci.distance(cj) - getRadius() - o.getRadius();
        return n.mul(forceMultiplier * getEvasiveForceMagnitude(D));

    }

    private double getEvasiveForceMagnitude(double distance) {
        double a = 5; // Va a haber que sacarlo de config
        double dmin = Simulation.config.getDmin();
        double dmid = Simulation.config.getDmid();
        double dmax = Simulation.config.getDmax();
        if (distance < dmin) {
            // return a  * dmin / (distance);
            double k = a / Math.pow(2, -dmin);
            return k * Math.pow(2, -distance);
        } else if ( distance < dmid) {
            return a;
        } else if ( distance < dmax){
            double m = a / (dmid - dmax);
            double b = (-1) * a  * dmax / (dmid - dmax);
            return  m* distance + b;
        } else {
            return 0;
        }
    }
}
