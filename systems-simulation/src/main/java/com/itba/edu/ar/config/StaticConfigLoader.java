package com.itba.edu.ar.config;

import com.itba.edu.ar.Particle;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StaticConfigLoader {

    public static StaticConfig load(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int timePartition = getSingleInteger(br.readLine(), "Time partition");
            int sampleSize = getSingleInteger(br.readLine(), "Sample size");
            double sideLength = getSingleDouble(br.readLine(), "Side length");
            int cellsPerSide = getSingleInteger(br.readLine(), "Cells per side");
            double interactionRadius = getSingleDouble(br.readLine(), "Interaction radius");
            Boundary boundaryMethod = Boundary.valueOf(getSingleInteger(br.readLine(), "Boundary method"))
                    .orElseThrow(() -> new IllegalArgumentException("Illegal boundary value"));
            Map<Integer, Particle> particlesMap = new HashMap<>();
            for (int i = 0; i < sampleSize; i++) {
                Particle p = getParticle(br.readLine());
                particlesMap.put(i, p);
            }

            // If there is anything left in the buffer, the format is incorrect
            if (br.readLine() != null) {
                throw new IllegalArgumentException("Static config has more lines than it should");
            }
            return new StaticConfig(timePartition, sampleSize, cellsPerSide, sideLength, interactionRadius, boundaryMethod, particlesMap);
        }
    }

    public static int getSingleInteger(String configLine, String parameterName) {
        if (configLine == null) throw new IllegalArgumentException("Static config is too short");
        String[] values = configLine.split(",");
        if (values.length != 1) throw new IllegalArgumentException(String.format("%s should only have 1 integer value", parameterName));
        return  Integer.parseInt(values[0]);
    }

    public static double getSingleDouble(String configLine, String parameterName) {
        if (configLine == null) throw new IllegalArgumentException("Static config is too short");
        String[] values = configLine.split(",");
        if (values.length != 1) throw new IllegalArgumentException(String.format("%s should only have 1 'double' value", parameterName));
        return  Double.parseDouble(values[0]);
    }

    public static Particle getParticle(String configLine) {
        if (configLine == null) throw new IllegalArgumentException("Static config is too short (missing particle properties)");
        String[] values = configLine.split(",");
        // TODO For now we only get the first value which is the radius. Will change when needed
        return new Particle(Double.parseDouble(values[0]));
    }

    public static void save(StaticConfig staticConfig, String path) throws IOException {
        FileWriter fw = new FileWriter(path);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(staticConfig.getTimePartition());
        pw.println(staticConfig.getSampleSize());
        pw.println(staticConfig.getSideLength());
        pw.println(staticConfig.getCellsPerSide());
        pw.println(staticConfig.getInteractionRadius());
        pw.println(staticConfig.getBoundaryMethod().ordinal());

        for (Particle p : staticConfig.getParticles().values()) {
            pw.printf("%f\n", p.getRadius());
        }
        pw.close();

    }
}
