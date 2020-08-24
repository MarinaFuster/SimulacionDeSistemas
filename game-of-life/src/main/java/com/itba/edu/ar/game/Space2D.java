package com.itba.edu.ar.game;

import com.itba.edu.ar.config.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Space2D implements Space {

    private final int dimension;
    private final int sideLength;
    private Cell[][] cells;

    public Space2D(int dimension, int sideLength) {
        this.dimension = dimension;
        this.sideLength = sideLength;
        this.cells = new Cell[sideLength][sideLength]; // TODO: assuming cell is 1x1
        this.initializeRandomSpace();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(int i=0; i<this.sideLength; i++) {
            for(int j=0; j<this.sideLength; j++){
                str.append(cells[i][j]);
            }
            str.append("\n");
        }
        return str.toString();
    }

    public void initializeRandomSpace() {
        if(cells == null) throw new IllegalArgumentException("Cells should not be null");

        for(int i=0; i<this.sideLength; i++) {
            for(int j=0; j<this.sideLength; j++){
                cells[i][j] = new Cell(Cell.getRandomState());
            }
        }
    }

    @Override
    public void applyRules() {
        Cell[][] newCells = new Cell[sideLength][sideLength];
        for(int i=0; i<sideLength; i++) {
            for(int j=0; j<sideLength; j++) {
                int aliveNeighbours = aliveNeighbours(i, j);
                Cell newCell;
                if((cells[i][j].isAlive() && (aliveNeighbours == 2 || aliveNeighbours == 3))
                        || (!cells[i][j].isAlive() && aliveNeighbours == 3)){
                    newCell = new Cell(CellState.ALIVE);
                }
                else{
                    newCell = new Cell(CellState.DEAD);
                }
                newCells[i][j] = newCell;
            }
        }
        this.cells = newCells;
    }

    // TODO: static moore, with r=1, periodic contour generalize this?
    private int aliveNeighbours(int x, int y) {
        int aliveNeighbours = 0;
        for(int i=-1; i<=1; i++) {
            for(int j=-1; j<=1; j++) {
                if(i==0 && j==0) {}
                else{
                    int periodicI = getPeriodicIndex(x+i);
                    int periodicJ = getPeriodicIndex(y+j);
                    if(cells[periodicI][periodicJ].isAlive()) aliveNeighbours++;
                }
            }
        }
        return aliveNeighbours;
    }

    private int getPeriodicIndex(int index) {
        if(index == -1) return sideLength-1;
        if(index == sideLength) return 0;
        else return index;
    }

    @Override
    public void save(int epoch) throws IOException {
        FileWriter fw = new FileWriter(Constants.OUTPUT_FOLDER +  "test.xyz", true);
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("%d\n\n", getCellCount());
        for(int i=0; i<sideLength; i++){
            for(int j=0; j<sideLength; j++) {
                if(cells[i][j].isAlive()) pw.printf("H\t%d\t%d\t0\n", i, j);
            }
        }
        pw.close();
    }

    private int getCellCount() {
        int count = 0;
        for(int i=0; i<sideLength; i++){
            for(int j=0; j<sideLength; j++) {
                if(cells[i][j].isAlive()) count += 1;
            }
        }
        return count;
    }

    public int getDimension() {
        return dimension;
    }

    public int getSideLength() {
        return sideLength;
    }

    public Cell[][] getCells() {
        return cells;
    }
}
