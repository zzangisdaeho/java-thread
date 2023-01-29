package throttle;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThrottleTest {

    private final int maxRequests;
    private final long timeWindowInSeconds;
    private final ConcurrentHashMap<String, Integer> requestCounter;
    private final ScheduledExecutorService scheduler;

    public ThrottleTest(int maxRequests, long timeWindowInSeconds) {
        this.maxRequests = maxRequests;
        this.timeWindowInSeconds = timeWindowInSeconds;
        this.requestCounter = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.scheduler.scheduleAtFixedRate(this::resetCounter, timeWindowInSeconds, timeWindowInSeconds, TimeUnit.SECONDS);
    }

    public boolean isRequestAllowed() {
        String currentWindow = getCurrentWindow();
        requestCounter.putIfAbsent(currentWindow, 0);
        int currentCount = requestCounter.get(currentWindow);
        if (currentCount < maxRequests) {
            requestCounter.put(currentWindow, currentCount + 1);
            return true;
        } else {
            return false;
        }
    }

    private void resetCounter() {
        requestCounter.clear();
    }

    private String getCurrentWindow() {
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        long currentWindowStart = currentTimeInSeconds - (currentTimeInSeconds % timeWindowInSeconds);
        return String.valueOf(currentWindowStart);
    }

    public static void main(String[] args) {
        ThrottleTest throttler = new ThrottleTest(100, 60);

        if (throttler.isRequestAllowed()) {
            System.out.println("api send");
            // Send API request
        } else {
            // Wait until the rate limit resets
        }
    }
}
