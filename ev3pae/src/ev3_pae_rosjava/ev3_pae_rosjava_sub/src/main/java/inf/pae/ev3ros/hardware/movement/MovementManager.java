package inf.pae.ev3ros.hardware.movement;

import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Der MovementManager regelt wie sich der Roboter, in welcher Reihenfolge,
 * bewegt
 */
public class MovementManager {

    public ArrayDeque<Movement> movementQueue;
    private Movement currentMovement;

    /**
     * Der Konstrukter initialisiert die Queue in welcher die Bewegungen
     * gespeichert werden
     */
    public MovementManager() {
        movementQueue = new ArrayDeque<>();
    }

    /**
     * Diese Funktion fuegt der Queue eine Bewegung hinzu
     *
     * @param movement Die Bewegung welche ausgefuehrt werden soll
     * @return Dieser Wert gibt an, ob die Bewegung sofort ausgefuehrt werden
     * kann
     */
    public boolean addMovement(Movement movement) {
        if (movementQueue.isEmpty()) {
            movementQueue.add(movement);
            makeMovement();
            return true;
        }
        return false;
    }

    /**
     * Diese Funktion entfernt eine Bewegung aus der Queue
     *
     * @param movement Die Bewegung die entfernt werden soll
     */
    public void removeMovement(Movement movement) {
        movementQueue.remove(movement);
    }

    /**
     * Diese Funktion leert die Queue und stoppt die aktuelle Bewegung
     */
    public void stopAllMovements() {
        movementQueue.clear();
        currentMovement.stopMovement();
    }

    /**
     * Diese Funktion fuehrt eine Bewegung aus
     */
    private void makeMovement() {
        if (!movementQueue.isEmpty() && !movementQueue.peek().equals(currentMovement)) {
            currentMovement = movementQueue.peek();
        } else {
            return;
        }

        currentMovement.addManager(this);
        currentMovement.run();
    }

    /**
     * Diese Funktion beendet eine Bewegung
     */
    public void movementFinished() {
        movementQueue.remove();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(MovementManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        makeMovement();
    }

    /**
     * Diese Funktion prueft ob sich der Roboter aktuell bewegt
     *
     * @return Der Wert gibt an ob sich der Roboter aktuell bewegt
     */
    public boolean isMovementFinished() {
        if (movementQueue.isEmpty()) {
            return true;
        }
        return false;
    }

}
