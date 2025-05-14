package example;

import io.gatling.javaapi.core.*;


import static io.gatling.javaapi.core.CoreDsl.csv;

public class FeederUtil {

    /**
     * Creates a feeder that loads names from a CSV file
     *
     * @return Feeder with name data
     */



    public static FeederBuilder<String> nameCsvFeeder() {
        return csv("data/transaction.csv").circular(); // ✅ this will loop forever
    }
}
