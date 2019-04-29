package net.bridgeint.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.bridgeint.app.fragments.FirstHelpFragment;
import net.bridgeint.app.fragments.FiveHelpFragment;
import net.bridgeint.app.fragments.FourHelpFragment;
import net.bridgeint.app.fragments.SecondHelpFragment;
import net.bridgeint.app.fragments.SixHelpFragment;
import net.bridgeint.app.fragments.ThirdHelpFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 6;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return FirstHelpFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return SecondHelpFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
                return ThirdHelpFragment.newInstance(2, "Page # 3");
            case 3: // Fragment # 1 - This will show SecondFragment
                return FourHelpFragment.newInstance(3, "Page # 4");
            case 4: // Fragment # 1 - This will show SecondFragment
                return FiveHelpFragment.newInstance(4, "Page # 5");
            case 5: // Fragment # 1 - This will show SecondFragment
                return SixHelpFragment.newInstance(5, "Page # 6");
            default:
                return FirstHelpFragment.newInstance(0, "Page # 1");
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
