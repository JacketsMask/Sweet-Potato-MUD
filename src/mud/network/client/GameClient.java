package mud.network.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import mud.network.server.GameServer;

/**
 * The client connection to the server. This is used to communicate information
 * with the server. A player should be able to interact with the game
 * exclusively using the client, as long as a connection to a server is
 * established (this would be the case if a friend connects to an existing
 * server).
 *
 * NOTE: Don't use System.out.print() unless just debugging. All output should
 * go to the JTextArea. The reason for this is that the server will be printing
 * to the console, and we don't need that kind of complication.
 *
 * @author Japhez
 */
public class GameClient {

    private JTextArea output;
    private JTextField commandBar;
    private Socket server;
    private ClientReader reader;
    private Thread readerThread;
    private PrintWriter writer;
    private ConnectionStep currentConnectionStep;
    private ConnectionChoice connectionChoice;

    /**
     * Creates a new ChatClient with the passed name. Attempts to connect to the
     * passed address and port.
     *
     * @param address the address of the server
     * @param port the port of the server
     * @param output the output JTextArea
     * @param commandBar the input JTextField
     * @throws UnknownHostException
     * @throws IOException
     */
    public GameClient(String address, int port, JTextArea output, JTextField commandBar) throws UnknownHostException, IOException {
        this.output = output;
        this.commandBar = commandBar;
        addWritingActionListener();
        this.server = new Socket(address, port);
        server.getOutputStream().flush();
        writer = new PrintWriter(server.getOutputStream(), true);
        reader = new ClientReader();
        output.append("Connection established on " + address + ":" + port);
        currentConnectionStep = ConnectionStep.CONNECTED;
    }

    /**
     * Creates a new ChatClient with only the passed name, and allows the user
     * to connect manually later.
     *
     * @param output
     * @param commandBar
     */
    public GameClient(JTextArea output, JTextField commandBar) throws IOException {
        this.output = output;
        this.commandBar = commandBar;
        addWritingActionListener();
        currentConnectionStep = ConnectionStep.DECIDING_CONNECTION_TYPE;
        printConnectionMenu();
    }

    /**
     * Prints the connection menu to the output box.
     */
    private void printConnectionMenu() {
        output.append("How would you like to play?"
                + "\n1. Play locally by yourself."
                + "\n2. Host a game to play with friends."
                + "\n3. Join a friend's game.");
    }

