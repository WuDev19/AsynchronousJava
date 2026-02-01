package executor_service_and_future;

public class MyRunnable implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < 1000; i++) {
            System.out.println("Lan chay thu " + i);
        }
    }
}
