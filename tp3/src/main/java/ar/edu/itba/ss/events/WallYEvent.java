package ar.edu.itba.ss.events;

import ar.edu.itba.ss.Particle;

public class WallYEvent extends WallEvent{


    public WallYEvent(double time, Particle particle) {
        super(time, particle);
    }

    @Override
    public void applyBounce() {
        this.particle.bounceY();
    }
}
