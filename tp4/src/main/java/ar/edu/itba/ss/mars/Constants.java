package ar.edu.itba.ss.mars;

public class Constants {

    public static final double GRAVITATIONAL_CONSTANT = 6.67408 * Math.pow(10,-11);   // m^3 kg^-1 s^-1

    public static class EarthConstants {
        public static final double MASS = 5.97219 * Math.pow(10,24);                // kg
//        public static final double RADIUS = 6378.137;                               // km
        public static final double RADIUS = 6378.137 * 1000;                               // m
        public static final double VISUALIZATION_RADIUS = RADIUS * 500;             // m

//        public static final double STARTX = 1.493188929636662 * Math.pow(10,8);     // ?? (suponemos km)
        public static final double STARTX = 1.493188929636662 * Math.pow(10,11);     // ?? (suponemos m)
//        public static final double STARTY = 1.318936357931255 * Math.pow(10,7);     // ?? (suponemos km/s)
        public static final double STARTY = 1.318936357931255 * Math.pow(10,10);     // ?? (suponemos km)

//        public static final double STARTVX = -3.113279917782445 * Math.pow(10,0);   // ?? (suponemos km/s)
//        public static final double STARTVY = 2.955205189256462 * Math.pow(10,1);    // ?? (suponemos km/s)
        public static final double STARTVX = -3.113279917782445 * Math.pow(10,3);   // ?? (suponemos m/s)
        public static final double STARTVY = 2.955205189256462 * Math.pow(10,4);    // ?? (suponemos m/s)
    }

    public static class SunConstants {
        public static final double MASS = 1988500 * Math.pow(10,24);                 // Kg
//        public static final double RADIUS = 696000;                                     // km
        public static final double RADIUS = 696000 * 1000;                                     // m
        public static final double VISUALIZATION_RADIUS = RADIUS * 20;                  //km
    }
}
