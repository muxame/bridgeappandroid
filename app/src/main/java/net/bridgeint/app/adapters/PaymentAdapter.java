package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.models.PaymentStatusModel;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private List<PaymentStatusModel> listItems;
    private Context ctx;
    private ServiceAdapter serviceAdapter;

    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_payment_status, parent, false);
        PaymentAdapter.ViewHolder viewHolder = new PaymentAdapter.ViewHolder(ctx, contactView);

        return viewHolder;
    }

    public PaymentAdapter(Context context, List<PaymentStatusModel> listItems) {
        this.listItems = listItems;
        ctx = context;
    }

    @Override
    public void onBindViewHolder(final PaymentAdapter.ViewHolder holder, final int position) {
        try {
            final PaymentStatusModel item = listItems.get(position);
            TextView txtPaymentStatus = holder.txtPaymentStatus;
            RecyclerView serviceList = holder.serviceList;
            if(item.getPayment_status().equalsIgnoreCase("p"))
            {
                txtPaymentStatus.setText(ctx.getString(R.string.pending));
            }else{
                txtPaymentStatus.setText(ctx.getString(R.string.success));
            }

            serviceAdapter = new ServiceAdapter(ctx,item.getServices());
            serviceList.setAdapter(serviceAdapter);

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
        public Context ctx;
        public TextView txtPaymentStatus;
        public RecyclerView serviceList;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            ctx = context;
            txtPaymentStatus = itemView.findViewById(R.id.txt_payment_status);
            serviceList = itemView.findViewById(R.id.serviceList);
        }

        @Override
        public void onClick(View v) {

        }
    }


}