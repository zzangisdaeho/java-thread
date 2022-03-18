package syncronized;

public class SyncMethodWait {

    public static class ShareThread {
        private int value = 0;
        private int value2 = 0;

        /**
         * Synchronized 메서드는 끝나면 자신을 wait하는 객체에 자동으로 notify해준다.
         * @param value
         */
        public synchronized void setValue(int value) {
            System.out.println("ShareThread.setValue");
            this.value = value;
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("now notify");
            notify();
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value + "입니다.");

        }


        public synchronized void setValue2(int value2) {
            System.out.println("ShareThread.setValue2");
            this.value2 = value2;
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("now notify");
            notify();
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value2 + "입니다.");

        }

        public int getValue() {
            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ShareThread shareTread = new ShareThread();
        Thread thred1 = new Thread(() -> {
            shareTread.setValue(100);

        });

        Thread thred2 = new Thread(() -> {
            shareTread.setValue2(10);
        });
        thred1.setName("스레드 1");
        thred2.setName("스레드 2");
        thred1.start();
        thred2.start();

        //특정 객체의 락이 풀리길 기다림.
        synchronized (shareTread){
            shareTread.wait();
        }

        System.out.println("End");
    }
}
