package ar.edu.itba.ss;

import java.util.Random;

public class ConfigBuilder {
    private double maxY = 20;
    private double obstacleRadius = 1;
    private double pedestrianRadius = 1;
    private double obstacleSpeed = 1;
    private double goalX = 50;

    // Default initial Y will be from pedestrianRandom/2 to maxY - pedestrianRadius / 2
    private double goalY = Math.random() * (maxY - pedestrianRadius) + (pedestrianRadius / 2);
    private double startY = Math.random() * (maxY - pedestrianRadius) + (pedestrianRadius / 2);;

    private double pedestrianToFirstObstacleDistance = 5;
    private double lastObstacleToGoalDistance = 5;

    private double cutoffTime = 1000;
    private double deltaT = 1;
    private int saveFrequency = 1;
    private String name = "unnamed";
    private String outFolder = "./output/";;

    public ConfigBuilder maxY(double maxY) {
        this.maxY = maxY;
        return this;
    }

    public ConfigBuilder obstacleRadius(double obstacleRadius) {
        this.obstacleRadius = obstacleRadius;
        return this;
    }

    public ConfigBuilder pedestrianRadius(double pedestrianRadius) {
        this.pedestrianRadius = pedestrianRadius;
        return this;
    }

    public ConfigBuilder obstacleSpeed(double obstacleSpeed) {
        this.obstacleSpeed = obstacleSpeed;
        return this;
    }

    public ConfigBuilder goalX(double goalX) {
        this.goalX = goalX;
        return this;
    }

    public ConfigBuilder goalY(double goalY) {
        this.goalY = goalY;
        return this;
    }

    public ConfigBuilder startY(double startY) {
        this.startY = startY;
        return this;
    }

    public ConfigBuilder pedestrianToFirstObstacleDistance(double pedestrianToFirstObstacleDistance) {
        this.pedestrianToFirstObstacleDistance = pedestrianToFirstObstacleDistance;
        return this;
    }

    public ConfigBuilder lastObstacleToGoalDistance(double lastObstacleToGoalDistance) {
        this.lastObstacleToGoalDistance = lastObstacleToGoalDistance;
        return this;
    }

    public ConfigBuilder cutoffTime(double cutoffTime) {
        this.cutoffTime = cutoffTime;
        return this;
    }

    public ConfigBuilder deltaT(double deltaT) {
        this.deltaT = deltaT;
        return this;
    }

    public ConfigBuilder saveFrequency(int saveFrequency) {
        this.saveFrequency = saveFrequency;
        return this;
    }

    public ConfigBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ConfigBuilder outFolder(String outFolder) {
        this.outFolder = outFolder;
        return this;
    }

    public Config createConfig() {
        return new Config(maxY, obstacleRadius, pedestrianRadius, obstacleSpeed, goalX, goalY, startY, pedestrianToFirstObstacleDistance, lastObstacleToGoalDistance, cutoffTime, deltaT, saveFrequency, name, outFolder);
    }
}