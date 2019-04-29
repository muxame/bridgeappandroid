package net.bridgeint.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.bridgeint.app.R;
import net.bridgeint.app.responces.packages.Package;
import net.bridgeint.app.responces.packages.PackagesDetail;
import net.bridgeint.app.singelton.Singelton;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static net.bridgeint.app.application.Application.getAppInstance;

/**
 * Created by ufraj on 10/21/2016.
 */
public class Packages_Adapter extends RecyclerView.Adapter<Packages_Adapter.ViewHolder> {

    private ArrayList<Package> listItems;
    private Context ctx;
    private int lastClicked = -1;
    private ArrayList<Integer> integers = new ArrayList<>();
    Singelton temp = Singelton.getInstance();
    private boolean loaded;
    private Button pay_now;
    private String price = "";
    private Activity activity;
    private String shopper_id;
    int minteger = 1;
    PackageDetailsAdapter mDetailsAdapter;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_packages, parent, false);
        ViewHolder viewHolder = new ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public Packages_Adapter(Activity activity, Context context, ArrayList<Package> listItems, Button pay_now, String shopper_id/*, FeeFragment feeFragment*/) {
        this.activity = activity;
        this.listItems = listItems;
        ctx = context;
        this.pay_now = pay_now;
        this.shopper_id = shopper_id;
        /*    this.fragment = feeFragment;*/
    }


    private void setPackageDetailsAdapter(List<PackagesDetail> packagesDetails, Package pkg) {

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Package pkg = listItems.get(position);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            holder.tv_pkg_name.setText(pkg.getPackage().getName_ar());
            holder.txt_bundle_name.setText(pkg.getPackage().getName());
            holder.btnMinus.setRotation(180);
            holder.btnPlus.setRotation(180);
        } else if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ru")) {
            holder.tv_pkg_name.setText(pkg.getPackage().getName_rs());
            holder.txt_bundle_name.setText(pkg.getPackage().getName());
        } else {
            holder.btnMinus.setRotation(360);
            holder.btnPlus.setRotation(360);
            holder.tv_pkg_name.setText(pkg.getPackage().getName());
            holder.txt_bundle_name.setText(pkg.getPackage().getName());
        }
        holder.tv_price.setText("$" + pkg.getPackage().getPrice());
        // holder.txt_bundle_price.setText(("$" + pkg.getPackage().getPrice()));

        int intValue = (int) Math.round(pkg.getPackage().getPrice());
        holder.txt_bundle_price.setText(("$" + intValue));


        if (pkg.getPackage().getName().equalsIgnoreCase("Elite")) {
            mDetailsAdapter = new PackageDetailsAdapter(ctx, pkg.getPackagesDetail(), 0);
            holder.tv_pkg_name.setTextColor(ctx.getResources().getColor(R.color.white));
            holder.txt_bundle_name.setTextColor(ctx.getResources().getColor(R.color.white));
        } else {

            mDetailsAdapter = new PackageDetailsAdapter(ctx, pkg.getPackagesDetail(), 1);
            if (pkg.getPackage().getName().equalsIgnoreCase("Gold")) {
                holder.tv_pkg_name.setTextColor(ctx.getResources().getColor(R.color.white));
                holder.txt_bundle_name.setTextColor(ctx.getResources().getColor(R.color.white));

            } else {
                holder.tv_pkg_name.setTextColor(ctx.getResources().getColor(R.color.black));
                holder.txt_bundle_name.setTextColor(ctx.getResources().getColor(R.color.black));

            }
        }


        holder.rvDetails.setNestedScrollingEnabled(false);
        holder.rvDetails.setAdapter(mDetailsAdapter);
        if (pkg.getPackage().getName().equalsIgnoreCase("free")) {
//            holder.txt_bundle_name.setVisibility(View.GONE);
//            holder.txt_bundle_price.setVisibility(View.GONE);
//            Picasso.with(ctx).load(R.drawable.img_free).placeholder(R.drawable.placeholder_silver_nt).into(holder.imgBundle);
        } else {
//            Picasso.with(ctx).load(Constants.BUNDLE_IMAGE_URL + pkg.getPackage().getIconImage()).placeholder(R.drawable.placeholder_silver_nt).into(holder.imgBundle);
        }

        Picasso.with(ctx).load(Constants.BUNDLE_IMAGE_URL + pkg.getPackage().getIconImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.ll_background.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
//
        if (pkg.getPackage().getName().equalsIgnoreCase("Elite")) {
            holder.ll_bubble_top.setBackground(ctx.getResources().getDrawable(R.drawable.ic_elite_black));
        } else if (pkg.getPackage().getName().equalsIgnoreCase("Gold")) {
            holder.ll_bubble_top.setBackground(ctx.getResources().getDrawable(R.drawable.ic_gold_bubble));

        } else {
            holder.ll_bubble_top.setBackground(ctx.getResources().getDrawable(R.drawable.ic_silver_bubble));
        }
        if (pkg.getPackage().getFlag().equalsIgnoreCase("1")) {
            holder.ll_increment.setVisibility(View.VISIBLE);
        } else {
            holder.ll_increment.setVisibility(View.INVISIBLE);
        }
        holder.txtValue.setText(String.valueOf(minteger));
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minteger > 1) {
                    minteger = minteger - 1;
                    holder.txtValue.setText(String.valueOf(minteger));
                    onSelectPackage();
                }

            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minteger < 6) {
                    minteger += 1;
                    holder.txtValue.setText(String.valueOf(minteger));
                    onSelectPackage();
                }
            }
        });

        holder.pkg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integers.clear();
                integers.add(position);
                price = pkg.getPackage().getPrice() + "";
                Constants.PACKAGEID = pkg.getPackage().getId() + "";
                Constants.PRICE = price;
                loaded = true;
                notifyDataSetChanged();
            }
        });


        if (pkg.getPackage().getIs_disable() != null) {
            if (pkg.getPackage().getIs_disable().equalsIgnoreCase("1")) {
                holder.rb_check.setImageResource(R.drawable.checkmark_on);
                holder.rb_check.setEnabled(false);
                holder.txtValue.setText(pkg.getPackage().getQty());
                holder.btnPlus.setEnabled(false);
                holder.btnMinus.setEnabled(false);
            } else {
                holder.rb_check.setImageResource(R.drawable.checkmark);
                holder.rb_check.setEnabled(true);
                if (pkg.isSelected()) {
                    holder.rb_check.setImageResource(R.drawable.checkmark_on);
                } else {
                    holder.rb_check.setImageResource(R.drawable.checkmark);
                }
                holder.btnPlus.setEnabled(true);
                holder.btnMinus.setEnabled(true);
            }
        } else {
            if (pkg.isSelected()) {
                holder.rb_check.setImageResource(R.drawable.checkmark_on);
            } else {
                holder.rb_check.setImageResource(R.drawable.checkmark);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pkg.setSelected(!pkg.isSelected());

                listItems.set(position, pkg);
                notifyDataSetChanged();
                onPackageClicked(pkg, position);
            }
        });
        holder.rb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();
                pkg.setSelected(!pkg.isSelected());

                listItems.set(position, pkg);
                notifyDataSetChanged();
                onSelectPackage();
            }
        });


        holder.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pkg.setSelected(!pkg.isSelected());

                listItems.set(position, pkg);
                notifyDataSetChanged();
                onPackageClicked(pkg, position);
            }
        });
