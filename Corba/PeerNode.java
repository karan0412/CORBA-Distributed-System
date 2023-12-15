
import PeerApp.Peer;
import PeerApp.PeerHelper;
import PeerApp.PeerPOA;
import PeerApp.Register;
import PeerApp.RegisterHelper;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import java.io.Console;

import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import NodeClasses.Node;

class PeerImpl extends PeerPOA {
    
    

    private ORB orb;

    // Set the ORB for this object
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    // Setup node's max value and next node name
    public int setup(int max, String nextName) {
        Node node = Node.getNode();
        node.setMaxValue(max);
        node.setNextNode(nextName);
        System.out.println("---");
        System.out.println("Node name: " + node.getNodeName() + ". Max value: " + node.getMaxValue() + ". Next node name: " + node.getNextNode());
        System.out.println("---");
        System.out.println("");

        return 0;
    }

    // Initiate the election process for the leader node
    public int startElection() {
        
        Node node = Node.getNode();
        System.out.println("Starting Election...");

        node.startElection(this.orb);

        return 0;
    }


    // Message processing method for leader election
    public int message1(int receivedMax) {

        Peer nodeImpl = null;
        Node node = Node.getNode();

        try {
            // Get a reference to the CORBA NameService
             org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            // Narrow the reference to the NamingContextExt type for extended naming operations
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Get the name of the next node to communicate with
            String name = node.getNextNode();

            // Narrow the reference to the Peer type to perform operations defined by the Peer interface
            nodeImpl = PeerHelper.narrow(ncRef.resolve_str(name));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the current node isn't a coordinator and is in an active state, forward the message to the next node
        if (!node.isCoordinatorStatus() && "Active".equals(node.getNodeState())) {
            nodeImpl.message1(node.getMaxValue());
        }


        // Handle the received message for an Active node
        if ("Active".equals(node.getNodeState())) {

            // Log the current node's status
            System.out.println(node.getNodeName() + " node is " + node.getNodeState());
            System.out.println("");


            // If the received max value differs from the current node's max value
            if (receivedMax != node.getMaxValue()) {

                // Update the current node's "left node" with the received max value
                node.setLeftNode(receivedMax);

            } else {

                // If the received max value matches the current node's max value, then the current node becomes the leader
                node.setNodeState("Leader");
                node.setLeaderName(node.getNodeName());

                // Announce the current node as the leader to the next node
                nodeImpl.announceLeader(node.getNodeName(), node.getMaxValue());
                System.out.println("**************************************************");
                System.out.println("This Node Is Now Elected As Leader With Max: " + node.getMaxValue());
                System.out.println("**************************************************");

            }

        } else if ("Passive".equals(node.getNodeState())) {

            // If the current node is in a passive state, forward the message to the next node without taking action
            System.out.println(node.getNodeName() + " node is " + node.getNodeState());
            nodeImpl.message1(receivedMax);

        }
        return 0;

    }


    // Second message processing method for leader election
    public int message2(int receivedLeft) {

        Node node = Node.getNode();
        Peer nodeImpl = null;

        try {
             org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = node.getNextNode();
            nodeImpl = PeerHelper.narrow(ncRef.resolve_str(name));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the current node isn't a coordinator and is in an active state, forward the message to the next node
        if (!node.isCoordinatorStatus() && "Active".equals(node.getNodeState())) {

            nodeImpl.message2(node.getLeftNode());

        }

        // Handle the received message for an Active node
        if ("Active".equals(node.getNodeState())) {

            // Set the value of the second left node based on the received value
            node.setSecondLeftNode(receivedLeft);

            // If the value from the left node is greater than both the max value and the second left node value
            if (node.getLeftNode() > node.getMaxValue() && node.getLeftNode() > node.getSecondLeftNode()) {

                // Update the max value with the left node's value
                node.setMaxValue(node.getLeftNode());
                return 0;

            } else {
                // Otherwise, set the node's state to Passive and log its status
                node.setNodeState("Passive");
                System.out.println(node.getNodeName() + " node is " + node.getNodeState());

            }

        } else {
            // If the node's state is not Active (e.g., Passive), forward the message to the next node
            System.out.println(node.getNodeName() + " node is " + node.getNodeState());
            nodeImpl.message2(receivedLeft);

        }

        System.out.println("");

        return 0;
    }


    // Pass coordinator responsibility to another node
    public int passCoordinator() {

        Node node = Node.getNode();
        node.setCoordinator();
        Peer nodeImpl = null;

        try {
             org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = node.getNextNode();
            nodeImpl = PeerHelper.narrow(ncRef.resolve_str(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Keep sending messages as long as the leader is not yet determined and the node is active
        while (node.getLeaderName().equals("") && "Active".equals(node.getNodeState())) {

            // Send a message to the peer (other node) with the maximum value of the current node
            nodeImpl.message1(node.getMaxValue());

            // If the leader is determined, break out of the loop
            if (!node.getLeaderName().equals("")) {
                break;
            }

            // Send another message to the peer with the left node value of the current node
            nodeImpl.message2(node.getLeftNode());

        }

        // If the leader is still not determined and the current node is passive
        if (node.getLeaderName().equals("") && "Passive".equals(node.getNodeState())) {

            // Print out the status of the current node
            System.out.println(node.getNodeName() + " node is " + node.getNodeState());
            System.out.println("");

            // Initiate the coordinator passing process on the peer node
            nodeImpl.passCoordinator();

        }

        return 0;
    }


    // Announce the leader node after election
    public int announceLeader(String name, int max) {

        Node node = Node.getNode();

        if (node.isCoordinatorStatus()) {
            return 0;
        }
        Peer nodeImpl = null;
        

        try {
             org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String nextName = node.getNextNode();
            nodeImpl = PeerHelper.narrow(ncRef.resolve_str(nextName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Print the leader announcement message
        System.out.println("************************************************************************");
        System.out.println("Node " + name + " is now the election leader with the max value of: " + max);
        System.out.println("************************************************************************");

        // Update the leader's name in the current node
        node.setLeaderName(name);

        //Announce the winner
        nodeImpl.announceLeader(name, max);

        return 0;
    }

}

public class PeerNode {

    public static void main(String args[]) {
        Register registerImpl;

        try {

            // Initialize the CORBA ORB (Object Request Broker) with provided arguments
            ORB orb = ORB.init(args, null);

            // Get the CORBA object reference to the NameService
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            // Narrow the CORBA object reference to a specific NamingContextExt type
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Specify the name of the registration service
            String name = "PeerRegister";

            // Narrow the object reference to the Register type
            registerImpl = RegisterHelper.narrow(ncRef.resolve_str(name));

            // Read the peer's name from the console input
            Console console = System.console();
            String peerid = console.readLine("\nWhat is your name? ");

            // Create a new node and set its name to the peer's name
            Node newNode = Node.getNode();
            newNode.setNodeName(peerid);

            // Obtain reference to the POA (Portable Object Adapter)
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

            // Activate the POA manager to start processing requests
            rootpoa.the_POAManager().activate();

            // Create and initialize a Peer implementation object
            PeerImpl peerImpl = new PeerImpl();
            peerImpl.setORB(orb);

            // Convert the Peer servant to a CORBA reference
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(peerImpl);

            // Narrow the reference to the Peer type
            Peer href = PeerHelper.narrow(ref);

            // Convert the peer's name to a name suitable for the Naming service
            NameComponent path[] = ncRef.to_name(peerid);

            // Bind the Peer reference to the specified name in the Naming service
            ncRef.rebind(path, href);

            // Register the peer with the PeerRegister service
            if (!(registerImpl.connect(peerid) < 0)) {
              System.out.println(peerid + " has been registered with " + name);
            } else {
              System.out.println("Error registering peer");
              System.exit(0);
            }

            // Wait for incoming requests
            orb.run();
      
          } catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace(System.out);
          }

        System.out.println("Node Exiting ...");

    }
}

