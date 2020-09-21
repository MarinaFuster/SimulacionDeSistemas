package ar.edu.itba.ss;

import java.util.Objects;

public class Particle {
    private double radius = Constants.PARTICLE_RADIUS;
    private double mass = Constants.PARTICLE_MASS;

    private double x,y, vx, vy;
    private int collisionCount = 0;
    private final int id;

    public Particle(int id, double x, double y, double vx, double vy) {
        this.id = id;
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
        double universeWidth = Main.activeConfig.getUniverseWidth();
        double minPoint = Main.activeConfig.getUniverseHeight() / 2 - Main.activeConfig.getOpeningSize() / 2;
        double maxPoint = Main.activeConfig.getUniverseHeight() / 2 + Main.activeConfig.getOpeningSize() / 2;
        double distanceToCenter, timeToCenter, yPosAtCenter;
        if ( vx == 0) {
            return  -1;
        } else if( vx < 0) {
            // velocidad negativa
            distanceToCenter =  (radius + universeWidth / 2) - x;
            timeToCenter = distanceToCenter / vx;
            yPosAtCenter = y + timeToCenter * vy;
            if (timeToCenter > 0 && (yPosAtCenter > maxPoint || yPosAtCenter < minPoint)) {
                return timeToCenter;
            } else {
                return (radius - x) / vx;
            }
        } else {
            // velocidad positiva
            distanceToCenter = (universeWidth / 2 ) - radius - x;
            timeToCenter = distanceToCenter / vx;
            yPosAtCenter = y + timeToCenter * vy;
            if (timeToCenter > 0 && (yPosAtCenter > maxPoint || yPosAtCenter < minPoint)) {
                return timeToCenter;
            } else {
                return (universeWidth - radius - x) / vx;
            }
        }
    }

    /*
        public double collidesY(): return the duration of time until the invoking particle collides with a horizontal wall,
        assuming it follows a straight-line trajectory. If the particle never collides with a horizontal wall, return a negative
        number.
     */

    // TODO: Checkear como collisionaria contra la parte de arriba de la abertura ( que en teoria no tiene ancho)
    public double collidesY() {
        if (vy == 0) {
            return -1; // If we're not moving on the Y axis we will never crash a wall
        } else if ( vy < 0 ) {
            return ((radius - y) / vy); // radius - vy is negative, when divided by vy gives us positive time
        } else {
            // vy > 0
            return (Main.activeConfig.getUniverseHeight() - radius - y) / vy;
        }
    }


    /*
        public double collides(Particle b): return the duration of time until the invoking particle collides with particle
        b, assuming both follow straight-line trajectories. If the two particles never collide, return a negative value.
     */
    public double collides(Particle b) {
        double deltaX = b.x - x;
        double deltaY = b.y - y;
        double deltaVX = b.vx - vx;
        double deltaVY = b.vy -vy;
        double sigma = radius + b.radius;

        double deltaVR = deltaVX * deltaX + deltaVY * deltaY;
        double deltaVV = deltaVX * deltaVX + deltaVY * deltaVY;
        double deltaRR = deltaX * deltaX + deltaY * deltaY;

        double d = deltaVR * deltaVR - deltaVV * (deltaRR - sigma * sigma);
        if(deltaVR >= 0 || d < 0) {
            return -1;
        } else {
            return -1 * (deltaVR + Math.sqrt(d)) / deltaVV;
        }
    }

    //  update the invoking particle to simulate it bouncing off a vertical wall.
    public void bounceX() {
        System.out.println("Particle " + id + " bounced with wall X");
        System.out.printf("%f\t%f\n", x, y);
        vx = - vx;
        collisionCount++;
    }
    // update the invoking particle to simulate it bouncing off a horizontal wall.
    public void bounceY() {
        System.out.println("Particle " + id + " bounced with wall Y");
        System.out.printf("%f\t%f\n", x, y);
        vy = -vy;
        collisionCount++;
    }
    // update both particles to simulate them bouncing off each other.
    public void bounce(Particle b){
        System.out.println("Particle " + id + " bounced with particle " + b.id);
        System.out.printf("%f\t%f\n", x, y);
        double deltaX = b.x - x;
        double deltaY = b.y - y;
        double deltaVX = b.vx - vx;
        double deltaVY = b.vy -vy;
        double sigma = radius + b.radius;

        double deltaDot = deltaVX * deltaX + deltaVY * deltaY;
        double j =  2 * mass * b.mass * deltaDot / (sigma *  (mass + b.mass));
        double jx = j * deltaX / sigma;
        double jy = j * deltaY / sigma;

        vx = vx + jx / mass;
        vy = vy + jy / mass;
        collisionCount++;

        // This is to ignore the corner particles
        if(b.radius != 0D) {
            b.vx = b.vx - jx / b.mass;
            b.vy = b.vy - jy / b.mass;
            b.collisionCount++;
        }


    }

    // return the total number of collisions involving this particle.
    public int getCollisionCount() {
        return collisionCount;
    }

    public void advanceStraight(double time) {
        x += time * vx;
        y += time * vy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public boolean overlaps(double x, double y) {
        return distance(x, y) <= 2*radius;
    }

    public double distance (double x, double y) {
        double deltaX = Math.abs(x - this.x);
        double deltaY = Math.abs(y - this.y);
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public double distance( Particle p) {
        return distance(p.getX(), p.getY());
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}
