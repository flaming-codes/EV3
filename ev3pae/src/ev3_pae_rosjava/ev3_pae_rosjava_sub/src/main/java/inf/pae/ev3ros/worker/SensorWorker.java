/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inf.pae.ev3ros.worker;

import inf.pae.ev3ros.hardware.IHardwareAction;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tom
 */
public abstract class SensorWorker extends Observable implements Runnable{
    
    protected final IHardwareAction ev3HA;
    private volatile boolean run = true;
    public float[] sample;

    public SensorWorker(IHardwareAction ev3HA){
        this.ev3HA = ev3HA;
    }
    
    @Override
    public final void run() {
        
        while(run){
            try {
                this.sleep();
                sample = this.provideSample();
                
                if(provideCondition())
                    reportDetection();
                
            } catch (InterruptedException ex) {
                Logger.getLogger(SensorWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public final void reportDetection(){
        setChanged();
        notifyObservers();
    }
    
    public final void changeRun(boolean run){
        this.run = run;
    }
    
    protected abstract void sleep() throws InterruptedException;
    protected abstract float[] provideSample();
    protected abstract boolean provideCondition();
}
