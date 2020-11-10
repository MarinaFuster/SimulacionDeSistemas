package ar.edu.itba.ss;

public class ConfigBuilder {
    private double maxY = 40;
    private double obstacleRadius = 0.5;
    private double pedestrianRadius = 1;
    private double obstacleSpeed = 0.5;
    private double goalX = 80;
    // Default initial Y will be from pedestrianRandom/2 to maxY - pedestrianRadius / 2
    private double goalY = Math.random() * (maxY - 2 * pedestrianRadius) + (pedestrianRadius);

    private double pedestrianToFirstObstacleDistance = 5;
    private double lastObstacleToGoalDistance = 5;
    private double cutoffTime = 1000;
    private double deltaT = 0.005;
    private int saveFrequency = 200;
    private String name = "unnamed";
    private String outFolder = "./output/";


    private double maxVelocity = 1;
    private double desiredVelocity = 0.8;
    private double idealAccelerationTime = 0.3; // TODO Ver si queda asi
    private double safeWallDistance = 1;
    private double wallSteepness = 2; // TODO Ver si cambiar esto arregla cosas
    private double safePedestrianDistance = pedestrianRadius * 1.3;
    private double anticipationTime = 4; // TODO Ver si cambiar esto arregla cosas
    private int collisionsToKeep = 5; // Tiene que estar entre 2 y 5

    private double startX = safeWallDistance + pedestrianRadius / 2;
    private double startY = Math.random() * (maxY - 2 * pedestrianRadius) + (pedestrianRadius);;

    // TODO Ver valores de estos
    private double dmin= 1;
    private double dmid = 10;
    private double dmax = 20;

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

    public ConfigBuilder idealAccelerationTime(double t) {
        this.idealAccelerationTime = t;
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

    public ConfigBuilder maxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
        return this;
    }

    public ConfigBuilder desiredVelocity(double desiredVelocity) {
        this.desiredVelocity = desiredVelocity;
        return this;
    }

    public ConfigBuilder safeWallDistance(double safeWallDistance) {
        this.safeWallDistance = safeWallDistance;
        return this;
    }

    public ConfigBuilder wallSteepness(double wallSteepness) {
        this.wallSteepness = wallSteepness;
        return this;
    }

    public ConfigBuilder safePedestrianDistance(double safePedestrianDistance) {
        this.safePedestrianDistance = safePedestrianDistance;
        return this;
    }

    public ConfigBuilder anticipationTime(double anticipationTime) {
        this.anticipationTime = anticipationTime;
        return this;
    }

    public ConfigBuilder collisionsToKeep(int collisionsToKeep) {
        this.collisionsToKeep = collisionsToKeep;
        return this;
    }

    public ConfigBuilder dmin(double dmin) {
        this.dmin = dmin;
        return this;
    }

    public ConfigBuilder dmid(double dmid) {
        this.dmid = dmid;
        return this;
    }

    public ConfigBuilder dmax(double dmax) {
        this.dmax = dmax;
        return this;
    }

    public ConfigBuilder startX(double startX) {
        this.startX = startX;
        return this;
    }


    public Config createConfig() {
        return new Config(startX, idealAccelerationTime, maxY, obstacleRadius, pedestrianRadius, obstacleSpeed, goalX, goalY, startY, pedestrianToFirstObstacleDistance, lastObstacleToGoalDistance, cutoffTime, deltaT, saveFrequency, name, outFolder, maxVelocity, desiredVelocity, safeWallDistance, wallSteepness, safePedestrianDistance, anticipationTime, collisionsToKeep, dmin, dmid, dmax);
    }
}