package inf.pae.ev3ros.hardware.movement;

import inf.pae.ev3ros.hardware.EV3;
import java.rmi.RemoteException;

/**
 * Drive laesst den Roboter vorwaerts und rueckwaerts fahren
 */
public class Drive extends WheelMovement {

    /**
     * Dieser Konsturktor ruft den Konstruktor von WheelMovement auf
     *
     * @param speed Die Geschwindigkeit der Fahrt
     * @param milliseconds Die Dauer der Fahrtx
     * @param direction Die Richtung der Fahrt
     * @param ev3 Der Roboter der fahren soll
     */
    public Drive(int speed, int milliseconds, int direction, EV3 ev3) {
        super(speed, milliseconds, direction, ev3);
    }

    @Override
    public synchronized void run() {
        try {
            //Beide Motoren bekommen die uebergebene Geschwindigkeit zugewiesen
            ev3.getLeftMotor().setSpeed(speed);
            ev3.getRightMotor().setSpeed(speed);

            ev3.getLeftMotor().setAcceleration(1000);
            ev3.getRightMotor().setAcceleration(1000);

            //Je nach Richtung faehrt der Roboter entweder vorwaerts oder rueckwaerts
            switch (direction) {
                case 0:
                    ev3.getLeftMotor().backward();
                    ev3.getRightMotor().backward();
                    break;
                case 1:
                    ev3.getLeftMotor().forward();
                    ev3.getRightMotor().forward();
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            // Check if device has to drive for a given time. If not, just drive infinitely.
            if (milliseconds != 0) {
                //Solange wie gefahren werden soll schlaeft der Thread
                Thread.sleep(milliseconds);

                //Wenn die Zeit abgelaufen ist wird die Fahrt beendet
                stopMovement();
            }

        } catch (RemoteException | IllegalArgumentException | InterruptedException ex) {
            System.err.println("Fehler beim Ansteuern der Motoren, Details: " + ex);
        }
    }
}
