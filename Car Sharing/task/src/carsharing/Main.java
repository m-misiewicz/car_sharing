package carsharing;

import java.util.List;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        String databaseFileName = getDatabaseFileName(args);

        // Construct the database URL
        String databaseURL = "jdbc:h2:./src/carsharing/db/" + databaseFileName;

        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            // Enable auto-commit mode
            connection.setAutoCommit(true);

            // Create the COMPANY table
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
                        // Log in as a manager
                        handleManagerMenu(scanner, companyDAO, carDAO);
                        break;
                    case 2:
                        handleCustomerLoginMenu(scanner, customerDAO);
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

    private static void handleCustomerLoginMenu(Scanner scanner, CustomerDAO customerDAO) throws SQLException {
        int numberCustomers = displayCustomerListMenu(customerDAO.findAll());
        if (numberCustomers == 0) {
            return;
        }
        int customerChoice = getUserChoice(scanner);
        if (customerChoice == 0) {
            return;
        }
        Customer customer = customerDAO.findById(customerChoice);
        handleCustomerMenu(scanner);
        
    }

    private static void handleCustomerMenu(Scanner scanner, CustomerDAO customerDAO) {
        while (true) {
            displayCustomerMenu();
            int customerChoice = getUserChoice(scanner);
            switch (customerChoice) {
                case 1:
                    // Company list

                    break;
                case 2:
                    // Create a company
                    createNewCompany(scanner, companyDAO);
                    break;
                case 0:
                    // Back to the main menu
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }

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
            int count = 1;
            for (Customer customer : customers) {
                System.out.println(customer.getID() + ". " + customer.getName());
                count++;
            }
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
                    // Company list
                    handleCompanyListMenu(scanner, companyDAO, carDAO);
                    break;
                case 2:
                    // Create a company
                    createNewCompany(scanner, companyDAO);
                    break;
                case 0:
                    // Back to the main menu
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
                    displayCarList(carDAO.getCarsByCompany(company.getID()));
                    continue;
                case 2:
                    createNewCar(scanner, carDAO, company.getID());
                    continue;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }



    }

    private static void displayCarList(List<Car> cars) {
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        }
        else {
            System.out.println("Car list:");
            int count = 1;
            for (Car car : cars) {
                System.out.println(count + ". " + car.getName());
                count++;
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
            System.out.println("Choose the company:");
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
//            System.out.println("");
        } catch (SQLException e) {
            System.out.println("Error creating the company: " + e.getMessage());
        }
    }

    private static int getUserChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextInt();
    }

}