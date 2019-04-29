package net.bridgeint.app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.HelpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FourHelpFragment extends BaseFragment {
    View view;
    @BindView(R.id.btn_next)
    AppCompatButton btnNext;
    Unbinder unbinder;

    public static Fragment newInstance(int i, String s) {
        FourHelpFragment thirdHelpFragment = new FourHelpFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", i);
        args.putString("someTitle", s);
        thirdHelpFragment.setArguments(args);
        return thirdHelpFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_help_page_four, container,false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        ((HelpActivity) getActivity()).stepNext();
    }
}
