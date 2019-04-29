package net.bridgeint.app.responces;

import net.bridgeint.app.models.ApplicationModel;
import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by DeviceBee on 8/16/2017.
 */

public class ApplicationResponce extends GenericResponce {

    @SerializedName(Constants.DATA)
    @Expose
    private ArrayList<ApplicationModel> applicationModels = new ArrayList<>();

    public ArrayList<ApplicationModel> getApplicationModels() {
        return applicationModels;
    }

    public void setApplicationModels(ArrayList<ApplicationModel> applicationModels) {
        this.applicationModels = applicationModels;
    }
}
