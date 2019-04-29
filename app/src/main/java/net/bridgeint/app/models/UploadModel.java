package net.bridgeint.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UploadModel implements Parcelable{
    /**
     * applyId : 30846
     * type : recommendation_letters
     * created : 2018-09-24 11:11:50
     * image : [{"id":2047,"applyId":30846,"name":"1537787506_1537787504532.jpg","type":"recommendation_letters","created":"2018-09-24 11:11:50"}]
     */

    private String applyId;

    public UploadModel(String name) {
        this.name = name;
    }

    private String type;
    private String created;
    private String name;
    private List<ImageBean> image = new ArrayList<>();

    public static UploadModel objectFromData(String str) {

        return new Gson().fromJson(str, UploadModel.class);
    }

    public String getName() {
        return name;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<ImageBean> getImage() {
        return image;
    }

    public void setImage(List<ImageBean> image) {
        this.image = image;
    }

    public static class ImageBean implements Parcelable{
        /**
         * id : 2047
         * applyId : 30846
         * name : 1537787506_1537787504532.jpg
         * type : recommendation_letters
         * created : 2018-09-24 11:11:50
         */

        private String id;
        private String applyId;
        private String name;
        private String type;
        private String created;

        public ImageBean(String name) {
            this.name = name;
        }

        protected ImageBean(Parcel in) {
            id = in.readString();
            applyId = in.readString();
            name = in.readString();
            type = in.readString();
            created = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(applyId);
            dest.writeString(name);
            dest.writeString(type);
            dest.writeString(created);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
            @Override
            public ImageBean createFromParcel(Parcel in) {
                return new ImageBean(in);
            }

            @Override
            public ImageBean[] newArray(int size) {
                return new ImageBean[size];
            }
        };

        public static ImageBean objectFromData(String str) {

            return new Gson().fromJson(str, ImageBean.class);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplyId() {
            return applyId;
        }

        public void setApplyId(String applyId) {
            this.applyId = applyId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.applyId);
        dest.writeString(this.type);
        dest.writeString(this.created);
        dest.writeString(this.name);
        dest.writeTypedList(this.image);
    }

    public UploadModel() {
    }

    protected UploadModel(Parcel in) {
        this.applyId = in.readString();
        this.type = in.readString();
        this.created = in.readString();
        this.name = in.readString();
        this.image = in.createTypedArrayList(ImageBean.CREATOR);
    }

    public static final Creator<UploadModel> CREATOR = new Creator<UploadModel>() {
        @Override
        public UploadModel createFromParcel(Parcel source) {
            return new UploadModel(source);
        }

        @Override
        public UploadModel[] newArray(int size) {
            return new UploadModel[size];
        }
    };

    public String getImagesName(){
        StringBuilder stringBuilder = new StringBuilder();
        for (ImageBean imageBean : getImage()) {
            if(stringBuilder.toString().length() > 0){
                stringBuilder.append("@");
            }
            stringBuilder.append(imageBean.getName());
        }
        return stringBuilder.toString();

    }


}
