package com.itba.edu.ar;

import com.itba.edu.ar.config.Boundary;

public class Point implements Configurable {

    double x;
    double y;


    // Returns a random point in space, the
    public static Point  getRandomPoint(double maxX, double maxY) {
        return new Point(Math.random() * maxX, Math.random() * maxY);
    }

    public double distance (Point otherPoint) {
        double deltaX = getDifference(x, otherPoint.getX());
        double deltaY = getDifference(y, otherPoint.getY());
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public double getDifference(double a, double b) {
        double diff = b - a;
        if (getStaticConfig().getBoundaryMethod() == Boundary.INFINITE) {
            return Math.min(diff, getStaticConfig().getSideLength() - diff);
        } else {
            return diff;
        }
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
