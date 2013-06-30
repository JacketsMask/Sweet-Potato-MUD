/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.input.interpreter;

import java.net.InetAddress;
import mud.network.server.Packet;

/**
 * An interface to be implemented by all interpreters.
 *
 * @author Japhez
 */
public interface Interpretable {

    public boolean interpret(InetAddress sender, Packet packet);
}
