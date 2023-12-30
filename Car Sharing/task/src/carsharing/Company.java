package carsharing;

public class Company {
    private final int ID;
    private final String name;


    public Company(int id, String name) {
        this.ID = id;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

}