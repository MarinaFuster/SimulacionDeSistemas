package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Pedestrian extends Particle{
    public Pedestrian(Vector2D position, double radius) {
        super(position, new Vector2D(0,0), radius, 1, 1, 1);
    }




    public void advance(double deltaT) {

        List<Vector2D> actingForces = new LinkedList<>();
        actingForces.add(getGoalForce());
        actingForces.addAll(getWallForces());

        Vector2D forcesSum = new Vector2D(0,0);

        for (Vector2D force : actingForces) {
            forcesSum = forcesSum.add(force);
        }
        System.out.println("Acting forces: " + forcesSum);
        Vector2D vDes = getSpeed().add(forcesSum.mul(deltaT));

        PriorityQueue<Collision> futureCollisions = getFutureCollisions(vDes);

        

        Vector2D newPosition = getPosition().add(vDes.mul(deltaT));
        setPosition(newPosition);
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
            Vector2D r = getPosition().sub(p.getPosition());
            Vector2D v = vDes.sub(p.getSpeed());
            double a = v.getX() * v.getX() + v.getY() * v.getY();
            double b = (-2) * r.getX() * v.getX() - 2 * r.getY() * v.getY();
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

        Vector2D nUpper = new Vector2D(0, -1);
        Vector2D nLower = new Vector2D(0, 1);

        double diLower = getPosition().getY();
        double diUpper = Simulation.config.getMaxY() - getPosition().getY();

        // Upper wall
        addWallForce(ds, forces, nUpper, diUpper);

        // Lower wall
        addWallForce(ds, forces, nLower, diLower);
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
}
