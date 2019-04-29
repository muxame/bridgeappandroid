package net.bridgeint.app.models.successResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 21/03/2018.
 */

public class Result {
    @SerializedName("SuccessPayment")
    @Expose
    private SuccessPayment successPayment;

    public SuccessPayment getSuccessPayment() {
        return successPayment;
    }

    public void setSuccessPayment(SuccessPayment successPayment) {
        this.successPayment = successPayment;
    }

}
