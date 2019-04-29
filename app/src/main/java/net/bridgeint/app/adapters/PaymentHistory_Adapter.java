package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.models.PaymentHistoryData;
import net.bridgeint.app.utils.Utility;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Created by ufraj on 10/21/2016.
 */
public class PaymentHistory_Adapter extends RecyclerView.Adapter<PaymentHistory_Adapter.ViewHolder> {

    private ArrayList<PaymentHistoryData> listItems;
    private Context ctx;
    private int type;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_payment_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public PaymentHistory_Adapter(Context context, ArrayList<PaymentHistoryData> listItems, boolean result) {
        this.listItems = listItems;
        ctx = context;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final PaymentHistoryData item = listItems.get(position);
            holder.tvTitle.setText(item.getName());
            if (item.getPrice() == 0) {
                holder.tvAmount.setText("");
            } else {
                holder.tvAmount.setText("$" + item.getPrice());
            }

            String time;
            if (item.getCreated().contains("-")) {
                String[] split = item.getCreated().split(" ");
                time = split[0];
            } else {
                time = item.getCreated();
            }

            holder.tvDate.setText(Utility.convertDate(time));


        } catch (Exception e) {
            Log.e("adapter ", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvDate;
        public TextView tvTitle;
        public TextView tvAmount;

        public Context ctx;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ctx = context;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
