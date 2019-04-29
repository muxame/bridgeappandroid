package net.bridgeint.app.responces;

import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by DeviceBee on 8/16/2017.
 */

public class GenericResponce {
    @SerializedName(Constants.ERROR)
    @Expose
    private Boolean error;

    @SerializedName(Constants.MESSAGE)
    @Expose
    private String message;


    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
