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
//        runOscillatorForMetrics();
        runPlanets();
    }

    public static void runOscillator() {
            OscillatorConfigurationBuilder builder = new OscillatorConfigurationBuilder();

            builder.deltaT(0.001)
                    .saveFrequency(15)
                    .integrator(Integrator.BEEMAN)
                    .name(Integrator.BEEMAN.getDescription());


            OscillatorSimulation simulation = new OscillatorSimulation(builder.get());
            simulation.run();
    }

    public static void runOscillatorForMetrics() {
        for(Integrator i : Integrator.values())  {
            System.out.println("Starting with " + i.getDescription() + "...");
            int exp = 2;
            while(exp <= 8) {
                System.out.println("Starting with delta exp " + exp + "...");
                OscillatorConfigurationBuilder builder = new OscillatorConfigurationBuilder();

                builder.deltaT(Math.pow(10, -1*exp))
                        .saveFrequency(50)
                        .integrator(i)
                        .name(i.getDescription());


                OscillatorSimulation simulation = new OscillatorSimulation(builder.get());

                simulation.run();
                System.out.println("Finished simulation with " + i.getDescription() + " and exp = " + exp + ".");
                exp += 1;
            }
        }
    }

    public static void runPlanets() {
        MarsConfigurationBuilder builder = new MarsConfigurationBuilder();

        double secondsInAYear = 3.154 * Math.pow(10,7);
        double secondsInADay = 60*60*24;
        builder.deltaT(50).cutoffTime(secondsInADay * 100).name("rocket").saveFrequency(1000);
        System.out.println(secondsInAYear);
//        builder.deltaT(50).cutoffTime(secondsInAYear).name("orbit").saveFrequency(31540);
        MarsSimulation simulation = new MarsSimulation(builder.get());

        simulation.run();
    }
}