    /**
     * Blocks until a decision is made for connecting to a server either locally
     * (solo play), locally (hosting), or by remote.
     *
     * @return 1 for solo local, 2 for local hosting, or 3 for remote.
     */
    public ConnectionChoice getConnectionChoice() {
        while (connectionChoice == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connectionChoice;
    }

    /**
     * Represents the client's connection choice.
     */
    public enum ConnectionChoice {

        LOCAL_SOLO, LOCAL_CO_OP, REMOTE;
    }

    /**
     * An enumeration that holds a stage of the connection process.
     */
    public enum ConnectionStep {

        DECIDING_CONNECTION_TYPE, WAITING_FOR_CONNECTION_INFO, CONNECTED, DISCONNECTED;
    }

    /**
     * Attempts to connect to the passed address and port.
     *
     * @param address
     * @param port
     */
    public void connect(String address, int port) {
        append("\nAttempting to connect to " + address + ":" + port + "...");
        try {
            this.server = new Socket(address, port);
            reader = new ClientReader();
            writer = new PrintWriter(server.getOutputStream(), true);
            currentConnectionStep = ConnectionStep.CONNECTED;
        } catch (UnknownHostException ex) {
            append("\nUnable to establish connection.\n");
            currentConnectionStep = ConnectionStep.WAITING_FOR_CONNECTION_INFO;
            append("Use \"connect address:port\".");
        } catch (IOException ex) {
            append("\nUnable to establish connection.\n");
            currentConnectionStep = ConnectionStep.WAITING_FOR_CONNECTION_INFO;
            append("Use \"connect address:port\".");
        }
    }

    /**
     * Cleanly closes the connection to the server by interrupting the reading
     * and listening threads, closing the connections to the reader and writer,
     * and finally closing the server connection.
     *
     * @throws IOException
     */
    private void cleanUpConnection() throws IOException {
        readerThread.interrupt();
        reader.fromServer.close();
        server.close();
        append("\nDisconnected from server.");
        currentConnectionStep = ConnectionStep.DISCONNECTED;
    }

    /**
     * @return true if this client is currently connected to a server
     */
    public boolean isConnected() {
        return currentConnectionStep == ConnectionStep.CONNECTED;
    }

    /**
     * Clears the contents of the output window.
     */
    public void clearOutputWindow() {
        output.setText("");
    }

    /**
     * Appends the passed text to the output window.
     *
     * @param text
     */
    public void append(String text) {
        output.append(text);
        if (output.getText().charAt(0) == '\n') {
            output.setText(output.getText().substring(1, output.getText().length()));
        }
    }

    /**
     * When enter is pressed, the command is interpreted and sent to the server.
     */
    private void addWritingActionListener() {
        commandBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Retrieve the text from the commandBar
                String text = commandBar.getText();
                if (text == null) {
                    return;
                }
                //Select the text for ease of typing next command
                commandBar.selectAll();
                //Deciding connection type
                if (currentConnectionStep.equals(ConnectionStep.DECIDING_CONNECTION_TYPE)) {
                    switch (text) {
                        //Playing locally
                        case "1":
                            clearOutputWindow();
                            connectionChoice = ConnectionChoice.LOCAL_SOLO;
                            connect("localhost", GameServer.DEFAULT_PORT);
                            currentConnectionStep = ConnectionStep.CONNECTED;
                            break;
                        //Host a game to play with friends
                        case "2":
                            clearOutputWindow();
                            connectionChoice = ConnectionChoice.LOCAL_CO_OP;
                            connect("localhost", GameServer.DEFAULT_PORT);
                            currentConnectionStep = ConnectionStep.CONNECTED;
                            break;
                        //Join a friend's game
                        case "3":
                            clearOutputWindow();
                            connectionChoice = ConnectionChoice.REMOTE;
                            append("Use \"connect address:port\".");
                            currentConnectionStep = ConnectionStep.WAITING_FOR_CONNECTION_INFO;
                            break;
                    }
                    //Waiting for connection input
                } else if (currentConnectionStep.equals(ConnectionStep.WAITING_FOR_CONNECTION_INFO)) {
                    if (text.contains("connect ")) {
                        //Connect to the address and port
                        String address = null;
                        int port = 0;
                        try {
                            text = text.substring(8);
                            text = text.trim();
                            String[] split = text.split(":");
                            address = split[0];
                            port = Integer.parseInt(split[1]);
                            connect(address, port); //Attempts to connect
                        } catch (NumberFormatException ex) {
                            append("\nThat doesn't make sense...");
                            append("\nUse \"connect address:port\".");
                            clearOutputWindow();
                        }
                    } else {
                        append("\nUse \"connect address:port\".");
                    }
                    //Connected
                } else if (currentConnectionStep.equals(ConnectionStep.CONNECTED)) {
                    //Check to see if the user is disconnecting
                    if (text.equalsIgnoreCase("disconnect")) {
                        currentConnectionStep = ConnectionStep.DISCONNECTED;
                        append("\nYou have disconnected from the server.");
                        try {
                            Thread.sleep(2000);
                            System.exit(0);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    writer.println(text);
                    //Disconnected
                } else if (currentConnectionStep.equals(ConnectionStep.DISCONNECTED)) {
                    append("\nYou're currently disconnected.\n");
                    printConnectionMenu();
                    currentConnectionStep = ConnectionStep.WAITING_FOR_CONNECTION_INFO;
                }
            }
        });
    }

    /**
     * A reader for a client program that receives messages from the server and
     * prints them to console.
     */
    public class ClientReader implements Runnable {

        private BufferedReader fromServer;

        /**
         * Create a new client reader.
         *
         * @throws IOException
         */
        public ClientReader() throws IOException {
            fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
            readerThread = new Thread(this);
            readerThread.start();
        }

        /**
         * Print any incoming messages from the server to the chat window.
         */
        @Override
        public void run() {
            String s;
            while (!Thread.interrupted()) {
                try {
                    if ((s = fromServer.readLine()) != null) {
                        //Display the server's message
                        append("\n" + s);
                    }
                } catch (IOException ex) {
                    break;
                }
            }
            //Clean up connection in case of error
            try {
                cleanUpConnection();
            } catch (IOException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
