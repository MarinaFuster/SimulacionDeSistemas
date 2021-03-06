package ar.edu.itba.ss.events;

import ar.edu.itba.ss.Particle;

import java.util.Arrays;
import java.util.List;

public class ParticleEvent extends Event{
    private final Particle particleA;
    private final Particle particleB;
    private final int initCollisionCountA, initCollisionCountB;

    public ParticleEvent(double time, Particle particleA, Particle particleB) {
        super(time);
        this.particleA = particleA;
        this.particleB = particleB;
        this.initCollisionCountA = particleA.getCollisionCount();
        this.initCollisionCountB = particleB.getCollisionCount();
    }

    // If any of the two collision counts have changed, the event is invalid
    @Override
    public boolean wasSuperveningEvent() {
        return particleA.getCollisionCount() != initCollisionCountA ||
                particleB.getCollisionCount() != initCollisionCountB;
    }

    @Override
    public List<Particle> getParticles() {
        return Arrays.asList(particleA, particleB);
    }


    @Override
    public void applyBounce() {
        particleA.bounce(particleB);
    }
}
