package net.bridgeint.app.models.getPaymentStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 20/03/2018.
 */

public class Card {

    @SerializedName("bin")
    @Expose
    private String bin;
    @SerializedName("binCountry")
    @Expose
    private String binCountry;
    @SerializedName("last4Digits")
    @Expose
    private String last4Digits;
    @SerializedName("holder")
    @Expose
    private String holder;
    @SerializedName("expiryMonth")
    @Expose
    private String expiryMonth;
    @SerializedName("expiryYear")
    @Expose
    private String expiryYear;

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getBinCountry() {
        return binCountry;
    }

    public void setBinCountry(String binCountry) {
        this.binCountry = binCountry;
    }

    public String getLast4Digits() {
        return last4Digits;
    }

    public void setLast4Digits(String last4Digits) {
        this.last4Digits = last4Digits;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

}
