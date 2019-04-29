package net.bridgeint.app.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bridgeint.app.R;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SharedClass;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by devicebee on 04/06/2018.
 */

public class LiveMessagesAdapter extends RecyclerView.Adapter<MessageVH> {

    private Activity activity;
    private List<String> list;

    public LiveMessagesAdapter(Activity activity, List<String> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MessageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view, parent, false);
        return new MessageVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageVH holder, int position) {
        try {
            JSONObject mainObject = new JSONObject(list.get(position));

            Picasso.with(activity).load(Constants.IMAGE_URL + mainObject.getString("image")).placeholder(R.drawable.user_placeholder).into(holder.user_pic);
            holder.msg.setText(URLDecoder.decode(mainObject.getString("message"), "UTF-8"));
            if (mainObject.getString("id").equalsIgnoreCase(SharedClass.userModel.getId() + "")) {
                holder.tv_name.setText("You");
            } else {
                holder.tv_name.setText(mainObject.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
