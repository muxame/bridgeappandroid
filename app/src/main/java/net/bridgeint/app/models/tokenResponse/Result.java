package net.bridgeint.app.models.tokenResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 22/03/2018.
 */

public class Result {

    @SerializedName("LiveSessionToken")
    @Expose
    private LiveSessionToken liveSessionToken;

    public LiveSessionToken getLiveSessionToken() {
        return liveSessionToken;
    }

    public void setLiveSessionToken(LiveSessionToken liveSessionToken) {
        this.liveSessionToken = liveSessionToken;
    }
}
