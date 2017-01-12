/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inf.pae.ev3ros.logic;

import inf.pae.ev3ros.hardware.HardwareAction;
import inf.pae.ev3ros.hardware.IHardwareAction;
import inf.pae.ev3ros.worker.ColourWorker;
import inf.pae.ev3ros.worker.SensorWorker;
import inf.pae.ev3ros.worker.TouchWorker;
import inf.pae.ev3ros.worker.UltrasonicWorker;
import java.util.Observable;
import java.util.Observer;

/**
 * Class containing the logic to process the following flow of steps:
 * 
 *  1.      Check environment for obstacles in an n-distance.
 *  2.      If an obstacle was detected, start approaching.
 *  2.1     If no obstacle was found, return to origin orientation (== 0°)
 *          and drive for n-steps straight forward. After every step scan
 *          for obstacles in front of the device. If nothing was detected,
 *          proceed with moving forward. Else approach obstacle.
 *  2.2     If obstacle was found, approach it.
 *  3.      Use color-sensor to determine which action has to be taken:
 *          - RED:      Turn 180* degree.
 *          - GREEN:    Turn n-degree counter-clockwise.
 *          - BLUE:     Turn n-degree clockwise.
 * 4.       Repeat above steps starting from 1. until termination condition is reached.
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
    private static final int speedDefault = 1000;
    private static int speed;
    private static final int pauseTimeInMillisDefault = 500;
    private static int pauseTimieInMillis;
    private static final double colorThreshold = .02;

    public SensorObserver(IHardwareAction ev3HA) {
        this.ev3HA = (HardwareAction) ev3HA;
        stallTime = stallTimeDefault;
        speed = speedDefault;
        pauseTimieInMillis = pauseTimeInMillisDefault;
    }

    /**
     * Equivalent of main-method for the class.
     * 
     * @throws InterruptedException 
     */
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
        advancedEnvironementalScan();

        /*
         * Main applicatoin loop. If 'run' gets false, 
         *  the device will shut down.
         */
        while (run) {

            while (driveForward) {

                if (drivesWithoutDetection++ == drivesWithoutDetectionDefault) {
                    drivesWithoutDetection = 0;
                    initEnvironment = true;
                    advancedEnvironementalScan();
                }

                ev3HA.drive(speed, pauseTimieInMillis, 1);
                Thread.sleep(pauseTimieInMillis);
            }

            // If the drive was interrupted (i.e. by a sensor observer), reset driving to true.
            driveForward = true;

            // Let the robot drive for n-seconds.
            Thread.sleep(stallTime);

            // TODO Legacy variable from debugging. Delete if unused.
            detectionCount++;

            // Check if the app has to terminate.
            checkTerminationCondition();
        }

        sensor.changeRun(false);
    }

    /**
     * Checks the device's environment and acts accordingly.
     *  During the check, the device doesn't drive but rotate.
     */
    private void advancedEnvironementalScan() {
        String newline = System.getProperty("line.separator");
        System.out.println(newline + newline + "Checking environment as described in use case 3." + newline + newline);
        int i = 0;

        while (initEnvironment) {
            
            ev3HA.ev3.getRemEV3().getLED().setPattern(1);

            // Rotate step-wise.
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
                    break;
            }

            i++;
        }
        
        ev3HA.ev3.getRemEV3().getLED().setPattern(0);
    }

    /**
     * Check if a given condition or set of conditions is true.
     *  If so, the application has reached its lifecycle end.
     */
    private void checkTerminationCondition() {
   
        // If at least 30 detections were made, the app's end has been reached.
        if (detectionCount >= 30) {
            String newline = System.getProperty("line.separator");
            System.out.println(newline + newline + "Terminating due to machting condition." + newline + newline);
            
            driveForward = false;
            run = false;
        }
    }

    /**
     * Listen for actions from observed objects.
     * 
     * @param o     The observed object sending the message.
     * @param arg   Unused in this context.
     */
    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof UltrasonicWorker) {

            UltrasonicWorker s = (UltrasonicWorker) o;

            // Check if the device is currently advanced-scanning its environment or not,
            //  thus using the right set of operations.
            if (initEnvironment) {

                // An obstacle is detected if it's as close as one meter at max.
                if (s.sample[0] < 1.0) {
                    System.out.println("Somethings out there..");
                    
                    // Stop scan of environment.
                    initEnvironment = false;
                    
                    // Let's go exploring!
                    driveForward = true;
                    
                    // Add to counted detections.
                    detectionCount++;
                }
                
            } 
            
            // The following operation applies only if the device isn't advanced-scanning its environment.
            else if (s.sample[0] < .20) {

                System.out.println("Detection in range of ultrasonic sensor.");

                ev3HA.turnBody(750, 1350, 1);
                driveForward = false;

                detectionCount++;
            }

        } 
        
        // The observed color sensor has notified the observer. Act accordingly.
        else if (o instanceof ColourWorker) {
            ColourWorker c = (ColourWorker) o;
            
            // Variables to store type of color and its measured intensiness.
            double highestUsedSample = 0.0;
            int colorID = -1;

            // Check if there's a matching color in the sample.
            for (int j = 0; j < c.sample.length; j++) {
                if (c.sample[j] > colorThreshold) {
                    if(highestUsedSample < c.sample[j]){
                        highestUsedSample = c.sample[j];
                        colorID = j;
                    }
                }
            }
            
            // If a color matching to the rules was determined, act accordingly.
            if(colorID != -1){
                driveForward = false;
                    System.out.println();

                    switch (colorID) {
                        case 0:
                            System.out.println("Red: " + highestUsedSample);
                            ev3HA.turnBody(750, 1350, 1);
                            ev3HA.turnBody(750, 1350, 1);
                            break;
                        case 1:
                            System.out.println("Green: " + highestUsedSample);
                            ev3HA.turnBody(350, 1000, 0);
                            break;
                        case 2:
                            System.out.println("Blue: " + highestUsedSample);
                            ev3HA.turnBody(350, 1000, 1);
                            break;
                    }
                    
                    System.out.println();
            }

        } 
        
        // The touch sensor has fired. Do something!
        else if (o instanceof TouchWorker) {
            System.out.println("Touch detected.");
            
            // Light the LED red.
            ev3HA.ev3.getRemEV3().getLED().setPattern(2);
            
            // Change stallTime to give the roboter more time for movement.
            stallTime = 8000;
            
            // Interrupt the drive.
            driveForward = false;

            // Make some noise!
            ev3HA.ev3.getRemEV3().getAudio().systemSound(2);

            // Indicate a no-gesture (aka shaking head).
            ev3HA.turnHead(-45);
            ev3HA.turnHead(90);
            ev3HA.turnHead(-45);

            // Turn around by roughly 180°.
            ev3HA.turnBody(750, 1350, 1);
            ev3HA.turnBody(750, 1350, 1);

            // Reset stallTime to its default value.
            stallTime = stallTimeDefault;
            
            ev3HA.ev3.getRemEV3().getLED().setPattern(0);
        }
    }

    /**
     * Correctly disconnect from the remote EV3 and exit the application.
     */
    public void endLogic() {
        ev3HA.disconnect();
        System.exit(0);
    }
}
