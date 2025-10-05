
/**
 * CSC450 Module 8 Discussion Post
 * Exception Handling Best Practices in Java
 * 
 * This example demonstrates proper exception handling patterns,
 * custom exception design, and security/performance considerations.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

// ============================================================================
// CUSTOM EXCEPTIONS - Best Practices
// ============================================================================

/**
 * Base custom exception for application-specific errors
 * BEST PRACTICE: Extends Exception for checked exceptions that must be handled
 */
class BankingException extends Exception {
    private final String errorCode;
    private final long timestamp;

    public BankingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = System.currentTimeMillis();
    }

    public BankingException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = System.currentTimeMillis();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("BankingException[code=%s, time=%d]: %s",
                errorCode, timestamp, getMessage());
    }
}

/**
 * Specific exception for insufficient funds
 * BEST PRACTICE: Create specific exception types for different error conditions
 */
class InsufficientFundsException extends BankingException {
    private final double requestedAmount;
    private final double availableBalance;

    public InsufficientFundsException(double requested, double available) {
        super(String.format("Insufficient funds: requested $%.2f, available $%.2f",
                requested, available), "INSUF_FUNDS");
        this.requestedAmount = requested;
        this.availableBalance = available;
    }

    public double getShortfall() {
        return requestedAmount - availableBalance;
    }
}

/**
 * Exception for invalid account operations
 * BEST PRACTICE: Provide meaningful context in exception messages
 */
class InvalidAccountException extends BankingException {
    private final String accountId;

    public InvalidAccountException(String accountId, String reason) {
        super(String.format("Invalid account %s: %s", accountId, reason), "INVALID_ACCT");
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}

// ============================================================================
// POOR EXCEPTION HANDLING EXAMPLES
// ============================================================================

class PoorExceptionHandling {
    private static final Logger logger = Logger.getLogger(PoorExceptionHandling.class.getName());

    // BAD: Using exceptions for control flow
    public static int findIndexBad(List<String> list, String target) {
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                if (list.get(i).equals(target)) {
                    return i;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // BAD: Using exception to exit loop
            return -1;
        }
        return -1;
    }

    // BAD: Catching Exception (too broad)
    public static void processFileBad(String filename) {
        try {
            FileReader reader = new FileReader(filename);
            // BAD: Silent failure, swallows all exceptions
        } catch (Exception e) {
            // BAD: Empty catch block
        }
    }

    // BAD: Exposing sensitive information in exceptions
    public static void authenticateUserBad(String username, String password) throws Exception {
        if (!password.equals("admin123")) {
            // SECURITY ISSUE: Exposes password in exception
            throw new Exception("Authentication failed for user " + username +
                    " with password: " + password);
        }
    }

    // BAD: Creating exceptions in tight loops (performance issue)
    public static int sumPositiveNumbersBad(int[] numbers) {
        int sum = 0;
        for (int num : numbers) {
            try {
                if (num < 0) {
                    // PERFORMANCE ISSUE: Exception creation is expensive
                    throw new IllegalArgumentException("Negative number: " + num);
                }
                sum += num;
            } catch (IllegalArgumentException e) {
                // Ignoring negative numbers
            }
        }
        return sum;
    }

    // BAD: Not cleaning up resources properly
    public static String readFileBad(String filename) throws IOException {
        FileReader reader = new FileReader(filename);
        BufferedReader buffered = new BufferedReader(reader);
        // BAD: If exception occurs, resources leak
        return buffered.readLine();
    }
}

// ============================================================================
// GOOD EXCEPTION HANDLING EXAMPLES
// ============================================================================

class GoodExceptionHandling {
    private static final Logger logger = Logger.getLogger(GoodExceptionHandling.class.getName());

