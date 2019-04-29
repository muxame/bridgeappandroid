package net.bridgeint.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class ServicesModel implements Parcelable{
    /**
     * services_id : 295
     * user_id : 32041
     * applies_id : 1983
     * packages_id : 5
     * name : Visa appointment booking
     * price : 99
     * qty : 1
     * tot_price : 99
     * created : 2018-10-03 09:38:29
     */

    private String services_id;
    private String user_id;
    private String applies_id;
    private String packages_id;
    private String name;
    private String name_ar;

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getName_rs() {
        return name_rs;
    }

    public void setName_rs(String name_rs) {
        this.name_rs = name_rs;
    }

    private String name_rs;
    private String price;
    private String qty;
    private String tot_price;
    private String created;

    public static ServicesModel objectFromData(String str) {

        return new Gson().fromJson(str, ServicesModel.class);
    }

    public String getServices_id() {
        return services_id;
    }

    public void setServices_id(String services_id) {
        this.services_id = services_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getApplies_id() {
        return applies_id;
    }

    public void setApplies_id(String applies_id) {
        this.applies_id = applies_id;
    }

    public String getPackages_id() {
        return packages_id;
    }

    public void setPackages_id(String packages_id) {
        this.packages_id = packages_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTot_price() {
        return tot_price;
    }

    public void setTot_price(String tot_price) {
        this.tot_price = tot_price;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public ServicesModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.services_id);
        dest.writeString(this.user_id);
        dest.writeString(this.applies_id);
        dest.writeString(this.packages_id);
        dest.writeString(this.name);
        dest.writeString(this.name_ar);
        dest.writeString(this.name_rs);
        dest.writeString(this.price);
        dest.writeString(this.qty);
        dest.writeString(this.tot_price);
        dest.writeString(this.created);
    }

    protected ServicesModel(Parcel in) {
        this.services_id = in.readString();
        this.user_id = in.readString();
        this.applies_id = in.readString();
        this.packages_id = in.readString();
        this.name = in.readString();
        this.name_ar = in.readString();
        this.name_rs = in.readString();
        this.price = in.readString();
        this.qty = in.readString();
        this.tot_price = in.readString();
        this.created = in.readString();
    }

    public static final Creator<ServicesModel> CREATOR = new Creator<ServicesModel>() {
        @Override
        public ServicesModel createFromParcel(Parcel source) {
            return new ServicesModel(source);
        }

        @Override
        public ServicesModel[] newArray(int size) {
            return new ServicesModel[size];
        }
    };
}
