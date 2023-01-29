package semaphore;

import java.time.ZonedDateTime;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class SemaphoreTest {

    private final Semaphore semaphore;

    public SemaphoreTest(int maxConcurrentRequests) {
        this.semaphore = new Semaphore(maxConcurrentRequests);
    }

    public void makeRequest(Runnable runnable) {
        try {
            semaphore.acquire();
//            int availablePermits = semaphore.availablePermits();
//            System.out.println("availablePermits = " + availablePermits);
            // Code to make the API request

            runnable.run();

        } catch (InterruptedException e) {
            // Handle the exception
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        SemaphoreTest semaphoreTest = new SemaphoreTest(3);
        System.out.println(ZonedDateTime.now());
        long l = System.currentTimeMillis();
        IntStream.range(0, 100)
                .parallel()
                .forEach(value -> semaphoreTest.makeRequest(() -> {
                    System.out.println("value = " + value);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }));


        System.out.println(ZonedDateTime.now());

        System.out.println(System.currentTimeMillis()-l);
    }

}
