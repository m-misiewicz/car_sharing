package carsharing;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CompanyDAOImpl implements CompanyDAO {

    private final Connection connection;

    public CompanyDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCompany(String name) throws SQLException {


        String sql = "INSERT INTO COMPANY (NAME) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
        }
    }

    @Override
    public Company findByName(String companyName) throws SQLException {
        String sql = "SELECT * FROM COMPANY WHERE NAME = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, companyName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    return new Company(id, companyName);
                }
            }
        }
        return null;
    }

    @Override
    public List<Company> findAll() throws SQLException {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM COMPANY";
        try (ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                companies.add(new Company(id, name));
            }
        }
        return companies;
    }

    @Override
    public Company findById(int companyId) throws SQLException {
        String sql = "SELECT * FROM COMPANY WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    return new Company(id, name);
                }
            }
        }
        return null;
    }

}