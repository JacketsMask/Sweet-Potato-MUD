package mud.network.server.input.interpreter;

import mud.network.server.Connection;

/**
 * An interface to be implemented by all interpreters.
 *
 * @author Japhez
 */
public interface Interpretable {

    public boolean interpret(Connection sender, ParsedInput input);
}
