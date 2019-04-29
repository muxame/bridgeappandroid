package net.bridgeint.app.models.getPaymentStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 20/03/2018.
 */

public class CustomParameters {

    @SerializedName("SHOPPER_MSDKIntegrationType")
    @Expose
    private String sHOPPERMSDKIntegrationType;
    @SerializedName("SHOPPER_device")
    @Expose
    private String sHOPPERDevice;
    @SerializedName("SHOPPER_OS")
    @Expose
    private String sHOPPEROS;
    @SerializedName("SHOPPER_MSDKVersion")
    @Expose
    private String sHOPPERMSDKVersion;
    @SerializedName("SHOPPER_deviceId")
    @Expose
    private String sHOPPERDeviceId;

    public String getSHOPPERMSDKIntegrationType() {
        return sHOPPERMSDKIntegrationType;
    }

    public void setSHOPPERMSDKIntegrationType(String sHOPPERMSDKIntegrationType) {
        this.sHOPPERMSDKIntegrationType = sHOPPERMSDKIntegrationType;
    }

    public String getSHOPPERDevice() {
        return sHOPPERDevice;
    }

    public void setSHOPPERDevice(String sHOPPERDevice) {
        this.sHOPPERDevice = sHOPPERDevice;
    }

    public String getSHOPPEROS() {
        return sHOPPEROS;
    }

    public void setSHOPPEROS(String sHOPPEROS) {
        this.sHOPPEROS = sHOPPEROS;
    }

    public String getSHOPPERMSDKVersion() {
        return sHOPPERMSDKVersion;
    }

    public void setSHOPPERMSDKVersion(String sHOPPERMSDKVersion) {
        this.sHOPPERMSDKVersion = sHOPPERMSDKVersion;
    }

    public String getSHOPPERDeviceId() {
        return sHOPPERDeviceId;
    }

    public void setSHOPPERDeviceId(String sHOPPERDeviceId) {
        this.sHOPPERDeviceId = sHOPPERDeviceId;
    }


}
