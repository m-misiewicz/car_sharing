package carsharing;

public class Customer {

    private final String name;

    public int getID() {
        return ID;
    }

    private final int ID;

    private Integer rentedCarID = null;

    public Customer(int ID, String name) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }
}
