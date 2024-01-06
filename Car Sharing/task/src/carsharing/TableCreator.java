package carsharing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    public static void createTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {
//            statement.execute("DROP TABLE IF EXISTS CUSTOMER");
//            statement.execute("DROP TABLE IF EXISTS CAR");
//            statement.execute("DROP TABLE IF EXISTS COMPANY");

            // Create the COMPANY table
            String createTableCompany = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) UNIQUE NOT NULL" +
                    ")";
            statement.executeUpdate(createTableCompany);

            String createTableCar = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) UNIQUE NOT NULL," +
                    "COMPANY_ID INT NOT NULL," +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)" +
                    ")";
            statement.executeUpdate(createTableCar);

            String createTableCustomer = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) UNIQUE NOT NULL," +
                    "RENTED_CAR_ID INT," +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)" +
                    ")";
            statement.executeUpdate(createTableCustomer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
