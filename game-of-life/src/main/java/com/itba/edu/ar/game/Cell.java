package com.itba.edu.ar.game;

import java.util.Random;

public class Cell {
    private final CellState state;

    public Cell(CellState state) {
        this.state = state;
    }

    public CellState getState() {
        return state;
    }

    public static CellState getRandomState() {
//        double r = Math.random();
//        return r < 0.4 ? CellState.ALIVE : CellState.DEAD;
        int randomState = new Random().nextInt(CellState.values().length);
        return CellState.values()[randomState];
    }

    public boolean isAlive() {
        return state == CellState.ALIVE;
    }

    @Override
    public String toString() {
        if(this.state == CellState.ALIVE) return "[A]";
        return "[ ]";
    }
}
