package net.bridgeint.app.models.getPaymentStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 15/03/2018.
 */

public class GetPaymentResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("paymentBrand")
    @Expose
    private String paymentBrand;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("card")
    @Expose
    private Card card;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("customParameters")
    @Expose
    private CustomParameters customParameters;
    @SerializedName("buildNumber")
    @Expose
    private String buildNumber;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("ndc")
    @Expose
    private String ndc;
    @SerializedName("success_payments_id")
    @Expose
    private String success_payments_id;

    public String getSuccess_payments_id() {
        return success_payments_id;
    }

    public void setSuccess_payments_id(String success_payments_id) {
        this.success_payments_id = success_payments_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentBrand() {
        return paymentBrand;
    }

    public void setPaymentBrand(String paymentBrand) {
        this.paymentBrand = paymentBrand;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomParameters getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(CustomParameters customParameters) {
        this.customParameters = customParameters;
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

}
