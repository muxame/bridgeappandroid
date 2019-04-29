package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.models.DegreeModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.singelton.Singelton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DegreeAdapterUpdate extends RecyclerView.Adapter<DegreeAdapterUpdate.ViewHolder> {

    private List<DegreeModel> listItems;
    private Context ctx;
    Singelton temp = Singelton.getInstance();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_levels_of_study, parent, false);
        ViewHolder viewHolder = new ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public DegreeAdapterUpdate(Context context, List<DegreeModel> listItems) {
        this.listItems = listItems;
        ctx = context;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DegreeModel degreeModel = listItems.get(position);
        if (!degreeModel.getCount().equalsIgnoreCase("")) {
            holder.tvDegreeName.setText(degreeModel.getTitle() + " (" + degreeModel.getCount() + ")");
        }else {
            holder.tvDegreeName.setText(degreeModel.getTitle());
            holder.tvDegreeName.setAlpha(0.5f);
        }

        holder.tvDegreeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!degreeModel.getCount().equalsIgnoreCase("")) {
                    if (degreeModel.getTitle().equalsIgnoreCase("Online Degrees")) {
                        showOnlineDegreeDialog(ctx, position, temp, degreeModel.getId(), degreeModel);
                    } else {
//                    temp.setFlagDegree(true);
//                    check(position);
                        ApplyModel.degreeId = degreeModel.getId().toString();
                        ApplyModel.courseType = "G";
                        onItemClick(position, degreeModel);
                    }
                } else {

                }

//                onItemClick(position,degreeModel);

            }
        });
    }

    public void showOnlineDegreeDialog(final Context activity, int position, Singelton temp, Integer id, DegreeModel degreeModel) {

        // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.CustomDialog);
        final FrameLayout frameView = new FrameLayout(activity);
        builder.setView(frameView);
        android.support.v7.app.AlertDialog myalertdialog = builder.create();
        LayoutInflater inflater = myalertdialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_online_degrees, frameView);
        TextView txtClose = dialoglayout.findViewById(R.id.close_button);
        TextView txtOnlineMaster = dialoglayout.findViewById(R.id.textOnlineMaster);
        TextView txtOnlineBachelor = dialoglayout.findViewById(R.id.textOnlineBachelor);
        TextView txtOnlinephd = dialoglayout.findViewById(R.id.textOnlinephd);

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myalertdialog.dismiss();
            }
        });

        txtOnlineBachelor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp.setFlagDegree(true);
//                check(position);
                ApplyModel.degreeId = id + "";
                ApplyModel.courseType = "OB";
                onItemClick(position, degreeModel);
                myalertdialog.dismiss();
            }
        });
        txtOnlineMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp.setFlagDegree(true);
//                check(position);
                ApplyModel.degreeId = id + "";
                ApplyModel.courseType = "OM";
                onItemClick(position, degreeModel);
                myalertdialog.dismiss();
            }
        });

        txtOnlinephd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp.setFlagDegree(true);
//                check(position);
                ApplyModel.degreeId = id + "";
                ApplyModel.courseType = "OPHD";
                onItemClick(position, degreeModel);
                myalertdialog.dismiss();
            }
        });

        myalertdialog.setCancelable(false);
        myalertdialog.setView(dialoglayout);
        myalertdialog.show();

    }

//    private void check(int position) {
//        for (int i = 0; i < listChecks.size(); i++) {
//            if (i == position) {
//                if (lastClicked == position) {
//                    listChecks.set(i, false);
//                    lastClicked = -1;
//                    temp.setFlagDegree(false);
//                } else {
//                    listChecks.set(i, true);
//                    lastClicked = i;
//                    temp.setFlagDegree(true);
//                }
//            } else {
//                listChecks.set(i, false);
//            }
//        }
//        notifyDataSetChanged();
//    }


    protected void onItemClick(int pos, DegreeModel degreeModel) {

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_degree_name)
        AppCompatButton tvDegreeName;
        public Context ctx;
        public View view;

        public ViewHolder(Context ctx, View contactView) {
            super(contactView);
            view = contactView;

            ButterKnife.bind(this, view);
        }
    }
}
