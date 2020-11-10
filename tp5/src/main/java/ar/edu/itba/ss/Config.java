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
    private final double startX;

    private final double pedestrianToFirstObstacleDistance;
    private final double lastObstacleToGoalDistance;

    private final double cutoffTime;
    private final double deltaT;
    private final int saveFrequency;
    private final String name;
    private final String outFolder;

    private final double maxVelocity; // Velocidad maxima del pedestrian
    private final double desiredVelocity; // Velocidad deseada del pedestrian (sin obstaculos)
    private final double idealAccelerationTime; // Tiempo que tarda en llegar a desiredVelocity
    private final double safeWallDistance; // Distancia segura del pedestrian y las paredes
    private final double wallSteepness; // "Steepness de las paredes"
    private final double safePedestrianDistance; // radio psicologico
    private final double anticipationTime;
    private final int collisionsToKeep;


    private final double dmin, dmid, dmax; // Parametros para la funcion de repulsion entre pedestrian y obstaculos


    public double getIdealAccelerationTime() {
        return idealAccelerationTime;
    }

    public Config(double startX, double idealAccelerationTime, double maxY, double obstacleRadius, double pedestrianRadius, double obstacleSpeed, double goalX, double goalY, double startY, double pedestrianToFirstObstacleDistance, double lastObstacleToGoalDistance, double cutoffTime, double deltaT, int saveFrequency, String name, String outFolder, double maxVelocity, double desiredVelocity, double safeWallDistance, double wallSteepness, double safePedestrianDistance, double anticipationTime, int collisionsToKeep, double dmin, double dmid, double dmax) {
        this.startX = startX;
        this.idealAccelerationTime = idealAccelerationTime;
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
        this.maxVelocity = maxVelocity;
        this.desiredVelocity = desiredVelocity;
        this.safeWallDistance = safeWallDistance;
        this.wallSteepness = wallSteepness;
        this.safePedestrianDistance = safePedestrianDistance;
        this.anticipationTime = anticipationTime;
        this.collisionsToKeep = collisionsToKeep;
        this.dmin = dmin;
        this.dmid = dmid;
        this.dmax = dmax;
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

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public double getDesiredVelocity() {
        return desiredVelocity;
    }

    public double getSafeWallDistance() {
        return safeWallDistance;
    }

    public double getWallSteepness() {
        return wallSteepness;
    }

    public double getSafePedestrianDistance() {
        return safePedestrianDistance;
    }

    public double getAnticipationTime() {
        return anticipationTime;
    }

    public int getCollisionsToKeep() {
        return collisionsToKeep;
    }

    public double getDmin() {
        return dmin;
    }

    public double getDmid() {
        return dmid;
    }

    public double getDmax() {
        return dmax;
    }
}

