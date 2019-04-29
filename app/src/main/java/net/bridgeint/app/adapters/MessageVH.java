package net.bridgeint.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bridgeint.app.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by devicebee on 04/06/2018.
 */

public class MessageVH extends RecyclerView.ViewHolder {

    CircleImageView user_pic;
    TextView msg, tv_name;

    public MessageVH(View itemView) {
        super(itemView);
        user_pic = itemView.findViewById(R.id.user_pic);
        msg = itemView.findViewById(R.id.tv_msg_received);
        tv_name = itemView.findViewById(R.id.tv_name);
    }
}
