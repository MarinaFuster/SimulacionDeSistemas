package com.itba.edu.ar;

import com.itba.edu.ar.config.AppConfig;
import com.itba.edu.ar.config.Boundary;
import com.itba.edu.ar.config.StaticConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Simulation {
    private int time = 0;
    Map<Integer, Particle> particleMap;

    public Simulation(Map<Integer, Particle> particleMap) {
        this.particleMap = particleMap;
    }

    public void run() throws IOException {
        AppConfig appConfig = App.getAppConfig();
        String outputFolder = appConfig.outputPath;
        int timePartition = App.getStaticConfig().getTimePartition();
        CellIndexMethod cim = new CellIndexMethod(App.getStaticConfig().getSideLength(), App.getStaticConfig().getCellsPerSide());
        cim.fillCells(particleMap);
        FileWriter fw = new FileWriter(outputFolder + time + ".txt");
        PrintWriter pw = new PrintWriter(fw);

        // CIM



        while (time <= appConfig.maxTime) {
            pw.println(time);
            for (Particle p : particleMap.values()) {
                pw.printf("%f,%f,%f,%f\n", p.getPosition().getX(), p.getPosition().getY(), p.getSpeed().getX(), p.getSpeed().getY());
            }

            // Calculate interactions
            Map<Integer, Set<Integer>> interactions = new HashMap<>();
            for (int i = 0; i < App.getStaticConfig().getCellsPerSide(); i++) {
                for (int j = 0; j < App.getStaticConfig().getCellsPerSide(); j++) {


                    // Check interactions in same cell
                    addInteractions(interactions, cim, i,j,i,j);
                    // In each cell we only check the cells to the right and the one that is at the bottom
                    // Check top right
                    addInteractions(interactions, cim, i, j, i + 1, j - 1);
                    addInteractions(interactions, cim, i, j, i + 1, j);
                    addInteractions(interactions, cim, i, j, i + 1, j + 1);
                    addInteractions(interactions, cim, i, j, i, j + 1);
                }
            }
            if (appConfig.printInteractionsList) {
                printInteractionList(outputFolder, interactions);
            }
            time++;
            if (time % App.getStaticConfig().getTimePartition() == 0) {
                fw.close();
                fw = new FileWriter(outputFolder + time + ".txt");
            }


        }
        fw.close();

    }

    public void printInteractionList(String outputFolder, Map<Integer, Set<Integer>> interactions) throws  IOException {
        FileWriter interactFw = new FileWriter(outputFolder + "InteractionList" + time + ".txt");
        PrintWriter interactPw = new PrintWriter(interactFw);

        Map<Integer, Set<Integer>> orderedInteractions = new TreeMap<>(interactions);
        for (int i = 0; i < particleMap.size(); i++) {
            orderedInteractions.computeIfAbsent(i, k -> new HashSet<>());
        }

        for (Map.Entry<Integer, Set<Integer>> entry : orderedInteractions.entrySet()) {

            boolean first = true;
            for (Integer i : entry.getValue()) {
                if (first) {
                    interactPw.print(i);
                    first = false;
                } else{
                    interactPw.print("," + i);
                }
            }
            interactPw.println();
        }
        interactFw.close();
    }

    public void addInteractions(Map<Integer,Set<Integer>> interactions, CellIndexMethod cim, int x1, int y1, int x2, int y2) {
        // First, check that both points are in the cell map
        StaticConfig config = App.getStaticConfig();
        int cps = config.getCellsPerSide();
        int maxX = cps - 1;
        int maxY = cps - 1;

        if(config.getBoundaryMethod() == Boundary.CLOSED) {
            if (x2 == -1 || y2 == -1) return;
            if (x2 > maxX || y2 > maxY) return;
        } else if(config.getBoundaryMethod() == Boundary.INFINITE) {
             if (x2 == -1) x2 += cps;
             if (y2 == -1) y2 += cps;
             if (x2 == cps) x2 = 0; // If we are at the end + 1, move it to 0
             if (y2 == cps) y2 = 0;
        } else {
            throw new RuntimeException("Invalid Boundary method");
        }

//        System.out.printf("Comparing cell (%d, %d) with (%d, %d)\n", x1,y1,x2,y2);

        // From now on both x1,y1 and x2,y2 are valid cells
        Set<Integer> cell1 = cim.getCells().get(x1).get(y1);
        Set<Integer> cell2 = cim.getCells().get(x2).get(y2);



        for (Integer id1 : cell1) {
            for (Integer id2 : cell2) {
                if (cell1 != cell2)  {
                    if (id1.equals(id2)) continue;
                } else {
                    // cell1 == cell2, we want to avoid duplicate checks
                    if (id1 >= id2) continue;
                }
                if (particleMap.get(id1).interacts(particleMap.get(id2))) {
                    interactions.computeIfAbsent(id1, k -> new HashSet<>());
                    interactions.computeIfAbsent(id2, k -> new HashSet<>());

                    interactions.get(id1).add(id2);
                    interactions.get(id2).add(id1);
                }

            }
        }


    }

}
