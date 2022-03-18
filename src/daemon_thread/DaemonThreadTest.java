package daemon_thread;

public class DaemonThreadTest {

    public static void main(String[] args) {
        Thread deamon = new Thread(()->{
            while ( true ) {
                System.out.println(" 데몬 스레드가 실행 중입니다.");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

//        deamon.setDaemon(false);
        deamon.setDaemon(true);
        deamon.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("메인 끝");
    }
}
