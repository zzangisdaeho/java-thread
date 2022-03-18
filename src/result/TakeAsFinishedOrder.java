package result;

import java.util.concurrent.*;

public class TakeAsFinishedOrder {

    public static void main(String[] args) {

        ResultShare resultShare = new ResultShare();

        ExecutorService executorServiceWithCached = Executors.newCachedThreadPool();

        //여러 개의 작업들이 순차적으로 처리될 필요성이 없고, 처리 결과도 순차적일 필요가 없다면 작업 처리 완료된 것부터 결과를 얻어 이용
        CompletionService<ResultShare> compliCompletionService = new ExecutorCompletionService<>(executorServiceWithCached);

        Runnable task1 = () -> {
            int result = 0;
            for (int index = 1; index <= 100; index++) {
                result += index;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1~100까지의 합은 " + result +"입니다.");
            resultShare.sum(result);

        };
        Runnable task2 = () -> {
            int result = 0;
            for (int index = 101; index <= 200; index++) {
                result += index;
            }
            System.out.println("101~200까지의 합은 " + result +"입니다.");
            resultShare.sum(result);
        };

        int taskCount = 0;
        //CompletionService 를 통해 큐잉
        compliCompletionService.submit( task1, resultShare );
        ++taskCount;
        compliCompletionService.submit( task2, resultShare );
        ++taskCount;

        /**
         * CompletionService
         *
         * Future<V>
         * poll()
         * 완료된 작업이 Future를 가져옴
         * 완료된 작업이 없다면 즉시 null을 리턴
         *
         * Future<V>
         * poll(long timeout, TimeUnit unit)
         * 완료된 작업이 Future를 가져옴
         * 완료된 작업이 없다면 timeout까지 블로킹됨
         *
         * Future<V>
         * take()
         * 완료된 작업이 Future를 가져옴
         * 완료된 작업이 없다면 있을 때까지 블로킹됨
         *
         * Future<V>
         * submit(Callable<V> task)
         * 스레드풀에 Callable 작업 처리 요청
         *
         * Future<V>
         * submit(Callable<V> task, V result)
         * 스레드풀에 Runnable 작업 처리 요청
         */

        while (taskCount != 0){
            Future<ResultShare> takeFirst = null;
            try {
                takeFirst = compliCompletionService.take();
                takeFirst.get().showSum();
                --taskCount;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorServiceWithCached.shutdown();
    }

    public static class ResultShare {
        private int result;

        public int sum( int number ) {
            return result+=number;
        }

        public void showSum() {
            System.out.println(" 지금 저장된 값은 : " + result + "입니다.");
        }

        public int getResult() {
            return result;
        }
    }
}
