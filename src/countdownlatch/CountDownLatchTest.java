package countdownlatch;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountDownLatchTest {

    public static void main(String args[]) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new Worker(countDownLatch)))
                .limit(5)
                .collect(Collectors.toList());

        System.out.println("Start multi threads (tid: " + Thread.currentThread().getId() + ")");

        workers.forEach(Thread::start);

        System.out.println("Waiting for some work to be finished (tid: " + Thread.currentThread().getId() + ")");

        countDownLatch.await();

        System.out.println("Finished (tid: " + Thread.currentThread().getId() + ")");
    }

    public static class Worker implements Runnable {
        private CountDownLatch countDownLatch;

        public Worker(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Do something (tid: " + Thread.currentThread().getId() + ")");
            countDownLatch.countDown();
        }
    }
}
