package com.itba.edu.ar;


import com.itba.edu.ar.config.StaticConfig;

public class Particle implements Configurable{
    final double radius;


    private Point position;
    private Point speed;

    public Particle(double radius) {
        this.radius = radius;
    }

    public Particle(double radius, Point position) {
        this.radius = radius;
        this.position = position;
        this.speed = new Point(0,0);
    }

    public Particle(double radius, Point position, Point speed) {
        this.radius = radius;
        this.position = position;
        this.speed = speed;
    }

    public boolean overlaps(Particle otherParticle) {
        return position.distance(otherParticle.getPosition()) <= radius + otherParticle.getRadius();
    }

    public boolean interacts(Particle otherParticle) {
        return position.distance(otherParticle.getPosition()) <= radius + otherParticle.getRadius() + getStaticConfig().getInteractionRadius();
    }

    public double getRadius() {
        return radius;
    }

    public Point getPosition() {
        return position;
    }

    public Point getSpeed() {
        return speed;
    }
}
