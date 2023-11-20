package throttle;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NotifyThreadByScheduleTest {

    public static void main(String[] args) {
        Object lock = new Object();

        ScheduledExecutorService scheduledExecutorService = getScheduledExecutorService(lock);

        System.out.println("lock = " + lock);
        try {
            synchronized (lock){
                lock.wait(30000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("end");
        scheduledExecutorService.shutdown();
    }

    private static ScheduledExecutorService getScheduledExecutorService(Object lock) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("Wake up!!!" + lock);
            synchronized (lock){
                try {
                    lock.notifyAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5, 10, TimeUnit.SECONDS);

        return scheduledExecutorService;
    }
}
