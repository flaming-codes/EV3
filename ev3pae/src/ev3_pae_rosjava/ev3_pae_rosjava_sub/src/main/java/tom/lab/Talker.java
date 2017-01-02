package tom.lab;

import at.ac.univie.inf.pae.ev3ros.hardware.HardwareAction;
import at.ac.univie.inf.pae.ev3ros.hardware.IHardwareAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.remote.ev3.RemoteEV3;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import tom.lab.logic.SensorObserver;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 */
public class Talker extends AbstractNodeMain {

    public String ipAdress = "192.168.0.23";
    public RemoteEV3 remEV3;
    public SensorObserver s;
    
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
            //applicationLogic = new TomLogic(ev3HA);
            //applicationLogic.demo();
            //applicationLogic = new Logic(ev3HA);
            //applicationLogic.demo();
            
            s = new SensorObserver(ev3HA);
            s.demo();

        } catch (Exception ex) {
            Logger.getLogger(at.ac.univie.inf.pae.ev3ros.Talker.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if(s != null){
                System.out.println("Ending application logic.");
                s.endLogic();
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
