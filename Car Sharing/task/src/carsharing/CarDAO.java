package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CarDAO {

    void addCar(String name, int companyID) throws SQLException;
    List<Car> getCarsByCompany(int companyID) throws SQLException;
    Car findById(int ID) throws SQLException;

}
