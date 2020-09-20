package ar.edu.itba.ss;

public class WallEvent extends Event{

    private final Particle particle;
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
}
