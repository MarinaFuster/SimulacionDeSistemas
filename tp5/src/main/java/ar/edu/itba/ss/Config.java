package ar.edu.itba.ss;

public class Config {

    private final double maxY; // [m] Max height of the space - we consider the min height of the space = 0

    private final double obstacleRadius;
    private final double pedestrianRadius;


    // [m/s] The abs value of speed will be constant, and the direction will be randomized for each obstacle
    private final double obstacleSpeed;


    private final double goalX;  // [m] X we need to reach for the goal
    private final double goalY;
    private final double startY;
    private final double startX = 0;

    private final double pedestrianToFirstObstacleDistance;
    private final double lastObstacleToGoalDistance;

    private final double cutoffTime;
    private final double deltaT;
    private final int saveFrequency;
    private final String name;
    private final String outFolder;

    public Config(double maxY, double obstacleRadius, double pedestrianRadius, double obstacleSpeed, double goalX, double goalY, double startY, double pedestrianToFirstObstacleDistance, double lastObstacleToGoalDistance, double cutoffTime, double deltaT, int saveFrequency, String name, String outFolder) {
        this.maxY = maxY;
        this.obstacleRadius = obstacleRadius;
        this.pedestrianRadius = pedestrianRadius;
        this.obstacleSpeed = obstacleSpeed;
        this.goalX = goalX;
        this.goalY = goalY;
        this.startY = startY;
        this.pedestrianToFirstObstacleDistance = pedestrianToFirstObstacleDistance;
        this.lastObstacleToGoalDistance = lastObstacleToGoalDistance;
        this.cutoffTime = cutoffTime;
        this.deltaT = deltaT;
        this.saveFrequency = saveFrequency;
        this.name = name;
        this.outFolder = outFolder;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getObstacleRadius() {
        return obstacleRadius;
    }

    public double getPedestrianRadius() {
        return pedestrianRadius;
    }

    public double getObstacleSpeed() {
        return obstacleSpeed;
    }

    public double getGoalX() {
        return goalX;
    }

    public double getGoalY() {
        return goalY;
    }

    public double getStartY() {
        return startY;
    }

    public double getStartX() {
        return startX;
    }

    public double getPedestrianToFirstObstacleDistance() {
        return pedestrianToFirstObstacleDistance;
    }

    public double getLastObstacleToGoalDistance() {
        return lastObstacleToGoalDistance;
    }

    public double getCutoffTime() {
        return cutoffTime;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public int getSaveFrequency() {
        return saveFrequency;
    }

    public String getName() {
        return name;
    }

    public String getOutFolder() {
        return outFolder;
    }
}

