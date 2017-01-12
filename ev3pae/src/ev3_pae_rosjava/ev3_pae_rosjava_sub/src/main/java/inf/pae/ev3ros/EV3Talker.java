package inf.pae.ev3ros;

import inf.pae.ev3ros.hardware.HardwareAction;
import inf.pae.ev3ros.hardware.IHardwareAction;
import inf.pae.ev3ros.logic.SensorObserver;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.remote.ev3.RemoteEV3;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 */
public class EV3Talker extends AbstractNodeMain {

    public String ipAdress = "192.168.0.17";
    public RemoteEV3 remEV3;
    public SensorObserver s;

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava/talker");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {

        connectToEV3();
        execute();
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
        } catch (RemoteException | MalformedURLException | NotBoundException ex) {
            System.err.println("Exception: " + ex);
            System.out.println("Konnte keine Verbindung zum Roboter aufbauen mit der IP-Adresse " + ipAdress);
            //TODO: Programm besser beenden
            System.exit(0);
        }

    }

    /**
     * Method to start (and in case of exceptions, end) the robot.
     */
    public void execute() {
        try {

            //Hier wird die Hardware Action Klasse initialisiert  um den EV3 zu steuern - noch ist diese Methode aber nicht implementiert
            IHardwareAction ev3HA = new HardwareAction(remEV3);
            //applicationLogic = new TomLogic(ev3HA);
            //applicationLogic.observe();
            //applicationLogic = new Logic(ev3HA);
            //applicationLogic.observe();

            s = new SensorObserver(ev3HA);
            s.observe();

        } catch (RemoteException | InterruptedException ex) {
            Logger.getLogger(inf.pae.ev3ros.EV3Talker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (s != null) {
                System.out.println("Ending application logic.");
                s.endLogic();
            }
        }
    }
}
