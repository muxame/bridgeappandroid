package net.bridgeint.app.responces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.bridgeint.app.models.CourseModel;
import net.bridgeint.app.models.Paging;
import net.bridgeint.app.utils.Constants;

import java.util.ArrayList;

/**
 * Created by DeviceBee on 8/16/2017.
 */

public class CourseResponce extends GenericResponce {

    @SerializedName("paging")
    @Expose
    private Paging paging;

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @SerializedName(Constants.DATA)
    @Expose
    private ArrayList<CourseModel> courseModels = new ArrayList<>();

    public ArrayList<CourseModel> getCourseModels() {
        return courseModels;
    }

    public void setCourseModels(ArrayList<CourseModel> courseModels) {
        this.courseModels = courseModels;
    }

}
