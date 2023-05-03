package com.scorpion.splashwalls.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.fragments.SearchCollectionsFragment;
import com.scorpion.splashwalls.fragments.SearchPhotosFragment;
import com.scorpion.splashwalls.fragments.SearchUsersFragment;

public class SearchTabsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES =
            new int[] { R.string.photos, R.string.collections, R.string.user };
    private final Context mContext;
    public SearchTabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchPhotosFragment.newInstance();
            case 1:
                return SearchCollectionsFragment.newInstance();
            case 2:
                return SearchUsersFragment.newInstance();
            default:
                return null;
        }
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString( TAB_TITLES[position]);
    }
    @Override
    public int getCount() {
        return 3;
    }
}