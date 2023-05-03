package com.scorpion.splashwalls.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.fragments.UserCollectionsFragment;
import com.scorpion.splashwalls.fragments.UserLikesFragment;
import com.scorpion.splashwalls.fragments.UserPhotosFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES =
            new int[] { R.string.photos, R.string.likes, R.string.collections };
    private final Context mContext;
    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UserPhotosFragment.newInstance();
            case 1:
                return UserLikesFragment.newInstance();
            case 2:
                return UserCollectionsFragment.newInstance();
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