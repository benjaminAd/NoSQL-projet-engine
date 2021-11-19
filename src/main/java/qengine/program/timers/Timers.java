package qengine.program.timers;

import qengine.program.utils.Constants;

public class Timers {
    private final Timer timerIndexes;
    private final Timer timerDictionnary;
    private long start;
    private static Timers instance = null;

    private Timers() {
        this.timerDictionnary = new Timer();
        this.timerIndexes = new Timer();
    }

    public void setDictionaryTimer(){
        this.timerDictionnary.start();
    }

    public void setIndexesTimer(){
        this.timerIndexes.start();
    }

//    public void setTimer() {
//        this.start = System.nanoTime();
//    }

    public void addTimerToDictionary() {
        this.timerDictionnary.countTimer();
    }

    public void addTimerToIndexes() {
        this.timerIndexes.countTimer();
    }

    private long convertToMs(Timer timer) {
        return timer.getTime() / Constants.CONVERT_NS_TO_MS;
    }

    public static Timers getInstance() {
        if (instance == null) instance = new Timers();
        return instance;
    }

    public void displayTimers() {
        System.out.println("Temps de création du dictionnaire : " + convertToMs(this.timerDictionnary) + " ms | Temps de création des index : " + convertToMs(this.timerIndexes) + " ms");
    }
}
