package net.bridgeint.app.responces.packages;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 17/01/2018.
 */

public class PackagesDetail implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("packageId")
    @Expose
    private Integer packageId;
    @SerializedName("detail")
    @Expose
    private String detail;

    protected PackagesDetail(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            packageId = null;
        } else {
            packageId = in.readInt();
        }
        detail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (packageId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(packageId);
        }
        dest.writeString(detail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackagesDetail> CREATOR = new Creator<PackagesDetail>() {
        @Override
        public PackagesDetail createFromParcel(Parcel in) {
            return new PackagesDetail(in);
        }

        @Override
        public PackagesDetail[] newArray(int size) {
            return new PackagesDetail[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
