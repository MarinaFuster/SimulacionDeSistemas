package ar.edu.itba.ss;

public class Particle {
    private final double radius = Constants.PARTICLE_RADIUS;
    private  final double mass = Constants.PARTICLE_MASS;

    private double x,y, vx, vy;
    private int collisionCount = 0;

    public Particle(double x, double y, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }


    /*
        public double collidesX(): return the duration of time until the invoking particle collides with a vertical wall,
        assuming it follows a straight-line trajectory. If the particle never collides with a vertical wall, return a negative
        number (or +infinity = DOUBLE_MAX or NaN???)
    */
    public double collidesX() {
        // TODO
        return 1;
    }

    /*
        public double collidesY(): return the duration of time until the invoking particle collides with a horizontal wall,
        assuming it follows a straight-line trajectory. If the particle never collides with a horizontal wall, return a negative
        number.
     */
    public double collidesY() {
        // TODO
        return 1;
    }


    /*
        public double collides(Particle b): return the duration of time until the invoking particle collides with particle
        b, assuming both follow straight-line trajectories. If the two particles never collide, return a negative value.
     */
    public double collides(Particle b) {
        // TODO
        return 1;
    }

    //  update the invoking particle to simulate it bouncing off a vertical wall.
    public void bounceX() {

    }
    // update the invoking particle to simulate it bouncing off a horizontal wall.
    public void bounceY() {

    }
    // update both particles to simulate them bouncing off each other.
    public void bounce(Particle b){

    }

    // return the total number of collisions involving this particle.
    public int getCollisionCount() {
        return collisionCount;
    }

    public void advanceStraight(double time) {
        x += time * vx;
        y += time * vy;
    }
}
