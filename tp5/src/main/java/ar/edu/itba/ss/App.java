package ar.edu.itba.ss;

import java.util.List;

public class App {


    public static void main( String[] args ) {
        runMultipleSimulations();

        //runStandardSimulation();
    }

    public static void runStandardSimulation() {
        Simulation clean = new Simulation(new ConfigBuilder().createConfig());
        clean.time = 0;
        clean.config = null;
        clean.goal = null;
        clean.obstacles = null;

        ConfigBuilder cb = new ConfigBuilder();
        Simulation sim = new Simulation(cb.createConfig());
        sim.run();
    }

    public static void runMultipleSimulations() {
        double[] safe_radius = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        int simulations_qty = 20;

        Simulation sim = new Simulation(new ConfigBuilder().createConfig());

        for(int i=0; i< safe_radius.length; i++) {
            for(int j=0; j<simulations_qty; j++) {

                sim.time = 0;
                sim.config = null;
                sim.goal = null;
                sim.obstacles = null;

                ConfigBuilder cb = new ConfigBuilder();
                cb.goalY(7); // fixed y for goal
                cb.startY(4); // fixed y for pedestrian
                cb.safePedestrianDistance(cb.pedestrianRadius + safe_radius[i]); // we update safe radius

                // filename
                StringBuilder sb = new StringBuilder();
                sb.append("sim_");
                sb.append(safe_radius[i]);
                sb.append("_");
                sb.append(j);
                cb.name(sb.toString());

                sim = new Simulation(cb.createConfig());

                sim.run();
            }
        }
    }
}
