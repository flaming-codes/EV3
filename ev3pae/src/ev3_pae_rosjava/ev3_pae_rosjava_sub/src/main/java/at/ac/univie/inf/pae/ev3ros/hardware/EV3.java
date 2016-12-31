package at.ac.univie.inf.pae.ev3ros.hardware;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

/**
 * Die Klasse EV3 bildet den EV3-Roboter im Programm ab und stellt grundlegende
 * Funktionen zur verfuegung
 */
public class EV3 {

    private final RemoteEV3 remEV3;
    private final RMIRegulatedMotor leftMotor;
    private final RMIRegulatedMotor rightMotor;
    private final RMIRegulatedMotor headMotor;
    private final RMISampleProvider colorSensor;
    private final RMISampleProvider touchSensor;
    private final RMISampleProvider gyroSensor;
    private final RMISampleProvider ultrasonicSensor;

    /**
     * Der Konstruktor ist fuer das komplette Mapping des Roboters zustaending
     *
     * @param remEV3
     */
    public EV3(RemoteEV3 remEV3) {
        colorSensor = remEV3.createSampleProvider("S4", "lejos.hardware.sensor.EV3ColorSensor", "ColorID");
        touchSensor = remEV3.createSampleProvider("S3", "lejos.hardware.sensor.EV3TouchSensor", "Touch");
        gyroSensor = remEV3.createSampleProvider("S1", "lejos.hardware.sensor.EV3GyroSensor", "Angle");
        ultrasonicSensor = remEV3.createSampleProvider("S2", "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
        leftMotor = remEV3.createRegulatedMotor("D", 'L');
        rightMotor = remEV3.createRegulatedMotor("A", 'L');
        headMotor = remEV3.createRegulatedMotor("C", 'M');
        this.remEV3 = remEV3;
    }

    /**
     * Retouniert den linken Motor
     *
     * @return Der linke Motor
     */
    public RMIRegulatedMotor getLeftMotor() {
        return leftMotor;
    }

    /**
     * Retouniert den rechten Motor
     *
     * @return Der rechte Motor
     */
    public RMIRegulatedMotor getRightMotor() {
        return rightMotor;
    }

    /**
     * Retouniert den Motor, welcher den Kopf dreht
     *
     * @return Der Motor, welcher den Kopf dreht
     */
    public RMIRegulatedMotor getHeadMotor() {
        return headMotor;
    }

    /**
     * Retouniert den Farbsensor
     *
     * @return Der Farbsensor
     */
    public RMISampleProvider getColorSensor() {
        return colorSensor;
    }

    /**
     * Retouniert den Touchsensor
     *
     * @return Der Touchsensor
     */
    public RMISampleProvider getTouchSensor() {
        return touchSensor;
    }

    /**
     * Retouniert den Gyrosensor
     *
     * @return Der Gyrosensor
     */
    public RMISampleProvider getGyroSensor() {
        return gyroSensor;
    }

    /**
     * Retouniert den Ultrasonicsensor
     *
     * @return Der Ultrasonicsensor
     */
    public RMISampleProvider getUltrasonicSensor() {
        return ultrasonicSensor;
    }

    /**
     * Retouniert den kompletten remote EV3
     *
     * @return Der komplette remote EV3
     */
    public RemoteEV3 getRemEV3() {
        return remEV3;
    }

}
