package ar.edu.itba.ss.events;

import ar.edu.itba.ss.Particle;

public class WallXEvent extends WallEvent{


    public WallXEvent(double time, Particle particle) {
        super(time, particle);
    }

    @Override
    public void applyBounce() {
        this.particle.bounceX();
    }
}
