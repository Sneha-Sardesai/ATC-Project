package model;

public class Controller implements Persistable {
    private int controllerId;
    private String name;

    public Controller(int controllerId, String name) {
        this.controllerId = controllerId;
        this.name = name;
    }

    public int getControllerId() {
        return controllerId;
    }

    public String getName() {
        return name;
    }

    // Persistable interface implementation
    @Override
    public int getId() { return controllerId; }

    @Override
    public void setId(int id) { this.controllerId = id; }
}
