package com.itba.edu.ar.game;

import java.io.IOException;

public class GameOfLife {
    private final Space space;
    private final int epochs;

    public GameOfLife(int dimension, int sideLength, int epochs, double alivePercentage, int center) throws IOException {
        this.epochs = epochs;
        if(dimension == 2) this.space = new Space2D(dimension, sideLength, alivePercentage, center); // TODO: different init methods??
        else this.space = new Space3D(dimension, sideLength, center);
        this.space.save(0);
    }

    public void run() throws IOException{
        for(int i=1; i<=epochs; i++){
            space.applyRules();
            space.save(i);
        }
    }
}
