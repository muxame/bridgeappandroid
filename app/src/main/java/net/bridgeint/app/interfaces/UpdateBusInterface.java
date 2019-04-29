package net.bridgeint.app.interfaces;

public class UpdateBusInterface {

    public String getShopperId() {
        return shopperId;
    }

    public void setShopperId(String shopperId) {
        this.shopperId = shopperId;
    }

    private String shopperId;


    public UpdateBusInterface(String shopperId) {
        this.shopperId = shopperId;
    }
}
