package ar.edu.itba.ss.mars;

public class Constants {


//    X      X-component of position vector (km)
//    Y      Y-component of position vector (km)
//    Z      Z-component of position vector (km)
//    VX     X-component of velocity vector (km/sec)
//    VY     Y-component of velocity vector (km/sec)
//    VZ     Z-component of velocity vector (km/sec)

    public static final double GRAVITATIONAL_CONSTANT = 6.67430 * Math.pow(10,-11);   // m^3 kg^-1 s^-2

    public static class EarthConstants {
        public static final double MASS = 5.97219 * Math.pow(10,24);                // kg
        public static final double RADIUS = 6378.137 * Math.pow(10,3);              // m
        public static final double VISUALIZATION_RADIUS = RADIUS * 400;             // m

        public static final double STARTX = 1.493188929636662 * Math.pow(10,11);    // m
        public static final double STARTY = 1.318936357931255 * Math.pow(10,10);    // m

        public static final double STARTVX = -3.113279917782445 * Math.pow(10,3);   // m/s
        public static final double STARTVY = 2.955205189256462 * Math.pow(10,4);    // m/s
    }


    public static class MarsConstants {
        public static final double MASS = 6.4171 * Math.pow(10,23);                // kg
        public static final double RADIUS = 3389.92 * Math.pow(10,3);              // m
        public static final double VISUALIZATION_RADIUS = RADIUS * 500;             // m

        public static final double STARTX = 2.059448551842169 * Math.pow(10,11);    // m
        public static final double STARTY = 4.023977946528339 * Math.pow(10,10);    // m

        public static final double STARTVX = -3.717406842095575 * Math.pow(10,3);   // m/s
        public static final double STARTVY = 2.584914078301731 * Math.pow(10,4);    // m/s
    }

    public static class SunConstants {
        public static final double MASS = 1988500 * Math.pow(10,24);                 // Kg
        public static final double RADIUS = 696000 * Math.pow(10,3);                                     // m
        public static final double VISUALIZATION_RADIUS = RADIUS * 10;                  //km
    }

    public static class RocketConstants {
        public static final double rocketVelocity = 8 * Math.pow(10,3); // m/s
        public static final double spaceStationDistance = 1500 * Math.pow(10,3); // m
        public static final double spaceStationVelocity = 7.12 *  Math.pow(10,3); // m/s
        public static final double rocketMass = 5 * Math.pow(10,5); // kg
        public static final double RADIUS = 1;                                     // m
        public static final double VISUALIZATION_RADIUS = EarthConstants.VISUALIZATION_RADIUS * 0.7; //km

//        public static final double STARTX = -1.493180746216876 * Math.pow(10,11);    // m
//        public static final double STARTY = -1.318587397592698 * Math.pow(10,10);    // m
//
//        public static final double STARTVX = 2.726922962092942 * Math.pow(10,3);   // m/s
//        public static final double STARTVY = -2.949654007434976 * Math.pow(10,4);    // m/s
    }
}
