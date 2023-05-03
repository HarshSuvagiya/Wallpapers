package com.scorpion.splashwalls.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.HomePaginationAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.HomeModel;
import com.scorpion.splashwalls.models.ImageDetailModel;
import com.scorpion.splashwalls.utils.PaginationScrollListener;
import com.scorpion.splashwalls.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 2803;
    private static final String TAG = "MainActivity";
    private static final int PAGE_START = 1;
    HomePaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    //    ProgressBar progressBar;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 300;
    private int currentPage = PAGE_START;
    ArrayList<HomeModel> list = new ArrayList<>();
    LinearLayout account;
    LottieAnimationView lav_actionBar;
    SwipeRefreshLayout swipeRefreshLayout;
    private WallpaperService homeService;
    ImageView filter;
    LinearLayout favImage;
    RadioButton latest, oldest, popular;
    RadioGroup radioGroup;
    String filterFlag = "latest";
    public static ArrayList<ImageDetailModel> MyFavImages = new ArrayList<>();
    public static Activity mainAct;
    ShowcaseView showCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPermission()) {
            createfolder();
        }

        AdHelper.LoadInter(MainActivity.this);
        mainAct = this;
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        if (Utils.myKey.equals("eX87UpxqD4p60iSwNArHr_vfK5KAL7319kiYVOsLJUU")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FirebaseApp.initializeApp(getApplicationContext());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("SplashWalls").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Utils.myKey = dataSnapshot.child("API").getValue().toString();
                            loadFirstPage();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }, 1500);
        }

        rv = (RecyclerView) findViewById(R.id.main_recycler);
//        progressBar = (ProgressBar) findViewById(R.id.main_progress);
//        account = findViewById(R.id.account);
        favImage = findViewById(R.id.favImage);
        filter = findViewById(R.id.filter);
        lav_actionBar = findViewById(R.id.lav_actionBar);
        adapter = new HomePaginationAdapter(this);
        swipeRefreshLayout = findViewById(R.id.swipe);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        int firstVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        requestRead();
        lav_actionBar.playAnimation();

        loadShowCase();

        findViewById(R.id.collectionsImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdHelper.ShowInter(MainActivity.this);
                AdHelper.LoadInter(MainActivity.this);
                startActivity(new Intent(getApplicationContext(), CollectionsActivity.class));
            }
        });

        findViewById(R.id.autoWallpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdHelper.ShowInter(MainActivity.this);
                AdHelper.LoadInter(MainActivity.this);
                startActivity(new Intent(getApplicationContext(), AutoWallpaperActivity.class));
            }
        });

        findViewById(R.id.searchImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openFolder();
                AdHelper.ShowInter(MainActivity.this);
                AdHelper.LoadInter(MainActivity.this);
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });

        findViewById(R.id.curatedPhotos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdHelper.ShowInter(MainActivity.this);
                AdHelper.LoadInter(MainActivity.this);
                startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                sharingIntent
//                        .putExtra(
//                                android.content.Intent.EXTRA_TEXT,
//                                "Try this awesome application for wallpapers -> "
//                                        + getResources().getString(
//                                        R.string.app_name)
//                                        + " .click the link to download now http://play.google.com/store/apps/details?id="
//                                        + getPackageName());
//                startActivity(Intent.createChooser(sharingIntent,
//                        "Share using"));
            }
        });

        findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/splash-walls-privacy-policy/home"));
//                startActivity(browserIntent);
            }
        });

        favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdHelper.ShowInter(MainActivity.this);
                AdHelper.LoadInter(MainActivity.this);
                startActivity(new Intent(getApplicationContext(), MyFavImagesActivity.class));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFirstPage();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdHelper.ShowInter(MainActivity.this);
                AdHelper.LoadInter(MainActivity.this);
                final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.filter_dialog);

                latest = dialog.findViewById(R.id.latest);
                oldest = dialog.findViewById(R.id.oldest);
                popular = dialog.findViewById(R.id.popular);

