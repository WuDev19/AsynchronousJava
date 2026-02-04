package executor_service_and_future.custom_thread_pool;

import executor_service_and_future.MyCallable;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

//ArrayBlockingQueue<>(): Tạo ra kích thước tối đa cho queue dùng ctdl array, buộc phải truyền capacity
//LinkedBlockingQueue<>(): Tạo ra kích thước tối đa cho queue dùng ctdl linked list, nếu ko truyền capacity thì mặc định là maxInt
//SynchronousQueue<>(): Ko tạo queue, tạo thread luôn
//Custom ThreadFactory để mình debug cho dễ, cấu hình cho thread, ví dụ thread name, ...
@SuppressWarnings("unchecked")
public class CustomThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //cái này khá giống cấu hình @Async trong Spring Boot
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                3, //thread trực thuộc
                5,//tối đa khi vượt quá thread trực thuộc
                60, //thread dư sau khi xong task thì sống trong bao lâu
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(5),
                new ThreadFactory() {
                    final AtomicInteger integer = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("my-thread " + integer.getAndIncrement()); //thread tạo ra sẽ sủ dụng các cấu hình này
                        return thread;
                    }
                },
                new ThreadPoolExecutor.DiscardOldestPolicy() // tối đa thread và đầy queue thì sẽ chạy vào đây
        );

        Future<String> result = threadPoolExecutor.submit(new MyCallable());
        System.out.println(result.get());
        threadPoolExecutor.close();
    }
}
