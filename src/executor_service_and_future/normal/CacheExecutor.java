package executor_service_and_future.normal;

import executor_service_and_future.MyRunnable;

import java.util.concurrent.*;

@SuppressWarnings("unchecked")
public class CacheExecutor {
    public static void main(String[] args) {
//        try (ExecutorService cacheExecutor = Executors.newCachedThreadPool()) {
//            executor_service_and_future.MyCallable myCallable = new executor_service_and_future.MyCallable();
//            Future<String> result = cacheExecutor.submit(myCallable);
//            System.out.println("hihihihi");
//            System.out.println(result.resultNow());//ép trả két quả ngay, nếu task chưa hoàn thành thì ném ra exception
//        }
        //Executors.newCachedThreadPool() sẽ tự quản lý luồng, nếu cần thì sẽ tự tạo luồng mới
        //Executors.newFixedThreadPool(10); tạo tối đa 10 luồng
        //Executors.newSingleThreadExecutor(); chỉ tạo 1 luồng, và tái sử dụng
        try (ExecutorService cacheExecutor = Executors.newCachedThreadPool()) {
            Future<String> result = cacheExecutor.submit(new MyRunnable(), "HIHI");
            System.out.println("hihihihi");
            Thread.sleep(2000);
            System.out.println("hehehehe");
            System.out.println(result.get());
            System.out.println("hohohoho");
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }
}
