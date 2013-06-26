package mud.network.server;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * A class meant to be used with toString to display the current time-stamp.
 * @author Jacob Dorman
 */
public class ConsoleLog
{

    static final GregorianCalendar calendar = new GregorianCalendar();
    static final SimpleDateFormat format = new SimpleDateFormat("HH:MM");

    @Override
    public String toString()
    {
        return "[" + format.format(calendar.getTime()) + "] ";
    }
}
