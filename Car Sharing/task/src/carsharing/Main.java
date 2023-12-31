package carsharing;

import java.util.List;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        String databaseFileName = getDatabaseFileName(args);
        String databaseURL = "jdbc:h2:./src/carsharing/db/" + databaseFileName;

        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            connection.setAutoCommit(true);
            TableCreator.createTables(connection);
            CompanyDAO companyDAO = new CompanyDAOImpl(connection);
            CarDAO carDAO = new CarDAOImpl(connection);
            CustomerDAO customerDAO = new CustomerDAOImpl(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                displayMainMenu();
                int mainChoice = getUserChoice(scanner);

                switch (mainChoice) {
                    case 1:
                        handleManagerMenu(scanner, companyDAO, carDAO);
                        break;
                    case 2:
                        handleCustomerLoginMenu(scanner, customerDAO, companyDAO, carDAO);
                        break;
                    case 3:
                        handleCustomerCreation(scanner, customerDAO);
                        break;
                    case 0:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose a valid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleCustomerCreation(Scanner scanner, CustomerDAO customerDAO) {
        scanner.nextLine();
        System.out.print("Enter the customer name: ");
        String customerName = scanner.nextLine();
        try {
            customerDAO.addCustomer(customerName, null);
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            System.out.println("Error creating the customer: " + e.getMessage());
        }
    }

    private static void handleCustomerLoginMenu(Scanner scanner, CustomerDAO customerDAO, CompanyDAO companyDAO, CarDAO carDAO) throws SQLException {
        int numberCustomers = displayCustomerListMenu(customerDAO.findAll());
        if (numberCustomers == 0) {
            return;
        }
        int customerChoice = getUserChoice(scanner);
        if (customerChoice == 0) {
            return;
        }
        Customer customer = customerDAO.findById(customerChoice);
        handleCustomerMenu(scanner, customer, customerDAO, companyDAO, carDAO);
    }

    private static void handleCustomerMenu(Scanner scanner, Customer customer, CustomerDAO customerDAO, CompanyDAO companyDAO, CarDAO carDAO) throws SQLException {
        while (true) {
            displayCustomerMenu();
            int customerChoice = getUserChoice(scanner);
            switch (customerChoice) {
                case 1:
                    rentCar(scanner, customer, customerDAO, companyDAO, carDAO);
                    break;
                case 2:
                    returnCar(customer, customerDAO, carDAO);
                    break;
                case 3:
                    checkRentStatus(customer, customerDAO, companyDAO, carDAO);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }

        }
    }

    private static void returnCar(Customer customer, CustomerDAO customerDAO, CarDAO carDAO) throws SQLException {
        Integer rentedCarID = customerDAO.findRentedCarID(customer.getID());
        Car rentedCar = carDAO.findById(rentedCarID);
        if (rentedCar != null) {
            customerDAO.updateRentedCar(customer.getID(), null);
            System.out.println("You've returned a rented car!");
            carDAO.updateCarStatus(rentedCarID, false);
        } else {
            System.out.println("You didn't rent a car!");
        }
    }

    private static void rentCar(Scanner scanner, Customer customer, CustomerDAO customerDAO, CompanyDAO companyDAO, CarDAO carDAO) throws SQLException {
        int customerID = customer.getID();
        int carID = customerDAO.findRentedCarID(customerID);
        Car rentedCar = carDAO.findById(carID);
        if (rentedCar != null) {
            System.out.println("You've already rented a car!");
            return;
        }
        displayCompanyList(companyDAO.findAll());
        int customerChoice = getUserChoice(scanner);
        if (customerChoice == 0) {
            return;
        }
        Company company = companyDAO.findById(customerChoice);
        carChoiceMenu(scanner, customer, customerDAO, company, carDAO);

    }

    private static void carChoiceMenu(Scanner scanner, Customer customer, CustomerDAO customerDAO, Company company, CarDAO carDAO) throws SQLException {
        List<Car> carList= carDAO.getCarsByCompany(company.getID());
        displayCarList(carList, true);
        int customerChoice = getUserChoice(scanner);
        if (customerChoice == 0) {
            return;
        }
        Car car = carList.get(customerChoice - 1);
        customerDAO.updateRentedCar(customer.getID(), car.getID());
        carDAO.updateCarStatus(car.getID(), true);
        System.out.println("You rented '" + car.getName() + "'");
    }

    private static void checkRentStatus(Customer customer, CustomerDAO customerDAO, CompanyDAO companyDAO, CarDAO carDAO) throws SQLException {

        Integer rentedCarID = customerDAO.findRentedCarID(customer.getID());
        Integer companyID = carDAO.getCompanyIdByCarId(rentedCarID);
        Car rentedCar = carDAO.findById(rentedCarID);
        if (rentedCar != null) {
            System.out.println("Your rented car:");
            System.out.println(rentedCar.getName());
            System.out.println("Company:");
            System.out.println(companyDAO.findById(companyID).getName());
        } else {
            System.out.println("You didn't rent a car!");
        }
    }

    private static void displayCustomerMenu() {
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
    }

    private static int displayCustomerListMenu(List<Customer> customers) {
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return 0;
        }
        else {
            System.out.println("Customer list:");
            for (Customer customer : customers) {
                System.out.println(customer.getID() + ". " + customer.getName());
            }
            System.out.println("0. Back");
        }
        return customers.size();
    }

    private static String getDatabaseFileName(String[] args) {
        // Check if the database file name is provided as a command-line argument
        if (args.length > 1 && args[0].equals("-databaseFileName")) {
            return args[1];
        } else {
            // If not provided, use a default name or any desired name
            return "defaultDatabase";
        }
    }


    private static void handleManagerMenu(Scanner scanner, CompanyDAO companyDAO, CarDAO carDAO) throws SQLException {
        while (true) {
            displayManagerMenu();
            int managerChoice = getUserChoice(scanner);
            switch (managerChoice) {
                case 1:
                    handleCompanyListMenu(scanner, companyDAO, carDAO);
                    break;
                case 2:
                    createNewCompany(scanner, companyDAO);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
    }

    private static void displayManagerMenu() {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }


    private static void handleCompanyListMenu(Scanner scanner, CompanyDAO companyDAO, CarDAO carDAO) throws SQLException {
            int numberOfCompanies = displayCompanyList(companyDAO.findAll());
            if (numberOfCompanies == 0)  {
                return;
            }
            int companyChoice = getUserChoice(scanner);
            if (companyChoice == 0) {
                return;
            }
            Company company = companyDAO.findById(companyChoice);
            handleCompanyMenu(company, scanner, carDAO);
    }

    private static void handleCompanyMenu(Company company, Scanner scanner, CarDAO carDAO) throws SQLException {
        String companyName = company.getName();
        System.out.println("'" + companyName + "' company");
        while (true) {
            displayCompanyMenu();
            int userChoice = getUserChoice(scanner);
            switch (userChoice) {
                case 1:
                    displayCarList(carDAO.getCarsByCompany(company.getID()), false);
                    break;
                case 2:
                    createNewCar(scanner, carDAO, company.getID());
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }

    private static void displayCarList(List<Car> cars, boolean custMode) {
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        }
        else {
            if (custMode) {
                System.out.println("Choose a car:");
            } else {
                System.out.println("Car list:");
            }
            int count = 1;
            for (Car car : cars) {
                System.out.println(count + ". " + car.getName());
                count++;
            }
            if (custMode) {
                System.out.println("0. Back");
            }
        }

    }

    private static void displayCompanyMenu() {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    private static int displayCompanyList(List<Company> companies) {
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return 0;
        }
        else {
            System.out.println("Choose a company:");
            for (Company company : companies) {
                System.out.println(company.getID() + ". " + company.getName());
            }
            System.out.println("0. Back");
            return companies.size();
        }
    }

    private static void createNewCompany(Scanner scanner, CompanyDAO companyDAO) {
        scanner.nextLine();
        System.out.print("Enter the company name: ");
        String companyName = scanner.nextLine();
        try {
            companyDAO.addCompany(companyName);
            System.out.println("The company was created!");
        } catch (SQLException e) {
            System.out.println("Error creating the company: " + e.getMessage());
        }
    }

    private static void createNewCar(Scanner scanner, CarDAO carDAO, int companyID) {
        scanner.nextLine();
        System.out.print("Enter the car name: ");
        String carName = scanner.nextLine();
        try {
            carDAO.addCar(carName, companyID);
            System.out.println("The car was added!");
        } catch (SQLException e) {
            System.out.println("Error creating the company: " + e.getMessage());
        }
    }

    private static int getUserChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}