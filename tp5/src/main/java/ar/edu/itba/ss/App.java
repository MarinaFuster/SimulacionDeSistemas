package ar.edu.itba.ss;

public class App {


    public static void main( String[] args ) {

        ConfigBuilder cb = new ConfigBuilder();

        Simulation sim = new Simulation(cb.createConfig());

        sim.run();


    }
}
