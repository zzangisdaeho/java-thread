package syncronized;

public class ThreadSyncTest2 {

    public static void main(String[] args) {
        Account2 account = new Account2(1000);

        Thread thread1 = new Thread(new MyThread2(account));
        Thread thread2 = new Thread(new MyThread3(account));

        thread1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();
    }
}

class Account2 {
    int amount;

    public Account2(int amount) {
        this.amount = amount;
    }

    /**
     * 전달받은 금액을 출금하기 위해 메서드에 락을 걸고 들어왔으나, 가지고 있는 잔고보다 출금하려는 금액이 더 많은 상황이다
     * 그러므로 출금하지 못하고, Account 객체의 wait()를 호출한다
     * withdraw 에 걸고 있는 락이 풀리게 된다 - Account객체의 락이 풀린다.
     * 쓰레드는 WAITING 상태로 들어가게 된다
     * 쓰레드는 Account 객체 인스턴스의 대기실(waiting room)에서 기다리게 된다
     * deposit 메서드가 호출되며 금액이 채워지고, notifyAll()을 호출한다
     * Account 객체 인스턴스의 대기실에 있는 모든 쓰레드들을 깨운다
     * 깨어난 쓰레드는 2번의 위치에서 다시 흐름을 이어가게 되는데, while 문이므로 다시 출금 가능한지 검사하게 된다
     * if문 대신 while문을 사용한 이유
     * @param money
     */
    public synchronized void withdraw(int money) {
        System.out.println("출금 시작");
        while(amount < money) { // 1
            System.out.println("잔액 부족");
            try {
                wait(); // 2
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        amount -= money;
        System.out.println("출금 후 잔액 : " + amount);
    }

    public synchronized void deposit(int money) {
        System.out.println("입금 시작");
        amount += money;
        System.out.println("입금 후 잔액 : " + amount);
        notifyAll(); // 3
    }

    public int getAmount() {
        return amount;
    }
}

class MyThread2 implements Runnable {
    private Account2 account;

    public MyThread2(Account2 account) {
        this.account = account;
    }

    @Override
    public void run() {
        account.withdraw(2000);

    }
}

class MyThread3 implements Runnable {
    private Account2 account;

    public MyThread3(Account2 account) {
        this.account = account;
    }

    @Override
    public void run() {
        account.deposit(1000);

    }
}
