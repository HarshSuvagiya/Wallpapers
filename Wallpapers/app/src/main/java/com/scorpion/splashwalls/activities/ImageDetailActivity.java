package com.scorpion.splashwalls.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.scorpion.splashwalls.BuildConfig;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.ImageRelatedAdapter;
import com.scorpion.splashwalls.adapters.SearchCollectionsAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.dialog.WallpaperDialog;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.helpers.DownloadHelper;
import com.scorpion.splashwalls.models.ImageDetailModel;
import com.scorpion.splashwalls.models.ImageRelatedModel;
import com.scorpion.splashwalls.models.ImageStateModel;
import com.scorpion.splashwalls.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.scorpion.splashwalls.helpers.DownloadHelper.DownloadType.DOWNLOAD;
import static com.scorpion.splashwalls.helpers.DownloadHelper.DownloadType.WALLPAPER;

public class ImageDetailActivity extends AppCompatActivity {

    public static String DOWNLOAD_PATH;
    String image_id;
    boolean isFav = false;
    private @DownloadHelper.DownloadType
    int currentAction;
    private WallpaperService imageDetailService;
    ArrayList<ImageDetailModel> list = new ArrayList<>();
    ImageDetailModel imageDetailModel;
    ImageStateModel imageStateModel;
    private long downloadReference;
    int progressMax = 100;
    ImageView image;
    NotificationManagerCompat notificationManager1;
    CircleImageView profile;
    TextView username, location, description, publishedOn;
    TextView camera, model, focal, aperture, shutter, iso, dimensions;
    TextView downloads, downloadsLastMonth, views, viewsLastMonth, contributor;
    LinearLayout cameraDetail;
    LinearLayout ll1;
    Notification.Builder builder;
    ImageRelatedModel imageRelatedModel;
    ImageRelatedAdapter imageRelatedAdapter;
    ImageView downloadBtn, setWallpaperBtn, setFavBtn;
    private WallpaperDialog wallpaperDialog;
    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    CardView relatedBtn;
    String key = "MyFavImages";
    ArrayList<ImageDetailModel> lstArrayList = new ArrayList<>();
    LinearLayout userProfile;
    String relativePath = Environment.getExternalStorageDirectory().getPath() + "/Splash Walls/";
    FrameLayout ad_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        AdHelper.LoadInter(this);
        loadShowCase();
        image_id = getIntent().getStringExtra("image_id");
//        Log.e("image_id", image_id);
        DOWNLOAD_PATH = "/" + getResources().getString(R.string.app_name) + "/";
        setFavBtn = findViewById(R.id.setFavBtn);
        setWallpaperBtn = findViewById(R.id.setWallpaperBtn);
        userProfile = findViewById(R.id.userProfile);
        relatedBtn = findViewById(R.id.relatedBtn);
        downloadBtn = findViewById(R.id.downloadBtn);
        ll1 = findViewById(R.id.ll1);
        cameraDetail = findViewById(R.id.cameraDetail);
        image = findViewById(R.id.image);
        profile = findViewById(R.id.profile);
        username = findViewById(R.id.username);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);
        publishedOn = findViewById(R.id.publlishedOn);
        camera = findViewById(R.id.camera);
        model = findViewById(R.id.model);
        focal = findViewById(R.id.focal);
        aperture = findViewById(R.id.aperture);
        shutter = findViewById(R.id.shutterSpeed);
        contributor = findViewById(R.id.contributor);
        iso = findViewById(R.id.iso);
        dimensions = findViewById(R.id.dimensions);
        downloads = findViewById(R.id.downloads);
        downloadsLastMonth = findViewById(R.id.lastMonthDownloads);
        views = findViewById(R.id.views);
        viewsLastMonth = findViewById(R.id.lastMonthViews);
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);
        shref = getSharedPreferences("MyFavImages", Context.MODE_PRIVATE);

        ad_view_container = findViewById(R.id.ad_view_container);

        File folder = new File((Environment.getExternalStorageDirectory() +
                File.separator + getResources().getString(R.string.app_name)));
        if (!folder.exists()) {
//            Log.e("ONACT", folder.getAbsolutePath());
            folder.mkdirs();
        }


