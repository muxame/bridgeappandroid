package net.bridgeint.app.models.tokenResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 22/03/2018.
 */

public class LiveSessionToken {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sessionId")
    @Expose
    private String sessionId;
    @SerializedName("apiKey")
    @Expose
    private String apiKey;
    @SerializedName("token")
    @Expose
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
