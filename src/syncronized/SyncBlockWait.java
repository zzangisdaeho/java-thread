package syncronized;

public class SyncBlockWait {

    public static class ShareThread {
        private int value = 0;
        private int value2 = 0;

        /**
         * 중간에 wait를 깨워주지 않고 Thread가 역할을 마치고 사라지면 자동으로 notify한다.
         *
         * @param value
         */
        public void setValue(int value) {
            System.out.println("ShareThread.setValue");

            synchronized (this) {
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value + "입니다.");
            }

        }

        public void setValue2(int value2) {
            System.out.println("ShareThread.setValue2");

            synchronized (this) {
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
            shareTread.setValue2(10);
        });
        thred1.setName("스레드 1");
        thred2.setName("스레드 2");
        thred1.start();
        thred2.start();

        //특정 블럭이 끝나길 기다림.
        //main쓰레드가 sharedTread를 락을 걸고 wait하면서 대기실로 이동
        //다른 쓰레드들이 sharedThread를 실행하기위해 움직임 (순서 경쟁에 의해 랜덤)
        //다른 쓰레드에서 notify를 호출해주면서 mainThread가 RUNNABLE상태로 이동.
        //하지만 이 경우는 다른 쓰레드에서 객체의 락을 잡아놓은상태로 notify하기때문에 같은 객체에 sync를 잡고있는 main thread가 진행할 수 없다.
        //다른 쓰레드의 락이 끝나고 나서야 main thread가 synchronized블럭을 빠져나올 수 있다.
        synchronized (shareTread){
            shareTread.wait();
        }

        System.out.println("End");
    }
}
