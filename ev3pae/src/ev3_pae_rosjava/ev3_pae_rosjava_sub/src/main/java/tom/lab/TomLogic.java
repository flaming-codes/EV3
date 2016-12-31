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
    
    public TomLogic(IHardwareAction ev3HA) {
        this.ev3HA = ev3HA;
    }
    
    public void demo(){
        ev3HA.drive(1000, 5000, 1);
        ev3HA.drive(1000, 5000, 0);
    }
    
    public void endLogic() {
        ev3HA.disconnect();
        //TODO: Node besser beenden
        System.exit(0);
    }
}
