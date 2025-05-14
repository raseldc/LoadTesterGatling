package example;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;


import static io.gatling.javaapi.core.CheckBuilder.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class SavingsTransactionSimulation extends Simulation {
    private static final String DB_URL = "jdbc:postgresql://localhost:5433/amar_hishab";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "postgres";
    // Pre-load account IDs to avoid database queries during the test
    private static final List<Long> ACCOUNT_IDS = new ArrayList<>();
    static {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT reference_id FROM savings_account LIMIT 2000")) {

            while (rs.next()) {
                ACCOUNT_IDS.add(rs.getLong("reference_id"));
            }

            if (ACCOUNT_IDS.isEmpty()) {
                // Fallback if no records found
                ACCOUNT_IDS.add(97232261L);
            }
            System.out.printf("Loaded %d account IDs from the database.%n", ACCOUNT_IDS.size());
        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback if query fails
            ACCOUNT_IDS.add(97232261L);
        }
    }

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // 🔁 replace with your target host
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private String getPayload(String uuid, long referenceId) {
        return String.format("""
            {
            "id": 1,
             "officeId": 1023,
             "referenceId": %d,
             "tracerId": "%s",
             "savingsAccountId": %d,
             "businessDate": "2025-04-24",
             "projectId": 1,
             "groupId": 629160,
             "memberId": 35606754,
             "transactionDate": "2025-04-24",
             "transactionInitiator": 9007199254740991,
             "transactionType": 3,
             "accountNo": "C0169503701",
             "accountName": "Samsun Nahar",
             "previousBalance": 0,
             "savingsBalance": 0,
             "depositAmount": 10,
             "withdrawalAmount": 0,
             "particulars": "Monthly deposit",
             "referenceSavingAccountId": 0,
             "transactionBy": 9007199254740991,
             "verifiedBy": 9007199254740991,
             "paymentMode": "CASH",
             "transactionFrom": "ERP"
            }
            """, referenceId, uuid, referenceId);
    }

    ScenarioBuilder scn = scenario("POST user with UUID and name")
            .feed(FeederUtil.nameCsvFeeder())
            .exec(session -> {
                String uuid = UUID.randomUUID().toString();
                // Get a random account ID from our preloaded list
                long savingsAccountId = ACCOUNT_IDS.get(ThreadLocalRandom.current().nextInt(ACCOUNT_IDS.size()));
                return session.set("uuid", uuid)
                        .set("savingsAccountId", savingsAccountId);

            })
            .exec(
                    http("POST User Transaction")
                            .post("/api/amar-hishab/transaction")
                            .body(StringBody(session -> getPayload(
                                    session.getString("uuid"),
                                    session.getLong("savingsAccountId")
                            )))
                            .asJson()
                            .check(status().in(200,201).saveAs("responseStatus"))
                            .check(bodyString().saveAs("responseBody"))

            )  ;
    {
        setUp(
                scn.injectOpen(
                        rampUsers(2000).during(Duration.ofSeconds(30)),  // Ramp up to 2000 users over 30 seconds
                        constantUsersPerSec(100).during(Duration.ofMinutes(1)).randomized()  // Steady phase
                )
        ).protocols(httpProtocol)
                .maxDuration(Duration.ofMinutes(1))
                .assertions(
//                        global().responseTime().percentile3().lt(100),
//                        global().successfulRequests().percent().gt(95.0),
//                        global().requestsPerSec().gt(50.0)  // Added assertion for requests per second
                ) ;
    }

    /*{
        setUp(
                scn.injectOpen(
                        rampUsers(2000).during(Duration.ofSeconds(30)),
                        constantUsersPerSec(1500).during(Duration.ofMinutes(2))
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().responseTime().percentile3().lt(100),
                        global().successfulRequests().percent().gt(95.0)
                );
    }*/


}
