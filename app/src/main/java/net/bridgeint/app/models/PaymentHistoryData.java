package net.bridgeint.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentHistoryData implements Parcelable{

    /**
     * services_id : 358
     * user_id : 32038
     * applies_id : 1998
     * packages_id : 5
     * name : Visa appointment booking
     * name_rs : Бронирование визы
     * name_ar : حجز موعد التأشيرة
     * price : 99
     * qty : 1
     * tot_price : 99
     * created : 2018-10-08 13:32:23
     */

    private int services_id;
    private int user_id;
    private int applies_id;
    private int packages_id;
    private String name;
    private String name_rs;
    private String name_ar;
    private int price;
    private int qty;
    private int tot_price;
    private String created;

    protected PaymentHistoryData(Parcel in) {
        services_id = in.readInt();
        user_id = in.readInt();
        applies_id = in.readInt();
        packages_id = in.readInt();
        name = in.readString();
        name_rs = in.readString();
        name_ar = in.readString();
        price = in.readInt();
        qty = in.readInt();
        tot_price = in.readInt();
        created = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(services_id);
        dest.writeInt(user_id);
        dest.writeInt(applies_id);
        dest.writeInt(packages_id);
        dest.writeString(name);
        dest.writeString(name_rs);
        dest.writeString(name_ar);
        dest.writeInt(price);
        dest.writeInt(qty);
        dest.writeInt(tot_price);
        dest.writeString(created);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentHistoryData> CREATOR = new Creator<PaymentHistoryData>() {
        @Override
        public PaymentHistoryData createFromParcel(Parcel in) {
            return new PaymentHistoryData(in);
        }

        @Override
        public PaymentHistoryData[] newArray(int size) {
            return new PaymentHistoryData[size];
        }
    };

    public int getServices_id() {
        return services_id;
    }

    public void setServices_id(int services_id) {
        this.services_id = services_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getApplies_id() {
        return applies_id;
    }

    public void setApplies_id(int applies_id) {
        this.applies_id = applies_id;
    }

    public int getPackages_id() {
        return packages_id;
    }

    public void setPackages_id(int packages_id) {
        this.packages_id = packages_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_rs() {
        return name_rs;
    }

    public void setName_rs(String name_rs) {
        this.name_rs = name_rs;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTot_price() {
        return tot_price;
    }

    public void setTot_price(int tot_price) {
        this.tot_price = tot_price;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
