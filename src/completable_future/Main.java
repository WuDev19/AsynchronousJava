package completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
- CompletableFuture có thể tạo chaining
- runAsync: chạy bất đồng bộ ko trả về kết quả, giống như runnable
- supplyAsync: chạy bất đồng bộ trả về kết quả, giống như callable
- thenApply: chuyển đổi kiểu dữ liệu trả về của completablefuture trước đó
- thenAccept: giống như .then() trong dart, trả về void, có data callback
- thenCombine: chạy hai future song song, khi cả hai xong thì gộp lại giá trị
- thenCompose: future sau phụ thuộc vào kết quả của future trước, giống flatmapconcat của flow kotlin
- join() giống get() nhưng ko cần xử lý checked exception, nếu có exception thì nó throw
- allOf(): như thenCombine() nhưng dùng cho nhiều hơn 2 future, tất cả phải cùng hoàn thành thì mới có kết quả
- anyOf(): chỉ cần 1 future hoàn thành thì anyOf() sẽ hoàn thành
 */

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        completableFuture.complete(doSomething()); //hoàn thành tác vụ và trả ra kết quả với giá trị truyền vào
        CompletableFuture<String> completableFuture = CompletableFuture
                .runAsync(() -> System.out.println("HIHIHI"))
                .thenApply(unused -> "hihi");
        CompletableFuture<Object> api = CompletableFuture.supplyAsync(() -> {
            try {
                return callApi();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenCompose(Main::callApi2).thenAccept(System.out::println).handle((_, throwable) -> {
            if (throwable != null) {
                return throwable.getMessage();
            }
            return new Void[]{};
        });
        CompletableFuture<Integer> api2 = CompletableFuture.supplyAsync(() -> {
            try {
                return callApi2();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenCompose(Main::callApi2).exceptionally(throwable -> 1000);
        CompletableFuture.supplyAsync(() -> {
            try {
                return callApi();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenCombine(callApi2("100"), (string, integer) -> string + integer).thenAccept(System.out::println);
        CompletableFuture<Integer> a1 = callApi2("100");
        CompletableFuture<String> a2 = callApi3(500);
        CompletableFuture<Integer> a3 = callApi2("1000");
        CompletableFuture<String> all = CompletableFuture.allOf(a1, a2, a3)
                .thenApply(_ -> {
                    Integer data = a1.join(); // join() và get() trong allOf thì ko block code vì trong này chỉ chạy khi tất cả future đều xong
                    String data1 = a2.join();
                    Integer data2 = a3.join();
                    return data + " va " + data1 + " va " + data2;
                });
        System.out.println(all.join());
        System.out.println(completableFuture.join());
        System.out.println(api2.join());
        System.out.println(api.join());
        System.out.println("hello world");
        System.out.println("my world");
        Thread.sleep(5000);
    }

    static String doSomething() throws InterruptedException {
        return "HEhE";
    }

    static String callApi() throws InterruptedException {
        Thread.sleep(2000);
        return "100i";
    }

    static String callApi2() throws InterruptedException {
        Thread.sleep(2000);
        return "20ấ0";
    }

    static CompletableFuture<Integer> callApi2(String data) {
        return CompletableFuture.supplyAsync(() -> Integer.valueOf(data));
    }

    static CompletableFuture<String> callApi3(Integer data) {
        return CompletableFuture.supplyAsync(data::toString);
    }

    static CompletableFuture<Integer> callApi4(String data) {
        return CompletableFuture.supplyAsync(() -> Integer.valueOf(data));
    }

}
