package inf.pae.ev3ros;

import inf.pae.ev3ros.hardware.HardwareAction;
import inf.pae.ev3ros.hardware.IHardwareAction;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.remote.ev3.RemoteEV3;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import inf.pae.ev3ros.logic.SensorObserver;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 */
public class Talker extends AbstractNodeMain {

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
        demoObserver();
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

    public void demoLocalLike() throws RemoteException, InterruptedException {
        System.out.println("Starting localLike demo.");

        HardwareAction eve = new HardwareAction(remEV3);

        for (int i = 0; i < 30; i++) {

            float[] sample = eve.getColorData();

            for (int j = 0; j < sample.length; j++) {
                if (sample[j] > .02) {
                    switch (j) {
                        case 0:
                            System.out.println("Red: " + sample[j]);
                            break;
                        case 1:
                            System.out.println("Green: " + sample[j]);
                            break;
                        case 2:
                            System.out.println("Blue: " + sample[j]);
                            break;
                    }
                } else {
                    System.out.println("Colours: " + Arrays.toString(sample));
                }
            }

            System.out.println();
            Thread.sleep(2000);
            i++;
        }

        System.out.println("Testing color sensor.");

        remEV3.getLED().setPattern(lejos.robotics.Color.BLUE);
        Thread.sleep(3000);
        remEV3.getLED().setPattern(lejos.robotics.Color.MAGENTA);
        Thread.sleep(3000);
        remEV3.getLED().setPattern(lejos.robotics.Color.YELLOW);
        Thread.sleep(3000);
        remEV3.getLED().setPattern(lejos.robotics.Color.WHITE);
        Thread.sleep(3000);

        //eve.disconnect();
        System.exit(1);

        /*
         Port p = remEV3.getPort("D");
         BasicMotor motorLeft = new EV3LargeRegulatedMotor(p); 
        
         p = remEV3.getPort("A");
         EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(p); 
        
         motorLeft.setAcceleration(1000);
         motorRight.setAcceleration(1000);
        
         motorLeft.setSpeed(1000);
         motorRight.setSpeed(1000);      
        
         Wheel leftWheel = WheeledChassis.modelWheel(motorLeft, 5.6).offset(15.6);
         Wheel rightWheel = WheeledChassis.modelWheel(motorRight, 5.6).offset(15.6);
         Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
         MovePilot pilot = new MovePilot(chassis);
         pilot.setAngularAcceleration(20);
         pilot.setAngularSpeed(20);
        
         pilot.travel(50); 
         pilot.rotate(-90);
        
         while(pilot.isMoving())Thread.yield();
        
         pilot.stop();
         */
    }

    // Works.
    public void demoObserver() {
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
            Logger.getLogger(inf.pae.ev3ros.Talker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (s != null) {
                System.out.println("Ending application logic.");
                s.endLogic();
            }
        }
    }
}
