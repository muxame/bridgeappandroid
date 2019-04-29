package net.bridgeint.app.models.ChatModel;

import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by DeviceBee on 8/23/2017.
 */

public class Message implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("fromUser")
    @Expose
    private int fromUser;

    @SerializedName("toUser")
    @Expose
    private int toUser;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("tempId")
    @Expose
    private String tempId;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("isRead")
    @Expose
    private int isRead;

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Message(JSONObject jsonObject) throws Exception {
        this.id = jsonObject.getInt(Constants.ID);
        this.fromUser = jsonObject.getInt(Constants.FROM_USER);
        this.toUser = jsonObject.getInt(Constants.TO_USER);
        this.message = jsonObject.getString(Constants.MESSAGE);
        this.duration = jsonObject.getString(Constants.DURATION);
        this.tempId = jsonObject.getString(Constants.TEMP_ID);
        this.status = jsonObject.getInt(Constants.STATUS);
        this.created = jsonObject.getString(Constants.CREATED);
        this.type = jsonObject.getString(Constants.TYPE);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUser() {
        return fromUser;
    }

    public void setFromUser(int fromUser) {
        this.fromUser = fromUser;
    }

    public int getToUser() {
        return toUser;
    }

    public void setToUser(int toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
