package NodeClasses;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import PeerApp.Peer;
import PeerApp.PeerHelper;


public class Node {

    private static Node node; 

    //Initialize the variables
    private int maxValue; //Max value of node
    public String nodeName; //name of string
    private int leftNode; //left node
    private int secondLeftNode; //second left node
    private String nextNode; //next node
    private String nodeState; //passive/active state
    private boolean coordinatorStatus; //the coordinator of the election
    private String leaderName; //elected leader name

    //Creating constructor
    protected Node() {
        this.maxValue = -1;
        this.nodeName = "";
        this.leftNode = -1;
        this.secondLeftNode = -1;
        this.nextNode = "";
        this.nodeState = "Active";
        this.coordinatorStatus = false;
        this.leaderName = "";
    }


    //Getter setter methods

    public static Node getNode() {
        if (node == null) {
            node = new Node();
        }
        return node;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getLeftNode() {
        return this.leftNode;
    }

    public void setLeftNode(int leftNode) {
        this.leftNode = leftNode;
    }

    public int getSecondLeftNode() {
        return this.secondLeftNode;
    }

    public void setSecondLeftNode(int secondLeftNode) {
        this.secondLeftNode = secondLeftNode;
    }

    public String getNextNode() {
        return this.nextNode;
    }

    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    public String getNodeState() {
        return this.nodeState;
    }

    public void setNodeState(String nodeState) {
        this.nodeState = nodeState;
    }

    public boolean isCoordinatorStatus() {
        return this.coordinatorStatus;
    }

    public void setCoordinator() {
        this.coordinatorStatus = true;
    }

    public String getLeaderName() {
        return this.leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    //start election
    public int startElection(ORB orb) {

        // Declare a variable for the remote object reference.
        Peer nodeImpl = null;
        try {
            // Resolve the initial references to get access to the naming service.
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            
            // Narrow the generic object reference to a NamingContextExt reference, 
            // which provides an extended naming context interface.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Obtain the name of the next node
            String name = this.getNextNode();

             // Using the naming service, resolve the name to get a reference to the remote object.
            // Then, narrow it to a specific Peer interface to interact with the remote object's methods.
            nodeImpl = PeerHelper.narrow(ncRef.resolve_str(name));

        } catch (Exception e) {
            e.printStackTrace();
        }

        //sets starting node as coordinator of the election
        this.setCoordinator();
     
        // While the leader has not yet been elected (denoted by an empty leader name) 
        // and the current node's state is "Active", proceed with the election process.
        while (node.getLeaderName().equals("")  && "Active".equals(node.getNodeState())) {
            // Send a message to the next node with the maximum known value.
            nodeImpl.message1(node.getMaxValue());

            // If a leader has been elected (denoted by a non-empty leader name), 
            // exit the loop.
            if (!node.getLeaderName().equals("")) {// breaks loop if leader was elected
                break;
            }

            // Send a second type of message to the next node with the 'left' value.
            nodeImpl.message2(node.getLeftNode());

        }

        // If, after the loop, no leader has been elected and the current node's 
        // state has changed to "Passive", handle the transition.
    
        if (node.getLeaderName().equals("") && "Passive".equals(node.getNodeState())) {

            // Print the node that is passive
            this.coordinatorStatus = false;
            nodeImpl.passCoordinator();

        }

        return 0;

    }

}

 