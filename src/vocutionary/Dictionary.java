package vocutionary;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dictionary {
    public WordNode root;
    public int totalWords;

    public Dictionary() {
        this.root = null;
        this.totalWords = 0;
    }

    // Public method to add a word to the tree
    public void insert(Word newWord) {
        root = insertRecursive(root, newWord);
    }

    // Helper method to recursively find the correct alphabetical spot
    private WordNode insertRecursive(WordNode current, Word newWord) {
        // Base case: We found an empty spot in the tree!
        if (current == null) {
            totalWords++;
            return new WordNode(newWord); // Fixed: Wraps the actual Word object
        }

        // Alphabetical comparison using Java's built-in String compareTo
        int comparison = newWord.text.compareTo(current.data.text);

        if (comparison < 0) {
            // Alphabetically smaller -> recursively move left
            current.left = insertRecursive(current.left, newWord);
        } else if (comparison > 0) {
            // Alphabetically larger -> recursively move right
            current.right = insertRecursive(current.right, newWord);
        }

        // If comparison == 0, it's an exact duplicate. We just ignore it for now.
        return current;
    }

    // Public method to check if a word already exists in the dictionary
    public boolean contains(String targetText) {
        return searchRecursive(root, targetText.toLowerCase());
    }

    // Helper method to recursively traverse the tree looking for a match
    private boolean searchRecursive(WordNode current, String targetText) {
        // Base case 1: We reached a dead end (null). The word does not exist.
        if (current == null) {
            return false;
        }

        // Alphabetical comparison
        int comparison = targetText.compareTo(current.data.text);

        if (comparison == 0) {
            // Base case 2: Match found!
            return true;
        } else if (comparison < 0) {
            // Target is smaller -> search left side only
            return searchRecursive(current.left, targetText);
        } else {
            // Target is larger -> search right side only
            return searchRecursive(current.right, targetText);
        }
    }

    // Method to load words from the plain text file into our BST
    public void loadFromFile(String fileName) {
        File file = new File(fileName);

        // If the file doesn't exist yet (e.g., first run ever), just exit quietly
        if (!file.exists()) {
            System.out.println("No existing data file found. Starting with an empty dictionary.");
            return;
        }

        try {
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                // Skip completely empty lines if there are any
                if (line.isEmpty()) {
                    continue;
                }

                // Split the line by the pipe symbol
                // Note: We use "\\|" because the pipe is a special character in regex,
                // so we have to "escape" it with two backslashes.
                String[] parts = line.split("\\|");

                // A valid line must have exactly 3 parts: word|meaning|mistakeCount
                if (parts.length == 3) {
                    String text = parts[0].toLowerCase();
                    String meaning = parts[1];
                    int mistakeCount = Integer.parseInt(parts[2]);

                    // Create the word using our second historical constructor
                    Word loadedWord = new Word(text, meaning, mistakeCount);

                    // Insert it directly into our custom BST!
                    this.insert(loadedWord);
                }
            }

            fileScanner.close();
            System.out.println("Successfully loaded " + totalWords + " words into the dictionary.");

        } catch (FileNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing mistake count number in data file.");
        }
    }
}