package executor_service_and_future.schedule;

import java.util.concurrent.Callable;

public class MyCallable implements Callable {
    @Override
    public String call() {
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        return String.valueOf(sum);
    }
}