//        ad_view_container.setVisibility(View.VISIBLE);
//        AdView mAdView = new AdView(getApplicationContext());
//        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId(getString(R.string.banner_id));
//        ad_view_container.addView(mAdView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        PRDownloader.initialize(getApplicationContext());
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserActivity.class).putExtra("username", imageDetailModel.getUser().getUsername()));
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AdHelper.ShowInter(ImageDetailActivity.this);
                currentAction = DOWNLOAD;
                downloadImage(imageDetailModel.getUrls().getRaw(), DOWNLOAD);
            }
        });

        setWallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdHelper.ShowInter(ImageDetailActivity.this);
                currentAction = WALLPAPER;
                downloadImage(imageDetailModel.getUrls().getRaw(), WALLPAPER);
            }
        });

        relatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ImageDetailActivity.this, R.style.RelatedDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.related_dialog);
                dialog.show();
                dialog.getWindow().getAttributes().windowAnimations = R.style.RelatedDialogAnimation;

                RecyclerView relatedRecycler;
                relatedRecycler = dialog.findViewById(R.id.relatedRecycler);

                LottieAnimationView relatedLoading;
                relatedLoading = dialog.findViewById(R.id.relatedLoading);

                getImageDetailsRelatedApi().enqueue(new Callback<ImageRelatedModel>() {
                    @Override
                    public void onResponse(Call<ImageRelatedModel> call, Response<ImageRelatedModel> response) {
                        imageRelatedModel = response.body();

                        if (response.body() != null) {
                            imageRelatedAdapter = new ImageRelatedAdapter(imageRelatedModel.getResults(), ImageDetailActivity.this);
                            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
                            manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

                            relatedRecycler.setLayoutManager(manager);
                            relatedRecycler.setAdapter(imageRelatedAdapter);
                            relatedLoading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ImageRelatedModel> call, Throwable t) {
//                        Log.e("fail", String.valueOf(t));
                    }
                });

            }
        });

        Gson gson = new Gson();
        String response = shref.getString(key, "");

        if (gson.fromJson(response, new TypeToken<List<ImageDetailModel>>() {
        }.getType()) != null)
            lstArrayList = gson.fromJson(response, new TypeToken<List<ImageDetailModel>>() {
            }.getType());

        setFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFav = !isFav;
                if (isFav) {
                    setFavBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
                    MainActivity.MyFavImages.add(imageDetailModel);
                    lstArrayList.add(imageDetailModel);

                    Gson gson = new Gson();
                    String json = gson.toJson(lstArrayList);
                    editor = shref.edit();
//                    editor.remove(key).commit();
                    editor.putString(key, json);
                    editor.commit();

//                    lstArrayList.addAll(MainActivity.MyFavImages);

//                    for (int i = 0; i < MainActivity.MyFavImages.size(); i++)
//                        Log.e("FavID", MainActivity.MyFavImages.get(i).getId());

                } else {
                    setFavBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                    for (int i = 0; i < lstArrayList.size(); i++)
                        if (lstArrayList.get(i).getId().equals(imageDetailModel.getId())) {
                            lstArrayList.remove(i);
                            break;
                        }

                    Gson gson = new Gson();
                    String json = gson.toJson(lstArrayList);
                    editor = shref.edit();
//                    editor.remove(key).commit();
                    editor.putString(key, json);
                    editor.commit();

//                    for (int i = 0; i < lstArrayList.size(); i++)
//                        Log.e("FavIDRemove", lstArrayList.get(i).getId());
                }
            }
        });

        getImageDetailsApi().enqueue(new Callback<ImageDetailModel>() {
            @Override
            public void onResponse(Call<ImageDetailModel> call, Response<ImageDetailModel> response) {

                imageDetailModel = response.body();
//                Log.e("HEADERS", response.headers().get("X-Ratelimit-Limit"));
//                Log.e("HEADERS", response.headers().get("X-Ratelimit-Remaining"));


                if (response.body() != null) {
                    contributor.setText("Photo by " + imageDetailModel.getUser().getName() + " on Unsplash");
                    try {
                        for (int i = 0; i < lstArrayList.size(); i++)
                            if (lstArrayList.get(i).getId().equals(imageDetailModel.getId())) {
                                setFavBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
                                isFav = true;
                            }
                    } catch (Exception e) {
                    }

                    Picasso.get().load(imageDetailModel.getUrls().getRegular())
                            .into(image, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
//                        progressDialog.dismiss();
                                    ll1.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(imageDetailModel.getUrls().getRegular()).into(image);
                                }
                            });
                    Picasso.get().load(imageDetailModel.getUser().getProfileImage().getLarge()).into(profile);
                    username.setText(imageDetailModel.getUser().getName());
                    description.setText((imageDetailModel.getDescription()));
                    location.setText(imageDetailModel.getLocation().getName());
                    if (description.getText().toString().length() == 0)
                        description.setVisibility(View.GONE);
                    if (location.getText().toString().length() == 0)
                        location.setVisibility(View.GONE);

                    String strCurrentDate = imageDetailModel.getCreatedAt().split("T")[0];
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    Date newDate = null;
                    try {
                        newDate = format.parse(strCurrentDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    format = new SimpleDateFormat("MMMM dd , yyyy");
                    String date = format.format(newDate);
//                System.out.println("Date is: "+date);

                    publishedOn.setText("Published on " + date);
                    camera.setText(imageDetailModel.getExif().getMake());
                    model.setText(imageDetailModel.getExif().getModel());
                    focal.setText(imageDetailModel.getExif().getFocalLength() + "mm");
                    aperture.setText("f/" + imageDetailModel.getExif().getAperture());
                    shutter.setText(imageDetailModel.getExif().getExposureTime() + "s");
                    iso.setText(String.valueOf(imageDetailModel.getExif().getIso()));
                    dimensions.setText(imageDetailModel.getWidth() + " x " + imageDetailModel.getHeight());

                    if (iso.getText().toString().equals("null")) {
                        cameraDetail.setVisibility(View.GONE);
                    }

                    getImageDetailsStateApi().enqueue(new Callback<ImageStateModel>() {
                        @Override
                        public void onResponse(Call<ImageStateModel> call, Response<ImageStateModel> response) {
//                        Log.e("response", String.valueOf((Double.parseDouble(response.body().getDownloads().getTotal()))));
//                        Log.e("response", String.valueOf(format(Double.parseDouble(response.body().getDownloads().getTotal()))));

                            if (response.body() != null) {
                                imageStateModel = response.body();
                                downloads.setText(String.valueOf(format(Double.parseDouble(imageStateModel.getDownloads().getTotal()))) + "");
                                downloadsLastMonth.setText(String.valueOf("+" + format(Double.parseDouble(imageStateModel.getDownloads().getHistorical().getChange()))) + " last month");
                                views.setText(String.valueOf(format(Double.parseDouble(imageStateModel.getViews().getTotal()))) + "");
                                viewsLastMonth.setText(String.valueOf("+" + format(Double.parseDouble(imageStateModel.getViews().getHistorical().getChange()))) + " last month");
                            }
                        }

                        @Override
                        public void onFailure(Call<ImageStateModel> call, Throwable t) {

                        }
                    });
                }


//                Log.e("desc",imageDetailModel.getDescription());
//                progressBar.setVisibility(View.GONE);
//                adapter.addAll(list);

            }

            @Override
            public void onFailure(Call<ImageDetailModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void setNotification(String filename,String url) {
//        NotificationManagerCompat notificationManager1;
        notificationManager1 = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= 26) {
//            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel("noni", "system", 4);
            notificationManager1.createNotificationChannel(notificationChannel);
            Context context;
            builder = new Notification.Builder(this, notificationChannel.getId());
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Download")
                    .setContentText("Download in progress")
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setProgress(progressMax, 0, true);
            notificationManager1.notify(8888,builder.build());
//            notificationManager.notify(8888, new Notification.Builder(this, notificationChannel.getId())
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("Download")
//                    .setContentText("Download in progress")
//                    .setOngoing(true)
//                    .setOnlyAlertOnce(true)
//                    .setProgress(progressMax, 0, true)
//                    .build());
            return;
        }
//        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle(getResources().getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Download")
                .setContentText("Download in progress")
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(progressMax, 0, true);
        notificationManager1.notify(8888, builder.build());

    }

    private static String[] suffix = new String[]{"", "k", "m", "b", "t"};
    private static int MAX_LENGTH = 4;

    public static String format(double value) {
        try {
            int power;
            String suffix = " kmbt";
            String formattedNumber = "";
//            Log.e("format", String.valueOf(value));
            NumberFormat formatter = new DecimalFormat("#,###.#");
            power = (int) StrictMath.log10(value);
            value = value / (Math.pow(10, (power / 3) * 3));
            formattedNumber = formatter.format(value);
            formattedNumber = formattedNumber + suffix.charAt(power / 3);
            return formattedNumber.length() > 4 ? formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
        } catch (Exception e) {
            return "0";
        }
    }

    private Call<ImageDetailModel> getImageDetailsApi() {
        return imageDetailService.getImageDetails(
                image_id,
                Utils.myKey
        );
    }

    private Call<ImageStateModel> getImageDetailsStateApi() {
        return imageDetailService.getImageDetailsState(
                image_id,
                Utils.myKey
        );
    }

    private Call<ImageRelatedModel> getImageDetailsRelatedApi() {
        return imageDetailService.getImageDetailsRelated(
                image_id,
                Utils.myKey
        );
    }

    private void downloadImage(String url, @DownloadHelper.DownloadType int downloadType) {
        String filename = getResources().getString(R.string.app_name) + " " + image_id + ".jpg";
        if (DownloadHelper.getInstance(this).fileExists(filename)) {
            if (downloadType == WALLPAPER) {
                Uri uri = FileProvider.getUriForFile(ImageDetailActivity.this,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        new File(Environment.getExternalStorageDirectory() + ImageDetailActivity.DOWNLOAD_PATH + filename));
                setWallpaper(uri);
                setNotification(filename,url);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.file_exists), Toast.LENGTH_LONG).show();
            }
        } else {
            if (downloadType == WALLPAPER) {
                wallpaperDialog = new WallpaperDialog();
                wallpaperDialog.setListener(() -> DownloadHelper.getInstance(ImageDetailActivity.this).removeDownloadRequest(downloadReference));
                wallpaperDialog.setCancelable(false);
                wallpaperDialog.show(getFragmentManager(), null);
            } else {
                setNotification(filename,url);
                Toast.makeText(getApplicationContext(), getString(R.string.download_started), Toast.LENGTH_SHORT).show();
            }

            downloadReference = PRDownloader.download(url, relativePath, filename)
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {
                            Log.d("URL",url);
                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {

                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                            long percentage = (progress.currentBytes / progress.totalBytes);
                            try {
                                builder.setProgress(progressMax, Integer.parseInt(String.valueOf(percentage)), true);
                                notificationManager1.notify(8888, builder.build());
                            }
                            catch (Exception e){}
//                            Log.e("PROGRESS", String.valueOf(progress.currentBytes));
//                            Log.e("PROGRESS", String.valueOf(progress.totalBytes));
//                            Log.e("PROGRESS", String.valueOf(percentage));
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            try {
                                notificationManager1.cancel(8888);
//                            Log.e("onDownloadComplete","onDownloadComplete");
                                builder.setContentText("Download finished")
                                        .setProgress(0, 0, false)
                                        .setOngoing(false);
                                notificationManager1.notify(8888, builder.build());
                            }
                            catch (Exception e){}
                            File file = new File(relativePath + filename);
                            Uri uri = FileProvider.getUriForFile(ImageDetailActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                            if (currentAction == WALLPAPER) {
                                setWallpaper(uri);
                                if (wallpaperDialog != null) wallpaperDialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(Error error) {

                        }
                    });

//            mService.reportDownload(mPhoto.id, mReportDownloadListener);
//            downloadReference = DownloadHelper.getInstance(this).addDownloadRequest(getApplicationContext(),downloadType, url, filename);
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

//            Log.e("REF1", String.valueOf(downloadReference));
//            Log.e("REF2", String.valueOf(reference));

            if (downloadReference == reference) {
                Cursor cursor = DownloadHelper.getInstance(ImageDetailActivity.this).getDownloadCursor(downloadReference);
                if (cursor != null) {
                    if (DownloadHelper.getInstance(ImageDetailActivity.this).getDownloadStatus(cursor) == DownloadHelper.DownloadStatus.SUCCESS) {
                        File file = new File(DownloadHelper.getInstance(ImageDetailActivity.this).getFilePath(downloadReference));
                        Uri uri = FileProvider.getUriForFile(ImageDetailActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                        getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                        if (currentAction == WALLPAPER) {
                            setWallpaper(uri);
                            if (wallpaperDialog != null) wallpaperDialog.setDownloadFinished(true);
                        }
                    }
                    cursor.close();
                }
                if (currentAction == WALLPAPER) {
                    if (wallpaperDialog != null) wallpaperDialog.dismiss();
                }
            }
        }
    };


    private void setWallpaper(Uri uri) {
        try {
//            Log.d(TAG, "Crop and Set: " + uri.toString());
            Intent wallpaperIntent = WallpaperManager.getInstance(ImageDetailActivity.this).getCropAndSetWallpaperIntent(uri);
            wallpaperIntent.setDataAndType(uri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            startActivityForResult(wallpaperIntent, 13451);
//                mFirebaseAnalytics.logEvent("set_wallpaper", null);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d(TAG, "Chooser: " + uri.toString());
            Intent wallpaperIntent = new Intent(Intent.ACTION_ATTACH_DATA);
            wallpaperIntent.setDataAndType(uri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            wallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT);
            wallpaperIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(Intent.createChooser(wallpaperIntent, getString(R.string.set_as_wallpaper)));
//                mFirebaseAnalytics.logEvent("set_wallpaper_alternative", null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    @Override
    public void onBackPressed() {
        AdHelper.ShowInter(ImageDetailActivity.this);
        super.onBackPressed();
    }

    private void loadShowCase() {
        RelativeLayout.LayoutParams lps;
        ShowcaseView showCase;
        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, 80 + ((Number) (getResources().getDisplayMetrics().density * 5)).intValue());

        showCase = new ShowcaseView.Builder(this)
                .blockAllTouches()
                .singleShot(2)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .setTarget(new ViewTarget(R.id.username, this))
                .setContentTitle("User")
                .setContentText("Click here to het more information about user...")
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
                        showCase.setTarget(new ViewTarget(R.id.relatedBtn, ImageDetailActivity.this));
                        showCase.setContentTitle("Related Images");
                        showCase.setContentText("To explore more of similiar content.");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Done");
                        break;
                    case 2:
                        showCase.hide();
                        break;
                }
            }

        });
    }

}

