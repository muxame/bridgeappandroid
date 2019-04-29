package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.bridgeint.app.R;
import net.bridgeint.app.models.ApplicationModel;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ufraj on 10/21/2016.
 */
public class myApplcationAdapterUpdate extends RecyclerView.Adapter<myApplcationAdapterUpdate.ViewHolder> {


    private List<ApplicationModel> listItems;
    private Context ctx;
    private int type;
    private boolean isResult;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_my_application, parent, false);
        ViewHolder viewHolder = new ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public myApplcationAdapterUpdate(Context context, List<ApplicationModel> listItems, boolean result) {
        this.listItems = listItems;
        ctx = context;
        isResult = result;
    }

    public void addT(int typee) {
        this.typee = typee;
    }

    int typee;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final ApplicationModel item = listItems.get(position);
//            Admission complete
//            Due for fees
            holder.ivRemoveCheckStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCloseClick(item);
                }
            });
            holder.tvUniName.setText(item.getTitle());
            if (item.getAmount().equalsIgnoreCase("Free")) {
                holder.tvAmount.setText("Free");
            } else {
                holder.tvAmount.setText("CAD " + item.getAmount());
            }
            holder.tvDegreeeName.setText(item.getCourse());
//            holder.tvAddress.setText(item.getCourse());
            if (item.getUpdated().contains(" ")) {
                String str[] = item.getUpdated().split(" ");
                String time = Utility.getDateWithMonthName(str[0]);
//                holder.tvDate.setText(time);
            } else {
//                holder.tvDate.setText(item.getUpdated());
            }
            if (item.getStatus().equalsIgnoreCase(Constants.APPROVED)) {
                holder.tvStatus.setText(ctx.getResources().getString(R.string.approved));
                holder.tvStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
//                holder.labelStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                holder.tvStatus.setBackgroundResource(R.drawable.green_rectangle);
//                holder.llStatus.setBackgroundResource(R.drawable.green_rectangle);

            } else if (item.getStatus().equalsIgnoreCase(Constants.COMPLETED)) {
                holder.tvStatus.setText(ctx.getResources().getString(R.string.admission_complete));
                holder.tvStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
//                holder.labelStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                holder.tvStatus.setBackgroundResource(R.drawable.green_rectangle);
//                holder.llStatus.setBackgroundResource(R.drawable.green_rectangle);

            } else if (item.getStatus().equalsIgnoreCase(Constants.DUE_FEE)) {
                holder.tvStatus.setText(ctx.getResources().getString(R.string.due_for_fees));
                holder.tvStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
//                holder.labelStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
//                holder.labelStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                holder.tvStatus.setBackgroundResource(R.drawable.yellow_rectangle);
//                holder.llStatus.setBackgroundResource(R.drawable.yellow_rectangle);

            } else if (item.getStatus().equals(Constants.PENDING)) {
                holder.tvStatus.setText(ctx.getResources().getString(R.string.pending));
                holder.tvStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
//                holder.labelStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                holder.tvStatus.setBackgroundResource(R.drawable.yellow_rectangle);
//                holder.llStatus.setBackgroundResource(R.drawable.yellow_rectangle);

            } else if (item.getStatus().equalsIgnoreCase(Constants.REJECTED)) {
                holder.tvStatus.setText(ctx.getResources().getString(R.string.rejected));
                holder.tvStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
//                holder.labelStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                holder.tvStatus.setBackgroundResource(R.drawable.red_rectangle);
//                holder.llStatus.setBackgroundResource(R.drawable.red_rectangle);

            } else if (item.getStatus().equalsIgnoreCase(Constants.UNDER_REVIEW)) {
                holder.tvStatus.setText(ctx.getResources().getString(R.string.under_review));
                holder.tvStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
//                holder.labelStatus.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                holder.tvStatus.setBackgroundResource(R.drawable.yellow_rectangle);
//                holder.llStatus.setBackgroundResource(R.drawable.yellow_rectangle);

            }
            if (position == listItems.size() - 1) {
//                holder.line.setVisibility(View.INVISIBLE);
            }
            if (position == 0) {
//                holder.lineTop.setVisibility(View.INVISIBLE);
            }
//            holder.root.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                        if (isResult) {
//                            Intent intent = new Intent(ctx, ServiceActivity.class);
//                            ArrayList<Package> packagesList = item.getPackages();
//                            intent.putExtra("packageList", packagesList);
//                            intent.putExtra("appliedId", item.getApplyId());
//                            intent.putExtra("action", "false");
//                            ctx.startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(ctx, ApplicationDetailActivity.class);
//                            intent.putExtra("application", item);
//                            intent.putExtra("appliedId",item.getApplyId());
//                            ctx.startActivity(intent);
//                        }
//                }
//            });

            if (item.getImage() != null && item.getImage().length() > 0) {
                Picasso.with(ctx)
                        .load(Constants.IMAGE_URL + item.getImage())
                        .into(holder.imgUniversity);
            }

        } catch (Exception e) {
            Log.e("adapter ", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    protected void onCloseClick(ApplicationModel applicationModel) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvUniName;
        public TextView tvStatus;
        public TextView tvDegreeeName;
        //        public TextView tvAddress;
//        public TextView tvDate;
//        public TextView line;
//        public TextView lineTop;
        public Context ctx;
        //        public RelativeLayout root;
        @BindView(R.id.img_university)
        ImageView imgUniversity;

        @BindView(R.id.tv_amount)
        TextView tvAmount;
        @BindView(R.id.iv_remove_check_status)
        ImageView ivRemoveCheckStatus;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvUniName = itemView.findViewById(R.id.tvUniName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDegreeeName = itemView.findViewById(R.id.tv_degree_name);
//            tvAddress = itemView.findViewById(R.id.tvAddress);
//            tvDate = itemView.findViewById(R.id.tvDate);
//            line = itemView.findViewById(R.id.line);
//            lineTop = itemView.findViewById(R.id.lineTop);
//            root = itemView.findViewById(R.id.root);
            ctx = context;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
