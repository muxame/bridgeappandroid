package net.bridgeint.app.models.ChatModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DeviceBee on 8/23/2017.
 */

public class PagingModel {

    @SerializedName("totalRecords")
    @Expose
    private int totalRecords;

    @SerializedName("numberofpage")
    @Expose
    private int numberofpage;

    @SerializedName("limit")
    @Expose
    private int limit;

    @SerializedName("currentpage")
    @Expose
    private int currentpage;

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getNumberofpage() {
        return numberofpage;
    }

    public void setNumberofpage(int numberofpage) {
        this.numberofpage = numberofpage;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }
}
