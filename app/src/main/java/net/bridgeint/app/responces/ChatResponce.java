package net.bridgeint.app.responces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.bridgeint.app.models.ChatModel.PagingModel;
import net.bridgeint.app.models.ChatModel.messageModel;
import net.bridgeint.app.utils.Constants;

import java.util.ArrayList;

/**
 * Created by DeviceBee on 8/23/2017.
 */

public class ChatResponce extends GenericResponce
{
    @SerializedName(Constants.PAGING)
@Expose
private PagingModel pagingModel;

    @SerializedName(Constants.MESSAGES)
    @Expose
    private ArrayList<messageModel> message = new ArrayList<>();

    public PagingModel getPagingModel() {
        return pagingModel;
    }

    public void setPagingModel(PagingModel pagingModel) {
        this.pagingModel = pagingModel;
    }

    public ArrayList<messageModel> getmessageModel() {
        return message;
    }

    public void setmessageModel(ArrayList<messageModel> message) {
        this.message = message;
    }
}