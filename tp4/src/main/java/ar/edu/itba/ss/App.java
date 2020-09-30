package ar.edu.itba.ss;

import ar.edu.itba.ss.mars.MarsConfigurationBuilder;
import ar.edu.itba.ss.mars.MarsSimulation;
import ar.edu.itba.ss.oscillator.OscillatorConfigurationBuilder;
import ar.edu.itba.ss.oscillator.OscillatorSimulation;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {

        runOscillator();
//        runPlanets();
    }

    public static void runOscillator() {
        OscillatorConfigurationBuilder builder = new OscillatorConfigurationBuilder();

        builder.deltaT(0.001)
                .saveFrequency(15)
                .integrator(Integrator.GEARPC)
                .name("gearpc");


        OscillatorSimulation simulation = new OscillatorSimulation(builder.get());

        simulation.run();

    }

    public static void runPlanets() {
        MarsConfigurationBuilder builder = new MarsConfigurationBuilder();


        double secondsInAYear = 3.154 * Math.pow(10,7);
        builder.deltaT(50).cutoffTime(secondsInAYear).name("orbit").saveFrequency(10000);

        MarsSimulation simulation = new MarsSimulation(builder.get());

        simulation.run();
    }
}
