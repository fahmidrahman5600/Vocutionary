package vocutionary;

import java.util.Scanner;

public class VocutionaryApp {
    public Dictionary dictionary;
    public MistakeTracker mistakeTracker;
    public ActivityTracker activityTracker;
    public SessionHistory sessionHistory;

    // Constructor to instantiate all core structures
    public VocutionaryApp() {
        this.dictionary = new Dictionary();
        this.mistakeTracker = new MistakeTracker();
        this.activityTracker = new ActivityTracker();
        this.sessionHistory = new SessionHistory();
    }

    public void start() {
        // 1. Initial Load from hard drive
        dictionary.loadFromFile("dictionary.txt");
        activityTracker.logActivity("Application initialized and dataset loaded.");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // 2. Main Interactive Game Loop
        while (running) {
            System.out.println("\n=================================");
            System.out.println("      VOCUTIONARY MAIN MENU      ");
            System.out.println("=================================");
            System.out.println("1. Practice Vocabulary (Quiz)");
            System.out.println("2. Review Mistargeted/Weak Words");
            System.out.println("3. Add New Word to Dictionary");
            System.out.println("4. View Recent Activity Log");
            System.out.println("5. View Quiz Session History");
            System.out.println("6. Exit Application");
            System.out.print("Select an option (1-6): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    runQuiz(scanner);
                    break;
                case "2":
                    runWeakWordsReview(scanner);
                    break;
                case "3":
                    runAddNewWordPanel(scanner);
                    break;
                case "4":
                    System.out.println();
                    activityTracker.displayLog();
                    break;
                case "5":
                    System.out.println();
                    sessionHistory.displayHistory();
                    activityTracker.logActivity("Viewed Session History.");
                    break;
                case "6":
                    System.out.println("\nThank you for using Vocutionary! Saving session... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\n❌ Invalid option! Please type a number from 1 to 6.");
            }
        }
        scanner.close();
    }

    private void runQuiz(Scanner scanner) {
        System.out.println("\n=================================");
        System.out.println("   STARTING PRACTICE QUIZ (5 Qs) ");
        System.out.println("=================================");

        int score = 0;
        int totalQuestions = 5;

        for (int i = 1; i <= totalQuestions; i++) {
            // 1. Grab the target word from our custom BST
            Word targetWord = dictionary.getRandomWord();
            if (targetWord == null) {
                System.out.println("❌ Dictionary is empty! Cannot run quiz.");
                return;
            }

            // 2. Fetch 3 distinct random distractor words from the BST
            Word distractor1 = dictionary.getRandomWord();
            while (distractor1 == null || distractor1.text.equals(targetWord.text)) {
                distractor1 = dictionary.getRandomWord();
            }

            Word distractor2 = dictionary.getRandomWord();
            while (distractor2 == null || distractor2.text.equals(targetWord.text) || distractor2.text.equals(distractor1.text)) {
                distractor2 = dictionary.getRandomWord();
            }

            Word distractor3 = dictionary.getRandomWord();
            while (distractor3 == null || distractor3.text.equals(targetWord.text) || distractor3.text.equals(distractor1.text) || distractor3.text.equals(distractor2.text)) {
                distractor3 = dictionary.getRandomWord();
            }

            // 3. Put options into a fixed-size array and place the correct answer at a random index (0 to 3)
            String[] options = new String[4];
            int correctOptionIndex = (int) (Math.random() * 4);

            int distractorCount = 0;
            String[] distractors = {distractor1.meaning, distractor2.meaning, distractor3.meaning};

            for (int j = 0; j < 4; j++) {
                if (j == correctOptionIndex) {
                    options[j] = targetWord.meaning;
                } else {
                    options[j] = distractors[distractorCount];
                    distractorCount++;
                }
            }

            // 4. Display the question and options
            System.out.println("\nQuestion " + i + ": What is the meaning of the word -> **" + targetWord.text + "**?");
            System.out.println("1. " + options[0]);
            System.out.println("2. " + options[1]);
            System.out.println("3. " + options[2]);
            System.out.println("4. " + options[3]);
            System.out.print("Your Answer (1-4): ");

            String userAnswer = scanner.nextLine().trim();

            // 5. Convert correct index back to a string choice "1"-"4"
            String correctChoiceString = String.valueOf(correctOptionIndex + 1);

            if (userAnswer.equals(correctChoiceString)) {
                System.out.println("✅ Correct! Excellent job.");
                score++;
            } else {
                System.out.println("❌ Incorrect!");
                System.out.println("-> The correct answer was option (" + correctChoiceString + "): " + targetWord.meaning);

                // Log failed word into our Min-Heap tracker
                mistakeTracker.addMistake(targetWord);
            }
        }

        // 6. Print summary metrics
        System.out.println("\n=== Quiz Finished! ===");
        System.out.println("Your Final Score: " + score + " / " + totalQuestions);

        // Record history and log activity
        String performanceDate = "2026-06-21";
        sessionHistory.push(performanceDate, "Multiple-Choice Quiz", score);
        activityTracker.logActivity("Completed MCQ Practice Quiz. Score: " + score + "/5");
    }

