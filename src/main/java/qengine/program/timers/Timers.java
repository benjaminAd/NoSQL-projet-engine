package qengine.program.timers;

public class Timers {
    private final Timer timerIndexes;
    private final Timer timerDictionnary;
    private final Timer timerQueryProcess;
    private final Timer timerQueryParsing;
    private final Timer timerworkload;
    private final Timer timerdataparsing;
    private static Timers instance = null;

    private Timers() {
        this.timerDictionnary = new Timer();
        this.timerIndexes = new Timer();
        this.timerQueryProcess = new Timer();
        this.timerQueryParsing = new Timer();
        this.timerworkload = new Timer();
        this.timerdataparsing = new Timer();
    }

    public void setDictionaryTimer() {
        this.timerDictionnary.start();
    }

    public void setIndexesTimer() {
        this.timerIndexes.start();
    }

    public void setQueriesProcessTimer() {
        this.timerQueryProcess.start();
    }

    public void setQueryParsingTimer() {
        this.timerQueryParsing.start();
    }

    public void setWorkloadTimer() {
        this.timerworkload.start();
    }

    public void setParsingDataTimer() {
        this.timerdataparsing.start();
    }

    public void addTimerToDictionary() {
        this.timerDictionnary.countTimer();
    }

    public void addTimerToIndexes() {
        this.timerIndexes.countTimer();
    }

    public void addTimerToQueriesProcess() {
        this.timerQueryProcess.countTimer();
    }

    public void addTimerToQueriesParsing() {
        this.timerQueryParsing.countTimer();
    }

    public void addWorkloadTimer() {
        this.timerworkload.countTimer();
    }

    public void addParsingDataTimer() {
        this.timerdataparsing.countTimer();
    }

    public double getIndexesTimer() {
        return this.timerIndexes.getTime();
    }

    public double getDictionaryTimer() {
        return this.timerDictionnary.getTime();
    }

    public double getQueriesParsingTimer() {
        return this.timerQueryParsing.getTime();
    }

    public double getQueriesProcessTimer() {
        return this.timerQueryProcess.getTime();
    }

    public double getWorkloadTimer() {
        return this.timerworkload.getTime();
    }

    public double getParsingDataTimer() {
        //On garde uniquement le temps de parsing de donn??es sans la cr??ation des indexes et du dictionnaires
        return (this.timerdataparsing.getTime() - (getDictionaryTimer() + getIndexesTimer()));
    }

    public static Timers getInstance() {
        if (instance == null) instance = new Timers();
        return instance;
    }
}
