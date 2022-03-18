package thread_pool;

import java.util.List;
import java.util.concurrent.*;

public class Basic {

    public static void main(String[] args) {
        // 스레드 풀 생성
        //1. 자동으로 스레드 수 생성
        ExecutorService executorServiceWithCached = Executors.newCachedThreadPool();

        //2. 원하는 개수만큼 생성
        ExecutorService executorServiceWithNum = Executors.newFixedThreadPool(2);

        //3. 최대치로 생성
        ExecutorService executorServiceWithMax = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        //4. 완전 수동
        // ThreadPoolExecutor( 코어 스레드 수, 최대 스레드 개수, 놀고 있는 시간, 놀고있는 시간 단위, 작업 큐 )
        // corePool이 먼저 차고, 이후 Queue에 대기열 등록, Queue가 넘치면 maxPoolSize까지 쓰레드갯수 확장
        ExecutorService executorServiceWithCustom = new ThreadPoolExecutor(3, 100, 120L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());


        // 스레드 풀 종료
        // 쓰레드풀도 사용자 쓰레드이기 때문에 쓰레드풀이 남아있을 경우 작업이 종료(shutdown)되지 않는다. 고로 직접 shutdown해줘야 사용자 쓰레드가 모두 사라져 프로그램이 종료된다
        // 1. 작업 큐에 대기하고 있는 모든 작업이 끝난 뒤 스레드를 종료한다.
        executorServiceWithCached.shutdown();

        // 2. 당장 중지한다. 리턴값은 작업큐에 남아있는 작업의 목록이다.
        List<Runnable> runable = executorServiceWithCached.shutdownNow();

        // 3. 작업은 대기 하지만 모든 작업처리를 특정 시간안에 하지 못하면 작업중인 스레드를 중지하고 false를 리턴한다. 아래는 100초 설정
        try {
            boolean isFinish = executorServiceWithCached.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
