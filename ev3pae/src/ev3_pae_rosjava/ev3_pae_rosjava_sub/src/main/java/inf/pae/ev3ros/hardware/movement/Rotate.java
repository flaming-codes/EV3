/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inf.pae.ev3ros.hardware.movement;

import inf.pae.ev3ros.hardware.EV3;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tom Schönmann <a1208739@unet.univie.ac.at>
 */
public class Rotate extends WheelMovement {
    
    private double rotateDeg;

    public Rotate(int speed, int milliseconds, int direction, int rotateDeg, EV3 ev3) {
        super(speed, milliseconds, direction, ev3);
        
        /*
         * Represents the degree to which the motor will rotate.
         */
        this.rotateDeg = rotateDeg;
    }

    @Override
    public void run() {

        try {
            
            if(direction == 0)
                rotateDeg *= -1;
            
            double distance = (3.1415 * (ev3.distanceBetweenWheels/2)) / rotateDeg;
            distance /= 360.0 / rotateDeg;
            double rotations = distance / (ev3.wheelDiameter * 3.1415);
            double degrees = rotations * 360.0;
            
            int result = (int) Math.round(degrees);
            
            ev3.getLeftMotor().setSpeed(speed);
            ev3.getRightMotor().setSpeed(speed);
            
            ev3.getLeftMotor().setAcceleration(1000);
            ev3.getRightMotor().setAcceleration(1000);
            
            ev3.getLeftMotor().rotate(result * -1, true);
            ev3.getRightMotor().rotate(result, true);
            
        } catch (RemoteException ex) {
            Logger.getLogger(Rotate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
