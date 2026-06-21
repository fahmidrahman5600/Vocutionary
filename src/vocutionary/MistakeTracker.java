package vocutionary;

public class MistakeTracker {
    public Word[] heap;
    public int size;

    // Constructor allocating an initial fixed maximum size for our vocabulary tracking
    public MistakeTracker() {
        this.heap = new Word[2000]; // Can track up to 2000 problematic words
        this.size = 0;
    }

    // Helper to get parent index
    private int parent(int i) {
        return (i - 1) / 2;
    }

    // Helper to get left child index
    private int leftChild(int i) {
        return (2 * i) + 1;
    }

    // Helper to get right child index
    private int rightChild(int i) {
        return (2 * i) + 2;
    }

    // Helper to swap two elements in our array
    private void swap(int index1, int index2) {
        Word temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    // Returns true if the word at index1 has HIGHER priority (more mistakes) than index2.
    // This allows our structural min-heap logic to surface the highest mistakes to the top.
    private boolean hasHigherPriority(int index1, int index2) {
        return heap[index1].mistakeCount > heap[index2].mistakeCount;
    }

    // Public method to log a mistake. If the word is already tracked, we update it.
    public void addMistake(Word word) {
        // First, check if this word is already existing in our heap array
        for (int i = 0; i < size; i++) {
            if (heap[i].text.equals(word.text)) {
                // Word found! Increment count and heapify up from its current position
                heap[i].mistakeCount++;
                heapifyUp(i);
                return;
            }
        }

        // If it's a completely new mistake, insert it at the end of the heap
        if (size < heap.length) {
            word.mistakeCount++; // Increment its count to 1
            heap[size] = word;
            heapifyUp(size);
            size++;
        }
    }

    // Helper method to restore heap properties by moving a node up to its correct position
    private void heapifyUp(int index) {
        // While we are not at the root node, check if current node has a higher priority than its parent
        while (index > 0 && hasHigherPriority(index, parent(index))) {
            swap(index, parent(index));
            index = parent(index); // Move our tracking pointer up to the parent's old index
        }
    }

    // Public method to extract and remove the word with the highest mistakes
    public Word extractMaxMistake() {
        if (size == 0) {
            return null;
        }

        // 1. Grab the top priority word from the root
        Word maxMistakeWord = heap[0];

        // 2. Move the very last element to the root position
        heap[0] = heap[size - 1];
        heap[size - 1] = null; // Clear the old reference
        size--;

        // 3. Sift the new root down to its proper home
        heapifyDown(0);

        return maxMistakeWord;
    }

    // Helper method to restore heap properties by moving a node down
    private void heapifyDown(int index) {
        int highestPriority = index;
        int left = leftChild(index);
        int right = rightChild(index);

        // Check if left child exists and has a higher mistake count than current highest
        if (left < size && hasHigherPriority(left, highestPriority)) {
            highestPriority = left;
        }

        // Check if right child exists and has a higher mistake count than current highest
        if (right < size && hasHigherPriority(right, highestPriority)) {
            highestPriority = right;
        }

        // If the highest priority node isn't the parent, swap and continue sinking down
        if (highestPriority != index) {
            swap(index, highestPriority);
            heapifyDown(highestPriority); // Recursive call
        }
    }
}