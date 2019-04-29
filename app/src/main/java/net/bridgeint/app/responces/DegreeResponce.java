package net.bridgeint.app.responces;

import net.bridgeint.app.models.DegreeModel;
import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DeviceBee on 8/16/2017.
 */

public class DegreeResponce extends GenericResponce {

    @SerializedName(Constants.DATA)
    @Expose
    private ArrayList<DegreeModel> degreeModels = new ArrayList<>();

    public ArrayList<DegreeModel> getDegreeModels() {
        return degreeModels;
    }

    public void setDegreeModels(ArrayList<DegreeModel> degreeModels) {
        this.degreeModels = degreeModels;
    }

}
