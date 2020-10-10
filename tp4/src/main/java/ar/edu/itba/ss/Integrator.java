package ar.edu.itba.ss;

public enum Integrator {
    VERLET("verlet"),
    BEEMAN("beeman"),
    GEARPC("gearpc"),
    ANALYTIC("analytic");

    private String description;

    private Integrator(String s){
        this.description = s;
    }

    public String getDescription() {
        return description;
    }
}
