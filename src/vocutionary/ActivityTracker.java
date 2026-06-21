package vocutionary;

public class ActivityTracker {
    public String[] activityLog;
    public int front;
    public int rear;
    public int count;
    public int capacity;

    // Constructor to track a fixed window of recent user actions (e.g., last 10 actions)
    public ActivityTracker() {
        this.capacity = 10;
        this.activityLog = new String[capacity];
        this.front = 0;
        this.rear = -1;
        this.count = 0;
    }

    // Method to add a new activity string into our rolling log
    public void logActivity(String action) {
        // If the queue is completely full, we forcefully eject the oldest item
        // by moving the front pointer forward to make room.
        if (count == capacity) {
            front = (front + 1) % capacity;
            count--; // Reduce count temporarily so the math stabilizes below
        }

        // Move rear pointer forward circularly and insert the new log entry
        rear = (rear + 1) % capacity;
        activityLog[rear] = action;
        count++;
    }

    // Method to print the active log entries from oldest to newest
    public void displayLog() {
        if (count == 0) {
            System.out.println("No recent activity recorded.");
            return;
        }

        System.out.println("--- Recent Activity Log (Rolling Feed) ---");
        int current = front;
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + activityLog[current]);
            current = (current + 1) % capacity; // Circularly walk forward
        }
    }
}