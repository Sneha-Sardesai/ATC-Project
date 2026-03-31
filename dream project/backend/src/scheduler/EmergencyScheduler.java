package scheduler;

import java.util.PriorityQueue;

public class EmergencyScheduler {

    private static class EmergencyTask {
        int flightId;
        int priority;

        EmergencyTask(int flightId, int priority) {
            this.flightId = flightId;
            this.priority = priority;
        }
    }

    private PriorityQueue<EmergencyTask> queue =
            new PriorityQueue<>(
                (a, b) -> Integer.compare(a.priority, b.priority)
            );

    public void addEmergency(int flightId, int priority) {
        queue.add(new EmergencyTask(flightId, priority));
    }

    public int getNextEmergencyFlight() {
        return queue.poll().flightId;
    }

    public boolean hasEmergency() {
        return !queue.isEmpty();
    }
}