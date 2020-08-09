package com.itba.edu.ar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CellIndexMethod {


    private double universeSideLength;
    private double cellsPerSide;
    private double cellLength;
    private Map<Integer,Map<Integer, Set<Integer>>> cells;



    public CellIndexMethod(double universeSideLength, int cellsPerSide) {
        this.universeSideLength = universeSideLength;
        this.cellsPerSide = cellsPerSide;
        this.cellLength = universeSideLength / cellsPerSide;

        // Initialize empty cells
        cells = new HashMap<>();
        for (int i = 0; i < cellsPerSide; i++) {
            cells.put(i, new HashMap<>());
            for (int j = 0; j < cellsPerSide; j++) {
                cells.get(i).put(j, new HashSet<>());
            }
        }
    }

    public void fillCells(Map<Integer, Particle> particleMap) {
        for (Map.Entry<Integer,Particle> entry : particleMap.entrySet()) {

            Particle p = entry.getValue();
            double x = p.getPosition().getX();
            double y = p.getPosition().getY();

            int xCell = (int) Math.floor(x / cellLength);
            int yCell = (int) Math.floor(y / cellLength);
            cells.get(xCell).get(yCell).add(entry.getKey());
        }
    }

    public Map<Integer, Map<Integer, Set<Integer>>> getCells() {
        return cells;
    }
}
