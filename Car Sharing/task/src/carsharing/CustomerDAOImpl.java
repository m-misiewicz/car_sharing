package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements  CustomerDAO {

    private final Connection connection;

    public CustomerDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCustomer(String name, Integer rentedCarID) throws SQLException {
        String sql = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            if (rentedCarID != null) {
                statement.setInt(2, rentedCarID);
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            statement.executeUpdate();
        }
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMER";
        try (ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                customers.add(new Customer(id, name));
            }
        }
        return customers;
    }

    @Override
    public Customer findById(Integer customerID) throws SQLException {
        String sql = "SELECT * FROM CUSTOMER WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    return new Customer(id, name);
                }
            }
        }
        return null;
    }

    @Override
    public Integer findRentedCar(Integer customerID) throws SQLException {
        String sql = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("RENTED_CAR_ID");
                }
            }
        }
        return null;
    }

}
