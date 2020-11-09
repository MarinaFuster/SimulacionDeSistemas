package ar.edu.itba.ss;

public class Vector2D {

    private final double x;
    private final double y;


    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D sub(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D mul(double scalar) {
        return new Vector2D(x * scalar, y * scalar);
    }

    public double distance(Vector2D other) {
        return Math.sqrt(distanceSquare(other));
    }

    public double distanceSquare(Vector2D other) {
        return Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2);
    }

    public double module() {
        return Math.sqrt(x*x+y*x);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
