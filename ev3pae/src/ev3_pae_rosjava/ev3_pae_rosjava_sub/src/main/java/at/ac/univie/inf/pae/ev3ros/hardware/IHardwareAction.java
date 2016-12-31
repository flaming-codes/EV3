package at.ac.univie.inf.pae.ev3ros.hardware;

/**
 * Das Interface IHardwareAction soll Moeglichkeiten zur Interaktion mit dem
 * EV3-Roboter bieten
 */
public interface IHardwareAction {

    /**
     * Diese Funktion dreht den Kopf des Roboters
     *
     * @param degrees Legt fest um wie viele Grad der Kopf gedreht werden soll
     */
    public void turnHead(double degrees);

    /**
     * Diese Funktion laesst den Roboter fahren
     *
     * @param direction Die Richtung in welche der Roboter fahren soll, 0 =
     * Rueckwaerts / 1 = Vorwaerts
     * @param milliseconds Die Dauer der Fahrt in Millisekunden
     * @param speed Die Geschwindigkeit des Roboters
     */
    public void drive(int direction, int milliseconds, int speed);

    //TODO: Hier waehre es schoen nur die Anzahl der Grad und vll. die Geschwindigkeit zu uebergeben um die Drehung durchzufuehren

    /**
     * Diese Funktion dreht den kompletten Roboter
     *
     * @param direction Die Richtung in welche der Roboter gedreht werden soll,
     * 0 = Links / 1 = Rechts
     * @param milliseconds Die Dauer der Drehbewegung
     * @param speed Die Geschwindigkeit mit welcher der Roboter gedreht werden
     * soll
     */
    public void turnBody(int direction, int milliseconds, int speed);

    /**
     * Diese Funktion aendert die Farbe der LED des Roboters
     *
     * @param color Die Farbe in welcher die LED leuchten soll, 0 = aus / 1 =
     * gruen / 2 = rot
     */
    public void colorLight(int color);

    /**
     * Retouniert die Daten des Farbsensors
     *
     * @return Die Daten des Farbsensors
     */
    public float[] getColorData();

    /**
     * Retouniert die Daten des Touchsensors
     *
     * @return Die Daten des Touchsensors
     */
    public float[] getTouchData();

    /**
     * Retouniert die Daten des Gyrosensors
     *
     * @return Die Daten des Gyrosensors
     */
    public float[] getGyroData();

    /**
     * Retouniert die Daten des Ultrasonicsensors
     *
     * @return Die Daten des Ultrasonicsensors
     */
    public float[] getUltrasonicData();

    /**
     * Diese Funktion beendet die Sensoren und Motoren
     */
    public void disconnect();

    /**
     * Diese Funktion prueft ob sich der Roboter aktuell bewegt
     *
     * @return true = Der Roboter bewegt sich nicht, false = Der Roboter bewegt
     * sich
     */
    public boolean isMovementFinished();

}
