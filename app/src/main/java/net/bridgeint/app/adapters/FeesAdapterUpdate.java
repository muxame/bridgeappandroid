package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.models.FeesModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.singelton.Singelton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ufraj on 10/21/2016.
 */
public class FeesAdapterUpdate extends RecyclerView.Adapter<FeesAdapterUpdate.ViewHolder> {
    private List<FeesModel> listItems;
    private List<FeesModel> copyList = new ArrayList<>();
    private Context ctx;
    private int lastClicked = -1;
    ArrayList<Boolean> listChecks = new ArrayList<>();
    Singelton temp = Singelton.getInstance();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_levels_of_study, parent, false);
        ViewHolder viewHolder = new ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public FeesAdapterUpdate(Context context, List<FeesModel> listItems) {
        this.listItems = listItems;
        this.copyList.addAll(listItems);
        ctx = context;
        for (int i = 0; i < listItems.size(); i++) {
            listChecks.add(false);
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        try {
        final FeesModel item = listItems.get(position);
        Log.d("mytag", "onBindViewHolder: " + listItems.size());
        Log.d("mytag", "onBindViewHolder: " +
                position);
        Log.d("mytag", "onBindViewHolder: " + item.toString());
        if (!item.getCount().equalsIgnoreCase("")) {
            holder.tvDegreeName.setText(item.getFees() + " (" + "" + item.getCount() + ")");
        } else {
            holder.tvDegreeName.setText(item.getFees());
            holder.tvDegreeName.setAlpha(0.5f);
        }



           /* if (item.getSelected() == true || (ApplyModel.courseId != null && ApplyModel.courseId.equals(item.getId().toString()))) {
                ivTick.setVisibility(View.VISIBLE);
                //lo.setBackgroundResource(R.drawable.rectangle_filled);
//                temp.setFlagDegree(true);

            } else {
                ivTick.setVisibility(View.INVISIBLE);
                //lo.setBackgroundResource(R.drawable.outline_rectangle_gray);
            }*/

        holder.tvDegreeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    temp.setFlagDegree(true);
//                    check(position);
//                    for (int i = 0; i <= listItems.size() - 1; i++) {
//                        CourseModel courseModel = listItems.get(i);
//                        if (courseModel.getSelected()) {
//                            courseModel.setSelected(false);
//                        }
//                    }
//                    item.setSelected(true);
                if (!item.getCount().equalsIgnoreCase("")) {


                    ApplyModel.fees = item.getFees();
                    notifyDataSetChanged();
                    onItemClick(position);
                }
            }
        });
//        } catch (Exception e) {
//            Log.e("adapter ", e.toString());
//            e.printStackTrace();
//        }
    }

    protected void onItemClick(int position) {
    }

    private void check(int position) {
        for (int i = 0; i < listChecks.size(); i++) {
            if (i == position) {
                if (lastClicked == position) {
                    listChecks.set(i, false);
                    lastClicked = -1;
                    temp.setFlagDegree(false);

                } else {
                    listChecks.set(i, true);
                    lastClicked = i;
                    temp.setFlagDegree(true);
                }
            } else {
                listChecks.set(i, false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Context ctx;
        @BindView(R.id.tv_degree_name)
        TextView tvDegreeName;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ctx = context;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void UpdateList(List<FeesModel> listItems) {
        this.listItems.clear();
        this.listItems.addAll(listItems);
        for (int i = 0; i < listItems.size(); i++) {
            listChecks.add(false);
        }
        notifyDataSetChanged();
    }

}
