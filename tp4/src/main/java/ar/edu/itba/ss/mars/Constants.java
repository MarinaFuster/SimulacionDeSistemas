package ar.edu.itba.ss.mars;

public class Constants {

    public static final double GRAVITATIONAL_CONSTANT = 6693 * Math.pow(10,-11);

    public static class EarthConstants {
        public static final double MASS = 5.97219 * Math.pow(10,24);
        public static final double RADIUS = 6378.137;
        public static final double VISUALIZATION_RADIUS = RADIUS * 500;

        public static final double STARTX = 1.493188929636662 * Math.pow(10,8);
        public static final double STARTY = 1.318936357931255 * Math.pow(10,7);

        public static final double STARTVX = -3.113279917782445 * Math.pow(10,0);
        public static final double STARTVY = 2.955205189256462 * Math.pow(10,1);


    }

    public static class SunConstants {
        public static final double MASS = 1988500 * Math.pow(10,24);
        public static final double RADIUS = 696000;
        public static final double VISUALIZATION_RADIUS = RADIUS * 20;
    }
}
