import PeerApp.RegisterHelper;
import PeerApp.Register;
import PeerApp.RegisterPOA;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.POA;

import org.omg.PortableServer.POAHelper;
import RegisterClasses.Node;
import RegisterClasses.Methods;

// Implementation of the Register interface
class RegisterImpl extends RegisterPOA {


    // Reference to the CORBA ORB
    private ORB orb;


    // Method to set the ORB reference
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    // Method for a node to connect
    public int connect(String name) {

        // Creates a single-threaded executor to schedule commands in the background
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Check if node list is full
        if (Node.nodeRing.size() == Node.maxList.size()) {
            System.out.println("Node cannot connect");
            return -1;
        }

        // If a leader election is in progress, prevent node from connecting
        if (Node.leaderElection) {
            System.out.println("Node cannot connect");

            return -2;
        }

        Node newNode = new Node(name, Node.maxList.get(Node.nodeRing.size()));

        System.out.println("Node: " + Node.nodeRing.size() + " Connected");

        Methods.connectionHandler(this.orb);

        // If the number of nodes reaches a threshold, start the leader election
        if (Node.nodeRing.size() >= Node.nodeNum) {
            Node.leaderElection = true;
            scheduler.schedule(this::election, 0, TimeUnit.SECONDS);
        }

        System.out.println("-----");

        // Return the size of maxList, presumably for diagnostic purposes
        return Node.maxList.size();
    }

    // Method to start the leader election process
    public int election() {
        Methods.startElection(this.orb);

        return 0;
    }
}


// Main class for the peer registration server
public class PeerRegister {

    public static void main(String args[]) {
        try {

            // Read configurations
            Methods.readFile(); 

            ORB orb = ORB.init(args, null);


            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();


            RegisterImpl registerImpl = new RegisterImpl();
            registerImpl.setORB(orb);


            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(registerImpl);
            Register href = RegisterHelper.narrow(ref);


            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);


            // Convert the server's name to a name suitable for the Naming service
            String name = "PeerRegister";
            NameComponent path[] = ncRef.to_name(name);

            // Bind the Register reference to the specified name in the Naming service
            ncRef.rebind(path, href);

            System.out.println("PeerRegister ready and waiting ...");


            orb.run();
        }

        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

        System.out.println("HelloServer Exiting ...");

    }
}
