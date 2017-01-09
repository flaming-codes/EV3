package inf.pae.ev3ros.hardware.movement;

import inf.pae.ev3ros.hardware.EV3;
import java.rmi.RemoteException;

/**
 * HeadTurn laesst den Roboter den Kopf drehen
 */
public class HeadTurn extends Movement {

    int degrees;

    /**
     * Der Konstruktor uebernimmt die grad der Drehung sowie den EV3
     *
     * @param degrees Um wie viele Grad der Kopf gedreht werden soll
     * @param ev3 Welcher Roboter bewegt werden soll
     */
    public HeadTurn(int degrees, EV3 ev3) {
        super(ev3);
        this.degrees = degrees;
    }

    @Override
    public void run() {
        try {
            ev3.getHeadMotor().rotate(degrees);
            movementManager.movementFinished();
        } catch (RemoteException ex) {
            System.err.println("Fehler beim drehen des Kopfes, Details: " + ex);
        }
    }

}
