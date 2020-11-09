package ar.edu.itba.ss;

public class Obstacle extends Particle{
    public Obstacle(Vector2D position, Vector2D speed, double radius) {
        super(position, speed, radius, 1, 1, 0);
    }


    public void advance(double time) {
        double x = getPosition().getX();
        double deltaY = getSpeed().getY() * time;
        double maxY = Simulation.config.getMaxY();

        double newSpeed = getSpeed().getY();
        double newY = getPosition().getY() + deltaY;
        if(newY < 0) {
            newY = -1 * newY;
            setSpeed(new Vector2D(0, -1 * newSpeed));
        } else if(newY > maxY) {
            newY = 2 * maxY - newY;
            setSpeed(new Vector2D(0, -1 * newSpeed));
        }
        setPosition(new Vector2D(x, newY));
    }
}