    // GOOD: Using return value instead of exceptions for control flow
    public static int findIndexGood(List<String> list, String target) {
        if (list == null || target == null) {
            return -1;
        }

        for (int i = 0; i < list.size(); i++) {
            if (target.equals(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    // GOOD: Specific exception handling with proper cleanup
    public static void processFileGood(String filename) {
        // GOOD: Try-with-resources ensures cleanup
        try (FileReader reader = new FileReader(filename);
                BufferedReader buffered = new BufferedReader(reader)) {

            String line;
            while ((line = buffered.readLine()) != null) {
                processLine(line);
            }

        } catch (FileNotFoundException e) {
            // GOOD: Specific exception with appropriate logging
            logger.log(Level.WARNING, "File not found: {0}", filename);
        } catch (IOException e) {
            // GOOD: Specific exception handling
            logger.log(Level.SEVERE, "Error reading file: " + filename, e);
        }
    }

    private static void processLine(String line) {
        // Process line logic
    }

    // GOOD: Security-conscious exception messages
    public static void authenticateUserGood(String username, String password)
            throws SecurityException {
        if (!isValidCredentials(username, password)) {
            // GOOD: No sensitive information exposed
            logger.log(Level.WARNING, "Failed login attempt for user: {0}", username);
            throw new SecurityException("Authentication failed");
        }
    }

    private static boolean isValidCredentials(String username, String password) {
        // Credential validation logic (stubbed)
        return password != null && password.length() >= 8;
    }

    // GOOD: Validation without exceptions (better performance)
    public static int sumPositiveNumbersGood(int[] numbers) {
        if (numbers == null) {
            return 0;
        }

        int sum = 0;
        for (int num : numbers) {
            // GOOD: Simple validation without exception overhead
            if (num > 0) {
                sum += num;
            }
        }
        return sum;
    }

    // GOOD: Proper resource management with try-with-resources
    public static String readFileGood(String filename) throws IOException {
        try (FileReader reader = new FileReader(filename);
                BufferedReader buffered = new BufferedReader(reader)) {
            return buffered.readLine();
        } // Resources automatically closed even if exception occurs
    }

    // GOOD: Fail-fast with early validation
    public static void validateInput(String input) {
        // GOOD: Check preconditions early
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        if (input.length() > 100) {
            throw new IllegalArgumentException("Input exceeds maximum length");
        }
    }
}

// ============================================================================
// COMPREHENSIVE BANKING EXAMPLE - BEST PRACTICES
// ============================================================================

class BankAccount {
    private final String accountId;
    private double balance;
    private boolean active;
    private static final Logger logger = Logger.getLogger(BankAccount.class.getName());

    public BankAccount(String accountId, double initialBalance) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        this.accountId = accountId;
        this.balance = initialBalance;
        this.active = true;
    }

    /**
     * Withdraw funds with comprehensive error handling
     * BEST PRACTICE: Use custom exceptions for domain-specific errors
     */
    public void withdraw(double amount) throws BankingException {
        // GOOD: Early validation
        validateAccountStatus();
        validateAmount(amount);

        // GOOD: Business logic validation with custom exception
        if (amount > balance) {
            throw new InsufficientFundsException(amount, balance);
        }

        // Perform operation
        balance -= amount;
        logger.log(Level.INFO, "Withdrawal successful: ${0} from account {1}",
                new Object[] { amount, accountId });
    }

    /**
     * Transfer funds between accounts
     * BEST PRACTICE: Multiple exception types for different failure modes
     */
    public void transferTo(BankAccount recipient, double amount) throws BankingException {
        // GOOD: Validate all preconditions
        if (recipient == null) {
            throw new IllegalArgumentException("Recipient account cannot be null");
        }

        validateAccountStatus();
        recipient.validateAccountStatus();
        validateAmount(amount);

        // GOOD: Check for sufficient funds before attempting transfer
        if (amount > balance) {
            throw new InsufficientFundsException(amount, balance);
        }

        try {
            // Perform transfer
            balance -= amount;
            recipient.deposit(amount);
            logger.log(Level.INFO, "Transfer successful: ${0} from {1} to {2}",
                    new Object[] { amount, accountId, recipient.accountId });

        } catch (Exception e) {
            // GOOD: Rollback on failure
            balance += amount;
            logger.log(Level.SEVERE, "Transfer failed, rolled back", e);
            throw new BankingException("Transfer failed: " + e.getMessage(),
                    "TRANSFER_FAILED", e);
        }
    }

    /**
     * Deposit funds
     * BEST PRACTICE: Simple validation without unnecessary exceptions
     */
    public void deposit(double amount) throws InvalidAccountException {
        validateAccountStatus();

        if (amount <= 0) {
            // GOOD: Use standard exception for programming errors
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        balance += amount;
        logger.log(Level.INFO, "Deposit successful: ${0} to account {1}",
                new Object[] { amount, accountId });
    }

    /**
     * GOOD: Private helper methods for validation
     */
    private void validateAccountStatus() throws InvalidAccountException {
        if (!active) {
            throw new InvalidAccountException(accountId, "Account is inactive");
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException("Invalid amount value");
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void deactivate() {
        this.active = false;
    }
}

// ============================================================================
// DEMONSTRATION CLASS
// ============================================================================

public class discussionpost {
    private static final Logger logger = Logger.getLogger(discussionpost.class.getName());

    public static void main(String[] args) {
        System.out.println("=== Exception Handling Best Practices Demonstration ===\n");

        // Configure logging
        configureLogging();

        // Demonstrate poor practices
        demonstratePoorPractices();

        // Demonstrate good practices
        demonstrateGoodPractices();

        // Demonstrate custom exceptions with banking example
        demonstrateBankingExceptions();

        System.out.println("\n=== Demonstration Complete ===");
    }

    private static void configureLogging() {
        try {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            System.err.println("Failed to configure logging: " + e.getMessage());
        }
    }

    private static void demonstratePoorPractices() {
        System.out.println("--- Poor Exception Handling Examples ---");

        // Example 1: Using exceptions for control flow
        List<String> items = Arrays.asList("apple", "banana", "cherry");
        int index = PoorExceptionHandling.findIndexBad(items, "banana");
        System.out.println("1. Exception for control flow (BAD): Found at index " + index);

        // Example 2: Performance issue with exceptions in loop
        int[] numbers = { 1, 2, -3, 4, -5, 6 };
        long start = System.nanoTime();
        int sum = PoorExceptionHandling.sumPositiveNumbersBad(numbers);
        long duration = System.nanoTime() - start;
        System.out.println("2. Exceptions in loop (BAD): Sum = " + sum +
                ", Time: " + duration + "ns");

        System.out.println();
    }

    private static void demonstrateGoodPractices() {
        System.out.println("--- Good Exception Handling Examples ---");

        // Example 1: Proper control flow without exceptions
        List<String> items = Arrays.asList("apple", "banana", "cherry");
        int index = GoodExceptionHandling.findIndexGood(items, "banana");
        System.out.println("1. No exceptions for control flow (GOOD): Found at index " + index);

        // Example 2: Better performance without exceptions
        int[] numbers = { 1, 2, -3, 4, -5, 6 };
        long start = System.nanoTime();
        int sum = GoodExceptionHandling.sumPositiveNumbersGood(numbers);
        long duration = System.nanoTime() - start;
        System.out.println("2. Validation without exceptions (GOOD): Sum = " + sum +
                ", Time: " + duration + "ns");

        // Example 3: Proper resource management
        try {
            GoodExceptionHandling.processFileGood("test.txt");
            System.out.println("3. Try-with-resources (GOOD): Proper cleanup");
        } catch (Exception e) {
            System.out.println("3. Exception handled gracefully: " + e.getClass().getSimpleName());
        }

        System.out.println();
    }

    private static void demonstrateBankingExceptions() {
        System.out.println("--- Custom Exception Handling (Banking Example) ---");

        try {
            // Create accounts
            BankAccount checking = new BankAccount("CHK-001", 1000.00);
            BankAccount savings = new BankAccount("SAV-001", 500.00);

            System.out.println("Initial balances:");
            System.out.println("  Checking: $" + checking.getBalance());
            System.out.println("  Savings: $" + savings.getBalance());

            // Successful withdrawal
            System.out.println("\n1. Attempting withdrawal of $200...");
            checking.withdraw(200.00);
            System.out.println("   SUCCESS: New balance: $" + checking.getBalance());

            // Successful transfer
            System.out.println("\n2. Attempting transfer of $300 to savings...");
            checking.transferTo(savings, 300.00);
            System.out.println("   SUCCESS: Checking: $" + checking.getBalance() +
                    ", Savings: $" + savings.getBalance());

            // Insufficient funds
            System.out.println("\n3. Attempting withdrawal of $1000 (insufficient funds)...");
            try {
                checking.withdraw(1000.00);
            } catch (InsufficientFundsException e) {
                System.out.println("   CAUGHT: " + e.getMessage());
                System.out.println("   Shortfall: $" + String.format("%.2f", e.getShortfall()));
            }

            // Invalid account operation
            System.out.println("\n4. Attempting operation on inactive account...");
            savings.deactivate();
            try {
                savings.withdraw(50.00);
            } catch (InvalidAccountException e) {
                System.out.println("   CAUGHT: " + e.getMessage());
                System.out.println("   Account ID: " + e.getAccountId());
            }

            // Invalid input
            System.out.println("\n5. Attempting invalid deposit...");
            try {
                checking.deposit(-50.00);
            } catch (IllegalArgumentException e) {
                System.out.println("   CAUGHT: " + e.getMessage());
            }

        } catch (BankingException e) {
            System.err.println("Banking error: " + e);
            logger.log(Level.SEVERE, "Banking operation failed", e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            logger.log(Level.SEVERE, "Unexpected error in demonstration", e);
        }
    }
}