    private void runWeakWordsReview(Scanner scanner) {
        System.out.println("\n=================================");
        System.out.println("   REVIEWING YOUR WEAKEST WORDS  ");
        System.out.println("=================================");

        // 1. Safety check: Are there any mistakes tracked in our Heap?
        if (mistakeTracker.size == 0) {
            System.out.println("🎉 Congratulations! Your mistake log is empty.");
            System.out.println("Go play a quiz first to identify tricky words.");
            return;
        }

        // We will review up to 3 problematic words in a single review session
        int itemsToReview = Math.min(mistakeTracker.size, 3);
        System.out.println("Reviewing your top " + itemsToReview + " most missed words:\n");

        for (int i = 1; i <= itemsToReview; i++) {
            // 2. Extract the word with the maximum mistake count from the heap root
            Word trickyWord = mistakeTracker.extractMaxMistake();

            System.out.println("Word #" + i + ": **" + trickyWord.text + "** (You have missed this " + trickyWord.mistakeCount + " times)");
            System.out.print("Type the exact keyword 'review' to reveal definition and test yourself: ");
            String validation = scanner.nextLine().trim();

            System.out.println("-> True Definition: " + trickyWord.meaning);
            System.out.print("Did you remember it? (yes/no): ");
            String feedback = scanner.nextLine().trim().toLowerCase();

            if (feedback.equals("yes") || feedback.equals("y")) {
                System.out.println("✨ Excellent! Word removed from immediate priority queue.\n");
            } else {
                System.out.println("❌ Sticking around. Re-inserting into priority queue for future review.\n");
                // Reset its tracking state locally and re-insert it so it sifts/heapifies up again
                mistakeTracker.addMistake(trickyWord);
            }
        }

        // 3. Log this interaction event to our Circular Activity Queue
        activityTracker.logActivity("Executed Weak Words Priority Heap review.");
    }

    private void runAddNewWordPanel(Scanner scanner) {
        System.out.println("\n=================================");
        System.out.println("    ADD NEW WORD TO DICTIONARY   ");
        System.out.println("=================================");

        System.out.print("Enter the new word (letters only): ");
        String text = scanner.nextLine().trim().toLowerCase();

        // Basic structural verification
        if (text.isEmpty() || text.contains(" ")) {
            System.out.println("❌ Invalid format! Word must be a single non-empty token.");
            return;
        }

        // 1. Core BST Duplicate Check
        if (dictionary.contains(text)) {
            System.out.println("❌ Duplicate Detected! The word '" + text + "' already exists in your dictionary.");
            return;
        }

        System.out.print("Enter the concise definition: ");
        String meaning = scanner.nextLine().trim();

        if (meaning.isEmpty()) {
            System.out.println("❌ Definition cannot be empty.");
            return;
        }

        // 2. Insert into the live runtime Binary Search Tree
        Word newWord = new Word(text, meaning, 0);
        dictionary.insert(newWord);

        // 3. Append to physical storage file (dictionary.txt) for permanent saving
        // We use standard Java IO which is safe under your assignment's rules
        try (java.io.FileWriter fw = new java.io.FileWriter("dictionary.txt", true);
             java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
             java.io.PrintWriter out = new java.io.PrintWriter(bw)) {

            // Append formatted line: word,meaning
            out.print("\n" + text + "|" + meaning + "|0");

            System.out.println("✅ Success! '" + text + "' has been integrated into the BST and saved permanently.");
            activityTracker.logActivity("Added new word to dataset: " + text);

        } catch (java.io.IOException e) {
            System.out.println("⚠️ Word added to RAM tree, but failed to write permanently to disk.");
        }
    }

    public static void main(String[] args) {
        // Create an instance of our app and trigger the controller loop
        VocutionaryApp app = new VocutionaryApp();
        app.start();
    }
}