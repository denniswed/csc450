package Portfolio.Module8;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ConcurrencyCounters {

    public static void main(String[] args) {
        // Name threads for easier debugging/logging
        ThreadFactory named = r -> {
            Thread t = new Thread(r);
            t.setName("worker-" + t.getId());
            return t;
        };

        ExecutorService pool = Executors.newFixedThreadPool(2, named);
        CountDownLatch gate = new CountDownLatch(1); // B waits until A signals

        Runnable countUp = () -> {
            StringBuilder line = new StringBuilder(128); // buffer to avoid per-iteration println cost
            for (int i = 0; i <= 20; i++) {
                line.append(i).append(' ');
                if (i == 20) {
                    gate.countDown(); // publish completion to other threads
                }
            }
            System.out.printf("[%s] up:   %s%n", Thread.currentThread().getName(), line);
        };

        Runnable countDown = () -> {
            try {
                gate.await(); // block until countUp signals
                StringBuilder line = new StringBuilder(128);
                for (int i = 20; i >= 0; i--) {
                    line.append(i).append(' ');
                }
                System.out.printf("[%s] down: %s%n", Thread.currentThread().getName(), line);
            } catch (InterruptedException ie) {
                // Best practice: restore interrupt status so upstream code can react
                Thread.currentThread().interrupt();
                System.err.println("Down-counter interrupted");
            }
        };

        try {
            pool.execute(countUp); // start both; B will wait on the latch
            pool.execute(countDown);
        } finally {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException ie) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
