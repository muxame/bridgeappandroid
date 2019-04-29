package net.bridgeint.app.responces;

import net.bridgeint.app.models.BannerModel;
import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by DeviceBee on 8/21/2017.
 */

public class BannerResponce {

    @SerializedName(Constants.ERROR)
    @Expose
    private Boolean error;

    @SerializedName(Constants.MESSAGE)
    @Expose
    private String message;
    @SerializedName(Constants.BANNER)
    @Expose
    public ArrayList<BannerModel> banner = null;

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<BannerModel> getBanner() {
        return banner;
    }
}
