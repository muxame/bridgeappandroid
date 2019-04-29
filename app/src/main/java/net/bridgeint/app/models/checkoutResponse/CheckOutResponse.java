package net.bridgeint.app.models.checkoutResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 14/03/2018.
 */

public class CheckOutResponse {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("buildNumber")
    @Expose
    private String buildNumber;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("ndc")
    @Expose
    private String ndc;
    @SerializedName("id")
    @Expose
    private String id;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNdc() {
        return ndc;
    }

    public void setNdc(String ndc) {
        this.ndc = ndc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
