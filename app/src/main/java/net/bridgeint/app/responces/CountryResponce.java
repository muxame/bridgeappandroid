package net.bridgeint.app.responces;

import net.bridgeint.app.models.CountryModel;
import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DeviceBee on 8/16/2017.
 */

public class CountryResponce extends GenericResponce {

    @SerializedName(Constants.DATA)
    @Expose
    private ArrayList<CountryModel> countryModels = new ArrayList<>();

    public ArrayList<CountryModel> getCountryModels() {
        return countryModels;
    }

    public void setCountryModels(ArrayList<CountryModel> countryModels) {
        this.countryModels = countryModels;
    }
}
