package net.bridgeint.app.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.adapters.Search_Adapter;
import net.bridgeint.app.interfaces.ApiInterface;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.Paging;
import net.bridgeint.app.models.UniversityModel;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.SuggestKeyowrdResponse;
import net.bridgeint.app.responces.UniversitiesResponce;
import net.bridgeint.app.utils.Config;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static net.bridgeint.app.utils.Constants.EVENT_SEARCH_SCREEN;
import static net.bridgeint.app.utils.Constants.PARAM_EMAIL;
import static net.bridgeint.app.utils.Constants.PARAM_USER;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements ResponceCallback {

    @BindView(R.id.iv_profile)
    CircleImageView ivProfile;
    @BindView(R.id.txt_student_d)
    TextView txtStudentD;
    Unbinder unbinder;
    @BindView(R.id.backBtn)
    ImageView backBtn;
    @BindView(R.id.appbar)
    LinearLayout appbar;
    @BindView(R.id.ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.etSearch)
    AutoCompleteTextView etSearch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.header)
    LinearLayout header;
    @BindView(R.id.rvSearch)
    RecyclerView rvSearch;
    @BindView(R.id.no_university)
    TextView noUniversity;

    Toolbar toolbar;
    boolean getAllUniversities;
    private Search_Adapter adapter;
    //    TextView tvFound;

    List<UniversityModel> universityModel = new ArrayList<>();
    private TextView tv_noUniversity;
    private ImageView iv_clear;

    private int totalPages, currentPage, nextPage;
    private int visibleItemCount, pastVisiblesItems, totalItemCount;
    private LinearLayoutManager linearManager;

    ArrayAdapter<String> search_adpter;
    private AppEventsLogger logger;

    private boolean isHasNextPage;

    public SearchFragment() {
        // Required empty public constructor
    }

    Callback<SuggestKeyowrdResponse> suggestKeyowrdResponseCallback;
    private Call<SuggestKeyowrdResponse> suggestkeyCall;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_update, container, false);
        ButterKnife.bind(this, view);
        getIds(view);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            backBtn.setRotation(180);
        }
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_SEARCH_SCREEN.replace(" ", "_").toLowerCase());
//        if (ApplyModel.courseId != null) {
//            getAllUniversities = false;
//            String degreeId = ApplyModel.degreeId;
//            String courseId = ApplyModel.courseId;
//            String countryId = ApplyModel.countryId;
//            etSearch.setVisibility(View.GONE);
//            appbar.setVisibility(View.VISIBLE);
//            backBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().finish();
//                }
//            });
//            Utility.hideLoadingDialog();
//            Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_university));
//            ServerCall.getUniversities(getActivity(), SharedClass.userModel.getId().toString(), SharedClass.userModel.getAccessKey(), degreeId, courseId, countryId, this);
//        } else {
            getAllUniversities = true;
            Utility.hideLoadingDialog();
            Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_university));
            ServerCall.getAllUniversities(getActivity(), this, 1 + "");
