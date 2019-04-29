package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.models.ServicesModel;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private List<ServicesModel> listItems;
    private Context ctx;

    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_service, parent, false);
        ServiceAdapter.ViewHolder viewHolder = new ServiceAdapter.ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public ServiceAdapter(Context context, List<ServicesModel> listItems) {
        this.listItems = listItems;
        ctx = context;
    }

    @Override
    public void onBindViewHolder(final ServiceAdapter.ViewHolder holder, final int position) {
        try {
            final ServicesModel item = listItems.get(position);
            TextView textViewServiceName = holder.txtServiceName;
            TextView textViewServicePrice = holder.txtServicePrice;

            if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
                textViewServiceName.setText(item.getName_ar());
            } else if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ru")) {
                textViewServiceName.setText(item.getName_rs());
            } else {
                textViewServiceName.setText(item.getName());
            }


            textViewServicePrice.setText("$" + item.getPrice());

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
        public TextView txtServiceName;
        public TextView txtServicePrice;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            ctx = context;
            txtServiceName = itemView.findViewById(R.id.txt_service_name);
            txtServicePrice = itemView.findViewById(R.id.txt_service_price);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
