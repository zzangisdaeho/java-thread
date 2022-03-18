package syncronized;

public class SyncBlock {

    public static class ShareThread {
        private int value = 0;
        private int value2 = 0;

        /**
         * Synchronized 블록에는 한번에 한 쓰레드만 접근 가능 - 객체에 락이 걸리기 때문
         *
         * @param value
         */
        public void setValue(int value) {
            System.out.println("ShareThread.setValue");
            synchronized (this) {
                this.value = value;
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value + "입니다.");
            }

        }

        /**
         * Synchronized 블록은 클래스를 지정하는게 보통.
         * 같은 클래스를 대상으로 공유하는 블록은 다른 메서드라도 Sync하게 동작함 - 객체에 락이 걸려있기 때문
         * Sync하게 작동하도록 다른 쓰레드를 막는 기준점이 해당 class가 점유된 상태인가를 보기 때문이다.
         * 반대로 sync하지 않은 같은 객체의 다른 메서드는 락이 걸리지 않는다.
         *
         * @param value2
         */
        public void setValue2(int value2) {
            System.out.println("ShareThread.setValue2");
            synchronized (this) {
                this.value2 = value2;
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value2 + "입니다.");
            }
        }

        public int getValue() {
            return value;
        }
    }

    public static void main(String[] args) {

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

        System.out.println("SyncBlock.main");
    }
}
