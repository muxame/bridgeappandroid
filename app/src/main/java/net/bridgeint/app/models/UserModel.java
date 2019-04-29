package net.bridgeint.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("accessKey")
    @Expose
    private String accessKey;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("lat")
    @Expose
    private Integer lat;

    @SerializedName("lng")
    @Expose
    private Integer lng;

    @SerializedName("notifStatus")
    @Expose
    private Integer notifStatus;

    @SerializedName("accStatus")
    @Expose
    private Integer accStatus;

    @SerializedName("isPhoneVerified")
    @Expose
    private boolean isPhoneVerified;

    @SerializedName("created")
    @Expose
    private String created;



    @SerializedName("fbId")
    @Expose
    private String fbId;

    private boolean isNew;


    public boolean isPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getLat() {
        return lat;
    }

    public void setLat(Integer lat) {
        this.lat = lat;
    }

    public Integer getLng() {
        return lng;
    }

    public void setLng(Integer lng) {
        this.lng = lng;
    }

    public Integer getNotifStatus() {
        return notifStatus;
    }

    public void setNotifStatus(Integer notifStatus) {
        this.notifStatus = notifStatus;
    }

    public Integer getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(Integer accStatus) {
        this.accStatus = accStatus;
    }

    public boolean getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public void setIsPhoneVerified(boolean isPhoneVerified) {
        this.isPhoneVerified = isPhoneVerified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.email);
        dest.writeString(this.accessKey);
        dest.writeString(this.image);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.type);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.country);
        dest.writeString(this.phone);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
        dest.writeValue(this.notifStatus);
        dest.writeValue(this.accStatus);
        dest.writeByte(this.isPhoneVerified ? (byte) 1 : (byte) 0);
        dest.writeString(this.created);
        dest.writeString(this.fbId);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
    }

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.email = in.readString();
        this.accessKey = in.readString();
        this.image = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.type = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.phone = in.readString();
        this.lat = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lng = (Integer) in.readValue(Integer.class.getClassLoader());
        this.notifStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.accStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isPhoneVerified = in.readByte() != 0;
        this.created = in.readString();
        this.fbId = in.readString();
        this.isNew = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}