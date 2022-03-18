package thread_pool;

import java.util.concurrent.*;

public class FutureReturnSample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        SynchronousQueue<Runnable> runnables = new SynchronousQueue<>();

        ExecutorService executorServiceWithCustom = new ThreadPoolExecutor(3, 100, 120L, TimeUnit.SECONDS, runnables);


        // Runable 구현 객체 ( 익명구현객체 사용  ) - 리턴값 없음
        Runnable task1 = ()-> {
            for (int index = 0; index < 3; index++) {
                System.out.println("작업 중입니다. Runnable");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // Callable 구현 - 리턴값 있음
        Callable<Boolean> task2 = () ->{
            Boolean isFinish = true;

            for (int index = 0; index < 10; index++) {
                System.out.println("작업 중입니다. Call" + Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return isFinish;
        };

        // 1. 리턴 값이 없는 단순 Runnable를 처리합니다.
//        executorServiceWithCustom.execute(task1);
        Future<?> submit = executorServiceWithCustom.submit(task1);

        try {
            //Future get을 하면 해당 메서드가 끝날떄까지 다음 코드 실행 보류
            Object o = submit.get();
            System.out.println("Runnable 끝났습니까 : " + o);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 2. 리턴 가능한 Callable도 넣을 수 있는 메서드 입니다.
        Future<Boolean> returnBoolean = executorServiceWithCustom.submit(task2);

        try {
            //Future get을 하면 해당 메서드가 끝날떄까지 다음 코드 실행 보류
            Boolean result = returnBoolean.get();
            System.out.println("끝났습니까 : " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        // main스레드의 작업이 멈추지 않기 위해 새로운 스레드로 구성
//        executorServiceWithCustom.execute(()->{
//            try {
//                if( returnBoolean.get() ) {
//                    System.out.println("작업이 완벽히 끝났습니다. ");
//                } else {
//                    System.out.println("작업이 끝나지 못했습니다.");
//                }
//            } catch (Exception e) {
//            }
//        });

//        // main스레드의 작업이 멈추지 않기 위해 새로운 스레드로 구성
//        executorServiceWithCustom.execute(()->{
//            try {
//                // 만약 특정 시간 내에 끝났는지 확인하려는 경우
//                if( returnBoolean.get(1,TimeUnit.SECONDS) ) {
//                    System.out.println("작업이 완벽히 끝났습니다. ");
//                }
//            } catch (Exception e) {
//                System.out.println("작업이 시간내에 끝나지 못했습니다.");
//            }
//        });

        executorServiceWithCustom.shutdown();

        System.out.println("=======end======");

    }



}
