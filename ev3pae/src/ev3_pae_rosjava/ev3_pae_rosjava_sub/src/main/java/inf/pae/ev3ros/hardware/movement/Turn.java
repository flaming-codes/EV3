package inf.pae.ev3ros.hardware.movement;

import inf.pae.ev3ros.hardware.EV3;

/**
 * Turn sorgt dafuer, dass sich der Roboter drehen kann
 */
public class Turn extends WheelMovement {

    /**
     * Dieser Konstruktor ruft den Konstrukter von WheelMovement auf
     *
     * @param speed Die Geschwindigkeit der Drehung
     * @param milliseconds Die Dauer der Drehung
     * @param direction Die Richtung der Drehung
     * @param ev3 Der Robter der gedreht werden soll
     */
    public Turn(int speed, int milliseconds, int direction, EV3 ev3) {
        super(speed, milliseconds, direction, ev3);
    }

    @Override
    public void run() {
        try {

            ev3.getLeftMotor().setSpeed(speed);
            ev3.getRightMotor().setSpeed(speed);

            switch (direction) {
                case 0:
                    ev3.getLeftMotor().backward();
                    ev3.getRightMotor().forward();
                    break;
                case 1:
                    ev3.getLeftMotor().forward();
                    ev3.getRightMotor().backward();
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            Thread.sleep(milliseconds);
            stopMovement();

        } catch (Exception ex) {
            System.err.println("Fehler beim Ansteuern der Motoren, Details: " + ex);
        }
    }

}
