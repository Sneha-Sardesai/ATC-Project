package model;

public class Controller {
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
}
