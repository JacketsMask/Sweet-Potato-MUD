/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.HashMap;
import mud.network.server.Client;

/**
 *
 * @author Japhez
 */
public class OtherCommandInterpreter {

    private HashMap<InetAddress, Client> clientMap;

    public OtherCommandInterpreter(HashMap<InetAddress, Client> clientMap) {
        this.clientMap = clientMap;
    }
    
    
}
