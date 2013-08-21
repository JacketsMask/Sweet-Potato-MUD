package mud.time;

import java.util.ArrayList;

/**
 *
 * @author Japhez
 */
public class MasterClock {

    private SortedArrayList<ScheduledEvent> scheduledEvents;
    private ArrayList<ScheduledEvent> firedEvents;
    private Thread thread;

    public MasterClock() {
        scheduledEvents = new SortedArrayList<>();
        firedEvents = new ArrayList<>();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        //Perform any events that should be performed now
                        for (ScheduledEvent se : scheduledEvents) {
                            if (se.readyToActivate()) {
                                //Perform the logic for this event
                                se.performEvent();
                                //Run completion logic
                                se.eventCompleted();
                                //Check to see if the event is completely done
                                if (se.isFinished()) {
                                    //Remove the event from the list
                                    scheduledEvents.remove(se);
                                }
                                //Check to see if we've reached the end of the events that are ready to fire
                            } else {
                                //Add to the list of fired events
                                firedEvents.add(se);
                                break;
                            }
                        }
                        //Resort the events that were fired
                        for (ScheduledEvent se : firedEvents) {
                            scheduledEvents.remove(se);
                            scheduledEvents.addSorted(se);
                        }
                        firedEvents.clear();
                        //Move time forward for all events
                        Thread.sleep(1000);
                        System.out.println("Tick (event list size: " + scheduledEvents.size() + ")");
                        for (ScheduledEvent se2 : scheduledEvents) {
                            se2.countDown();
                            System.out.println("time left: " + se2.getRemainingEventDelay());
                        }
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
            }
        });
    }

    public void startClock() {
        thread.start();
    }

    public void stopClock() {
        thread.interrupt();
    }

    public void addEvent(ScheduledEvent event) {
        scheduledEvents.addSorted(event);
    }

    public static void main(String[] args) throws InterruptedException {
        MasterClock masterClock = new MasterClock();
        masterClock.addEvent(new ScheduledEvent(6, true) {
            @Override
            public void performEvent() {
                System.out.println("This should display every 6 ticks.");
            }
        });
        masterClock.startClock();
        masterClock.addEvent(new ScheduledEvent(3, true) {
            @Override
            public void performEvent() {
                System.out.println("This should display every 3 ticks.");
            }
        });
        Thread.sleep(3000);
        masterClock.addEvent(new ScheduledEvent(4, true) {
            @Override
            public void performEvent() {
                System.out.println("This should display every 4 ticks.");
            }
        });
    }
}
