package result;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ResultSharing {

    public static void main(String[] args) {

        ResultShare resultShare = new ResultShare();

        // 스레드 풀 생성
        //자동으로 스레드 수 생성
        ExecutorService executorServiceWithCached = Executors.newCachedThreadPool();

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

        // Runable이라도 작업이 끝난 후 Future<리턴 객체>로 무엇을 받을 지 임의로 정할 수 있다. run()의 리턴값으로 아무 값이 없더라도 종료 후에 아무 원하는 객체를 이런 식으로 리턴할 수 있다.
        Future<ResultShare> future1 =  executorServiceWithCached.submit( task1, resultShare );
        Future<ResultShare> future2 =  executorServiceWithCached.submit( task2, resultShare );

        ResultShare temp = null;
        try {
            temp = future1.get();
            temp.showSum();
            temp = future2.get();
            temp.showSum();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("쓰레드 합산이 끝났습니다. 최종 결과는 : " + resultShare.getResult());

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
