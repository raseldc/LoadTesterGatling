package example;

import io.gatling.javaapi.core.ChainBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class StatusCounter {

    private static final Map<String, LongAdder> statusCounts = new ConcurrentHashMap<>();



    public static void displayStatusCounts() {
        System.out.println("Response Status Counts:");
        statusCounts.forEach((status, count) ->
                System.out.println("Status " + status + ": " + count.sum()));
    }
}