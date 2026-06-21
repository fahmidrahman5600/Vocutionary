package vocutionary;

public class SessionHistory {
    public ListNode head;
    public ListNode tail;
    public int totalSessions;

    // Constructor to initialize an empty session history list
    public SessionHistory() {
        this.head = null;
        this.tail = null;
        this.totalSessions = 0;
    }

    // Push: Adds a new session to the front of the linked list (LIFO / Stack behavior)
    public void push(String date, String type, int score) {
        ListNode newNode = new ListNode(date, type, score);

        if (head == null) {
            // If list is empty, both head and tail point to the new node
            head = newNode;
            tail = newNode;
        } else {
            // Insert at the front
            newNode.next = head;
            head = newNode;
        }
        totalSessions++;
    }

    // Display: Prints the history from head (newest) to tail (oldest)
    public void displayHistory() {
        if (head == null) {
            System.out.println("No recent session history found.");
            return;
        }

        System.out.println("--- Session Navigation History (Newest First) ---");
        ListNode current = head;
        while (current != null) {
            System.out.println(" -> Date: " + current.date + " | Type: " + current.type + " | Score: " + current.score + "/5");
            current = current.next;
        }
    }
}

class ListNode {
    String date;
    String type;
    int score;
    ListNode next;

    // Constructor for a node
    public ListNode(String date, String type, int score) {
        this.date = date;
        this.type = type;
        this.score = score;
        this.next = null;
    }
}