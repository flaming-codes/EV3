/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tom.lab.logic;

import at.ac.univie.inf.pae.ev3ros.hardware.IHardwareAction;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tom
 */
public class SensorWorker extends Observable implements Runnable{
    
    private final IHardwareAction ev3HA;
    private volatile boolean run = true;
    public float sample;

    public SensorWorker(IHardwareAction ev3HA){
        this.ev3HA = ev3HA;
    }
    
    @Override
    public void run() {
        
        while(run){
            try {
                Thread.sleep(500);
                reportDetection();
                
            } catch (InterruptedException ex) {
                Logger.getLogger(SensorWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void reportDetection(){
        sample = ev3HA.getUltrasonicData()[0];
        
        setChanged();
        notifyObservers();
    }
    
    public void changeRun(boolean run){
        this.run = run;
    }
}
