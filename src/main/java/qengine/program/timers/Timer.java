package qengine.program.timers;

public class Timer {
    private long time = 0;
    private long start = 0;

    public long getTime() {
        return time;
    }

    public void start() {
        start = System.nanoTime();
    }

    public void countTimer() {
        time += (System.nanoTime() - start);
    }
}
