package net.bridgeint.app.models;

import net.bridgeint.app.responces.GenericResponce;

import java.util.ArrayList;

public class PaymentHistoryResponse extends GenericResponce {

    private ArrayList<PaymentHistoryData> data;

    public ArrayList<PaymentHistoryData> getData() {
        return data;
    }

    public void setData(ArrayList<PaymentHistoryData> data) {
        this.data = data;
    }


}
