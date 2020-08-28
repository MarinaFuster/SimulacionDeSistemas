package com.itba.edu.ar.game;

import com.itba.edu.ar.config.ConfigConst;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class Space2D extends Space {

    private Cell[][] cells;

    public Space2D(int dimension, int sideLength, int center, double alivePercentage) {
        this.dimension = dimension;
        this.sideLength = sideLength;
        this.center = center;
        this.cells = new Cell[sideLength][sideLength];
        this.initializeRandomSpace(GameConst.INIT_SPACE_PERCENTAGE_2D, alivePercentage, this.cells);
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

    protected void randomAliveSpace(int from, int to, int aliveCells) {
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

    protected void sortedAliveSpace(int from, int to, int aliveCells){
        int currentAliveCells = 0;
        for(int i=from; i<=to && currentAliveCells < aliveCells; i++){
            for(int j=from; j<=to && currentAliveCells < aliveCells; j++){
                cells[i][j] = new Cell(CellState.ALIVE);
                currentAliveCells++;
            }
        }
    }

    protected void deadSpace() {
        for(int i=0; i<sideLength; i++) {
            for(int j=0; j<sideLength; j++){
                cells[i][j] = new Cell(CellState.DEAD);
            }
        }
    }

    /*
    *   Alive cells remain alive if they have 2 or 3 neighbours alive
    *   Dead cells turn into alive cells if they have 3 neighbours alive
    */
    public void applyRules() {
        Cell[][] newCells = new Cell[sideLength][sideLength];
        int newCellsAliveCount = 0;

        for(int i=0; i<sideLength; i++) {
            for(int j=0; j<sideLength; j++) {
                int aliveNeighbours = aliveNeighbours(i, j);
                Cell newCell;
                if((cells[i][j].isAlive() && (aliveNeighbours == (2 | 3)))
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

    public void save(int epoch) throws IOException {
        Date date = new Date();
        FileWriter fw = new FileWriter(ConfigConst.OUTPUT_FOLDER + new Timestamp(date.getTime()) + ".xyz", true);
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
