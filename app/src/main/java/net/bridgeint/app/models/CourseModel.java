package net.bridgeint.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DeviceBee on 8/17/2017.
 */

public class CourseModel {

//"courseType":"G","created":"2017-10-16 14:44:53","updated":"2017-10-16 14:44:53","destinationsCount":11274
    private boolean isSelected;

    @SerializedName("stream_id")
    @Expose
    private Integer stream_id;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("courseType")
    @Expose
    private String courseType;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("destinationsCount")
    @Expose
    private String destinationsCount;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("count")
    @Expose
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStream_id() {
        return stream_id;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStream_id(Integer id) {
        this.stream_id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestinationsCount() {
        return count;
    }

    public void setDestinationsCount(String destinationsCount) {
        this.count = destinationsCount;
    }

    public String getDescription() {
        return description;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
