package net.bridgeint.app.responces;

import net.bridgeint.app.models.Paging;
import net.bridgeint.app.models.UniversityModel;
import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by DeviceBee on 8/16/2017.
 */

public class UniversitiesResponce extends GenericResponce{

    @SerializedName(Constants.DATA)
    @Expose
    private ArrayList<UniversityModel> universityModel = new ArrayList<>();

    public ArrayList<UniversityModel> getUniversityModel() {
        return universityModel;
    }

    @SerializedName("paging")
    @Expose
    private Paging paging;

    public void setUniversityModel(ArrayList<UniversityModel> universityModel) {
        this.universityModel = universityModel;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
