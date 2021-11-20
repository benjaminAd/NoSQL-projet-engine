package qengine.program.timers;

public class Timer {
    private double time = 0;
    private double start = 0;

    public double getTime() {
        return time;
    }

    public void start() {
        start = System.nanoTime();
    }

    public void countTimer() {
        time += (System.nanoTime() - start);
    }
}
