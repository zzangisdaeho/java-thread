package throttle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class PointTest {

    public static void main(String[] args) throws InterruptedException {


        PointTest pointTest = new PointTest();

//        getScheduledExecutorService(pointTest);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 100, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

        List<Future<Object>> futures = IntStream.range(0, 5).boxed().map(value -> executor.submit(getObjectCallable(pointTest))).toList();

        futures.parallelStream().forEach(future -> {
            try {
                System.out.println("o = " + future.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });


        System.out.println("=end= ");


        executor.shutdown();

    }

    private static void getScheduledExecutorService(PointTest pointTest) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("Wake up!!!" + pointTest);
            synchronized (pointTest){
                try {
                    pointTest.notifyAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5, 10, TimeUnit.SECONDS);
    }

    private static Callable<Object> getObjectCallable(PointTest pointTest) {
        return () -> {
            try {
                return pointTest.checkWeight(3);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    private PointManager pointManager = new PointManager(10);

    public synchronized Object checkWeight(int weight) throws Throwable {
        while (true) {
            System.out.println("this = " + this);
            if (pointManager.addPointAndCheckLimit(weight)) {
                this.notifyAll();
                pointManager.addNewPoints(weight);
                return "Success";
            } else {
                this.wait(TimeUnit.SECONDS.toMillis(30));
            }
        }
    }

    public static class PointManager {
        private static final long ONE_MINUTE_IN_MILLIS = TimeUnit.MINUTES.toMillis(1);

        private final ConcurrentHashMap<Long, Integer> points;
        private final int maxPoints;

        public PointManager(int maxPoints) {
            this.points = new ConcurrentHashMap<>();
            this.maxPoints = maxPoints;
        }

        public boolean addPointAndCheckLimit(int point) {
            long now = System.currentTimeMillis();
            removePointsOlderThanOneMinute(now);
            int newPoints = getRecentPoints(now) + point;
//            points.forEach((key, value) -> System.out.println("key = " + key + " value = " + value));
//            System.out.println("current point : " + newPoints);
//            System.out.println("return value : " + (newPoints < maxPoints));
            return newPoints < maxPoints;
        }

        private void removePointsOlderThanOneMinute(long now) {
            points.keySet().removeIf(timestamp -> now - timestamp > ONE_MINUTE_IN_MILLIS);
        }


        private int getRecentPoints(long now) {
            return points.entrySet().stream()
                    .filter(entry -> now - entry.getKey() <= ONE_MINUTE_IN_MILLIS)
                    .mapToInt(Map.Entry::getValue)
                    .sum();
        }

        private void addNewPoints(int point) {
            long now = System.currentTimeMillis();
            points.compute(now, (key, value) -> {
                if (value == null) return point;
                else return value + point;
            });
        }
    }
}
