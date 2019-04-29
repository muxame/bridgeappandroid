package net.bridgeint.app.interfaces;

public class FeeBusInterface {

    public String getShopperId() {
        return shopperId;
    }

    public void setShopperId(String shopperId) {
        this.shopperId = shopperId;
    }

    private String shopperId;


    public FeeBusInterface(String shopperId) {
        this.shopperId = shopperId;
    }
}
