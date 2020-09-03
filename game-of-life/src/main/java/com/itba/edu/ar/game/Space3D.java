package com.itba.edu.ar.game;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Space3D extends Space {

    private Cell[][][] cells;

    public Space3D(int dimension, int sideLength, int center, double alivePercentage, Rule rule, String name) {
        this.dimension = dimension;
        this.sideLength = sideLength;
        this.center = center;
        this.rule = rule;
        this.cells = new Cell[sideLength][sideLength][sideLength];
        this.initializeRandomSpace(GameConst.INIT_SPACE_PERCENTAGE_3D, alivePercentage, this.cells);
        this.name = name;
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

    @Override
    protected void randomAliveSpace(int from, int to, int aliveCells) {
        int currentAliveCells = 0;
        while(currentAliveCells < aliveCells) {
            int i = randIndex(from, to);
            int j = randIndex(from, to);
            int k = randIndex(from, to);
            if(!cells[i][j][k].isAlive()){
                cells[i][j][k] = new Cell(CellState.ALIVE);
                currentAliveCells++;
            }
        }
    }

    @Override
    protected void sortedAliveSpace(int from, int to, int aliveCells){
        int currentAliveCells = 0;
        for(int i=from; i<=to && currentAliveCells < aliveCells; i++){
            for(int j=from; j<=to && currentAliveCells < aliveCells; j++){
                for(int k=from; k<=to && currentAliveCells < aliveCells; k++){
                    cells[i][j][k] = new Cell(CellState.ALIVE);
                    currentAliveCells++;
                }
            }
        }
    }

    @Override
    protected void deadSpace() {
        for(int i=0; i<sideLength; i++) {
            for(int j=0; j<sideLength; j++) {
                for(int k=0; k<sideLength; k++) {
                    cells[i][j][k] = new Cell(CellState.DEAD);
                }
            }
        }
    }

    @Override
    public void applyRules() {
        Cell[][][] newCells = new Cell[sideLength][sideLength][sideLength];
        int newCellsAliveCount = 0;
        for(int i=0; i<sideLength; i++) {
            for(int j=0; j<sideLength; j++) {
                for(int k=0; k<sideLength; k++){
                    int aliveNeighbours = aliveNeighbours(i, j, k);
                    CellState state = chooseRule(rule, aliveNeighbours, cells[i][j][k].getState());
                    if(state == CellState.ALIVE) newCellsAliveCount++;
                    newCells[i][j][k] = new Cell(state);
                }
            }
        }
        this.aliveCellCount = newCellsAliveCount;
        this.cells = newCells;
    }

    private CellState chooseRule(Rule rule, int aliveNeighbours, CellState state) {
        switch(rule){
            case PRIME_BETWEEN_4_AND_20_PRIME_RULE_SET: {
                return Rules.between4And20PrimeRule(aliveNeighbours, state);
            }
            case PRIME_LESS_40_PRIME_3D_RULE_SET: {
                return Rules.lessThan40PrimeRule(aliveNeighbours, state);
            }
            case DIFFERENT_MULTIPLES_RULE_SET: {
                return Rules.differentMultiplesRule(aliveNeighbours, state, aliveCellCount, sideLength);
            }
            default: {
                throw new IllegalArgumentException("No existing rule for 3D space");
            }
        }
    }

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

    @Override
    public void save(int epoch) throws IOException {
        FileWriter fw = new FileWriter(getOutputFileName(epoch), true);
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("%d\n\n", aliveCellCount);
        for(int i=0; i<sideLength; i++){
            for(int j=0; j<sideLength; j++) {
                for(int k=0; k<sideLength; k++){
                    if(cells[i][j][k].isAlive()) pw.printf("H\t%d\t%d\t%d\n", i, j, k);
                }
            }
        }
        pw.close();
    }
}
