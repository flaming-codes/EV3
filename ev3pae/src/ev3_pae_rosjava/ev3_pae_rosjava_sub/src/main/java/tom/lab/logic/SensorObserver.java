/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tom.lab.logic;

import at.ac.univie.inf.pae.ev3ros.hardware.IHardwareAction;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Tom Schönmann
 */
public class SensorObserver implements Observer{
    
    private final IHardwareAction ev3HA;
    private static boolean driveForward = true;
    private static int detectionCount = 0;
    
    public SensorObserver(IHardwareAction ev3HA){
        this.ev3HA = ev3HA;
    }
    
    public void demo() throws InterruptedException{
        
        System.out.println("Starting test of ultra sonic.");
        
        SensorWorker sensor = new SensorWorker(ev3HA);
        sensor.addObserver(new SensorObserver(ev3HA));
        
        Thread t = new Thread(sensor);
        t.start();
        
        while(detectionCount < 30){
            
            while(driveForward){
                ev3HA.drive(1000, 500, 1);
                Thread.sleep(1000);
            }
            
            driveForward = true;
        }
        
        System.out.println("Shutting down observables.");
        sensor.changeRun(false);
        
        System.out.println("Reached end of observer.");
    }
    
    @Override
    public void update(Observable o, Object arg) {
        
        if(o instanceof SensorWorker){
            SensorWorker s = (SensorWorker) o;
            System.out.println("The UltrasonicSensor has detected something: " + s.sample);
            
            if(s.sample < .30){
                driveForward = false;
                detectionCount++;
                ev3HA.turnBody(750, 1350, 1);
            }
        }
    }
    
    public void endLogic() {
        ev3HA.disconnect();
        System.exit(0);   
    }
}
