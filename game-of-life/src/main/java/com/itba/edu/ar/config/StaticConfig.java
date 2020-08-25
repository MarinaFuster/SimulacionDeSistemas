package com.itba.edu.ar.config;

public class StaticConfig {
    final private int dimension;
    final private int sideLength;
    final private int epochs;
    final private double alivePercentage;

    public StaticConfig(int dimension, int sideLength, int epochs, double alivePercentage) {
        this.dimension = dimension;
        this.sideLength = sideLength;
        this.epochs = epochs;
        this.alivePercentage = alivePercentage;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Static Configuration\n");
        str.append("Dimension: " + this.dimension + "D\n");
        str.append("Side Length: " + this.sideLength + "\n");
        str.append("Epochs: " + this.epochs + "\n");
        str.append("Starting Alive Percentage: " + this.alivePercentage + "\n");
        return str.toString();
    }

    public boolean isValid() {
        if(this.dimension < 2 || this.dimension > 3) {
            System.out.println("Dimension can only be 2 or 3");
            return false;
        }
        if(this.alivePercentage < 0.0 || this.alivePercentage > 1.0){
            System.out.println("Starting Alive Percentage must be between 0 and 1");
            return false;
        }
        return true;
    }

    public double getAlivePercentage() {
        return alivePercentage;
    }

    public int getDimension() {
        return dimension;
    }

    public int getSideLength() {
        return sideLength;
    }

    public int getEpochs() {
        return epochs;
    }
}
