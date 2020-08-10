package com.itba.edu.ar.config;

import com.itba.edu.ar.Particle;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

public class StaticConfig {

    final private int timePartition;
    final private int sampleSize;
    final private int cellsPerSide;
    final private double sideLength;
    final private double interactionRadius;
    final private Boundary boundaryMethod;
    final private Map<Integer, Particle> particles;

    public StaticConfig(int timePartition, int sampleSize, int cellsPerSide, double sideLength,
                        double interactionRadius, Boundary boundaryMethod, Map<Integer, Particle> particles) {
        this.timePartition = timePartition;
        this.sampleSize = sampleSize;
        this.cellsPerSide = cellsPerSide;
        this.sideLength = sideLength;
        this.interactionRadius = interactionRadius;
        this.boundaryMethod = boundaryMethod;
        this.particles = particles;
    }

    public int getTimePartition() {
        return timePartition;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public int getCellsPerSide() {
        return cellsPerSide;
    }

    public double getSideLength() {
        return sideLength;
    }

    public double getInteractionRadius() {
        return interactionRadius;
    }

    public Boundary getBoundaryMethod() {
        return boundaryMethod;
    }

    public Map<Integer, Particle> getParticles() {
        return particles;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Time Partition: ").append(timePartition).append("\n");
        s.append("Sample Size: ").append(sampleSize).append("\n");
        s.append("Side Length: ").append(sideLength).append("\n");
        s.append("Cells Per Side: ").append(cellsPerSide).append("\n");
        s.append("Interaction Radius: ").append(interactionRadius).append("\n");
        s.append("Boundary Method: ").append(boundaryMethod).append("\n");

        s.append("Particles:\n");
        int i = 0;
        for (Particle p : particles.values()) {
            s.append("Particle ").append(i++).append(" - Radius: ").append(p.getRadius()).append("\n");
        }
        return s.toString();
    }

    public boolean valid() {
        if (particles.size() != sampleSize)  {
            System.out.println(String.format("[ERROR} Sample size %d is different to particle amount %d", sampleSize, particles.size()));
            return false;
        }

        double maxParticleRadius  = particles.values().stream().max(Comparator.comparing(Particle::getRadius))
                .orElseThrow(NoSuchElementException::new).getRadius();
        double maxEffectiveInteractionRadius = 2 * maxParticleRadius + interactionRadius;
        if (sideLength / cellsPerSide <= maxEffectiveInteractionRadius) {
            System.out.println(String.format("Cell index method condition not valid: L / M (%.2f) is less than effective interaction radius %.2f",
                    sideLength / cellsPerSide, maxEffectiveInteractionRadius));
            return false;
        }
        return true;
    }
}
