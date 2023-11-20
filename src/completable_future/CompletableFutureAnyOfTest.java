package completable_future;

import java.util.List;
import java.util.concurrent.*;

/**
    여러 쓰레드가 경합시 먼저 끝난것을 return하는 케이스
 */
public class CompletableFutureAnyOfTest {

    public static void main(String[] args) {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello : " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            System.out.println("World : " + Thread.currentThread().getName());
            return "World";
        });

        //anyOf(): 여러 작업 중에 가장 빨리 끝난 하나의 결과에 콜백을 실행한다. 반대로는 allOf()가 있다.
        //executorService.invokeAny() 와 같은기능. 반대로는 invokeAll()이 있다.
        CompletableFuture<Object> future = CompletableFuture.anyOf(hello, world).thenApply(o -> "first : " + o);
        Object join = future.join();

        System.out.println("join = " + join);

        //thread가 1개면 항상 hello가 먼저끝난다. 해당 작업이 어쩃든 먼저 들어가고 쓰레드가 한개라서 해당 작업이 끝나기 전에 world쪽으로 못넘어가기 때문이다.
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try {
            Object o = executorService.invokeAny(List.of(getCallable("Hello"), getCallable("World")));
            System.out.println("first : " + o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }
    }

    public static Callable<String> getCallable(String word){
        return () -> {
            System.out.println("word = " + word + " " + Thread.currentThread().getName());
            return word;
        };
    }
}
