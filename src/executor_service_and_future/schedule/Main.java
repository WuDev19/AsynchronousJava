package executor_service_and_future.schedule;

import java.util.Random;
import java.util.concurrent.*;

@SuppressWarnings("unchecked")
public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        //cái này là delay giữa các lần lặp, định kì
        //bên trong dùng runnable ko trả về giá trị gì trong runnable nhưng executorService.scheduleWithFixedDelay vẫn trả về Future để có thể hủy task, ...
        ScheduledFuture<?> stringScheduledFuture = executorService.scheduleWithFixedDelay(() -> {
            System.out.println("Hello world");
            try {
                throw new Exception("hihi");
            } catch (Exception e) {
                System.out.println(e.getMessage());  //nếu catch exception thì vẫn sẽ chạy tiếp, ko bị hủy
            }
        }, 1, 3, TimeUnit.SECONDS);
//        Thread.sleep(5000);
        stringScheduledFuture.cancel(true);

        //cái này đám bảo mỗi lần chạy cách đều nhau, ko bị lệch nhịp
        //nếu task nhiều hơn period thì sẽ chờ task xong rồi mới chạy tiếp => bị delay so với lý thuyết
        /* nếu task1 nhiều hơn period thì sẽ đợi task1 hoàn thành, task2 nhanh hơn period và thời gian xong nhanh hơn so với lý thuyết
            thì sẽ đọi đến thời gian tiếp theo trong lý thuyết mới chạy tiếp
        */
        /*
        - nếu task hiện tại kết thúc mà thời gian kết thúc muộn hơn so với lý thuyết thì task sau sẽ chạy ngay lập tức
        */
        ScheduledFuture<?> stringScheduledFuture1 = executorService.scheduleAtFixedRate(() -> {
            boolean check = new Random().nextBoolean();
            try {
                if (check) {
                    Thread.sleep(2000);
                    System.out.println("hehe sau 3s");
                } else {
                    System.out.println("hehe");
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);
//        Thread.sleep(5000);
        stringScheduledFuture1.cancel(true);

        //cái này là chạy 1 lần và trả về kết quả do dùng callable
        //nếu dùng runnable thì cũng tương tự nhưng ko trả về kết quả
        MyCallable myCallable = new MyCallable();
        ScheduledFuture<String> stringScheduledFuture2 = executorService.schedule(myCallable, 2, TimeUnit.SECONDS);
        System.out.println(stringScheduledFuture2.get()); //block thread đợi kết quả, những dòng code đằng sau sẽ đợi dòng này chạy xong mới chạy
        Thread.sleep(10000);
        executorService.close();
    }

}