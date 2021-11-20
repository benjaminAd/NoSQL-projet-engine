package qengine.program.timers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import qengine.program.utils.Constants;

public class Timers {
    private final Timer timerIndexes;
    private final Timer timerDictionnary;
    private static Timers instance = null;
    private static final Logger logger = LogManager.getLogger(Timers.class);

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

    public void addTimerToDictionary() {
        this.timerDictionnary.countTimer();
    }

    public void addTimerToIndexes() {
        this.timerIndexes.countTimer();
    }

    private double convertToSec(Timer timer) {
        return timer.getTime() / Constants.CONVERT_NS_TO_SEC;
    }

    public static Timers getInstance() {
        if (instance == null) instance = new Timers();
        return instance;
    }

    public void displayTimers() {
        logger.info("Temps de création du dictionnaire : {} sec \nTemps de création des index : {} sec.\n", convertToSec(this.timerDictionnary), convertToSec(this.timerIndexes));
    }
}
