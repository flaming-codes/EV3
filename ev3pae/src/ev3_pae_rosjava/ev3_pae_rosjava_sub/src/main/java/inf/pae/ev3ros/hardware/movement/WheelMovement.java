package inf.pae.ev3ros.hardware.movement;

import inf.pae.ev3ros.hardware.EV3;

/**
 * WheelMovement stellt die abstrakte Basisiklasse aller Bewegungen der grossen
 * Reifenmotoren dar
 */
public abstract class WheelMovement extends Movement {

    int speed = 500;
    int milliseconds = 1000;
    int direction = 1;

    /**
     * Der Konstruktor uebernimmt alle uebergebenen Werte
     *
     * @param speed Die Geschwindigkeit der Bewegung
     * @param milliseconds Die Dauer der Bewegung
     * @param direction Die Richtung der Bewegung
     * @param ev3 Der Roboter, der bewegt werden soll
     */
    public WheelMovement(int speed, int milliseconds, int direction, EV3 ev3) {
        super(ev3);
        this.speed = speed;
        this.milliseconds = milliseconds;
        this.direction = direction;
    }

}
