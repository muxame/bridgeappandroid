package net.bridgeint.app.responces.packages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FreepackagesResponse {

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String text;

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Packages")
    @Expose
    private ArrayList<FreePackges> Packages = null;

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

    public ArrayList<FreePackges> getPackages() {
        return Packages;
    }

    public void setPackages(ArrayList<FreePackges> packages) {
        this.Packages = packages;
    }
}
