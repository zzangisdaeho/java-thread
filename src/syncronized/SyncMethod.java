package syncronized;

public class SyncMethod {

    public static class ShareThread {
        private int value = 0;
        private int value2 = 0;

        /**
         * Synchronized 메서드는 한번에 한 쓰레드만 접근 가능
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

        /**
         * Synchronized 메서드를 호출한 Thread끼리는 다른 메서드라도 Sync하게 움직이게됨.
         * Synchronized 메서드는 내부적으로 synchronized block(class)로 감싸져있다고 볼 수 있을듯. 고로 같은 클래스를 대상으로 접근하는 여러 메소드들이 sync하게 작동하는것.
         * @param value2
         */
        public synchronized void setValue2(int value2) {
            this.value2 = value2;
            try {
//                Thread.sleep(3000);
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

        System.out.println("SyncBlock.main");
    }
}
