package completable_future;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompletableFutureTest {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Long> seqList = Stream.iterate(1L, n -> n + 1L).limit(100).collect(Collectors.toList());

        List<CompletableFuture<Long>> completableFutureList = seqList.stream()
                .map(seq -> returnCompletableFuture(seq, executorService)
                        .exceptionally(throwable -> {
                            System.out.println("throwable = " + throwable);
                            return 1000L;
                        }))
                .collect(Collectors.toList());

        CompletableFuture<List<Long>> listCompletableFuture = CompletableFuture
                .allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .thenApply(unused -> completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList()));

        List<Long> join = listCompletableFuture.join();
        System.out.println("join = " + join);


        executorService.shutdown();

    }

    private static CompletableFuture<Long> returnCompletableFuture(Long seq, ExecutorService executorService){

        return CompletableFuture.supplyAsync(() -> {
            threadSleep(100);
            if(seq % 10 == 0) throw new IllegalArgumentException("10의 배수는 통과할수 없다. 들어온 값 : " + seq);
            return seq*2;
        }, executorService);
    }

    private static void threadSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
