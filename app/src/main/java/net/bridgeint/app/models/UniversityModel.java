package net.bridgeint.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DeviceBee on 8/16/2017.
 */

public class UniversityModel implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("state")
    @Expose
    private String state;

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

    @SerializedName("image2")
    @Expose
    private String image2;

    @SerializedName("image3")
    @Expose
    private String image3;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("youtubeVideo")
    @Expose
    private String youtubeVideo;

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

    @SerializedName("countryId")
    @Expose
    private String countryId;
    @SerializedName("tuition_fees")
    @Expose
    private String tuition_fees;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("average_tuition")
    @Expose
    private String average_tuition;
    @SerializedName("acceptance_rate")
    @Expose
    private String acceptance_rate;
    @SerializedName("students")
    @Expose
    private String students;
    @SerializedName("is_partner")
    @Expose
    private String is_partner;

    public String getTuition_fees() {
        return tuition_fees;
    }

    public void setTuition_fees(String tuition_fees) {
        this.tuition_fees = tuition_fees;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAverage_tuition() {
        return average_tuition;
    }

    public void setAverage_tuition(String average_tuition) {
        this.average_tuition = average_tuition;
    }

    public String getAcceptance_rate() {
        return acceptance_rate;
    }

    public void setAcceptance_rate(String acceptance_rate) {
        this.acceptance_rate = acceptance_rate;
    }

    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    public String getIs_partner() {
        return is_partner;
    }

    public void setIs_partner(String is_partner) {
        this.is_partner = is_partner;
    }

    public ArrayList<UploadModel> getApplication() {
        return application;
    }

    public void setApplication(ArrayList<UploadModel> application) {
        this.application = application;
    }

    private ArrayList<UploadModel> application;

    public UniversityModel(String title, String address) {
        this.title = title;
        this.address = address;
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

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getYoutubeVideo() {
        return youtubeVideo;
    }
}
