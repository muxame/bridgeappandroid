package net.bridgeint.app.responces.packages;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by devicebee on 17/01/2018.
 */

public class Package implements Parcelable{
    @SerializedName("Package")
    @Expose
    private Package_ _package;
    @SerializedName("PackagesDetail")
    @Expose
    private List<PackagesDetail> packagesDetail = null;

    public Package_ getPackage() {
        return _package;
    }

    public void setPackage(Package_ _package) {
        this._package = _package;
    }

    public List<PackagesDetail> getPackagesDetail() {
        return packagesDetail;
    }

    public void setPackagesDetail(List<PackagesDetail> packagesDetail) {
        this.packagesDetail = packagesDetail;
    }

    private boolean isSelected;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this._package, flags);
        dest.writeTypedList(this.packagesDetail);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public Package() {
    }

    protected Package(Parcel in) {
        this._package = in.readParcelable(Package_.class.getClassLoader());
        this.packagesDetail = in.createTypedArrayList(PackagesDetail.CREATOR);
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<Package> CREATOR = new Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel source) {
            return new Package(source);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };
}

