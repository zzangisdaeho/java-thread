package syncronized;

public class ThreadSleepTest {

    public static void main(String[] args) throws InterruptedException {

        Thread newThread = new TestThread();

        newThread.start();

        System.out.println("newThread = " + newThread);
        synchronized (newThread){
            try {
                newThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread.sleep(10);
        System.out.println("I'm main");
    }

    public static class TestThread extends Thread{

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
