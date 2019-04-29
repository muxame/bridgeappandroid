package net.bridgeint.app.responces;

import net.bridgeint.app.models.UserModel;
import net.bridgeint.app.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse extends GenericResponce {


    @SerializedName(Constants.DATA)
    @Expose
    private UserModel userModel;

    public UserModel getUserModel() {
    return userModel;
    }

    public void setUserModel(UserModel userModel) {
    this.userModel = userModel;
    }

}
