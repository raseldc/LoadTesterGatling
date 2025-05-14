package example;

import java.sql.*;
import java.util.*;

public class DbUtils {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "owares2002";

    public static List<Integer> getSavingsAccountIds(int limit) {
        List<Integer> savingsAccountIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM savings_accounts LIMIT " + limit)) {

            while (rs.next()) {
                savingsAccountIds.add(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return savingsAccountIds;
    }
}
