//package example;
//
//import io.gatling.app.Gatling;
//import io.gatling.core.config.GatlingPropertiesBuilder;
//
//public class GatlingRunner {
//    public static void main(String[] args) {
//        GatlingPropertiesBuilder properties = new GatlingPropertiesBuilder()
//                .simulationClass("example.SavingsTransactionSimulation")
//                .resultsDirectory("target/gatling-results")
//                .resourcesDirectory("src/test/resources");
//
//        Gatling.fromMap(properties.build());
//    }
//}
