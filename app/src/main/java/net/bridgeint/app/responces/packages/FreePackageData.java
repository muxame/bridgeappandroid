package net.bridgeint.app.responces.packages;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FreePackageData {


    /**
     * apply_id : 2167
     * package : Free
     * tot_price :
     * status : Under Review
     * comments : Test
     * created : 26-Jan-2019
     * documents : [{"id":2516,"applyId":2167,"name":"1548482641_1511794058_advertise-here-full-640x360.jpg","type":"extra_documents","created":"2019-01-26 08:50:50"}]
     * freedata : [{"free_id":38,"apply_id":2167,"user_id":34156,"country_id":"AU","country_name":"Australia","universities_id":627,"universities_name":"University of Wollongong","status":"Under Review","created":"2019-01-26 08:50:50"},{"free_id":39,"apply_id":2167,"user_id":34156,"country_id":"AU","country_name":"Australia","universities_id":629,"universities_name":"La Trobe University ","status":"Under Review","created":"2019-01-26 08:50:50"},{"free_id":40,"apply_id":2167,"user_id":34156,"country_id":"CA","country_name":"Canada","universities_id":633,"universities_name":"Fulford Preparatory College ","status":"Under Review","created":"2019-01-26 08:50:50"},{"free_id":41,"apply_id":2167,"user_id":34156,"country_id":"GB","country_name":"United Kingdom","universities_id":276,"universities_name":"Cambridge Education Group","status":"Under Review","created":"2019-01-26 08:50:50"},{"free_id":42,"apply_id":2167,"user_id":34156,"country_id":"GB","country_name":"United Kingdom","universities_id":278,"universities_name":"INTO study","status":"Under Review","created":"2019-01-26 08:50:50"}]
     */

    private int apply_id;
    @SerializedName("package")
    private String packageX;
    private String tot_price;
    private String status;
    private String comments;
    private String created;
    private List<DocumentsBean> documents;
    private List<FreedataBean> freedata;

    public int getApply_id() {
        return apply_id;
    }

    public void setApply_id(int apply_id) {
        this.apply_id = apply_id;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getTot_price() {
        return tot_price;
    }

    public void setTot_price(String tot_price) {
        this.tot_price = tot_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<DocumentsBean> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentsBean> documents) {
        this.documents = documents;
    }

    public List<FreedataBean> getFreedata() {
        return freedata;
    }

    public void setFreedata(List<FreedataBean> freedata) {
        this.freedata = freedata;
    }

    public static class DocumentsBean {
        /**
         * id : 2516
         * applyId : 2167
         * name : 1548482641_1511794058_advertise-here-full-640x360.jpg
         * type : extra_documents
         * created : 2019-01-26 08:50:50
         */

        private int id;
        private int applyId;
        private String name;
        private String type;
        private String created;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getApplyId() {
            return applyId;
        }

        public void setApplyId(int applyId) {
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

    public static class FreedataBean {
        /**
         * free_id : 38
         * apply_id : 2167
         * user_id : 34156
         * country_id : AU
         * country_name : Australia
         * universities_id : 627
         * universities_name : University of Wollongong
         * status : Under Review
         * created : 2019-01-26 08:50:50
         */

        private int free_id;
        private int apply_id;
        private int user_id;
        private String country_id;
        private String country_name;
        private int universities_id;
        private String universities_name;
        private String status;
        private String created;

        public int getFree_id() {
            return free_id;
        }

        public void setFree_id(int free_id) {
            this.free_id = free_id;
        }

        public int getApply_id() {
            return apply_id;
        }

        public void setApply_id(int apply_id) {
            this.apply_id = apply_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

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

        public int getUniversities_id() {
            return universities_id;
        }

        public void setUniversities_id(int universities_id) {
            this.universities_id = universities_id;
        }

        public String getUniversities_name() {
            return universities_name;
        }

        public void setUniversities_name(String universities_name) {
            this.universities_name = universities_name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
}
