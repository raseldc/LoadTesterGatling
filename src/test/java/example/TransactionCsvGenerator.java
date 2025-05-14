package example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class TransactionCsvGenerator {

    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("src/test/resources/data/transaction.csv");

        // Write header
        writer.append("uuid\n");

        // Write data rows
        for (int i = 1; i <= 100000; i++) {
            writer.append(UUID.randomUUID().toString()).append("\n");
        }

        writer.flush();
        writer.close();

        System.out.println("transaction.csv generated with 1000 rows.");
    }
}

