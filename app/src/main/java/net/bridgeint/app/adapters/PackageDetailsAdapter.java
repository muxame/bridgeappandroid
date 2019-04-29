package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bridgeint.app.R;
import net.bridgeint.app.responces.packages.PackagesDetail;
import net.bridgeint.app.views.XTextView;

import java.util.List;

public class PackageDetailsAdapter extends RecyclerView.Adapter<PackageDetailsAdapter.ViewHolder> {
    private Context ctx;
    private List<PackagesDetail> mPackageDetails;
    private int packagewhite;

    @Override
    public PackageDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_package_details, parent, false);
        PackageDetailsAdapter.ViewHolder viewHolder = new PackageDetailsAdapter.ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public PackageDetailsAdapter(Context context, List<PackagesDetail> packagesDetails) {
        ctx = context;
        mPackageDetails = packagesDetails;
    }
    public PackageDetailsAdapter(Context context, List<PackagesDetail> packagesDetails,int isWhite) {
        ctx = context;
        mPackageDetails = packagesDetails;
        this.packagewhite = isWhite;
    }
    @Override
    public void onBindViewHolder(final PackageDetailsAdapter.ViewHolder holder, final int position) {
        if(packagewhite ==0){
            holder.txtPackageName.setTextColor(ctx.getResources().getColor(R.color.white));
        }else {
            holder.txtPackageName.setTextColor(ctx.getResources().getColor(R.color.black));
        }
        holder.txtPackageName.setText("- " + mPackageDetails.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return mPackageDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Context ctx;
        public XTextView txtPackageName;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            ctx = context;
            txtPackageName = itemView.findViewById(R.id.txt_service_name);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
