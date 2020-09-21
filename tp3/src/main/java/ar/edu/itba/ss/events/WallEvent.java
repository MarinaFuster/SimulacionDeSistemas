package ar.edu.itba.ss.events;

import ar.edu.itba.ss.Particle;

import java.util.Arrays;
import java.util.List;

public abstract class WallEvent extends Event{

    /* package */ final Particle particle;
    private final int initCollisionCount;

    public WallEvent(double time, Particle particle) {
        super(time);
        this.particle = particle;
        this.initCollisionCount = particle.getCollisionCount();
    }

    @Override
    public boolean wasSuperveningEvent() {
        return particle.getCollisionCount() != initCollisionCount;
    }

    @Override
    public List<Particle> getParticles() {
        return Arrays.asList(particle);
    }
}
