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
public class TouchWorker extends SensorWorker {

    public TouchWorker(IHardwareAction ev3HA) {
        super(ev3HA);
    }

    @Override
    protected void sleep() throws InterruptedException {
        Thread.sleep(500);
    }

    @Override
    protected float[] provideSample() {
        return ev3HA.getTouchData();
    }

    @Override
    protected boolean provideCondition() {
        
        if(sample != null && sample[0] == 1.0){
            sample = null;
            return true;
            
        }else{
            return false;
        }
    }
}
