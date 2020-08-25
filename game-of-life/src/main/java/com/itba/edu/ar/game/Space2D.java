package com.itba.edu.ar.game;

import com.itba.edu.ar.config.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Space2D implements Space {

    private final int dimension;
    private final int sideLength;
    private int aliveCellCount;
    private Cell[][] cells;

    public Space2D(int dimension, int sideLength, double alivePercentage) {
        this.dimension = dimension;
        this.sideLength = sideLength;
        this.aliveCellCount = 0;
        this.cells = new Cell[sideLength][sideLength];
        this.initializeRandomSpace(alivePercentage);
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

    public void initializeRandomSpace(double alivePercentage) {
        if(cells == null) throw new IllegalArgumentException("Cells should not be null");

        this.deadSpace();

        int from = (int) Math.ceil(sideLength/2 - sideLength*0.1);
        int to = (int) Math.ceil(sideLength/2 + sideLength*0.1);
        int aliveCells = (int) Math.floor(Math.pow(from-to, 2)*alivePercentage);

        // TODO: add some sort of timeout
        if(alivePercentage > 0.7){
            sortedAliveSpace(from, to, aliveCells);
        }
        else{
            randomAliveSpace(from, to, aliveCells);
        }

        this.aliveCellCount = aliveCells;
    }

    private void randomAliveSpace(int from, int to, int aliveCells) {
        int currentAliveCells = 0;
        while(currentAliveCells < aliveCells) {
            int i = randIndex(from, to);
            int j = randIndex(from, to);
            if(!cells[i][j].isAlive()){
                cells[i][j] = new Cell(CellState.ALIVE);
                currentAliveCells++;
            }
        }
    }

    private void sortedAliveSpace(int from, int to, int aliveCells){
        int currentAliveCells = 0;
        for(int i=from; i<=to && currentAliveCells < aliveCells; i++){
            for(int j=from; j<=to && currentAliveCells < aliveCells; j++){
                cells[i][j] = new Cell(CellState.ALIVE);
                currentAliveCells++;
            }
        }
    }

    private void deadSpace() {
        for(int i=0; i<sideLength; i++) {
            for(int j=0; j<sideLength; j++){
                cells[i][j] = new Cell(CellState.DEAD);
            }
        }
    }

    private static int randIndex(int from, int to) {
        Random r = new Random();
        return r.nextInt((to-from)+1) + from;
    }

    @Override
    public void applyRules() {
        Cell[][] newCells = new Cell[sideLength][sideLength];
        int newCellsAliveCount = 0;

        for(int i=0; i<sideLength; i++) {
            for(int j=0; j<sideLength; j++) {
                int aliveNeighbours = aliveNeighbours(i, j);
                Cell newCell;
                if((cells[i][j].isAlive() && (aliveNeighbours == 2 || aliveNeighbours == 3))
                        || (!cells[i][j].isAlive() && aliveNeighbours == 3)){
                    newCell = new Cell(CellState.ALIVE);
                    newCellsAliveCount++;
                }
                else{
                    newCell = new Cell(CellState.DEAD);
                }
                newCells[i][j] = newCell;
            }
        }
        this.cells = newCells;
        this.aliveCellCount = newCellsAliveCount;
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
        pw.printf("%d\n\n", aliveCellCount);
        for(int i=0; i<sideLength; i++){
            for(int j=0; j<sideLength; j++) {
                if(cells[i][j].isAlive()) pw.printf("H\t%d\t%d\t0\n", i, j);
            }
        }
        pw.close();
    }
}
