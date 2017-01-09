package inf.pae.ev3ros.hardware.movement;

import inf.pae.ev3ros.hardware.EV3;

/**
 * Die Klasse Movement ist die abstrakte Basisklasse aller Roboterbewegungen
 */
public abstract class Movement implements Runnable {

    MovementManager movementManager;
    EV3 ev3;

    /**
     * Der Konstrukter speichert den Roboter, welcher bewegt werden soll
     *
     * @param ev3 Der Roboter, welcher bewegt werden soll
     */
    public Movement(EV3 ev3) {
        this.ev3 = ev3;
    }

    /**
     * Diese Funktion weist der Bewegung einen Manager zu, welcher
     * benachrichtigt wird, wenn die Bewegung abgeschlossen ist
     *
     * @param movementManager Der Bewegungs-Manager
     */
    public void addManager(MovementManager movementManager) {
        this.movementManager = movementManager;
    }

    //TODO: Es waere ganz schoen wenn die Bewegungen nicht immer abrupt stoppen wuerden, sondern die Motoren immer langsamer werden wuerden. Hierzu muesste man aber einiges anders berechnen aufgrund der zeitlichen Komponente
    /**
     * Diese Funktion beendet jegliche Bewegungen sofort
     */
    public void stopMovement() {
        try {
            ev3.getLeftMotor().stop(true);
        } catch (Exception ex) {
        }
        try {
            ev3.getRightMotor().stop(true);
        } catch (Exception ex) {
        }
        try {
            ev3.getHeadMotor().stop(true);
        } catch (Exception ex) {
        }
        movementManager.movementFinished();
    }

}
