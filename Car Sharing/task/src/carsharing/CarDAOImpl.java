package carsharing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAOImpl implements CarDAO {

    private final Connection connection;

    public CarDAOImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void addCar(String name, int companyID) throws SQLException {
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, companyID);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Car> getCarsByCompany(int companyID) throws SQLException {

        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID = ? AND IS_RENTED = FALSE";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, companyID);  // Set the parameter dynamically

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    cars.add(new Car(id, name));
                }
            }
        }
        return cars;
    }

    @Override
    public Car findById(int carID) throws SQLException {
        String sql = "SELECT * FROM CAR WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, carID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    return new Car(id, name);
                }
            }
        }
        return null;
    }

    @Override
    public Integer getCompanyIdByCarId(int carId) throws SQLException {

        String sql = "SELECT COMPANY_ID FROM CAR WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, carId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("COMPANY_ID");
                }
            }
        }

        return null;
    }

    @Override
    public void updateCarStatus(int carId, boolean isRented) throws SQLException {
        String sql = "UPDATE CAR SET IS_RENTED = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isRented);
            statement.setInt(2, carId);
            statement.executeUpdate();
        }
    }


}

