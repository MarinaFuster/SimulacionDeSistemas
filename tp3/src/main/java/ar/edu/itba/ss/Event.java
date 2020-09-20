package ar.edu.itba.ss;

public abstract class Event implements Comparable<Event>{
    private double time;

    public Event(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public int compareTo(Event o) {
        return Double.compare(time, o.getTime());
    }

    /*
        public boolean wasSuperveningEvent(): return true if the event has been invalidated since creation, and false if
        the event has been invalidated.

        In order to implement wasSuperveningEvent, the event data type should store the collision counts of two particles at the
        time the event was created. The event corresponds to a physical collision if the current collision counts of the particles are
        the same as when the event was created
     */
    public abstract  boolean wasSuperveningEvent();
}
