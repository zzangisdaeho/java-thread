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
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value + "입니다.");

        }


        public synchronized void setValue2(int value2) {
            this.value2 = value2;
            try {
                Thread.sleep(3000);
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
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            shareTread.setValue(10);
            shareTread.setValue2(10);
        });
        thred1.setName("스레드 1");
        thred2.setName("스레드 2");
        thred1.start();
        thred2.start();

        //특정 쓰레드가 끝나길 기다림.
        synchronized (thred1){
            thred1.wait();
        }

        synchronized (thred2){
            thred2.wait();
        }

        System.out.println("End");
    }
}
