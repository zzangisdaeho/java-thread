package syncronized;

public class SyncMethodWait {

    public static class ShareThread {
        private int value = 0;
        private int value2 = 0;

        /**
         * Synchronized 메서드는 끝나면(sync block을 벗어나면) 자신을 wait하는 객체에 자동으로 notify해준다.
         * @param value
         */
        public synchronized void setValue(int value) {
            System.out.println("ShareThread.setValue");
            this.value = value;
            threadSleep(1000);
            System.out.println("setValue 1 now notify" + Thread.currentThread());
            notify();
            threadSleep(1000);

            System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value + "입니다.");

        }


        public synchronized void setValue2(int value2) {
            System.out.println("ShareThread.setValue2");
            this.value2 = value2;
            threadSleep(1000);
            System.out.println("setValue 2 now notify" + Thread.currentThread());
            notify();
            threadSleep(1000);
            System.out.println(Thread.currentThread().getName() + "의 Value 값은 " + this.value2 + "입니다.");

        }

        public int getValue() {
            return value;
        }


    }

    private static void threadSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ShareThread shareTread = new ShareThread();
        Thread thred1 = new Thread(() -> {
            threadSleep(100);
            shareTread.setValue(100);

        });

        Thread thred2 = new Thread(() -> {
            threadSleep(100);
            shareTread.setValue2(10);
        });
//        thred1.setName("스레드 1");
//        thred2.setName("스레드 2");
        thred1.start();
        thred2.start();

        System.out.println("main thread priority : " + Thread.currentThread().getPriority());
        System.out.println("thread1 priority : " + thred1.getPriority());
        System.out.println("thread2 priority : " + thred2.getPriority());

        //특정 객체의 락이 풀리길 기다림.
        //main쓰레드가 다른 쓰레드들을 실행시키되 더 빠르게 sync블럭에 진입(다른쓰레드가 syncBlock에 들어가기전 약간 sleep시켰기때문) - ShareThread를 lock함
        //main쓰레드가 wait하면서 대기실로 이동 (WAITING 상태로 이동)
        //객체 lock이 풀리면서, 다른 쓰레드들이 sharedThread를 실행하기위해 움직임 (순서 경쟁에 의해 랜덤)
        //다른 쓰레드에서 notify를 호출해주면서 mainThread가 RUNNABLE상태로 이동. (이때 Runnable로 전환되는것에 우선순위를 주는것인진 모르겠는데 선순위로 들어간다)
        //하지만 이 경우는 다른 쓰레드에서 객체의 락을 잡아놓은상태로 notify하기때문에 같은 객체에 sync를 잡고있는 main thread가 진행할 수 없다.
        //다른 쓰레드의 락이 끝나고 나서야 main thread가 synchronized블럭안에서 실행하며을 빠져나올 수 있다.
        synchronized (shareTread){
            System.out.println("wait in" + Thread.currentThread());
            shareTread.wait();
            System.out.println("wait out");
        }

        System.out.println("End");
    }
}
