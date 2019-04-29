package net.bridgeint.app.responces;

import java.util.ArrayList;

/**
 * Created by keval on 19/7/18.
 */

public class SuggestKeyowrdResponse extends GenericResponce{

    private ArrayList<String> data;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }


}
