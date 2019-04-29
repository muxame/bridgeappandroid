package net.bridgeint.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import net.bridgeint.app.responces.packages.Package;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DeviceBee on 8/21/2017.
 */

public class ApplicationModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("information")
    @Expose
    private String information;

    @SerializedName("howToApply")
    @Expose
    private String howToApply;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("lng")
    @Expose
    private Double lng;

    @SerializedName("admissionOpened")
    @Expose
    private boolean admissionOpened;

    @SerializedName("isFeatured")
    @Expose
    private boolean isFeatured;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("updated")
    @Expose
    private String updated;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("degree")
    @Expose
    private String degree;

    @SerializedName("course")
    @Expose
    private String course;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("comments")
    @Expose
    private String comments;

    private String applyId;

    protected ApplicationModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        state = in.readString();
        description = in.readString();
        information = in.readString();
        howToApply = in.readString();
        address = in.readString();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
        admissionOpened = in.readByte() != 0;
        isFeatured = in.readByte() != 0;
        image = in.readString();
        icon = in.readString();
        created = in.readString();
        updated = in.readString();
        city = in.readString();
        country = in.readString();
        degree = in.readString();
        course = in.readString();
        status = in.readString();
        comments = in.readString();
        applyId = in.readString();
        application = in.createTypedArrayList(UploadModel.CREATOR);
        Packages = in.createTypedArrayList(Package.CREATOR);
        payment_status = in.createTypedArrayList(PaymentStatusModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeString(state);
        dest.writeString(description);
        dest.writeString(information);
        dest.writeString(howToApply);
        dest.writeString(address);
        if (lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lat);
        }
        if (lng == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lng);
        }
        dest.writeByte((byte) (admissionOpened ? 1 : 0));
        dest.writeByte((byte) (isFeatured ? 1 : 0));
        dest.writeString(image);
        dest.writeString(icon);
        dest.writeString(created);
        dest.writeString(updated);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(degree);
        dest.writeString(course);
        dest.writeString(status);
        dest.writeString(comments);
        dest.writeString(applyId);
        dest.writeTypedList(application);
        dest.writeTypedList(Packages);
        dest.writeTypedList(payment_status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ApplicationModel> CREATOR = new Creator<ApplicationModel>() {
        @Override
        public ApplicationModel createFromParcel(Parcel in) {
            return new ApplicationModel(in);
        }

        @Override
        public ApplicationModel[] newArray(int size) {
            return new ApplicationModel[size];
        }
    };

    public ArrayList<UploadModel> getApplication() {
        return application;
    }

    public void setApplication(ArrayList<UploadModel> application) {
        this.application = application;
    }

    private ArrayList<UploadModel> application;
    private ArrayList<Package> Packages;

    public ArrayList<PaymentStatusModel> getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(ArrayList<PaymentStatusModel> payment_status) {
        this.payment_status = payment_status;
    }

    private ArrayList<PaymentStatusModel> payment_status;

    public ArrayList<Package> getPackages() {
        return Packages;
    }

    public void setPackages(ArrayList<Package> packages) {
        Packages = packages;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getHowToApply() {
        return howToApply;
    }

    public void setHowToApply(String howToApply) {
        this.howToApply = howToApply;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public boolean isAdmissionOpened() {
        return admissionOpened;
    }

    public void setAdmissionOpened(boolean admissionOpened) {
        this.admissionOpened = admissionOpened;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getApplyId() {
        return applyId;
    }

    public ApplicationModel() {
    }

}
