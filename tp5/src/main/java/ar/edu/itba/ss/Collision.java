package ar.edu.itba.ss;

public class Collision implements Comparable<Collision>{


    private final double time;
    private final Particle particle;

    public Collision(Particle particle, double time) {
        this.time = time;
        this.particle = particle;
    }

    public double getTime() {
        return time;
    }

    public Particle getParticle() {
        return particle;
    }

    @Override
    public int compareTo(Collision o) {
        return Double.compare(time, o.time);
    }
}
