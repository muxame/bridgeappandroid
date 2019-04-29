package net.bridgeint.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.models.UniversityModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class HowToApplyFragment extends BaseFragment {

    TextView countryCity,address,information, tvHowtoapply;
    UniversityModel universityModel = null;

    public HowToApplyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how_to_apply, container, false);
        countryCity = view.findViewById(R.id.tvCity);
        address = view.findViewById(R.id.tvAddress);
        information = view.findViewById(R.id.tvDescription);
        tvHowtoapply = view.findViewById(R.id.tvHowtoapply);
        universityModel = ApplyModel.universityModel;
        countryCity.setVisibility(View.GONE);
        address.setVisibility(View.GONE);
        if(universityModel!=null)
        {
            countryCity.setText(universityModel.getCity()+","+universityModel.getCountry());
            address.setText(universityModel.getAddress());
//            information.setText(universityModel.getInformation());
            tvHowtoapply.setText(universityModel.getHowToApply());
            information.setMovementMethod(new ScrollingMovementMethod());
        }
        BaseActivity.logEvent(Constants.EVENT_UNIVERSITY_ENTRY_REQUIRMENTS,BaseActivity.getUserDetails());
        BaseActivity.logFirebaseEvent(Constants.EVENT_UNIVERSITY_ENTRY_REQUIRMENTS.replace(" ","_").toLowerCase());
        return view;
    }

}
