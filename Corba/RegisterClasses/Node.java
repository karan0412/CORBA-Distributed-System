package RegisterClasses;

import java.util.ArrayList;
public class Node {

    // Static list to store all Node instances. It represents a ring of nodes.
    public static ArrayList<Node> nodeRing = new ArrayList<>();

    // Static list to store max values.
    public static ArrayList<Integer> maxList = new ArrayList<>();

    // Static variable to keep track of the number of nodes.
    public static int nodeNum;

    // Static flag to check if leader election is happening.
    public static boolean leaderElection = false;

    // Instance variable to store the name of the node.
    private final String nodeName;

    // Instance variable to store the maximum value associated with the node.
    private final int maxValue;

    /**
     * Constructor for the Node class.
     *
     * @param nodeName The name of the node.
     * @param maxValue  The maximum value associated with the node.
     */
    public Node(String nodeName, int maxValue) {
        this.nodeName = nodeName;
        this.maxValue = maxValue;
        nodeRing.add(this);
    }

    //Getter setter methods
    public String getNodeName() {
        return nodeName;
    }

    public int getMaxValue() {
        return maxValue;
    }
    

    
    public String getNextName(int i) {
        return nodeRing.get((i + 1) % nodeRing.size()).getNodeName();
    }

}
