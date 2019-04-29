package net.bridgeint.app.responces.packages;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FreePackges implements Parcelable {

    /**
     * country_id : AU
     * country_name : Australia
     * country_image : 1512296505_SydneyOpera.jpg
     * university_count : 6
     * university : [{"id":627,"title":"University of Wollongong","country":"AU","icon":"1513678866_university_of_wollongong_logo.jpg","image":"1513678706_aus_university_of_wollongong_science_buildings_hi-2.jpg"},{"id":628,"title":"Griffith University ","country":"AU","icon":"1511945065_images.png","image":"1511945065_Griffith1.jpg"},{"id":629,"title":"La Trobe University ","country":"AU","icon":"1513679730_881481d22e2eac7241f4944047ccd157_400x400.png","image":"1513679893_SetWidth1280-130725_lims_2.jpg"},{"id":630,"title":"Bond University ","country":"AU","icon":"1513680316_top-logo.png","image":"1513680316_Bond_University.jpg"},{"id":631,"title":"Murdoch University","country":"AU","icon":"1511262043_logomurdch.png","image":"1511262184_unimurd.jpg"},{"id":698,"title":"Murdoch University","country":"AU","icon":"1513679096_Murdoch-University.png","image":"1513679096_Murdoch2.jpg"}]
     */

    private String country_id;
    private String country_name;
    private String country_image;
    private int university_count;
    private ArrayList<UniversityBean> university;

    protected FreePackges(Parcel in) {
        country_id = in.readString();
        country_name = in.readString();
        country_image = in.readString();
        university_count = in.readInt();
        needToShowSubCategory = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country_id);
        dest.writeString(country_name);
        dest.writeString(country_image);
        dest.writeInt(university_count);
        dest.writeByte((byte) (needToShowSubCategory ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FreePackges> CREATOR = new Creator<FreePackges>() {
        @Override
        public FreePackges createFromParcel(Parcel in) {
            return new FreePackges(in);
        }

        @Override
        public FreePackges[] newArray(int size) {
            return new FreePackges[size];
        }
    };

    public boolean isNeedToShowSubCategory() {
        return needToShowSubCategory;
    }

    public void setNeedToShowSubCategory(boolean needToShowSubCategory) {
        this.needToShowSubCategory = needToShowSubCategory;
    }

    private boolean needToShowSubCategory;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_image() {
        return country_image;
    }

    public void setCountry_image(String country_image) {
        this.country_image = country_image;
    }

    public int getUniversity_count() {
        return university_count;
    }

    public void setUniversity_count(int university_count) {
        this.university_count = university_count;
    }

    public ArrayList<UniversityBean> getUniversity() {
        return university;
    }

    public void setUniversity(ArrayList<UniversityBean> university) {
        this.university = university;
    }

    public static class UniversityBean implements Parcelable{
        /**
         * id : 627
         * title : University of Wollongong
         * country : AU
         * icon : 1513678866_university_of_wollongong_logo.jpg
         * image : 1513678706_aus_university_of_wollongong_science_buildings_hi-2.jpg
         */

        private int id;
        private String title;
        private String country;

        protected UniversityBean(Parcel in) {
            id = in.readInt();
            title = in.readString();
            country = in.readString();
            countryname = in.readString();
            icon = in.readString();
            image = in.readString();
            total_count = in.readInt();
            isSelected = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(title);
            dest.writeString(country);
            dest.writeString(countryname);
            dest.writeString(icon);
            dest.writeString(image);
            dest.writeInt(total_count);
            dest.writeByte((byte) (isSelected ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UniversityBean> CREATOR = new Creator<UniversityBean>() {
            @Override
            public UniversityBean createFromParcel(Parcel in) {
                return new UniversityBean(in);
            }

            @Override
            public UniversityBean[] newArray(int size) {
                return new UniversityBean[size];
            }
        };

        public String getCountryname() {
            return countryname;
        }

        public void setCountryname(String countryname) {
            this.countryname = countryname;
        }

        private String countryname;
        private String icon;
        private String image;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        private int total_count;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        private boolean isSelected;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
