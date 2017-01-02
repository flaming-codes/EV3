package tom.lab.logic;

import at.ac.univie.inf.pae.ev3ros.hardware.IHardwareAction;

/**
 * Implementation of the logic enabling the device to drive as long as
 * it has power, avoiding any collisions (if possible).
 *
 * @author Thomas Schönmann
 */
public class DriveToInfinity {

    private final IHardwareAction ev3HA;

    public DriveToInfinity(IHardwareAction ev3HA) {
        this.ev3HA = ev3HA;
    }

    public void demo(){
        ev3HA.drive(1000, 5000, 1);
        ev3HA.drive(1000, 5000, 0);
        ev3HA.drive(1000, 5000, 1);
        ev3HA.drive(1000, 5000, 0);
    }

    public void endLogic() {
        ev3HA.disconnect();
        //TODO: Node besser beenden
        System.exit(0);
    }
}
