package service;

public class ControllerSession {

    private int controllerId;
    private String controllerName;

    public ControllerSession(int controllerId, String controllerName) {
        this.controllerId = controllerId;
        this.controllerName = controllerName;
    }

    public int getControllerId() {
        return controllerId;
    }

    public String getControllerName() {
        return controllerName;
    }
}