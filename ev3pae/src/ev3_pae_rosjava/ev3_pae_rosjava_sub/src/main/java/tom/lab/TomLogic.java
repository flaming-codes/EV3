/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tom.lab;

import at.ac.univie.inf.pae.ev3ros.hardware.IHardwareAction;

/**
 *
 * @author tom
 */
public class TomLogic {
    
    private final IHardwareAction ev3HA;
    public static volatile boolean sonicInUse = true;
    
    public TomLogic(IHardwareAction ev3HA) {
        this.ev3HA = ev3HA;
    }
    
    public void demo() throws InterruptedException{

        System.out.println("Starting test of ultra sonic.");
        
        Thread t = new Thread(new SonicDetector(ev3HA));
        t.start();
        
        while(sonicInUse){
            Thread.sleep(5000);
        }
    }
    
    public void endLogic() {
        ev3HA.disconnect();
        //TODO: Node besser beenden
        System.exit(0);
    }
}
