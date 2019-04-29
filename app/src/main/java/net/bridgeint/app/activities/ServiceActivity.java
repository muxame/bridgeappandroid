package net.bridgeint.app.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.Transaction;
import com.oppwa.mobile.connect.provider.TransactionType;
import com.squareup.picasso.Picasso;

import net.bridgeint.app.R;
import net.bridgeint.app.adapters.PackageDetailsAdapter;
import net.bridgeint.app.adapters.Packages_Adapter;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.interfaces.UpdateBusInterface;
import net.bridgeint.app.models.PaymentType;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.models.checkoutResponse.CheckOutResponse;
import net.bridgeint.app.models.getPaymentStatus.GetPaymentResponse;
import net.bridgeint.app.models.successResponse.SavePaymentResponse;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.responces.packages.Package;
import net.bridgeint.app.responces.packages.PackagesDetail;
import net.bridgeint.app.responces.packages.PackagesResponse;
import net.bridgeint.app.utils.Config;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ServiceActivity extends BaseActivity implements ResponceCallback {


    @BindView(R.id.rv_packages)
    RecyclerView rvPackages;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.paynow)
    Button paynow;
    @BindView(R.id.bottom)
    FrameLayout bottom;
    @BindView(R.id.ibBack)
    ImageView ibBack;
    @BindView(R.id.ll_packges_details_title)
    LinearLayout llPackgesDetailsTitle;
    @BindView(R.id.ll_packges)
    LinearLayout llPackges;
    private ArrayList<Package> pkgList;
    private Packages_Adapter adapter;
    private String resourcePath;
    public ProgressDialog dialog;
    String commaSeperatedIDs = "";
    String commaSeperatedName = "";
    String commaSeperatedPrice = "";
    String qty = "";
    public String shopper_id = "hyperpay://com.devicebee.tryapply/id";
    int FLAG_NUMBER = 0;
    Intent intent;
    String action;
    String applyId = "";

    PackageDetailsAdapter mDetailsAdapter;
    @BindView(R.id.llPackageDetails)
    LinearLayout llPackageDetails;
    @BindView(R.id.rv_packages_details)
    RecyclerView rvPackagesDetails;
    @BindView(R.id.llMainBackground)
    LinearLayout llMainBackground;
    @BindView(R.id.imgBundle)
    ImageView imgBundle;
    @BindView(R.id.txtBundleName)
    TextView txtBundleName;
    @BindView(R.id.txtBundlePrice)
    TextView txtBundlePrice;


    @Override
    public void onBackPressed() {
        if (llPackageDetails.getVisibility() == View.VISIBLE) {
            llPackageDetails.setVisibility(View.GONE);
            llPackges.setVisibility(View.VISIBLE);
            llPackgesDetailsTitle.setVisibility(View.VISIBLE);
            rvPackages.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);

//            llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_bundles));
            ibBack.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(UpdateBusInterface event) {
        String shooperId = event.getShopperId();
        requestPaymentStatus(shooperId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_USER, SharedClass.userModel.getFirstName() + " " + SharedClass.userModel.getLastName());
        bundle.putString(Constants.PARAM_EMAIL, SharedClass.userModel.getEmail());

        BaseActivity.logEvent(Constants.EVENT_BUNDLE_SCREEN, bundle);
        BaseActivity.logFirebaseEvent(Constants.EVENT_BUNDLE_SCREEN.replace(" ", "_").toLowerCase());

        ButterKnife.bind(this);
        intent = getIntent();
        action = intent.getStringExtra("action");
        if (!EventBus.getDefault().isRegistered(this)) {

            EventBus.getDefault().register(this);
        }

        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            ibBack.setRotation(180);
        }

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*  callAppyRequestUniversity();*/

                if (action.equalsIgnoreCase("false")) {
                    applyId = intent.getStringExtra("appliedId");
                    if (!txtTotal.getText().toString().equalsIgnoreCase("0")) {
                        Utility.showAddCommentDialog(ServiceActivity.this, new ResponceCallback() {
                            @Override
                            public void onSuccessResponce(Object obj) {
                                PaymentType paymentType = (PaymentType) obj;
                                if (!paymentType.getPaymentType().equalsIgnoreCase(Config.COD)) {
                                    Utility.showLoadingDialog(ServiceActivity.this, "Please wait...");

                                    ServerCall.getCheckoutId(ServiceActivity.this, txtTotal.getText().toString(), shopper_id, new ResponceCallback() {
                                        @Override
                                        public void onSuccessResponce(Object obj) {
                                            Utility.hideLoadingDialog();
                                            CheckOutResponse checkOutResponse = ((Response<CheckOutResponse>) obj).body();
                                            String check_id = checkOutResponse.getId();
                                            checkOut(check_id);
                                            Constants.CHECKOUTID = check_id;
                                        }

                                        @Override
                                        public void onFailureResponce(Object obj) {
                                            Utility.hideLoadingDialog();
                                        }
                                    });
                                } else {
                                    Utility.showLoadingDialog(ServiceActivity.this, "Please wait...");
                                    ServerCall.saveCODPayment(ServiceActivity.this, paymentType.getPaymentType(), "-", getSelectedPackedId(), txtTotal.getText().toString(),
                                            new ResponceCallback() {
                                                @Override
                                                public void onSuccessResponce(Object obj) {
                                                    Utility.hideLoadingDialog();
                                                    SavePaymentResponse savePaymentResponse = ((Response<SavePaymentResponse>) obj).body();
                                                    //Utility.showToast(ServiceActivity.this,savePaymentResponse.getMessage());
                                                    //startMainActivity();
                                                    callAppyRequestUniversity(savePaymentResponse.getResult().getSuccessPayment().getId() + "", adapter, savePaymentResponse.getResult().getSuccessPayment().getPayment_status());
                                                }

                                                @Override
                                                public void onFailureResponce(Object obj) {
                                                    Utility.hideLoadingDialog();

                                                }
                                            });
                                }
                            }

                            @Override
                            public void onFailureResponce(Object obj) {
                            }
                        });

                    } else {
                        Toast.makeText(ServiceActivity.this, getString(R.string.select_package), Toast.LENGTH_SHORT).show();
                    }
                } else if (!txtTotal.getText().toString().equalsIgnoreCase("0")) {
                    Utility.showAddCommentDialog(ServiceActivity.this, new ResponceCallback() {

                        @Override
                        public void onSuccessResponce(Object obj) {
                            Utility.hideConfirmDialog();
                            PaymentType paymentType = (PaymentType) obj;
                            if (!paymentType.getPaymentType().equalsIgnoreCase(Config.COD)) {
                                Utility.showLoadingDialog(ServiceActivity.this, "Please wait...");
                                ServerCall.getCheckoutId(ServiceActivity.this, txtTotal.getText().toString(), shopper_id, new ResponceCallback() {
                                    @Override
                                    public void onSuccessResponce(Object obj) {
                                        Utility.hideLoadingDialog();
                                        CheckOutResponse checkOutResponse = ((Response<CheckOutResponse>) obj).body();
                                        String check_id = checkOutResponse.getId();
                                        checkOut(check_id);
                                        Constants.CHECKOUTID = check_id;
                                    }

                                    @Override
                                    public void onFailureResponce(Object obj) {
                                        Utility.hideLoadingDialog();
                                    }
                                });
                            } else {
                                Utility.showLoadingDialog(ServiceActivity.this, "Please wait...");
                                ServerCall.saveCODPayment(ServiceActivity.this, paymentType.getPaymentType(), "-", getSelectedPackedId(), txtTotal.getText().toString(),
                                        new ResponceCallback() {
                                            @Override
                                            public void onSuccessResponce(Object obj) {
                                                Utility.hideLoadingDialog();
                                                SavePaymentResponse savePaymentResponse = ((Response<SavePaymentResponse>) obj).body();
                                                callAppyRequestUniversity(savePaymentResponse.getResult().getSuccessPayment().getId() + "", adapter, savePaymentResponse.getResult().getSuccessPayment().getPayment_status());
                                                //Utility.showToast(ServiceActivity.this,savePaymentResponse.getMessage());
                                                //startMainActivity();
                                            }

                                            @Override
                                            public void onFailureResponce(Object obj) {
                                                Utility.hideLoadingDialog();

                                            }
                                        });
                            }
                        }

                        @Override
                        public void onFailureResponce(Object obj) {

                        }
                    });

                } else {
                    Toast.makeText(ServiceActivity.this, "" + getResources().getString(R.string.select_package_first), Toast.LENGTH_SHORT).show();
                }


            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llPackageDetails.getVisibility() == View.VISIBLE) {
                    llPackageDetails.setVisibility(View.GONE);
                    llPackges.setVisibility(View.VISIBLE);
                    llPackgesDetailsTitle.setVisibility(View.VISIBLE);
                    rvPackages.setVisibility(View.VISIBLE);
                    bottom.setVisibility(View.GONE);
//                    llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_bundles));
                    ibBack.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
            }
        });
        intialize();
    }

    private void setPackageDetailsAdapter(List<PackagesDetail> packagesDetails, Package pkg) {
        mDetailsAdapter = new PackageDetailsAdapter(this, packagesDetails);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            txtBundleName.setText(pkg.getPackage().getName());
        } else if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ru")) {
            txtBundleName.setText(pkg.getPackage().getName());
        } else {
            txtBundleName.setText(pkg.getPackage().getName());
        }
        Picasso.with(this).load(Constants.BUNDLE_IMAGE_URL + pkg.getPackage().getIconImage()).into(imgBundle);
        txtBundlePrice.setText("$ " + pkg.getPackage().getPrice());
        rvPackagesDetails.setNestedScrollingEnabled(false);
        rvPackagesDetails.setAdapter(mDetailsAdapter);
    }


    private void startMainActivity() {
        startActivity(new Intent(ServiceActivity.this, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void checkOut(String id) {
        if (id != null) {
            Set<String> paymentBrands = new LinkedHashSet<String>();
            paymentBrands.add("VISA");
            paymentBrands.add("MASTER");
            CheckoutSettings checkoutSettings = new CheckoutSettings(id, paymentBrands, Connect.ProviderMode.LIVE);
            Constants.eventFocus = "serviceApplication";
            Intent intent = checkoutSettings.createCheckoutActivityIntent(ServiceActivity.this);
            startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT);
        } else {
            Toast.makeText(ServiceActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestPaymentStatus(String resourcePath) {
        Utility.showLoadingDialog(this, "Please wait...");
        ServerCall.getPaymentStatus(ServiceActivity.this, resourcePath, new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                Utility.hideLoadingDialog();

                GetPaymentResponse paymentResponse = ((Response<GetPaymentResponse>) obj).body();

                String msg = paymentResponse.getResult().getDescription();
                Log.e("StatusMessage", msg);
                final String successPaymentId = paymentResponse.getSuccess_payments_id();
                /* Toast.makeText(ServiceActivity.this, msg, Toast.LENGTH_SHORT).show();*/
                callAppyRequestUniversity(successPaymentId, adapter, "S");
            }

            @Override
            public void onFailureResponce(Object obj) {
                Utility.hideLoadingDialog();
            }
        });
    }

    private String getSelectedPackedId() {
        ArrayList<Package> packagesData = adapter.getSelectedPackages();
        ArrayList<String> packageIds = new ArrayList<>();

        for (int i = 0; i < packagesData.size(); i++) {
            packageIds.add(packagesData.get(i).getPackage().getId() + "");

        }
        String commaSeperatedIDs = TextUtils.join(",", packageIds);
        return commaSeperatedIDs;
    }

    private void callAppyRequestUniversity(String successPaymentId, Packages_Adapter adapter, String paymentStatus) {

        ArrayList<Package> packagesData = adapter.getSelectedPackages();
        ArrayList<String> packageIds = new ArrayList<>();
        ArrayList<String> packageName = new ArrayList<>();
        ArrayList<String> packagePrice = new ArrayList<>();
        ArrayList<String> qtyArr = new ArrayList<>();
        for (int i = 0; i < packagesData.size(); i++) {
            packageIds.add(packagesData.get(i).getPackage().getId() + "");
            packageName.add(packagesData.get(i).getPackage().getName());
            packagePrice.add(packagesData.get(i).getPackage().getPrice() + "");
            if (packagesData.get(i).getPackage().getFlag().equalsIgnoreCase("1")) {
                qtyArr.add(String.valueOf(this.adapter.getMinteger()));
            } else {
                qtyArr.add("1");
            }
        }
        commaSeperatedIDs = TextUtils.join(",", packageIds);
        commaSeperatedName = TextUtils.join(",", packageName);
        commaSeperatedPrice = TextUtils.join(",", packagePrice);
        qty = TextUtils.join(",", qtyArr);

        Log.e("====PackageID===", commaSeperatedIDs);
        Log.e("====PackageName===", commaSeperatedName);
        Log.e("====PackagePrice===", commaSeperatedPrice);
        Log.e("====qty===", qty);


        if (Utility.isNetConnected(ServiceActivity.this)) {
            SaveAllDataToServer(commaSeperatedIDs, commaSeperatedName, commaSeperatedPrice, qty, successPaymentId, paymentStatus);
            FLAG_NUMBER = 2;
        } else {
            /* Utility.showToast(ServiceActivity.this, Constants.NET_CONNECTION_LOST);*/
        }
    }

    private void SaveAllDataToServer(String commaSeperatedIDs, String commaSeperatedName, String commaSeperatedPrice, String qty, String successPaymentId, String paymentstatus) {
        if (action.equalsIgnoreCase("true")) {
            ServerCall.ApplyApplication(ServiceActivity.this, dialog, ServiceActivity.this, commaSeperatedIDs, commaSeperatedName, commaSeperatedPrice, qty, successPaymentId, paymentstatus);
        } else {
            ArrayList<Package> packagesData = adapter.getSelectedPackages();
            ArrayList<String> packageIds = new ArrayList<>();
            ArrayList<String> packageName = new ArrayList<>();
            ArrayList<String> packagePrice = new ArrayList<>();
            ArrayList<String> qtyArr = new ArrayList<>();
            for (int i = 0; i < packagesData.size(); i++) {
                packageIds.add(packagesData.get(i).getPackage().getId() + "");
                packageName.add(packagesData.get(i).getPackage().getName());
                packagePrice.add(packagesData.get(i).getPackage().getPrice() + "");
                if (packagesData.get(i).getPackage().getFlag().equalsIgnoreCase("1")) {
                    qtyArr.add(String.valueOf(this.adapter.getMinteger()));
                } else {
                    qtyArr.add("1");
                }
            }
            commaSeperatedIDs = TextUtils.join(",", packageIds);
            commaSeperatedName = TextUtils.join(",", packageName);
            commaSeperatedPrice = TextUtils.join(",", packagePrice);
            qty = TextUtils.join(",", qtyArr);

            Log.e("====PackageID===", commaSeperatedIDs);
            Log.e("====PackageName===", commaSeperatedName);
            Log.e("====PackagePrice===", commaSeperatedPrice);
            Log.e("====qty===", qty);
            ServerCall.UpdateApplyApplication(ServiceActivity.this, dialog, ServiceActivity.this, commaSeperatedIDs, commaSeperatedName, commaSeperatedPrice, qty, successPaymentId, applyId);
        }
    }

    private void intialize() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_USER, SharedClass.userModel.getFirstName() + " " + SharedClass.userModel.getLastName());
        bundle.putString(Constants.PARAM_EMAIL, SharedClass.userModel.getEmail());
        dialog = new ProgressDialog(ServiceActivity.this);
        pkgList = new ArrayList<>();
        rvPackages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if (action.equalsIgnoreCase("false")) {
            ArrayList<Package> packageList = intent.getParcelableArrayListExtra("packageList");
            adapter = new Packages_Adapter(this, this, packageList, paynow, shopper_id) {
                @Override
                protected void onSelectPackage() {
                    super.onSelectPackage();
                    txtTotal.setText(adapter.getTotalAmount() + "");
                }

                @Override
                protected void onPackageClicked(Package pkg, int position) {
                    super.onPackageClicked(pkg, position);
                    ibBack.setVisibility(View.VISIBLE);
                    llPackageDetails.setVisibility(View.VISIBLE);
                    llPackgesDetailsTitle.setVisibility(View.VISIBLE);
                    llPackges.setVisibility(View.GONE);
                    rvPackages.setVisibility(View.GONE);
                    bottom.setVisibility(View.VISIBLE);
//                    llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_gold));
                    List<PackagesDetail> packagesDetails = pkg.getPackagesDetail();
                    setPackageDetailsAdapter(packagesDetails, pkg);
                    txtTotal.setText(Math.round(pkg.getPackage().getPrice()) + "");
                    if (pkg.getPackage().getName().equalsIgnoreCase("Elite")) {
//                        logEvent(EVENT_ELITE_BUNDLE_SCREEN, bundle);
//                        logFirebaseEvent(EVENT_ELITE_BUNDLE_SCREEN.replace(" ","_").toLowerCase());
//                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_bundles));
                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Gold")) {
//                        logEvent(EVENT_GOLD_BUNDLE_SCREEN, bundle);
//                        logFirebaseEvent(EVENT_GOLD_BUNDLE_SCREEN.replace(" ","_").toLowerCase());
//                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_gold));
//                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Silver")) {
//                        logEvent(EVENT_SILVER_BUNDLE_SCREEN, bundle);
//                        logFirebaseEvent(EVENT_SILVER_BUNDLE_SCREEN.replace(" ","_").toLowerCase());
//                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_silver));
                    } else {
//                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_silver));
                    }
//                    ibBack.setVisibility(View.VISIBLE);
//                    llPackageDetails.setVisibility(View.VISIBLE);
//                    rvPackages.setVisibility(View.GONE);
//                    bottom.setVisibility(View.VISIBLE);
//                    llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_gold));
//                    List<PackagesDetail> packagesDetails = pkg.getPackagesDetail();
//                    setPackageDetailsAdapter(packagesDetails, pkg);
//                    txtTotal.setText(Math.round(pkg.getPackage().getPrice()) + "");
//                    if (pkg.getPackage().getName().equalsIgnoreCase("Elite")) {
////                        logEvent(EVENT_ELITE_BUNDLE_SCREEN, bundle);
////                        logFirebaseEvent(EVENT_ELITE_BUNDLE_SCREEN.replace(" ","_").toLowerCase());
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.ic_background_elite));
//                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Gold")) {
////                        logEvent(EVENT_GOLD_BUNDLE_SCREEN, bundle);
////                        logFirebaseEvent(EVENT_GOLD_BUNDLE_SCREEN.replace(" ","_").toLowerCase());
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.ic_background_gold));
//                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Silver")) {
////                        logEvent(EVENT_SILVER_BUNDLE_SCREEN, bundle);
////                        logFirebaseEvent(EVENT_SILVER_BUNDLE_SCREEN.replace(" ","_").toLowerCase());
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.ic_background_silver));
//                    } else {
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.ic_background_silver));
//                    }
////                    paynow.performClick();

                }

            };
        } else {

            adapter = new Packages_Adapter(this, this, pkgList, paynow, shopper_id) {
                @Override
                protected void onSelectPackage() {
                    super.onSelectPackage();
                    txtTotal.setText(adapter.getTotalAmount() + "");

                }

                @Override
                protected void onPackageClicked(Package pkg, int position) {
                    super.onPackageClicked(pkg, position);
//                    ibBack.setVisibility(View.VISIBLE);
//                    llPackageDetails.setVisibility(View.GONE);
//                    rvPackages.setVisibility(View.VISIBLE);
//                    llPackgesDetailsTitle.setVisibility(View.GONE);
//                    llPackges.setVisibility(View.GONE);
//                    bottom.setVisibility(View.GONE);
//                    llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_gold));
                    List<PackagesDetail> packagesDetails = pkg.getPackagesDetail();
                    setPackageDetailsAdapter(packagesDetails, pkg);
                    txtTotal.setText(Math.round(pkg.getPackage().getPrice()) + "");
                    if (pkg.getPackage().getName().equalsIgnoreCase("Elite")) {
//                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_bundles));
                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Gold")) {
//                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_gold));
                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Silver")) {
//                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_silver));
                    } else {
//                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_silver));
                    }
                    paynow.performClick();
//                    ibBack.setVisibility(View.VISIBLE);
//                    llPackageDetails.setVisibility(View.VISIBLE);
//                    rvPackages.setVisibility(View.GONE);
//                    bottom.setVisibility(View.VISIBLE);
////                    llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_gold));
//                    List<PackagesDetail> packagesDetails = pkg.getPackagesDetail();
//                    setPackageDetailsAdapter(packagesDetails, pkg);
//                    txtTotal.setText(Math.round(pkg.getPackage().getPrice()) + "");
////                    if (pkg.getPackage().getName().equalsIgnoreCase("Elite")) {
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_bundles));
////                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Gold")) {
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_gold));
////                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Silver")) {
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_silver));
////                    } else {
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.bg_silver));
////                    }
//
////                    List<PackagesDetail> packagesDetails = pkg.getPackagesDetail();
////                    setPackageDetailsAdapter(packagesDetails, pkg);
////                    txtTotal.setText(Math.round(pkg.getPackage().getPrice()) + "");
//                    if (pkg.getPackage().getName().equalsIgnoreCase("Elite")) {
////                        logEvent(EVENT_ELITE_BUNDLE_SCREEN, bundle);
////                        logFirebaseEvent(EVENT_ELITE_BUNDLE_SCREEN.replace(" ","_").toLowerCase());
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.ic_background_elite));
//                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Gold")) {
////                        logEvent(EVENT_GOLD_BUNDLE_SCREEN, bundle);
////                        logFirebaseEvent(EVENT_GOLD_BUNDLE_SCREEN.replace(" ","_").toLowerCase());
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.ic_background_gold));
//                    } else if (pkg.getPackage().getName().equalsIgnoreCase("Silver")) {
////                        logEvent(EVENT_SILVER_BUNDLE_SCREEN, bundle);
////                        logFirebaseEvent(EVENT_SILVER_BUNDLE_SCREEN.replace(" ","_").toLowerCase());
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.ic_background_silver));
//                    } else {
////                        llMainBackground.setBackground(getResources().getDrawable(R.drawable.ic_background_silver));
//                    }
//                    paynow.performClick();
//
                }
            };
        }

        rvPackages.setAdapter(adapter);
        Utility.showLoadingDialog(ServiceActivity.this, "" + getResources().getString(R.string.fatching_package));

        ServerCall.getPackages(this,SessionManager.get(Constants.LANGUAGE));

        FLAG_NUMBER = 1;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            if (intent.getScheme().equals("hyperpay")) {
                shopper_id = intent.getData().getQueryParameter("id");
                Log.e("shopperId", shopper_id);
                requestPaymentStatus(shopper_id);

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccessResponce(Object obj) {
        Utility.hideLoadingDialog();
        if (FLAG_NUMBER == 1) {
            PackagesResponse packagesResponse = ((Response<PackagesResponse>) obj).body();
            if (!packagesResponse.getError()) {
                for (int i = 0; i < packagesResponse.getPackages().size(); i++) {
                    Package pack = packagesResponse.getPackages().get(i);
                    pkgList.add(pack);
                }
                adapter.notifyDataSetChanged();
            }
        } else if (FLAG_NUMBER == 2) {
            final GenericResponce response = ((Response<GenericResponce>) obj).body();
            try {
                if (!response.getError()) {
                    Utility.showToast(ServiceActivity.this, response.getMessage());
                    ApplyModel.clearAll();
                    startActivity(new Intent(ServiceActivity.this, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    if (response.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(ServiceActivity.this);
                    } else {
                        /* Utility.showToast(ServiceActivity.this, response.getMessage());*/
                    }
                }
            } catch (Exception exp) {

            }
        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        Utility.hideLoadingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CheckoutActivity.REQUEST_CODE_CHECKOUT) {
            switch (resultCode) {
                case CheckoutActivity.RESULT_OK:
                    Transaction transaction = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION);
                    resourcePath = data.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH);

                    if (transaction.getTransactionType() == TransactionType.SYNC) {
                    } else {
                        if (hasCallbackScheme(getIntent())) {

                        } else {
                            /* Toast.makeText(ServiceActivity.this, "WAIT", Toast.LENGTH_SHORT).show();*/
                        }
                    }
                    break;
                case CheckoutActivity.RESULT_CANCELED:
                    /* shopper canceled the checkout process */
                    break;
                case CheckoutActivity.RESULT_ERROR:
                    /* error occurred */
                    PaymentError error = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_ERROR);
                    Toast.makeText(ServiceActivity.this, "error" + " " + error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    protected boolean hasCallbackScheme(Intent intent) {
        String scheme = intent.getScheme();

        return getString(R.string.checkout_ui_callback_scheme).equals(scheme) ||
                getString(R.string.payment_button_callback_scheme).equals(scheme) ||
                getString(R.string.custom_ui_callback_scheme).equals(scheme);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
