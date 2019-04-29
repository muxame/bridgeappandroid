package net.bridgeint.app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DeviceBee on 10/30/2017.
 */

public class BannerModelForAdapter implements Parcelable {
    public String title_url;
    public boolean isVideo;

    public BannerModelForAdapter(String title_url, boolean isVideo) {
        this.title_url = title_url;
        this.isVideo = isVideo;
    }

    protected BannerModelForAdapter(Parcel in) {
        title_url = in.readString();
        isVideo = in.readByte() != 0;
    }

    public static final Creator<BannerModelForAdapter> CREATOR = new Creator<BannerModelForAdapter>() {
        @Override
        public BannerModelForAdapter createFromParcel(Parcel in) {
            return new BannerModelForAdapter(in);
        }

        @Override
        public BannerModelForAdapter[] newArray(int size) {
            return new BannerModelForAdapter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title_url);
        dest.writeByte((byte) (isVideo ? 1 : 0));
    }
}
