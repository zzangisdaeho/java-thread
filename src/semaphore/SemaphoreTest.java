package semaphore;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SemaphoreTest {

    public static void main(String[] args) {
        AtomicBoolean flag = new AtomicBoolean(true);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            flag.set(!flag.get());
            System.out.println("flag set to = " + flag.get());
        }, 10, 30, TimeUnit.SECONDS);

        Semaphore semaphore = new Semaphore(3);

        System.out.println(ZonedDateTime.now());
        long l = System.currentTimeMillis();
        List<Integer> collect = IntStream.range(0, 30).boxed().collect(Collectors.toList());

        List<CompletableFuture<Integer>> completableFutureList = collect.stream().map(value -> CompletableFuture.supplyAsync(getSupplier(semaphore, flag, value))).collect(Collectors.toList());
        List<Integer> join = CompletableFuture.allOf(completableFutureList.toArray(CompletableFuture[]::new)).thenApply(unused -> completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList())).join();
        System.out.println("join = " + join);

//        List<Thread> threads = collect.stream().map(integer -> new Thread(getRunnable(semaphore, integer))).collect(Collectors.toList());
//        threads.forEach(Thread::start);
//        for (Thread thread : threads) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


        System.out.println(ZonedDateTime.now());

        System.out.println(System.currentTimeMillis() - l);
    }

    private static Runnable getRunnable(Semaphore semaphore, Integer integer) {
        return () -> {
            try {
                semaphore.acquire();
                System.out.println("remain permits : " + semaphore.availablePermits());
                Thread.sleep(3000);
                System.out.println("integer = " + integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        };
    }

//    private static Callable getCallable(Semaphore semaphore, Integer integer) {
//        return () -> {
//            try {
//                semaphore.acquire();
//                System.out.println("remain permits : " + semaphore.availablePermits());
//                Thread.sleep(3000);
//                System.out.println("integer = " + integer);
//                return integer;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                semaphore.release();
//            }
//        };
//    }

    private static Supplier<Integer> getSupplier(Semaphore semaphore, AtomicBoolean flag, Integer integer) {
        return () -> {
            try {
                semaphore.acquire();
//                while(!flag.get());
//                System.out.println("remain permits : " + semaphore.availablePermits());

                Thread.sleep(3000);
                System.out.println("integer = " + integer);
                System.out.println("thread = " + Thread.currentThread());
                return integer;

            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("");
            } finally {
                semaphore.release();
            }
        };
    }

}
