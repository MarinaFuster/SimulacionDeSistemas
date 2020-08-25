package com.itba.edu.ar.game;

import com.itba.edu.ar.config.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Space3D implements Space {
    private final int dimension;
    private final int sideLength;
    private Cell[][][] cells;

    public Space3D(int dimension, int sideLength) {
        this.dimension = dimension;
        this.sideLength = sideLength;
        this.cells = new Cell[sideLength][sideLength][sideLength];
        this.initializeRandomSpace(1);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(int i=0; i<this.sideLength; i++) {
            for(int j=0; j<this.sideLength; j++){
                for(int k=0; k<this.sideLength; k++){
                    str.append(cells[i][j][k]);
                }
            }
            str.append("\n");
        }
        return str.toString();
    }

    public void initializeRandomSpace(double alivePercentage) {
        if(cells == null) throw new IllegalArgumentException("Cells should not be null");

        for(int i=0; i<sideLength; i++) {
            for(int j=0; j<sideLength; j++){
                for(int k=0; k<sideLength; k++){
                    cells[i][j][k] = new Cell(Cell.getRandomState());
                }
            }
        }
    }

    @Override
    public void applyRules() {
        Cell[][][] newCells = new Cell[sideLength][sideLength][sideLength];
        for(int i=0; i<sideLength; i++) {
            for(int j=0; j<sideLength; j++) {
                for(int k=0; k<sideLength; k++){
                    int aliveNeighbours = aliveNeighbours(i, j, k);
                    Cell newCell;
                    if((cells[i][j][k].isAlive() && (aliveNeighbours == 2 || aliveNeighbours == 3))
                            || (!cells[i][j][k].isAlive() && aliveNeighbours == 3)){
                        newCell = new Cell(CellState.ALIVE);
                    }
                    else{
                        newCell = new Cell(CellState.DEAD);
                    }
                    newCells[i][j][k] = newCell;
                }
            }
        }
        this.cells = newCells;
    }

    // TODO: static moore, with r=1, periodic contour generalize this?
    private int aliveNeighbours(int x, int y, int z) {
        int aliveNeighbours = 0;
        for(int i=-1; i<=1; i++) {
            for(int j=-1; j<=1; j++) {
                for(int k=-1; k<=1; k++) {
                    if(i==0 && j==0 && k==0) {}
                    else{
                        int periodicI = getPeriodicIndex(x+i);
                        int periodicJ = getPeriodicIndex(y+j);
                        int periodicK = getPeriodicIndex(z+k);
                        if(cells[periodicI][periodicJ][periodicK].isAlive()) aliveNeighbours++;
                    }
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
        FileWriter fw = new FileWriter(Constants.OUTPUT_FOLDER + epoch + ".txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("%d\n", epoch);
        for(int i=0; i<sideLength; i++){
            for(int j=0; j<sideLength; j++) {
                for(int k=0; k<sideLength; k++) {
                    if(cells[i][j][k].isAlive()) pw.printf("%d,%d,%d\n", i, j, k);
                }
            }
        }
        pw.close();
    }
}
