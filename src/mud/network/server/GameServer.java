package mud.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import mud.GameMaster;
import mud.Player;
import mud.network.server.input.interpreter.MasterInterpreter;
import mud.network.server.log.ConsoleLog;

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
    private HashMap<InetAddress, ClientConnection> clientMap;
    private GameMaster game;
    private MasterInterpreter interpreter;

    /**
     * Creates a new chat server operating at the passed port.
     *
     * @param port the port to operate the server on
     * @throws IOException
     */
    public GameServer(int port) throws IOException{
        System.out.println(ConsoleLog.log() + "Server starting on port " + port);
        serverSocket = new ServerSocket(port);
        clientMap = new HashMap<>();
        game = new GameMaster();
        interpreter = new MasterInterpreter(clientMap);
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
                ClientConnection client;
                //Client has connected in the past, reconnect them
                if (clientMap.containsKey(clientAddress)) {
                    client = clientMap.get(clientAddress);
                    client.reconnect(newClient);
                    System.out.println(ConsoleLog.log() + "Player reconnected from "
                            + clientAddress);
                } //Client hasn't connected before
                else {
                    client = new ClientConnection(newClient, clientAddress, new Player("Temp"), interpreter);
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
