package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDAO {
    List<Company> findAll() throws SQLException;
    Company findById(int id) throws SQLException;
    void addCompany(String name) throws SQLException;
}