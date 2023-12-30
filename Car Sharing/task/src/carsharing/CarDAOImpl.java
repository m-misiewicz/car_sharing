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
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID = ?";

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
}

