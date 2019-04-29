package net.bridgeint.app.models.ChatModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.bridgeint.app.utils.Constants;


/**
 * Created by DeviceBee on 8/23/2017.
 */

public class messageModel {

    @SerializedName(Constants.M_MESSAGE)
    @Expose
    private Message message;

    @SerializedName(Constants.TO)
    @Expose
    private ToModel toModel;

    @SerializedName(Constants.FROM)
    @Expose
    private FromModel fromModel;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ToModel getToModel() {
        return toModel;
    }

    public void setToModel(ToModel toModel) {
        this.toModel = toModel;
    }

    public FromModel getFromModel() {
        return fromModel;
    }

    public void setFromModel(FromModel fromModel) {
        this.fromModel = fromModel;
    }
}
