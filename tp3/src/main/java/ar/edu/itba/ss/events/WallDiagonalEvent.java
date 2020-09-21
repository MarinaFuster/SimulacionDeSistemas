package ar.edu.itba.ss.events;

import ar.edu.itba.ss.Particle;

public class WallDiagonalEvent extends WallEvent{


    public WallDiagonalEvent(double time, Particle particle) {
        super(time, particle);
    }

    @Override
    public void applyBounce() {
        // TODO diagonal: Creo que es tan simple como descomentar estos 2 metodos de abajo?
//        this.particle.bounceX();
//        this.particle.bounceY();
    }
}
