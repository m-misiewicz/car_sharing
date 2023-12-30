package carsharing;

public class Car {

    private final String name;

    private final int ID;

    public Car(int ID, String name) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

}
