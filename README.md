# Savings Transaction Simulation with Gatling

This project simulates savings transactions using Gatling, a powerful load testing tool. It's designed to assess the performance and stability of the `amar-hishab` application under heavy load.

## Overview

The simulation performs the following actions:

- Loads account IDs from a PostgreSQL database to avoid querying during the test.
- Sends POST requests to the `/api/amar-hishab/transaction` endpoint with transaction data.
- Uses a CSV feeder (`data/transaction.csv`) to provide names for the transactions.
- Measures response times, success rates, and requests per second.

## Prerequisites

- Java 11 or higher
- Maven
- PostgreSQL database
- Gatling Maven Plugin

## Setup

1.  **Clone the repository:**

    ```bash
    git clone <repository-url>
    cd <project-directory>
    ```

2.  **Configure the database:**

  - Ensure a PostgreSQL database is running.
  - Update the database connection details in `src/test/java/example/SavingsTransactionSimulation.java`:

    ```java
    private static final String DB_URL = "jdbc:postgresql://localhost:5433/amar_hishab";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "postgres";
    ```

3.  **Prepare the CSV feeder:**

  - Create a `data` directory under `src/test/resources`.
  - Create a `transaction.csv` file inside the `data` directory.
  - Add name data to the `transaction.csv` file.

4.  **Install dependencies:**

    ```bash
    ./mvnw clean install
    ```

## Running the Simulation

To run the simulation, use the following Maven command:
```bash
 mvn gatling:test -Dgatling.simulationClass=example.SavingsTransactionSimulation