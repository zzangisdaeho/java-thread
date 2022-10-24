package thread_pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelQueueWithSomeQueueSize {

    public static void main(String[] args) {

        BlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>(100);

        ExecutorService executorServiceWithCustom = new ThreadPoolExecutor(3, 100, 120L, TimeUnit.SECONDS, runnables);


        // Callable 구현 - 리턴값 있음
        Callable<Boolean> task = () ->{
            Boolean isFinish = true;

            Thread.sleep(1000);
            System.out.println("작업 중입니다. Call" + Thread.currentThread().getName());


            return isFinish;
        };

        List<Future<Boolean>> results = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            results.add(executorServiceWithCustom.submit(task));
        }

        List<Boolean> resultGet = new ArrayList<>();

        results.forEach(result -> {
            try {
                System.out.println("forEach는 병렬처리 안된다");
                resultGet.add(result.get());
                Thread.sleep(3000);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        System.out.println("results.size() = " + results.size());
        System.out.println("resultGet = " + resultGet);

        executorServiceWithCustom.shutdown();

    }



}
