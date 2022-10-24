package thread_pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelQueueWithZeroQueue {

    public static void main(String[] args) {

        SynchronousQueue<Runnable> runnables = new SynchronousQueue<>();

        ExecutorService executorServiceWithCustom = new ThreadPoolExecutor(3, 100, 120L, TimeUnit.SECONDS, runnables);


        // Callable 구현 - 리턴값 있음
        Callable<Boolean> task = () ->{
            Boolean isFinish = true;

            Thread.sleep(1000);
            System.out.println("작업 중입니다. Call" + Thread.currentThread().getName());

            return isFinish;
        };

        List<Future<Boolean>> results = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            results.add(executorServiceWithCustom.submit(task));
        }

        List<Boolean> resultGet = new ArrayList<>();

        results.parallelStream().forEach(result -> {
            try {
                System.out.println("그냥 forEach는 병렬처리 안된다 parallelStream으로 생성해야 병렬처리된다");
                resultGet.add(result.get());
                Thread.sleep(1000);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        System.out.println("results.size() = " + results.size());
        System.out.println("resultGet = " + resultGet);

        executorServiceWithCustom.shutdown();

    }



}
