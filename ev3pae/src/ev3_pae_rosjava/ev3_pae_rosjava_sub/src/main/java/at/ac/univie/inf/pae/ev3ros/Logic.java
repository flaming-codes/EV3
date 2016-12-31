package at.ac.univie.inf.pae.ev3ros;

import at.ac.univie.inf.pae.ev3ros.hardware.IHardwareAction;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Die Logik-Klasse ist der Startpunkt der Programmlogik, aktuell ist es eher
 * eine Testklasse
 */
public class Logic {

    private final IHardwareAction ev3HA;

    /**
     * Der Konstrukter speichert die IHardwareAction
     *
     * @param ev3HA
     */
    public Logic(IHardwareAction ev3HA) {
        this.ev3HA = ev3HA;
    }

    /**
     * Die demo-Funktion ist zum demonstrieren verschiedener Funktionen
     */
    public void demo() {

        //Zuerst werden alle Sensordaten ausgegeben
        System.out.println(Arrays.toString(ev3HA.getColorData()));
        System.out.println(Arrays.toString(ev3HA.getGyroData()));
        System.out.println(Arrays.toString(ev3HA.getTouchData()));
        System.out.println(Arrays.toString(ev3HA.getUltrasonicData()));

        //Nun faehrt der Roboter zuerst ein Stueck rueckwaerts, dann vorwaerts
        ev3HA.drive(1000, 500, 1);
        ev3HA.drive(1000, 500, 0);

        //Nun dreht sich der komplette Roboter (Die Werte hier sorgen fuer eine (grobe) 90-Grad Drehung
        ev3HA.turnBody(750, 1350, 0);
        ev3HA.turnBody(750, 1350, 1);

        //Zum Schluss dreht sich der Kopf des Roboters
        ev3HA.turnHead(45);
        ev3HA.turnHead(-90);
        ev3HA.turnHead(45);

        do {

            try {
                //Bis alle Bewegungen abgeschlossen sind passiert nichts
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while (!ev3HA.isMovementFinished());

        endLogic();
    }

    /**
     * endLogic beendet aktuell die Verbindung mit dem Roboter und beendet das
     * komplette Programm
     */
    public void endLogic() {
        ev3HA.disconnect();
        //TODO: Node besser beenden
        System.exit(0);
    }

}
