package tom.lab;

import at.ac.univie.inf.pae.ev3ros.Logic;
import at.ac.univie.inf.pae.ev3ros.hardware.EV3;
import at.ac.univie.inf.pae.ev3ros.hardware.HardwareAction;
import at.ac.univie.inf.pae.ev3ros.hardware.IHardwareAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.hardware.motor.Motor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 */
public class Talker extends AbstractNodeMain {

    public String ipAdress = "192.168.0.23";
    public RemoteEV3 remEV3;
    public TomLogic applicationLogic;
    
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava/talker");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {

        connectToEV3();
        
        try {

            //Hier wird die Hardware Action Klasse initialisiert  um den EV3 zu steuern - noch ist diese Methode aber nicht implementiert
            IHardwareAction ev3HA = new HardwareAction(remEV3);
            applicationLogic = new TomLogic(ev3HA);
            applicationLogic.demo();
            //applicationLogic = new Logic(ev3HA);
            //applicationLogic.demo();

        } catch (Exception ex) {
            Logger.getLogger(at.ac.univie.inf.pae.ev3ros.Talker.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if(applicationLogic != null){
                System.out.println("Ending application logic.");
                applicationLogic.endLogic();
            }
        }
    }

    /**
     * Diese Funktion stellt eine Verbindung zum EV3 her
     */
    private void connectToEV3() {

        try {
            //Hier wird die Verbindung mit dem Roboter aufgebaut
            remEV3 = new RemoteEV3(ipAdress);
            remEV3.setDefault();
            //Ein grünes Licht am Roboter signalisiert, dass die Verbindung erfolgreich aufgebaut wurde
            remEV3.getLED().setPattern(1);
            remEV3.getLED().setPattern(0);
        } catch (Exception ex) {
            System.err.println("Exception: " + ex);
            System.out.println("Konnte keine Verbindung zum Roboter aufbauen mit der IP-Adresse " + ipAdress);
            //TODO: Programm besser beenden
            System.exit(0);
        }

    }
}
