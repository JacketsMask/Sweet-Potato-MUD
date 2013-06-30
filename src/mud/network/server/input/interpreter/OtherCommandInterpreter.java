/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.HashMap;
import mud.Player;
import mud.network.server.Client;
import mud.network.server.Packet;

/**
 *
 * @author Japhez
 */
public class OtherCommandInterpreter implements Interpretable{

    private HashMap<InetAddress, Client> clientMap;

    public OtherCommandInterpreter(HashMap<InetAddress, Client> clientMap) {
        this.clientMap = clientMap;
    }

    @Override
    public boolean interpret(InetAddress sender, Packet packet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
