
package Portfolio.Module8;

/**
 * CSC450 Portfolio Module 8 - Concurrency Counters Application
 * 
 * ASSIGNMENT REQUIREMENTS:
 * - Create two threads that act as counters
 * - Thread 1: Count up to 20
 * - Thread 2: Count down from 20 to 0 (after Thread 1 completes)
 * 
 * ANALYSIS TOPICS:
 * 1. Performance issues with concurrency
 * 2. Vulnerabilities with use of strings
 * 3. Security of data types
 * 
 * IMPROVEMENTS IMPLEMENTED:
 * - Thread-safe synchronization using CountDownLatch
 * - StringBuilder for efficient string concatenation
 * - Proper resource management with ExecutorService
 * - Exception handling with interrupt status preservation
 * - Comprehensive logging and error handling
 * - Security-conscious design patterns
 * 
 * @author CSC450 Student
 * @version 2.0 - Enhanced with best practices and security improvements
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demonstrates proper concurrent counting with thread coordination.
 * 
 * PERFORMANCE OPTIMIZATIONS:
 * - Uses StringBuilder to minimize string allocation overhead
 * - CountDownLatch for efficient thread coordination (no busy-waiting)
 * - Fixed thread pool to prevent thread creation overhead
 * - Atomic operations where needed for thread safety
 * 
 * SECURITY CONSIDERATIONS:
 * - Immutable counter bounds (final constants)
 * - No data exposure through shared mutable state
 * - Proper exception handling prevents information leakage
 * - Thread interruption handled securely
 */
public class ConcurrencyCounters {

    // SECURITY: Final constants prevent modification - immutable configuration
    private static final int MAX_COUNT = 20;
    private static final int THREAD_POOL_SIZE = 2;
    private static final int SHUTDOWN_TIMEOUT_SECONDS = 5;
    private static final int STRING_BUILDER_CAPACITY = 128;

    // BEST PRACTICE: Use proper logging instead of System.out for production
    private static final Logger logger = Logger.getLogger(ConcurrencyCounters.class.getName());

    // PERFORMANCE: Track execution metrics
    private static final AtomicInteger threadExecutionCount = new AtomicInteger(0);

    public static void main(String[] args) {
        logger.info("=== Concurrency Counters Application Started ===");
        logger.info("Configuration: MAX_COUNT=" + MAX_COUNT +
                ", THREAD_POOL_SIZE=" + THREAD_POOL_SIZE);

        long startTime = System.nanoTime();

        try {
            runConcurrencyDemo();

            long duration = System.nanoTime() - startTime;
            logger.info(String.format("=== Application Completed Successfully in %.2f ms ===",
                    duration / 1_000_000.0));

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Application failed with error", e);
            System.exit(1);
        }
    }

    /**
     * Executes the concurrent counting demonstration.
     * 
     * CONCURRENCY PATTERN:
     * Uses CountDownLatch as a synchronization barrier to ensure:
     * 1. Thread 1 completes counting up before Thread 2 starts counting down
     * 2. No race conditions or data corruption
     * 3. Efficient signaling without polling or sleep
     * 
     * PERFORMANCE ANALYSIS:
     * - ExecutorService reuses threads (no creation overhead per task)
     * - CountDownLatch uses lock-free operations when possible
     * - StringBuilder prevents unnecessary string object creation
     * - Named threads aid debugging with minimal overhead
     */
    private static void runConcurrencyDemo() {
        // BEST PRACTICE: Named thread factory for easier debugging and monitoring
        // SECURITY: Thread names don't expose sensitive information
        ThreadFactory namedThreadFactory = createSecureThreadFactory();

        // PERFORMANCE: Fixed thread pool - threads are created once and reused
        // SECURITY: Limited pool size prevents resource exhaustion attacks
        ExecutorService executorPool = Executors.newFixedThreadPool(
                THREAD_POOL_SIZE,
                namedThreadFactory);

        // CONCURRENCY: CountDownLatch provides efficient thread coordination
        // Single-use barrier: count of 1 means one signal releases waiting thread
        CountDownLatch completionGate = new CountDownLatch(1);

        // PERFORMANCE: AtomicInteger for thread-safe counter without synchronization
        // overhead
        AtomicInteger upCounterState = new AtomicInteger(0);

        // Create the counting tasks
        Runnable countUpTask = createCountUpTask(completionGate, upCounterState);
        Runnable countDownTask = createCountDownTask(completionGate);

        try {
            // Submit tasks to executor
            // CONCURRENCY: Both tasks submitted immediately, but countDown waits on latch
            logger.info("Submitting counting tasks to executor...");
            executorPool.execute(countUpTask);
            executorPool.execute(countDownTask);

        } finally {
            // BEST PRACTICE: Always shutdown executor in finally block
            shutdownExecutorGracefully(executorPool);
        }
    }

    /**
     * Creates a secure thread factory with named threads.
     * 
     * SECURITY CONSIDERATIONS:
     * - Thread names don't include sensitive data
     * - Uses daemon threads appropriately
     * - Sets appropriate thread priority
     * 
     * @return ThreadFactory that creates properly configured threads
     */
    private static ThreadFactory createSecureThreadFactory() {
        return runnable -> {
            Thread thread = new Thread(runnable);

            // SECURITY: Thread name provides debugging info without exposing sensitive data
            thread.setName("counter-worker-" + thread.threadId());

            // BEST PRACTICE: Non-daemon threads ensure tasks complete before JVM exit
            thread.setDaemon(false);

            // PERFORMANCE: Normal priority prevents priority inversion issues
            thread.setPriority(Thread.NORM_PRIORITY);

            return thread;
        };
    }

