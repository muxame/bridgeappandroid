package net.bridgeint.app.models.getPaymentStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 20/03/2018.
 */

public class Customer {

    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("ipCountry")
    @Expose
    private String ipCountry;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpCountry() {
        return ipCountry;
    }

    public void setIpCountry(String ipCountry) {
        this.ipCountry = ipCountry;
    }

}
