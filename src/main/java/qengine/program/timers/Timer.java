package qengine.program.timers;

public class Timer {
    private long timerIndexes;
    private long timerDictionnary;
    private static Timer instance = null;

    private Timer() {
        this.timerDictionnary = 0;
        this.timerIndexes = 0;
    }

    public void addTimerToDictionary(long time) {
        this.timerDictionnary += time;
    }

    public void addTimerToIndexes(long time) {
        this.timerIndexes += time;
    }

    private void convertToMs() {
        this.timerDictionnary = this.timerDictionnary / 1000000;
        this.timerIndexes = this.timerIndexes / 1000000;
    }

    public static Timer getInstance() {
        if (instance == null) instance = new Timer();
        return instance;
    }

    public void displayTimers() {
        convertToMs();
        System.out.println("Temps de création du dictionnaire : " + this.timerDictionnary + " ms | Temps de création des index : " + this.timerIndexes + " ms");
    }
}