/*
        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (price.length() > 0) {
                    Utility.showLoadingDialog(activity, "Please wait...");
                    ServerCall.getCheckoutId(activity, price, shopper_id, new ResponceCallback() {
                        @Override
                        public void onSuccessResponce(Object obj) {
                            Utility.hideLoadingDialog();
                            CheckOutResponse checkOutResponse = ((Response<CheckOutResponse>) obj).body();
                            String check_id = checkOutResponse.getId();
                            fragment.checkOut(check_id);
                            Constants.CHECKOUTID = check_id;
                        }

                        @Override
                        public void onFailureResponce(Object obj) {
                            Utility.hideLoadingDialog();
                        }
                    });
                } else {
                    Toast.makeText(ctx, "Please select package first", Toast.LENGTH_SHORT).show();
                }


//                Intent i = new Intent(activity, TestActivity.class);
//                activity.startActivity(i);
            }
        });
*/

    }

    protected void onPackageClicked(Package pkg, int position) {
    }

    protected void onSelectPackage() {
    }

    public int getDeviceWidthInPercentage(int percentage) {
        DisplayMetrics displayMetrics = getAppInstance().getResources().getDisplayMetrics();
        float toMultiply = percentage / 100f;
        float pixels = displayMetrics.widthPixels * toMultiply;
        return (int) pixels;
    }


    public ArrayList<Package> getSelectedPackages() {
        ArrayList<Package> tempArr = new ArrayList<>();
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).isSelected()) {
                tempArr.add(listItems.get(i));
            }
        }
        return tempArr;
    }

    public int getMinteger() {
        return minteger;
    }

    public int getTotalAmount() {
        int total = 0;
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).isSelected()) {
                if (listItems.get(i).getPackage().getFlag().equalsIgnoreCase("1")) {
                    total += listItems.get(i).getPackage().getPrice() * minteger;
                } else {
                    total += listItems.get(i).getPackage().getPrice();
                }
            }
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView pkg_img, rb_check;
        public RecyclerView recyclerView;
        public TextView tv_price, tv_pkg_name;
        public TextView txt_bundle_price, txt_bundle_name;
        public LinearLayout txt_container;
        public LinearLayout ll_increment, ll_package_bg;
        public Context ctx;
        ImageView btnMinus;
        TextView txtValue;
        TextView tv_continue;
        ImageView btnPlus;
        ImageView imgBundle;
        AppCompatButton btnContinue;
        RecyclerView rvDetails;
        LinearLayout ll_background;
        LinearLayout ll_bubble_top;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            super(itemView);

            pkg_img = itemView.findViewById(R.id.pkg_img);
            rb_check = itemView.findViewById(R.id.rb_check);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_pkg_name = itemView.findViewById(R.id.pkg_name);
            recyclerView = itemView.findViewById(R.id.rv_packages);
            txt_container = itemView.findViewById(R.id.txt_container);
            ll_increment = itemView.findViewById(R.id.llIncrement);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            txtValue = itemView.findViewById(R.id.txtValue);
            txt_bundle_name = itemView.findViewById(R.id.txtBundleName);
            txt_bundle_price = itemView.findViewById(R.id.txtBundlePrice);
            imgBundle = itemView.findViewById(R.id.imgBundle);
            ll_bubble_top = itemView.findViewById(R.id.ll_top_bubble);
            ll_background = itemView.findViewById(R.id.ll_package_details);

//            ll_package_bg = itemView.findViewById(R.id.ll_backgound);
            rvDetails = itemView.findViewById(R.id.rv_packages_details);
            btnContinue = itemView.findViewById(R.id.btn_continue);

            ctx = context;


        }

        @Override
        public void onClick(View v) {

        }
    }


}
