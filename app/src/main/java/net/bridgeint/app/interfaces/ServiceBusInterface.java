package net.bridgeint.app.interfaces;

public class ServiceBusInterface {
    public String getShopperId() {
        return shopperId;
    }

    public void setShopperId(String shopperId) {
        this.shopperId = shopperId;
    }

    private String shopperId;


    public ServiceBusInterface(String shopperId) {
        this.shopperId = shopperId;
    }
}
