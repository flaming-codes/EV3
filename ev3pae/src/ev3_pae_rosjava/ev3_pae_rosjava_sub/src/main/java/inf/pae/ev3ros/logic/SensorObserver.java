/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inf.pae.ev3ros.logic;

import inf.pae.ev3ros.hardware.HardwareAction;
import inf.pae.ev3ros.hardware.IHardwareAction;
import java.util.Observable;
import java.util.Observer;
import inf.pae.ev3ros.worker.ColourWorker;
import inf.pae.ev3ros.worker.SensorWorker;
import inf.pae.ev3ros.worker.TouchWorker;
import inf.pae.ev3ros.worker.UltrasonicWorker;

/**
 *
 * @author Tom Schönmann
 */
public class SensorObserver implements Observer {

    private final HardwareAction ev3HA;
    private static volatile boolean driveForward = true;
    private static volatile boolean initEnvironment = true;
    private static volatile boolean run = true;
    private static final int stallTimeDefault = 500;
    private static int stallTime;
    private static int detectionCount = 0;
    private static final int drivesWithoutDetectionDefault = 10;
    private static int drivesWithoutDetection = 0;

    public SensorObserver(IHardwareAction ev3HA) {
        this.ev3HA = (HardwareAction) ev3HA;
        stallTime = stallTimeDefault;
    }

    public void observe() throws InterruptedException {

        System.out.println("Starting test of ultra sonic.");

        SensorWorker sensor = new UltrasonicWorker(ev3HA);
        sensor.addObserver(this);

        ColourWorker color = new ColourWorker(ev3HA);
        color.addObserver(this);

        TouchWorker touch = new TouchWorker(ev3HA);
        touch.addObserver(this);

        new Thread(sensor).start();
        new Thread(color).start();
        new Thread(touch).start();

        /*
         * Initial n-degree-scan for obstacles around the robot.
         */
        environementalScan();

        /*
         * Main applicatoin loop. If 'run' gets false, 
         *  the device will shut down.
         */
        while (run) {

            while (driveForward) {

                if (drivesWithoutDetection++ == drivesWithoutDetectionDefault) {
                    drivesWithoutDetection = 0;
                    initEnvironment = true;
                    environementalScan();
                }

                ev3HA.drive(1000, 500, 1);
                Thread.sleep(500);
            }

            // If the drive was interrupted (i.e. a sensor observer), reset driving to true.
            driveForward = true;

            // Let the robot drive for 1 second.
            Thread.sleep(stallTime);

            // TODO Legacy variable from debugging. Delte if unused.
            detectionCount++;

            // Check if the app has to terminate.
            checkTerminationCondition();
        }

        sensor.changeRun(false);
    }

    private void environementalScan() {
        String newline = System.getProperty("line.separator");
        System.out.println(newline + newline + "Checking environment as described in use case 3." + newline + newline);
        int i = 0;

        Thread leds = new Thread() {
            @Override
            public void run() {
                ev3HA.ev3.getRemEV3().getLED().setPattern(1);
            }
        };
        leds.start();

        while (initEnvironment) {

            switch (i) {
                case 0:
                case 1:
                case 2:
                case 3:
                    // Turn a little bit clockwise.
                    ev3HA.turnBody(300, 300, 1);
                    break;
                case 4:
                    // TODO More debugging requiered to get close to intended travel.
                    // Return to origin orientation.
                    ev3HA.turnBody(300, 600, 0);
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                    // Return a little bit counter-clockwise.
                    ev3HA.turnBody(300, 300, 0);
                    break;
                case 9:
                    // TODO More debugging requiered to get close to intended travel.
                    // Return to origin orientation and finish scan.
                    ev3HA.turnBody(300, 600, 1);
                    initEnvironment = false;
                    leds.interrupt();
                    break;
            }

            i++;
        }
    }

    private void checkTerminationCondition() {
        String newline = System.getProperty("line.separator");
        
        System.out.println(newline + newline + "Terminating due to machting condition." + newline + newline);
        if (detectionCount >= 30) {
            driveForward = false;
            run = false;
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof UltrasonicWorker) {

            UltrasonicWorker s = (UltrasonicWorker) o;

            if (initEnvironment) {

                if (s.sample[0] < 1.0) {
                    System.out.println("Somethings out there..");
                    initEnvironment = false;
                    driveForward = true;
                    detectionCount++;
                }
                
            } else if (s.sample[0] < .30) {

                System.out.println("Detection in range of ultrasonic sensor.");

                ev3HA.turnBody(750, 1350, 1);
                driveForward = false;

                detectionCount++;
            }

        } else if (o instanceof ColourWorker) {
            ColourWorker c = (ColourWorker) o;

            for (int j = 0; j < c.sample.length; j++) {
                if (c.sample[j] > .02) {

                    driveForward = false;
                    System.out.println();

                    switch (j) {
                        case 0:
                            System.out.println("Red: " + c.sample[j]);
                            ev3HA.turnBody(750, 1350, 1);
                            ev3HA.turnBody(750, 1350, 1);
                            break;
                        case 1:
                            System.out.println("Green: " + c.sample[j]);
                            ev3HA.turnBody(350, 1000, 0);
                            break;
                        case 2:
                            System.out.println("Blue: " + c.sample[j]);
                            ev3HA.turnBody(350, 1000, 1);
                            break;
                    }
                    
                    System.out.println();
                }
            }

        } else if (o instanceof TouchWorker) {
            System.out.println("Touch detected.");
            stallTime = 4000;
            driveForward = false;

            /*
             * Make some noise!
             */
            ev3HA.ev3.getRemEV3().getAudio().playTone(16, 1500);

            /*
             * Indicate a no-gesture (aka shaking head).
             */
            ev3HA.turnHead(-45);
            ev3HA.turnHead(90);
            ev3HA.turnHead(-45);

            /*
             * Turn around by 180°.
             */
            ev3HA.turnBody(750, 1350, 1);
            ev3HA.turnBody(750, 1350, 1);

            stallTime = stallTimeDefault;
        }
    }

    public void endLogic() {
        ev3HA.disconnect();
        System.exit(0);
    }
}
