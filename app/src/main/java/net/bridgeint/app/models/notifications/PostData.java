package net.bridgeint.app.models.notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devicebee on 19/03/2018.
 */

public class PostData {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("appointmentId")
    @Expose
    private String appointmentId;
    @SerializedName("timeSlotId")
    @Expose
    private String timeSlotId;
    @SerializedName("clinicId")
    @Expose
    private String clinicId;
    @SerializedName("isUser")
    @Expose
    private Integer isUser;
    @SerializedName("doctorId")
    @Expose
    private String doctorId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getIsUser() {
        return isUser;
    }

    public void setIsUser(Integer isUser) {
        this.isUser = isUser;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

}
