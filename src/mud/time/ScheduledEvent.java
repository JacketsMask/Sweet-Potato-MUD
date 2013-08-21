package mud.time;

/**
 *
 * @author Japhez
 */
public abstract class ScheduledEvent implements Comparable<ScheduledEvent> {

    //The delay in seconds before performing this action
    private int eventDelay;
    //The remaining delay before performing this action
    private int remainingEventDelay;
    //Whether or not this event should repeat
    private boolean repeating;
    //Whether or not this event is done and should be removed
    private boolean finished;

    public ScheduledEvent(int eventDelay, boolean repeating) {
        this.eventDelay = eventDelay;
        this.remainingEventDelay = this.eventDelay;
        this.repeating = repeating;
        this.finished = false;
    }

    abstract public void performEvent();

    public void eventCompleted() {
        if (repeating) {
            remainingEventDelay = eventDelay;
        } else {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public boolean readyToActivate() {
        if (remainingEventDelay < 0) {
            System.err.println("Error: Event was passed over by one second.");
        }
        return (remainingEventDelay < 1);
    }

    public int getRemainingEventDelay() {
        return remainingEventDelay;
    }

    @Override
    public int compareTo(ScheduledEvent o) {
        if (remainingEventDelay == o.remainingEventDelay) {
            return 0;
        }
        if (remainingEventDelay > o.remainingEventDelay) {
            return 1;
        } else {
            return -1;
        }
    }

    public void countDown() {
        remainingEventDelay--;
    }
}
