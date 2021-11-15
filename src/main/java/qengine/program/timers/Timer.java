package qengine.program.timers;

import qengine.program.utils.Constants;

public class Timer {
    private long timerIndexes;
    private long timerDictionnary;
    private long start;
    private static Timer instance = null;

    private Timer() {
        this.timerDictionnary = 0;
        this.timerIndexes = 0;
    }

    public void setTimer() {
        this.start = System.nanoTime();
    }

    public void addTimerToDictionary() {
        this.timerDictionnary += (System.nanoTime() - start);
    }

    public void addTimerToIndexes() {
        this.timerIndexes += (System.nanoTime() - start);
    }

    private long convertToMs(long timer) {
        return timer / Constants.CONVERT_NS_TO_MS;
    }

    public static Timer getInstance() {
        if (instance == null) instance = new Timer();
        return instance;
    }

    public void displayTimers() {
        System.out.println("Temps de création du dictionnaire : " + convertToMs(this.timerDictionnary) + " ms | Temps de création des index : " + convertToMs(this.timerIndexes) + " ms");
    }
}
