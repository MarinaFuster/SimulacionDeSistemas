package ar.edu.itba.ss;

public class Particle {

    private Vector2D position;
    private Vector2D speed;
    private double radius;
    private double r,g,b;

    public Particle(Vector2D position, Vector2D speed, double radius, double r, double g, double b) {
        this.position = position;
        this.speed = speed;
        this.radius = radius;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public double getRadius() {
        return radius;
    }

    public double getR() {
        return r;
    }

    public double getG() {
        return g;
    }

    public double getB() {
        return b;
    }

    public Particle setPosition(Vector2D position) {
        this.position = position;
        return this;
    }

    public Particle setSpeed(Vector2D speed) {
        this.speed = speed;
        return this;
    }

    public Particle setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public Particle setR(double r) {
        this.r = r;
        return this;
    }

    public Particle setG(double g) {
        this.g = g;
        return this;
    }

    public Particle setB(double b) {
        this.b = b;
        return this;
    }
}
