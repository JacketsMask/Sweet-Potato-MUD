package mud.time;

import java.util.ArrayList;

/**
 *
 * @author Japhez
 */
public class MasterClock {

    private ArrayList<ScheduledEvent> scheduledEvents;
    private int currentTick;

    public MasterClock() {
        scheduledEvents = new ArrayList<>();
        currentTick = 0;
    }
}
