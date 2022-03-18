package syncronized;

public class ThreadSyncTest {

    public static void main(String[] args) {
        Account account = new Account(1000);

        Thread thread1 = new Thread(new MyThread(account));
        Thread thread2 = new Thread(new MyThread(account));

        thread1.start();
        thread2.start();
    }
}

class Account {
    int amount;

    public Account(int amount) {
        this.amount = amount;
    }

    // if문을 두 쓰레드가 다 통과하면 -1000 이 나올 수 있음
    public void withdraw(int money) {
        if (amount >= money) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            amount -= money;
        }
    }

//    public synchronized void withdraw(int money) {
//        if (amount >= money) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            amount -= money;
//        }
//    }

//    public void withdraw(int money) {
//        synchronized (this) {
//
//            if (amount >= money) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                amount -= money;
//            }
//        }
//    }

    public int getAmount() {
        return amount;
    }
}

class MyThread implements Runnable {
    private Account account;

    public MyThread(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        account.withdraw(1000);
        System.out.println("잔액 : " + account.getAmount());
    }
}