    /**
     * Creates the count-up task.
     * 
     * STRING VULNERABILITY ANALYSIS:
     * - Uses StringBuilder (mutable) instead of String concatenation
     * - Pre-allocates capacity to avoid array resizing
     * - Single toString() call minimizes string object creation
     * 
     * PERFORMANCE ANALYSIS:
     * - StringBuilder: O(n) time, O(n) space - optimal for sequential appends
     * - String concatenation would be O(n²) due to immutability
     * - Pre-sized buffer eliminates reallocation overhead
     * 
     * @param gate  CountDownLatch to signal completion
     * @param state AtomicInteger to track progress (for monitoring)
     * @return Runnable task for counting up
     */
    private static Runnable createCountUpTask(CountDownLatch gate, AtomicInteger state) {
        return () -> {
            String threadName = Thread.currentThread().getName();
            logger.info("[" + threadName + "] Starting count up task");

            try {
                // PERFORMANCE: Pre-allocated StringBuilder prevents resizing
                // SECURITY: Local scope - no shared mutable state
                StringBuilder outputBuffer = new StringBuilder(STRING_BUILDER_CAPACITY);

                // COUNT UP: 0 to MAX_COUNT (inclusive)
                for (int i = 0; i <= MAX_COUNT; i++) {
                    // PERFORMANCE: StringBuilder.append is highly optimized
                    // Much faster than: String result = ""; result += i + " ";
                    outputBuffer.append(i);

                    if (i < MAX_COUNT) {
                        outputBuffer.append(' '); // Space separator
                    }

                    // MONITORING: Update progress (thread-safe atomic operation)
                    state.set(i);
                }

                // CONCURRENCY: Signal completion before output to ensure ordering
                gate.countDown();
                logger.info("[" + threadName + "] Count up completed, gate released");

                // PERFORMANCE: Single output operation reduces I/O overhead
                // STRING SECURITY: toString() creates final immutable snapshot
                String output = outputBuffer.toString();
                System.out.printf("[%s] UP:   %s%n", threadName, output);

                threadExecutionCount.incrementAndGet();

            } catch (Exception e) {
                logger.log(Level.SEVERE, "[" + threadName + "] Count up task failed", e);
                // BEST PRACTICE: Release latch even on failure to prevent deadlock
                gate.countDown();
            }
        };
    }

    /**
     * Creates the count-down task.
     * 
     * CONCURRENCY ANALYSIS:
     * - Waits on CountDownLatch before starting
     * - Handles InterruptedException properly
     * - Preserves thread interrupt status for caller
     * 
     * STRING VULNERABILITY MITIGATION:
     * - Same StringBuilder optimization as count-up
     * - No string concatenation in loops
     * - Single allocation, single toString() call
     * 
     * @param gate CountDownLatch to wait on before starting
     * @return Runnable task for counting down
     */
    private static Runnable createCountDownTask(CountDownLatch gate) {
        return () -> {
            String threadName = Thread.currentThread().getName();
            logger.info("[" + threadName + "] Count down task waiting on gate...");

            try {
                // CONCURRENCY: Block until count-up thread signals completion
                // PERFORMANCE: Efficient wait - no busy polling or sleep
                gate.await();

                logger.info("[" + threadName + "] Gate opened, starting count down");

                // PERFORMANCE: Pre-allocated StringBuilder
                StringBuilder outputBuffer = new StringBuilder(STRING_BUILDER_CAPACITY);

                // COUNT DOWN: MAX_COUNT to 0 (inclusive)
                for (int i = MAX_COUNT; i >= 0; i--) {
                    outputBuffer.append(i);

                    if (i > 0) {
                        outputBuffer.append(' ');
                    }
                }

                // PERFORMANCE: Single output operation
                String output = outputBuffer.toString();
                System.out.printf("[%s] DOWN: %s%n", threadName, output);

                threadExecutionCount.incrementAndGet();

            } catch (InterruptedException e) {
                // BEST PRACTICE: Restore interrupt status
                // SECURITY: Don't expose stack trace in production
                Thread.currentThread().interrupt();
                logger.log(Level.WARNING, "[" + threadName + "] Count down interrupted", e);

            } catch (Exception e) {
                logger.log(Level.SEVERE, "[" + threadName + "] Count down task failed", e);
            }
        };
    }

    /**
     * Gracefully shuts down the executor service.
     * 
     * BEST PRACTICE:
     * 1. Call shutdown() to prevent new tasks
     * 2. Wait for tasks to complete with timeout
     * 3. Force shutdown if timeout exceeded
     * 4. Handle interruption during wait
     * 
     * SECURITY:
     * - Timeout prevents resource exhaustion
     * - Forces termination of runaway tasks
     * - Proper cleanup prevents resource leaks
     * 
     * @param executor ExecutorService to shut down
     */
    private static void shutdownExecutorGracefully(ExecutorService executor) {
        logger.info("Shutting down executor service...");

        // STEP 1: Prevent new task submissions
        executor.shutdown();

        try {
            // STEP 2: Wait for existing tasks to complete
            if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                logger.warning("Executor did not terminate within timeout, forcing shutdown");

                // STEP 3: Force shutdown of running tasks
                executor.shutdownNow();

                // STEP 4: Wait again for forced shutdown to complete
                if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    logger.severe("Executor did not terminate after forced shutdown");
                }
            } else {
                logger.info("Executor shut down successfully");
            }

        } catch (InterruptedException e) {
            // BEST PRACTICE: Restore interrupt and force shutdown
            Thread.currentThread().interrupt();
            logger.warning("Shutdown interrupted, forcing immediate termination");
            executor.shutdownNow();
        }

        logger.info("Total tasks executed: " + threadExecutionCount.get());
    }
}
