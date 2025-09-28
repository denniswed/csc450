package module7;

/**
 * CSC450 Module 7 Discussion Post
 * Scope Minimization in Java - Benefits and Best Practices
 * 
 * This example demonstrates the benefits of proper scope minimization
 * versus poor scoping practices in Java applications.
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class discussionpost {

    // Example of POOR scope management
    static class PoorScopeExample {
        // BAD: Overly broad scope - these variables are accessible throughout the class
        private String userInput;
        private List<String> allData;
        private FileWriter writer;
        private Scanner scanner;
        private int totalCount;
        private boolean isProcessing;

        public void processUserData() {
            // BAD: Variables declared at method start, regardless of when they're used
            String fileName = "poor_output.txt";
            StringBuilder result = new StringBuilder();
            Map<String, Integer> wordCount = new HashMap<>();
            List<String> filteredData = new ArrayList<>();

            try {
                scanner = new Scanner(System.in);
                writer = new FileWriter(fileName);
                allData = new ArrayList<>();
                isProcessing = true;
                totalCount = 0;

                System.out.println("Poor Scope Example - Enter text (or 'quit' to exit):");

                // BAD: userInput accessible throughout entire method
                System.out.print("> ");
                userInput = scanner.nextLine();

                while (userInput != null && !userInput.trim().equalsIgnoreCase("quit")) {
                    if (!userInput.trim().isEmpty()) {
                        allData.add(userInput);
                        totalCount++;

                        // Process data - variables declared far from usage
                        String[] words = userInput.toLowerCase().split("\\s+");
                        for (String word : words) {
                            if (!word.trim().isEmpty()) {
                                String cleanWord = word.replaceAll("[^a-zA-Z0-9]", "");
                                if (!cleanWord.isEmpty()) {
                                    wordCount.put(cleanWord, wordCount.getOrDefault(cleanWord, 0) + 1);
                                    if (cleanWord.length() > 3) {
                                        filteredData.add(cleanWord);
                                    }
                                }
                            }
                        }
                    }

                    System.out.print("> ");
                    userInput = scanner.nextLine();
                }

                // BAD: All variables still in scope here, even if not needed
                writer.write("Total entries: " + totalCount + "\n");
                writer.write("Filtered words: " + filteredData.size() + "\n");
                writer.write("Unique words: " + wordCount.size() + "\n");

                isProcessing = false;
                System.out.println("Poor scope example completed. Check " + fileName);

            } catch (IOException e) {
                System.err.println("IO Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            } finally {
                // Cleanup - but variables were exposed too long
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        System.err.println("Error closing writer: " + e.getMessage());
                    }
                }
                // Note: Don't close scanner if it uses System.in in real applications
                if (scanner != null) {
                    scanner.close();
                }
            }
        }
    }

    // Example of GOOD scope management
    static class GoodScopeExample {

        public void processUserData() {
            final String fileName = "good_output.txt";

            try {
                System.out.println("Good Scope Example - Demonstrating proper scope management:");

                // Scope 1: Input collection phase
                List<String> collectedData = collectUserInput();

                if (!collectedData.isEmpty()) {
                    // Scope 2: Data processing phase
                    ProcessingResult result = processData(collectedData);

                    // Scope 3: Output phase
                    writeResults(fileName, result);

                    System.out.println("Good scope processing completed successfully!");
                    System.out.println("Results written to " + fileName);
                } else {
                    System.out.println("No data collected.");
                }

            } catch (Exception e) {
                System.err.println("Processing failed: " + e.getMessage());
            }
        }

        private List<String> collectUserInput() {
            List<String> data = new ArrayList<>();
            Scanner scanner = new Scanner(System.in); // Don't use try-with-resources for System.in

            try {
                System.out.println("Enter text (or 'quit' to exit):");
                System.out.print("> ");
                String input = scanner.nextLine();

                while (input != null && !input.trim().equalsIgnoreCase("quit")) {
                    if (!input.trim().isEmpty()) {
                        data.add(input.trim());
                    }
                    System.out.print("> ");
                    input = scanner.nextLine(); // input scope limited to loop
                }
            } catch (Exception e) {
                System.err.println("Error reading input: " + e.getMessage());
            }
            // Note: Don't close scanner for System.in in real applications

            return data;
        }

        private ProcessingResult processData(List<String> inputData) {
            Map<String, Integer> wordCount = new HashMap<>();
            List<String> filteredWords = new ArrayList<>();

            for (String entry : inputData) {
                // GOOD: Processing variables have minimal scope
                {
                    String[] words = entry.toLowerCase().split("\\s+");
                    for (String word : words) {
                        // GOOD: Loop variables have very limited scope
                        String cleanWord = word.replaceAll("[^a-zA-Z0-9]", "").trim();

                        if (!cleanWord.isEmpty()) {
                            wordCount.put(cleanWord, wordCount.getOrDefault(cleanWord, 0) + 1);

                            if (cleanWord.length() > 3) {
                                filteredWords.add(cleanWord);
                            }
                        }
                    } // words array scope ends here
                } // cleanWord scope ends here
            }

            return new ProcessingResult(inputData.size(), wordCount, filteredWords);
        }

        private void writeResults(String fileName, ProcessingResult result) throws IOException {
            // GOOD: FileWriter only exists in this limited scope
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("=== Processing Results ===\n");
                writer.write("Total entries: " + result.totalEntries() + "\n");
                writer.write("Unique words: " + result.wordCount().size() + "\n");
                writer.write("Filtered words (>3 chars): " + result.filteredWords().size() + "\n\n");

                // GOOD: StringBuilder scope limited to where it's needed
                if (!result.wordCount().isEmpty()) {
                    StringBuilder wordList = new StringBuilder("Top 5 most frequent words:\n");

                    result.wordCount().entrySet().stream()
                            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                            .limit(5)
                            .forEach(entry -> wordList.append("  ")
                                    .append(entry.getKey())
                                    .append(": ")
                                    .append(entry.getValue())
                                    .append(" times\n"));

                    writer.write(wordList.toString());
                } // wordList scope ends here

            } // writer automatically closed here
        }

        // GOOD: Simple record with minimal scope for data transfer
        private record ProcessingResult(
                int totalEntries,
                Map<String, Integer> wordCount,
                List<String> filteredWords) {
        }
    }

    // Demonstration of thread-safe scope minimization
    static class ThreadSafeScopeExample {

        public void demonstrateConcurrentScope() {
            final int numberOfThreads = 3;
            final int tasksPerThread = 5;

            System.out.println("Thread-Safe Scope Example:");

            // GOOD: ExecutorService scope is minimized
            try (ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {

                List<Future<String>> futures = new ArrayList<>();

                for (int i = 0; i < numberOfThreads; i++) {
                    final int taskId = i; // GOOD: Effectively final for lambda capture

                    Future<String> future = executor.submit(() -> {
                        // GOOD: Each thread has its own local scope
                        try {
                            StringBuilder result = new StringBuilder();
                            Random random = new Random();

                            for (int j = 0; j < tasksPerThread; j++) {
                                int value = random.nextInt(100);
                                result.append("Task ").append(taskId)
                                        .append(".").append(j)
                                        .append(": ").append(value).append(" ");

                                // Simulate work
                                Thread.sleep(10);
                            } // j scope ends here

                            return result.toString().trim();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return "Task " + taskId + ": Interrupted";
                        }
                    });

                    futures.add(future);
                } // taskId scope ends here

                // Collect results with timeout
                for (int i = 0; i < futures.size(); i++) {
                    try {
                        String result = futures.get(i).get(2, TimeUnit.SECONDS);
                        System.out.println("Thread " + i + " result: " + result);
                    } catch (TimeoutException e) {
                        System.err.println("Thread " + i + " timed out");
                    } catch (Exception e) {
                        System.err.println("Thread " + i + " failed: " + e.getMessage());
                    }
                } // future scope ends here

            } // executor automatically shutdown here

            System.out.println("All threads completed.");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Scope Minimization Demonstration ===\n");

        try {
            // Create instances for demonstration
            PoorScopeExample poorExample = new PoorScopeExample();
            GoodScopeExample goodExample = new GoodScopeExample();
            ThreadSafeScopeExample threadExample = new ThreadSafeScopeExample();

            System.out.println("Choose an example to run:");
            System.out.println("1. Poor Scope Management Example");
            System.out.println("2. Good Scope Management Example");
            System.out.println("3. Thread-Safe Scope Example");
            System.out.println("4. Run Thread-Safe Example Only (non-interactive)");

            Scanner menuScanner = new Scanner(System.in);
            System.out.print("Enter choice (1-4): ");

            try {
                int choice = Integer.parseInt(menuScanner.nextLine().trim());

                switch (choice) {
                    case 1 -> {
                        System.out.println("\n--- Running Poor Scope Example ---");
                        poorExample.processUserData();
                    }
                    case 2 -> {
                        System.out.println("\n--- Running Good Scope Example ---");
                        goodExample.processUserData();
                    }
                    case 3 -> {
                        System.out.println("\n--- Running Interactive Thread Example ---");
                        threadExample.demonstrateConcurrentScope();
                    }
                    case 4 -> {
                        System.out.println("\n--- Running Non-Interactive Thread Example ---");
                        threadExample.demonstrateConcurrentScope();
                    }
                    default -> {
                        System.out.println("Invalid choice. Running thread example by default.");
                        threadExample.demonstrateConcurrentScope();
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Running thread example by default.");
                threadExample.demonstrateConcurrentScope();
            }

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
        }

        System.out.println("\n=== Demonstration Complete ===");
    }
}