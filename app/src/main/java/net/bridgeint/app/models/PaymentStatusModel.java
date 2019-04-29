package net.bridgeint.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PaymentStatusModel implements Parcelable{

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public ArrayList<ServicesModel> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServicesModel> services) {
        this.services = services;
    }

    private String payment_status;

    private ArrayList<ServicesModel> services;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payment_status);
        dest.writeTypedList(this.services);
    }

    public PaymentStatusModel() {
    }

    protected PaymentStatusModel(Parcel in) {
        this.payment_status = in.readString();
        this.services = in.createTypedArrayList(ServicesModel.CREATOR);
    }

    public static final Creator<PaymentStatusModel> CREATOR = new Creator<PaymentStatusModel>() {
        @Override
        public PaymentStatusModel createFromParcel(Parcel source) {
            return new PaymentStatusModel(source);
        }

        @Override
        public PaymentStatusModel[] newArray(int size) {
            return new PaymentStatusModel[size];
        }
    };
}
