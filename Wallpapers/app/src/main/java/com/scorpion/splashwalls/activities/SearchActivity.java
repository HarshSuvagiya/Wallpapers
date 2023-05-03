package com.scorpion.splashwalls.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.iammert.library.ui.multisearchviewlib.MultiSearchView;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.SearchTabsPagerAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.fragments.SearchCollectionsFragment;
import com.scorpion.splashwalls.fragments.SearchPhotosFragment;
import com.scorpion.splashwalls.fragments.SearchUsersFragment;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.UserModel;

public class SearchActivity extends AppCompatActivity {

    MultiSearchView multiSearchView;
    public static String username1;
    private WallpaperService imageDetailService;
    UserModel userModel;
    ImageView profile;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    public static String searchItem;
    LinearLayout ll1;
    public static int totalPagePhotos;
    public static int totalPageLikes;
    public static int totalPageCollections;
    SearchPhotosFragment searchPhotosFragment = new SearchPhotosFragment();
    AdView adView;
    LinearLayout layBanner;
    FrameLayout ad_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AdHelper.LoadInter(this);
        adView = findViewById(R.id.adView);
        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.AdLoadHelper(SearchActivity.this,adView, layBanner);

        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);

        ll1 = findViewById(R.id.ll1);
        mTabLayout = findViewById(R.id.user_tabs);
        mViewPager = findViewById(R.id.user_viewpager);
        profile = findViewById(R.id.profile);
        SearchTabsPagerAdapter tabsPagerAdapter = new SearchTabsPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(tabsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        loadShowCase();
        multiSearchView = findViewById(R.id.multiSearchView);
        multiSearchView.setSearchViewListener(new MultiSearchView.MultiSearchViewListener() {
            @Override
            public void onTextChanged(int i, CharSequence charSequence) {
            }

            @Override
            public void onSearchComplete(int i, CharSequence charSequence) {
                searchItem = charSequence.toString();
                if(searchItem.length() != 0){
                    Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.user_viewpager + ":" + 0);
                    ((SearchPhotosFragment) page).loadFirstPage();

                    Fragment page2 = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.user_viewpager + ":" + 1);
                    ((SearchCollectionsFragment) page2).loadFirstPage();

                    Fragment page3 = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.user_viewpager + ":" + 2);
                    ((SearchUsersFragment)page3).loadFirstPage();

                    SearchPhotosFragment.newInstance();
                    SearchCollectionsFragment.newInstance();
//                Log.e("SEARCHonSearchComplete", String.valueOf(charSequence));
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                else
                    Toast.makeText(SearchActivity.this, "PLease enter value", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchItemRemoved(int i) {

            }

            @Override
            public void onItemSelected(int i, CharSequence charSequence) {
                searchItem = charSequence.toString();
                if(searchItem.length()!=0){
                    Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.user_viewpager + ":" + 0);
                    ((SearchPhotosFragment) page).loadFirstPage();

                    Fragment page2 = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.user_viewpager + ":" + 1);
                    ((SearchCollectionsFragment) page2).loadFirstPage();

                    Fragment page3 = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.user_viewpager + ":" + 2);
                    ((SearchUsersFragment)page3).loadFirstPage();

                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                else
                    Toast.makeText(SearchActivity.this, "PLease enter value", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadShowCase() {
        RelativeLayout.LayoutParams lps;
        ShowcaseView showCase;
        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, 80 + ((Number) (getResources().getDisplayMetrics().density * 5)).intValue());

        showCase = new ShowcaseView.Builder(this)
                .blockAllTouches()
                .singleShot(1)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .setTarget(new ViewTarget(R.id.multiSearchView, this))
                .setContentTitle("Search")
                .setContentText("Click on search icon to search photos...")
                .build();
        showCase.setButtonText("Next");
        showCase.setButtonPosition(lps);
        showCase.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;

            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
                        showCase.setTarget(new ViewTarget(R.id.user_tabs, SearchActivity.this));
                        showCase.setContentTitle("Tabs");
                        showCase.setContentText("Navigate through tabs to explore more.");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Next");
                        break;
                    case 2:
                        showCase.hide();
                        break;
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        AdHelper.ShowInter(SearchActivity.this);
        super.onBackPressed();
    }
}
