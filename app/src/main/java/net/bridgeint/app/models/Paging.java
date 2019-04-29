package net.bridgeint.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 10/01/2018.
 */

public class Paging {

    @SerializedName("totalRecords")
    @Expose
    private Integer totalRecords;
    @SerializedName("numberofpage")
    @Expose
    private Integer numberofpage;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("currentpage")
    @Expose
    private Integer currentpage;

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getNumberofpage() {
        return numberofpage;
    }

    public void setNumberofpage(Integer numberofpage) {
        this.numberofpage = numberofpage;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(Integer currentpage) {
        this.currentpage = currentpage;
    }

}
