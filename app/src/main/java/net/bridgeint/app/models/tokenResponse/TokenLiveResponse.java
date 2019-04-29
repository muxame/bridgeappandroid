package net.bridgeint.app.models.tokenResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 22/03/2018.
 */

public class TokenLiveResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("resullt")
    @Expose
    private Result resullt;

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

    public Result getResullt() {
        return resullt;
    }

    public void setResullt(Result resullt) {
        this.resullt = resullt;
    }


}
