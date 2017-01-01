/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tom.lab;

import at.ac.univie.inf.pae.ev3ros.hardware.IHardwareAction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tom
 */
public class SonicDetector implements Runnable{
    
    IHardwareAction ev3HA;
    
    public SonicDetector(IHardwareAction ev3HA){
        this.ev3HA = ev3HA;
    }

    @Override
    public void run() {
        
        System.out.println("New Detector-thread started.");
        int count = 0;
        
        while(TomLogic.sonicInUse){
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SonicDetector.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(ev3HA.getUltrasonicData()[0] < 1)
                System.out.println("Object closer than 1 meter.");
            
            if(++count == 20)
                TomLogic.sonicInUse = false;
        }
    }
    
}
