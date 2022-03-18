package syncronized;

public class SyncBlockWait {

    public static class ShareThread {
        private int value = 0;
        private int value2 = 0;

        /**
         * Synchronized 블록이 끝나면 자동으로 notify한다.
         *
         * @param value
         */
        public void setValue(int value) {
            System.out.println(this);
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
         * 중간에 notify해도 mainThread는 블럭이 끝나고나서 움직인다.
         * wait의 기준은 블럭단위라고 이해해도 될것 같다.
         * @param value2
         */
        public void setValue2(int value2) {
            System.out.println(this);

            synchronized (this) {

                this.value2 = value2;
                try {
                Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("now notify");
                this.notify();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value2 + "입니다.");
            }

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
