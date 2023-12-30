package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO {

    void addCustomer(String name, Integer customerID) throws SQLException;

    List<Customer> findAll() throws SQLException;

    Customer findById(int customerID) throws SQLException;
}
