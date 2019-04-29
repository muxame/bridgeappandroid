package net.bridgeint.app.responces;

import net.bridgeint.app.models.FeesModel;
import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DeviceBee on 8/16/2017.
 */

public class FeesResponse extends GenericResponce {

    @SerializedName(Constants.DATA)
    @Expose
    private ArrayList<FeesModel> courseModels = new ArrayList<>();

    public ArrayList<FeesModel> getCourseModels() {
        return courseModels;
    }

    public void setCourseModels(ArrayList<FeesModel> courseModels) {
        this.courseModels = courseModels;
    }

}
