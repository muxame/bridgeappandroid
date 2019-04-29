package net.bridgeint.app.responces;

import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by DeviceBee on 8/21/2017.
 */

public class UploadFileResponce {

    @SerializedName(Constants.ERROR)
    @Expose
    private Boolean error;

    @SerializedName(Constants.NAME)
    @Expose
    private String name;

    @SerializedName(Constants.MESSAGE)
    @Expose
    private String message;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
