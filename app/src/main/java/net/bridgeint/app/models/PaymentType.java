package net.bridgeint.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentType implements Parcelable {

    private String paymentType;
    private String comment;

    public PaymentType() {

    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PaymentType(Parcel in) {
        paymentType = in.readString();
        comment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paymentType);
        dest.writeString(comment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentType> CREATOR = new Creator<PaymentType>() {
        @Override
        public PaymentType createFromParcel(Parcel in) {
            return new PaymentType(in);
        }

        @Override
        public PaymentType[] newArray(int size) {
            return new PaymentType[size];
        }
    };
}
