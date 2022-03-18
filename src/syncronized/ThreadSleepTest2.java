package syncronized;

public class ThreadSleepTest2 {

    public static void main(String[] args) throws InterruptedException {

        TestThread testThread = new TestThread();

        Thread newThread = new Thread(testThread);

        newThread.start();

        System.out.println("testThread = " + testThread);
        synchronized (testThread){
            try {
                testThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread.sleep(10);
        System.out.println("I'm main");
    }

    public static class TestThread implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this){
                System.out.println("notify to : " + this);
                notify();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("뺴꼼!");
        }
    }

}