//                Log.e("filterFlag", filterFlag);

                if (filterFlag.equals("latest"))
                    latest.setChecked(true);
                else if (filterFlag.equals("oldest"))
                    oldest.setChecked(true);
                else
                    popular.setChecked(true);


                latest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            filterFlag = "latest";
                            loadFirstPage();
                            dialog.dismiss();
                        }
                    }
                });

                oldest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            filterFlag = "oldest";
                            loadFirstPage();
                            dialog.dismiss();
                        }
                    }
                });

                popular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            filterFlag = "popular";
                            loadFirstPage();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

        if (!isNetworkAvailable()) {
            Context context;
            Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            dialog.setContentView(R.layout.no_internet);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            TextView retry;
            retry = dialog.findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                createFolder();
                File folder = new File((Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)));
                if (!folder.exists()) {
//            Log.e("ONACT", folder.getAbsolutePath());
                    folder.mkdirs();
                }
            }
        }, 5000);

//        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                int firstVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//                Log.e("firstVisiblePosition", String.valueOf(firstVisiblePosition));
//            }
//        });

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        homeService = WallpaperApi.getClient().create(WallpaperService.class);

        loadFirstPage();

    }

    private void loadShowCase() {
        RelativeLayout.LayoutParams lps;

        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, 80 + ((Number) (getResources().getDisplayMetrics().density * 5)).intValue());

        showCase = new ShowcaseView.Builder(this)
                .blockAllTouches()
                .singleShot(0)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .hideOnTouchOutside()
                .setTarget(new ViewTarget(R.id.searchImageView, this))
                .setContentTitle("Search")
                .setContentText("Search your favorites photos with " +
                        "advanced search technology\n")
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
                        showCase.setTarget(new ViewTarget(R.id.collectionsImageView, MainActivity.this));
                        showCase.setContentTitle("Collections");
                        showCase.setContentText("Amazing collections powered by unsplash!!!");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Next");
                        break;
                    case 2:
                        showCase.setTarget(new ViewTarget(R.id.curatedPhotos, MainActivity.this));
                        showCase.setContentTitle("Categories");
                        showCase.setContentText("An collections of some fabulous categories...");
                        showCase.setButtonPosition(lps);
                        showCase.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);
                        showCase.setButtonText("Next");
                        break;
                    case 3:
                            showCase.setTarget(new ViewTarget(R.id.favImage, MainActivity.this));
                        showCase.setContentTitle("Favourites");
                        showCase.setContentText("Make your own local collections by adding them into favorites.");
                        showCase.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Next");
                        break;
                    case 4:
                        showCase.setTarget(new ViewTarget(R.id.autoWallpaper, MainActivity.this));
                        showCase.setContentTitle("Auto wallpaper");
                        showCase.setContentText("Enabled auto wallpaper service to change wallpaper as per time configured.");
                        showCase.setButtonPosition(lps);
                        showCase.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
                        showCase.setButtonText("Next");
                        break;
                    case 5:
                        showCase.setTarget(new ViewTarget(R.id.filter, MainActivity.this));
                        showCase.setContentTitle("Sort by");
                        showCase.setContentText("Sort the entire bunch of photos by latest,oldest,popular.");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Next");
                        break;
                    case 6:
                        showCase.setTarget(new ViewTarget(R.id.main_recycler, MainActivity.this));
                        showCase.setContentTitle("Contributor");
                        showCase.setContentText("Tap of contributor name to get all information.");
                        showCase.setButtonPosition(lps);
                        showCase.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
                        showCase.setButtonText("Done");
                        break;
                    case 7:
                        showCase.hide();
                        break;
                }
            }

        });
    }

    private void loadFirstPage() {
//        Log.d(TAG, "loadFirstPage: ");
        list.clear();
        getHomePageWallpapersApi().enqueue(new Callback<ArrayList<HomeModel>>() {
            @Override
            public void onResponse(Call<ArrayList<HomeModel>> call, Response<ArrayList<HomeModel>> response) {

                if (response.body() != null) {
                    adapter.clear();
                    list = response.body();
//                progressBar.setVisibility(View.GONE);
                    lav_actionBar.setVisibility(View.GONE);
//                Log.e("size", String.valueOf(list.size()));
//                    Log.e("firstVisiblePosition", String.valueOf(Utils.myKey));
//                Log.e("size", String.valueOf(list.get(0).getId()));
                    adapter.addAll(list);
                    swipeRefreshLayout.setRefreshing(false);
                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }
//
//                if (currentPage <= TOTAL_PAGES)
//                    adapter.addLoadingFooter();
//                else
//                    isLastPage = true;
            }

            @Override
            public void onFailure(Call<ArrayList<HomeModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void loadNextPage() {
//        Log.d(TAG, "loadNextPage: " + currentPage);

        getHomePageWallpapersApi().enqueue(new Callback<ArrayList<HomeModel>>() {
            @Override
            public void onResponse(Call<ArrayList<HomeModel>> call, Response<ArrayList<HomeModel>> response) {

                if (response.body() != null) {
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    List<HomeModel> results = response.body();
                    adapter.addAll(results);
//
                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<HomeModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Call<ArrayList<HomeModel>> getHomePageWallpapersApi() {
        return homeService.getHomePageWallpapers(
                Utils.myKey,
                30,
                filterFlag,
                currentPage
        );
    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            Log.e("REQUEST","REQUEST");
////            Intent intent = getIntent();
////            finish();
////            startActivity(intent);
////            overridePendingTransition(0, 0);
//            createFolder();
//        }
//
//    }

    public void createFolder() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.e("ONACT", "ONACT1");
                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + getResources().getString(R.string.app_name) + File.separator);
                boolean success = true;
                if (!folder.exists()) {
                    folder.mkdirs();
                }
            }
        }, 4000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
//            Log.e("ONACT", "ONACT");
        }
    }

    public void openFolder() {
        File root = new File(Environment.getExternalStorageDirectory().getPath()
                + "/Splash Walls/");
        Uri uri = Uri.fromFile(root);

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivityForResult(intent, 1);
    }

    private boolean checkWriteExternalPermission() {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.e("handler", "destroy1");
//        startService(new Intent(getApplicationContext(),MyService.class));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseApp.initializeApp(getApplicationContext());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("SplashWalls").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utils.myKey = dataSnapshot.child("API").getValue().toString();
                Utils.adPos = Integer.parseInt(dataSnapshot.child("AdPerItem").getValue().toString());
                Utils.loadAd = Boolean.parseBoolean(dataSnapshot.child("LoadAd").getValue().toString());
                Utils.AdmobFacebook = Integer.parseInt(dataSnapshot.child("AdmobFacebook").getValue().toString());
                Utils.TimesInterAd = Integer.parseInt(dataSnapshot.child("TimesInterAd").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createFolder();
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle((CharSequence) "Permission necessary");
            builder.setMessage((CharSequence) "Write Storage permission is necessary !!!");
            builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MainActivity.MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            builder.create().show();
            return false;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        return false;
    }

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle((CharSequence) "Permission necessary");
            builder.setMessage((CharSequence) "Write Storage permission is necessary  !!!");
            builder.setPositiveButton((CharSequence) "Allow", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MainActivity.MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            builder.create().show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
//        if (i == 2803) {
//            if (iArr.length <= 0 || iArr[0] != 0) {
//                checkAgain();
//            } else {
//                createfolder();
//            }
//        }
    }

    private void createfolder() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append("/");
        sb.append(getString(R.string.app_name));
        File file = new File(sb.toString());
        boolean exists = file.exists();
        if (!exists) {
            exists = file.mkdir();
        }
        if (exists) {
            Log.d("Folder", "Already Created");
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(file);
        Log.d("dir:", sb2.toString());
    }

    @Override
    public void onBackPressed() {

//        AdHelper.ShowInter(MainActivity.this);
//        finish();
        super.onBackPressed();
    }
}
