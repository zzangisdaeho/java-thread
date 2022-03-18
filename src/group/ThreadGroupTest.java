package group;

public class ThreadGroupTest {

    public static void main(String args[]){
        ThreadGroup root = new ThreadGroup("Root_Group");
        ThreadGroup groupChild = new ThreadGroup( root,"myGroup" );

        // root 그룹에 세팅
        Thread root_group = new Thread( root, ()->{
            while ( true ) {
                System.out.println("Root 그룹입니다.");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("Root 그룹 스레드가 종료됩니다.");
                    break;
                }
            }
        });
        // groupChild 그룹에 세팅
        Thread child_group1 = new Thread( groupChild, ()->{
            while ( true ) {
                System.out.println("child_group 그룹의 child_group1입니다.");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("child_group 그룹의 child_group1 종료됩니다.");
                    break;
                }
            }
        });

        // groupChild 그룹에 세팅
        Thread child_group2 = new Thread( groupChild, ()->{
            while ( true ) {
                System.out.println("child_group 그룹의 child_group2입니다.");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("child_group 그룹의 child_group2 종료됩니다.");
                    break;
                }
            }
        });

        root_group.start();
        child_group1.start();
        child_group2.start();


        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //groupChild그룹 일괄 중지
        System.out.println("======= groupChild 일괄 중지 =======");
        groupChild.interrupt();

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //root그룹 일괄 중지
        System.out.println("======= root 일괄 중지 =======");
        root.interrupt();
    }
}
