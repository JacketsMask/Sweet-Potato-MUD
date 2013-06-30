package mud.network.server;

import mud.network.server.log.ConsoleLog;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import mud.network.server.input.interpreter.MasterInterpreter;

/**
 * This is the top-level server implementation. Lower level features such as
 * chat should be abstracted to allow for easier debugging and testing. Only top
 * level server interactions should be directly done here. (connecting,
 * disconnecting, etc.)
 *
 * @author Jacob Dorman
 */
public class GameServer implements Runnable {

    private ServerSocket serverSocket;
    private HashMap<InetAddress, Client> clientMap;
    private MasterInterpreter interpreter;


    /**
     * Creates a new chat server operating at the passed port.
     *
     * @param port the port to operate the server on
     * @throws IOException
     */
    public GameServer(int port) throws IOException {
        System.out.println(ConsoleLog.log() + "Server starting on port " + port);
        serverSocket = new ServerSocket(port);
        clientMap = new HashMap<>();
        interpreter = new MasterInterpreter(clientMap);
    }

    /**
     * @return a list of server clients that have connected to the server
     */
    private String getUserList() {
        String serverClients = "Server clients:\n";
        Set<InetAddress> keySet = clientMap.keySet();
        for (InetAddress s : keySet) {
            //Retrieve each client's name
            Client client = clientMap.get(s);
            serverClients += client.getName();
            //If the client is online, mark them as online
            if (clientMap.get(s).isOnline()) {
                serverClients += " [Online]";
            } else {
                serverClients += " [Offline]";
            }
            serverClients += "\n";
        }
        //Remove the final newline character
        serverClients = serverClients.substring(0, serverClients.length() - 1);
        return serverClients;
    }

    /**
     * Returns true if the passed name is a client that has connected to the
     * server.
     *
     * @param name the name to check
     * @return true if the name has been taken by a client
     */
    private boolean isNameTaken(String name) {
        Set<InetAddress> keySet = clientMap.keySet();
        for (InetAddress i : keySet) {
            if (clientMap.get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Continually checks for new connections. When a client connects, requests
     * their player information.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Socket newClient = serverSocket.accept();
                //Each client stores their address, and uses it for their
                //default name
                InetAddress clientAddress = newClient.getInetAddress();
                Client client;
                //Client has connected in the past, reconnect them
                if (clientMap.containsKey(clientAddress)) {
                    client = clientMap.get(clientAddress);
                    client.reconnect(newClient);
                    System.out.println(ConsoleLog.log() + "Player reconnected from "
                            + clientAddress);
                } //Client hasn't connected before
                else {
                    client = new Client(newClient, clientAddress, interpreter);
                    clientMap.put(clientAddress, client);
                    System.out.println(ConsoleLog.log() + "Player connected from "
                            + clientAddress);
                }
            } catch (IOException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final int PORT = 1337;
        GameServer server = new GameServer(PORT);
        new Thread(server).start();
    }
}
