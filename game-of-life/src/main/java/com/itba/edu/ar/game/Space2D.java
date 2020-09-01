package com.itba.edu.ar.game;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Space2D extends Space {

    private Cell[][] cells;

    public Space2D(int dimension, int sideLength, int center, double alivePercentage, Rule rule) {
        this.dimension = dimension;
        this.sideLength = sideLength;
        this.center = center;
        this.rule = rule;
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
                CellState state = chooseRule(rule, aliveNeighbours, cells[i][j].getState());
                if(state == CellState.ALIVE) newCellsAliveCount++;
                newCells[i][j] = new Cell(state);
            }
        }
        this.cells = newCells;
        this.aliveCellCount = newCellsAliveCount;
    }

    private CellState chooseRule(Rule rule, int aliveNeighbours, CellState state) {
        switch(rule){
            case CONWAY_2D_RULE_SET: {
                return Rules.conwayRule(aliveNeighbours, state);
            }
            case UNRESTRICTED_MULTIPLE_2D_RULE_SET: {
                return Rules.unrestrictedMultipleRule(aliveNeighbours, state);
            }
            case THIRD_2D_RULE_SET: {
                return Rules.testingRule(aliveNeighbours, state);
            }
            default: {
                throw new IllegalArgumentException("No existing rule for 2D space");
            }
        }
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

    public void save(int epoch  ) throws IOException {
        FileWriter fw = new FileWriter(getOutputFileName(epoch), true);
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
