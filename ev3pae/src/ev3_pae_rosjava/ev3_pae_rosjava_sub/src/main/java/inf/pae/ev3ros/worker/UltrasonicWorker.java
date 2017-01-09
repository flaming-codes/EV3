/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inf.pae.ev3ros.worker;

import inf.pae.ev3ros.hardware.IHardwareAction;

/**
 *
 * @author Tom Schönmann <a1208739@unet.univie.ac.at>
 */
public class UltrasonicWorker extends SensorWorker{

    public UltrasonicWorker(IHardwareAction ev3HA) {
        super(ev3HA);
    }

    @Override
    protected void sleep() throws InterruptedException {
        Thread.sleep(100);
    }

    @Override
    protected float[] provideSample() {
        return ev3HA.getUltrasonicData();
    }

    @Override
    protected boolean provideCondition() {
       
        // Not-so-good implementation. Currently, the observer checks its own condition.
        return true;
    }
    
}