//        }

        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER, SharedClass.userModel.getFirstName() + " " + SharedClass.userModel.getLastName());
        bundle.putString(PARAM_EMAIL,SharedClass.userModel.getEmail());

        BaseActivity.logEvent(EVENT_SEARCH_SCREEN, bundle);
        BaseActivity.logFirebaseEvent(EVENT_SEARCH_SCREEN.replace(" ","_").toLowerCase());

        return view;
    }

   /* public void logEvent(String eventName, Bundle param) {
        if (logger != null) {
            logger.logEvent(eventName, param);
        }
    }
    public void logEvent(String eventName) {
        if (logger != null) {
            logger.logEvent(eventName);
        }
    }*/

    private void getIds(View view) {
        rvSearch = view.findViewById(R.id.rvSearch);
//        tvFound = (TextView) view.findViewById(R.id.tvFound);
        tv_noUniversity = view.findViewById(R.id.no_university);
        etSearch = view.findViewById(R.id.etSearch);

        iv_clear = view.findViewById(R.id.iv_clear);

        linearManager = new LinearLayoutManager(getContext());
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);

        txtStudentD.setText(SharedClass.userModel.getId() + "");
        Picasso.with(getActivity()).load("http://188.226.178.195/tryAndApply/uploads/" + SharedClass.userModel.getImage()).error(R.drawable.user_placeholder).into(ivProfile);

        rvSearch.setLayoutManager(linearManager);
        adapter = new Search_Adapter(getActivity(), universityModel);
        rvSearch.setAdapter(adapter);

        etSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.i("hello", "Enter pressed");
                    if (etSearch.getText().toString().length() == 0) {
                        getAllUniversities = true;
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                        Toast.makeText(getActivity(), R.string.validation_search, Toast.LENGTH_SHORT).show();
                        Utility.hideLoadingDialog();
                        Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_university));
                        ServerCall.getAllUniversities(getActivity(), SearchFragment.this, "");
                    } else {
                        getAllUniversities = false;
                        search(etSearch.getText().toString(), "" +1);
                    }

                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    iv_clear.setVisibility(View.GONE);
                    Utility.hideLoadingDialog();
                    Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_university));
                    ServerCall.getAllUniversities(getActivity(), SearchFragment.this, 1 + "");
                } else {
                    iv_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    getSuggest(s.toString());
                }
            }
        });

        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != search_adpter) {
                    Log.e("Serach keyword", "==" + search_adpter.getItem(position));

                    String select_keyword = search_adpter.getItem(position);
                    getAllUniversities = false;
                    search(select_keyword, "" + 1) ;
                }

            }
        });

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                iv_clear.setVisibility(View.GONE);
                getAllUniversities = true;


            }
        });

        if (SharedClass.isApply) {
            etSearch.setVisibility(View.GONE);
            ((DashboardUpdateActivity) getActivity()).toolbarText.setText("Results");
        }

        rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    if (currentPage <= totalPages) {

                        visibleItemCount = linearManager.getChildCount();
                        totalItemCount = linearManager.getItemCount();
                        pastVisiblesItems = linearManager.findFirstVisibleItemPosition();

                        int lastPositon = linearManager.findLastCompletelyVisibleItemPosition();
                        Log.d("last_item", lastPositon + "");
                        if (getAllUniversities){
                            if (lastPositon == universityModel.size() - 1) {
                                if (isHasNextPage){
                                    Utility.hideLoadingDialog();
                                    Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_university));
                                    ServerCall.getAllUniversities(getActivity(), SearchFragment.this, nextPage + "");

                                }

                            }
                        }else {
                            if (lastPositon == universityModel.size() - 1) {

                                if (isHasNextPage){
                                    Utility.hideLoadingDialog();
                                    Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_university));
                                    ServerCall.searchUniversities(getActivity(),etSearch.getText().toString(), SearchFragment.this, nextPage + "");
                                }


                            }
                        }

                    }
                }
            }
        });

        suggestKeyowrdResponseCallback = new Callback<SuggestKeyowrdResponse>() {
            @Override
            public void onResponse(Call<SuggestKeyowrdResponse> call, Response<SuggestKeyowrdResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        search_adpter = new ArrayAdapter<String>
                                (getContext(), R.layout.list_item, R.id.text_item, response.body().getData());

                        etSearch.setThreshold(1);
                        etSearch.setAdapter(search_adpter);

                        adapter.notifyDataSetChanged();
                    }


                } else {
                    Utility.showToast(getActivity(), "");

                }
            }

            @Override
            public void onFailure(Call<SuggestKeyowrdResponse> call, Throwable t) {

            }
        };


    }

    private void search(String select_keyword, String page) {
//        Utility.showLoadingDialog(getActivity(), getString(R.string.fetch_university));
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        ServerCall.searchUniversities(getActivity(), select_keyword,  this,page);
    }

    @Override
    public void onSuccessResponce(Object obj) {
        Utility.hideLoadingDialog();
        UniversitiesResponce universitiesResponce = ((Response<UniversitiesResponce>) obj).body();
        try {
            if (!universitiesResponce.getError()) {
//                tvFound.setText(universitiesResponce.getUniversityModel().size()+" universities found");
                if (universitiesResponce.getUniversityModel().size() == 0) {
                    tv_noUniversity.setVisibility(View.VISIBLE);
                    universityModel.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    tv_noUniversity.setVisibility(View.GONE);

                    if (!isHasNextPage){
                        universityModel.clear();
                    }
                    universityModel.addAll(universitiesResponce.getUniversityModel());
                    adapter.notifyDataSetChanged();
                }
//                SharedClass.allUniversitiesList.clear();
                if (universitiesResponce.getPaging() != null) {
                    Paging paging = universitiesResponce.getPaging();
                    totalPages = paging.getNumberofpage();
                    currentPage = paging.getCurrentpage();
                    nextPage = paging.getCurrentpage() + 1;

                    isHasNextPage = totalPages > currentPage;
                }
                if (getAllUniversities) {
                    Log.e("Universities", new Gson().toJson(universitiesResponce.getUniversityModel()));
                    SharedClass.ParseUniversity(universitiesResponce.getUniversityModel());
                    SharedClass.allUniversitiesList.addAll(universitiesResponce.getUniversityModel());
                    universityModel.clear();
                    universityModel.addAll(SharedClass.allUniversitiesList);
                    adapter.notifyDataSetChanged();
                } else {
                     universityModel.clear();

                    if (adapter == null){
                        linearManager = new LinearLayoutManager(getContext());
                        linearManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rvSearch.setLayoutManager(linearManager);
                        adapter = new Search_Adapter(getActivity(), universityModel);
                        rvSearch.setAdapter(adapter);
                        universityModel.clear();
                    }

                    universityModel.addAll(universitiesResponce.getUniversityModel());
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (universitiesResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(getActivity());
                } else {
                    if (universitiesResponce.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(getActivity());
                    } else {
                        Utility.showToast(getActivity(), universitiesResponce.getMessage());
                    }
                }
            }

        } catch (Exception ex) {
            Utility.showToast(getActivity(), ex.getMessage());
        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        Utility.hideLoadingDialog();
        try {
            Response<UniversitiesResponce> response = (Response<UniversitiesResponce>) obj;
            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                SharedClass.logout(getActivity());
            } else {
                ErrorUtils.responseError(getActivity(), response);
            }
        } catch (Exception exp) {
        }
    }

    private void getSuggest(String s) {
        if (Utility.isNetConnected(getContext())) {
            if (suggestkeyCall != null && suggestkeyCall.isExecuted()) {
                suggestkeyCall.cancel();
            }

            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String accessToken = SharedClass.userModel.getAccessKey();

            suggestkeyCall = api.getSearchKeywod(s, accessToken, Config.API_KEY, "en", "1");
            suggestkeyCall.enqueue(suggestKeyowrdResponseCallback);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
