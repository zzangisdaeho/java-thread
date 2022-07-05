package syncronized;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {


    public static void main(String[] args) throws InterruptedException {

        SyncService syncService = new SyncService();

        ArrayList<Thread> threads = new ArrayList<Thread>();

        for (int i = 0; i < 100; i++) {

            int finalI = i;
            Thread thread = new Thread(() -> syncService.sleepPrint(finalI));

            thread.start();

            threads.add(thread);

        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("FIN!!!");


    }

    /**
     * SyncService2의 sleepPrint와 같은 역할을 한다.
     */
    static class SyncService {

        private Object lock = new Object();

        public int sleepPrint(int i) {

            synchronized (lock) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("i = " + i);

            }

            return i;

        }
    }

    static class SyncService2 {

        private ReentrantLock reentrantLock = new ReentrantLock();

        public int sleepPrint(int i) {

            reentrantLock.lock();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("i = " + i);

            reentrantLock.unlock();

            return i;

        }
    }
}
