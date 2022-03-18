package syncronized;

public class NonSync {

    public static class ShareThread {
        private int value = 0;

        public void setValue(int value) {
            this.value = value;
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value + "입니다.");

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
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shareTread.setValue(10);
        });
        thred1.setName("스레드 1");
        thred2.setName("스레드 2");
        thred1.start();
        thred2.start();
    }
}
