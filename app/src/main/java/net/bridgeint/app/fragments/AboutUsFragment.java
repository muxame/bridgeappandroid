package net.bridgeint.app.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static net.bridgeint.app.utils.Constants.EVENT_ABOUT_TRY_APPY_SCREEN;

public class AboutUsFragment extends BaseFragment {
    View view;
    @BindView(R.id.backBtn)
    ImageView backBtn;
    @BindView(R.id.appbar)
    LinearLayout appbar;
    @BindView(R.id.contactBtn)
    Button contactBtn;
    @BindView(R.id.fbBtn)
    ImageButton fbBtn;
    @BindView(R.id.twitterBtn)
    ImageButton twitterBtn;
    @BindView(R.id.instagramBtn)
    ImageButton instagramBtn;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar"))
        {
            backBtn.setRotation(180);
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_ABOUT_TRY_APPY_SCREEN.replace(" ", "_").toLowerCase());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
