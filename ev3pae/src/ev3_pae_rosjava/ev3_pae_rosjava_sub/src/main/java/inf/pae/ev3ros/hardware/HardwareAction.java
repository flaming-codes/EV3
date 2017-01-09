package inf.pae.ev3ros.hardware;

import inf.pae.ev3ros.hardware.movement.Drive;
import inf.pae.ev3ros.hardware.movement.HeadTurn;
import inf.pae.ev3ros.hardware.movement.Movement;
import inf.pae.ev3ros.hardware.movement.MovementManager;
import inf.pae.ev3ros.hardware.movement.Rotate;
import inf.pae.ev3ros.hardware.movement.Turn;
import java.rmi.RemoteException;
import lejos.remote.ev3.RemoteEV3;

/**
 * Die Klasse HadwareAction implementiert das Interface IHardwareAction und
 * bietet somit Moeglichkeiten mit dem EV3 zu interagieren
 */
public class HardwareAction implements IHardwareAction {

    public EV3 ev3;
    public MovementManager wheelMovementManager;
    public MovementManager headMovementManager;

    /**
     * Der Konstruktor speichert sich den uebergebenen remote EV3 und
     * initialisiert die Movement Manager
     *
     * @param remEV3 Der Remote EV3
     * @throws RemoteException
     */
    public HardwareAction(RemoteEV3 remEV3) throws RemoteException {

        try {

            ev3 = new EV3(remEV3);
            wheelMovementManager = new MovementManager();
            headMovementManager = new MovementManager();

        } catch (Exception e) {
            System.err.println("Es gab einen Fehler beim Initialisieren der Roboterteile! Details: " + e);
            System.out.println("Verbindung zum Roboter wird aufgeloest");
            disconnect();
            System.out.println("Rosnode wird beendet");
            //TODO richtig Rosnode beenden
            System.exit(0);
        }

    }

    @Override
    public void turnHead(double degrees) {

        degrees = degrees * 2.4; //Korrektur, da die API scheinbar fehlerhaft ist
        Movement movement = new HeadTurn((int) degrees, ev3);
        headMovementManager.addMovement(movement);

    }

    @Override
    public void drive(int speed, int milliseconds, int direction) {

        Movement movement = new Drive(speed, milliseconds, direction, ev3);
        wheelMovementManager.addMovement(movement);

    }

    @Override
    public void turnBody(int speed, int milliseconds, int direction) {

        Movement movement = new Turn(speed, milliseconds, direction, ev3);
        wheelMovementManager.addMovement(movement);

    }

    @Override
    public void colorLight(int color) {
        ev3.getRemEV3().getLED().setPattern(color);
    }

    @Override
    public float[] getColorData() {

        float[] colorData = null;

        try {
            colorData = ev3.getColorSensor().fetchSample();
        } catch (Exception ex) {
            System.err.println("Fehler beim Lesen der Farbsensordaten! Details: " + ex);
        }

        return colorData;
    }

    @Override
    public float[] getTouchData() {

        float[] touchData = null;

        try {
            touchData = ev3.getTouchSensor().fetchSample();
        } catch (Exception ex) {
            System.err.println("Fehler beim Lesen der Touchsensordaten! Details: " + ex);
        }

        return touchData;
    }

    @Override
    public float[] getGyroData() {

        float[] gyroData = null;

        try {
            gyroData = ev3.getGyroSensor().fetchSample();
        } catch (Exception ex) {
            System.err.println("Fehler beim Lesen der Gyrosensordaten! Details: " + ex);
        }

        return gyroData;
    }

    @Override
    public float[] getUltrasonicData() {

        float[] ultrasonicData = null;

        try {
            ultrasonicData = ev3.getUltrasonicSensor().fetchSample();
        } catch (Exception ex) {
            System.err.println("Fehler beim Lesen der Ultraschallsensordaten! Details: " + ex);
        }

        return ultrasonicData;
    }

    @Override
    public final void disconnect() {
        try {
            ev3.getColorSensor().close();
        } catch (Exception ex) {
        }
        try {
            ev3.getTouchSensor().close();
        } catch (Exception ex) {
        }
        try {
            ev3.getGyroSensor().close();
        } catch (Exception ex) {
        }
        try {
            ev3.getUltrasonicSensor().close();
        } catch (Exception ex) {
        }
        try {
            ev3.getLeftMotor().close();
        } catch (Exception ex) {
        }
        try {
            ev3.getRightMotor().close();
        } catch (Exception ex) {
        }
        try {
            ev3.getHeadMotor().close();
        } catch (Exception ex) {
        }
    }

    @Override
    public boolean isMovementFinished() {
        if (headMovementManager.isMovementFinished() == true && wheelMovementManager.isMovementFinished() == true) {
            return true;
        }
        return false;
    }
    
    @Override
    public void rotateBody(int speed, int milliseconds, int direction, int rotateDeg) {
        
        Movement movement = new Rotate(speed, milliseconds, direction, rotateDeg, ev3);
        headMovementManager.addMovement(movement);
    }
}
