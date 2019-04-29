package net.bridgeint.app.responces.packages;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 17/01/2018.
 */

public class Package_ implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public Package_(Integer id, String name, String name_rs, String name_ar, Float price, Integer status, String iconImage, String created, String flag, String is_disable, String qty, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.name_rs = name_rs;
        this.name_ar = name_ar;
        this.price = price;
        this.status = status;
        this.iconImage = iconImage;
        this.created = created;
        this.flag = flag;
        this.is_disable = is_disable;
        this.qty = qty;
        this.isChecked = isChecked;
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

    @SerializedName("name_rs")
    @Expose
    private String name_rs;
    @SerializedName("name_ar")
    @Expose
    private String name_ar;
    @SerializedName("price")
    @Expose
    private Float price;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("is_disable")
    @Expose
    private String is_disable;

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    @SerializedName("qty")
    @Expose
    private String qty;

    public String getIs_disable() {
        return is_disable;
    }

    public void setIs_disable(String is_disable) {
        this.is_disable = is_disable;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.name_rs);
        dest.writeString(this.name_ar);
        dest.writeValue(this.price);
        dest.writeValue(this.status);
        dest.writeString(this.iconImage);
        dest.writeString(this.created);
        dest.writeString(this.flag);
        dest.writeString(this.is_disable);
        dest.writeString(this.qty);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    protected Package_(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.name_rs = in.readString();
        this.name_ar = in.readString();
        this.price = (Float) in.readValue(Float.class.getClassLoader());
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.iconImage = in.readString();
        this.created = in.readString();
        this.flag = in.readString();
        this.is_disable = in.readString();
        this.qty = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public static final Creator<Package_> CREATOR = new Creator<Package_>() {
        @Override
        public Package_ createFromParcel(Parcel source) {
            return new Package_(source);
        }

        @Override
        public Package_[] newArray(int size) {
            return new Package_[size];
        }
    };
}
