package net.bridgeint.app.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.DashboardUpdateActivity;

public abstract class BaseFragment extends Fragment {
    public void replaceFragment(Fragment fragment, String transactionTag) {
        if (getActivity() instanceof DashboardUpdateActivity) {
            if (fragment instanceof StepOneLevelsOfstudy) {
                ((DashboardUpdateActivity) getActivity()).backbuttonHide();
            } else {
                ((DashboardUpdateActivity) getActivity()).backButtonshow();
            }
        } else {

        }

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        transaction.addToBackStack(transactionTag)
                .replace(R.id.container, fragment)
                .commit();

//        updateToolbar(transactionTag);
    }

}
