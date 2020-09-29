package ar.edu.itba.ss;

import ar.edu.itba.ss.oscillator.OscillatorConfigurationBuilder;
import ar.edu.itba.ss.oscillator.OscillatorSimulation;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {

        runOscillator();
    }

    public static void runOscillator() {
        OscillatorConfigurationBuilder builder = new OscillatorConfigurationBuilder();

        builder.deltaT(0.001)
                .saveFrequency(15)
                .integrator(Integrator.BEEMAN)
                .name("beeman");


        OscillatorSimulation simulation = new OscillatorSimulation(builder.get());

        simulation.run();

    }
}
