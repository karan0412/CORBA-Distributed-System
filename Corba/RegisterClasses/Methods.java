package RegisterClasses;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Scanner;

import PeerApp.Peer;
import PeerApp.PeerHelper;


import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;


public class Methods {

    /**
     * Method to handle connections, send values, and update the node ring.
     *
     * @param orb The ORB instance used for CORBA operations.
     * @return Always returns 0 as per the current implementation.
     */

    public static int connectionHandler(ORB orb) {
        System.out.println("Sending Values and Updating Ring");

        // Get the list of nodes from the Node class
        ArrayList<Node> nodeList = Node.nodeRing;

        for (int i = 0; i < nodeList.size(); i++) {

            // Sleep for 100ms. This might be to avoid rapid execution or for synchronization purposes.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Peer nodeImpl = null;
            try {
                // CORBA operations to resolve node references
                org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

                // Get the name of the node at the current index
                String name = nodeList.get(i).getNodeName();
                nodeImpl = PeerHelper.narrow(ncRef.resolve_str(name));

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Fetch node attributes for further operations
            int max = nodeList.get(i).getMaxValue();
            String nextName = nodeList.get(i).getNextName(i);
            String name = nodeList.get(i).getNodeName();

            // Displaying details about the node
            System.out.println("---");
            System.out.println("Node Name: " + name + ". Max: " + max + ". Next Name: " + nextName);
            System.out.println("---");
            System.out.println("");

            // Setup method
            nodeImpl.setup(max, nextName);
        }

        return 0;
    }

    /**
     * Method to start the election process among nodes.
     *
     * @param orb The ORB instance used for CORBA operations.
     * @return Always returns 0 as per the current implementation.
     */
    public static int startElection(ORB orb) {

        Peer nodeImpl = null;

        try {
            // CORBA operations to resolve node references
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Getting the name of the first node in the ring
            String name = Node.nodeRing.get(0).getNodeName();
            nodeImpl = PeerHelper.narrow(ncRef.resolve_str(name));

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\nElection Will Now Begin Among Nodes With Node: " + 1);


        // Initiating the election on the first node
        nodeImpl.startElection();
        System.out.println("\nElection Finished Successfully");

        // Setting the leaderElection flag to false after the election is completed
        Node.leaderElection = false;

        return 0;

    }


    /**
     * Reads max values for nodes from a file named "nodes.txt" and adds them to the maxList of Node class.
     */
    public static void readFile() {
        String fileName = "nodes.txt";


        ArrayList<Integer> dataList = new ArrayList<>();

        // Load the file using ClassLoader, ensuring it's found in the right directory
        ClassLoader classLoader = Methods.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            System.err.println(fileName + " Not Found!");
            return;
        }

        // Using a Scanner to read values from the file
        try (Scanner scanner = new Scanner(inputStream)) {

            // Assuming the first line is a header or non-data, so skipping it
            scanner.nextLine();


            // Reading max values and adding them to the dataList
            while (scanner.hasNextLine()) {
                int max = scanner.nextInt();
                dataList.add(max);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\nRetreived Values:");

        // Displaying retrieved max values and adding them to Node's maxList
        for (int max : dataList) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("\nMax: " + max);
            Node.maxList.add(max);

        }

        // Setting the nodeNum to the size of maxList
        Node.nodeNum = Node.maxList.size();
    }

}
