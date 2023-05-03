package com.scorpion.splashwalls.api;

import androidx.annotation.Keep;

import com.scorpion.splashwalls.models.CuratedPhotosModel;
import com.scorpion.splashwalls.models.HomeModel;
import com.scorpion.splashwalls.models.ImageDetailModel;
import com.scorpion.splashwalls.models.ImageRelatedModel;
import com.scorpion.splashwalls.models.ImageStateModel;
import com.scorpion.splashwalls.models.SearchCollectionsModel;
import com.scorpion.splashwalls.models.SearchPhotosModel;
import com.scorpion.splashwalls.models.SearchUserModel;
import com.scorpion.splashwalls.models.UserCollectionsModel;
import com.scorpion.splashwalls.models.UserModel;
import com.scorpion.splashwalls.models.UserPhotosModel;
import com.scorpion.splashwalls.models.VideoWallpaperModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Keep
public interface WallpaperService {

    @GET("photos")
    Call<ArrayList<HomeModel>> getHomePageWallpapers(
            @Query("client_id") String apiKey,
            @Query("per_page") int per_page,
            @Query("order_by") String order_by,
            @Query("page") int pageIndex
    );

    @GET("photos/{image_id}")
    Call<ImageDetailModel> getImageDetails(
            @Path("image_id") String image_id,
            @Query("client_id") String apiKey

    );

    @GET("photos/{image_id}/statistics")
    Call<ImageStateModel> getImageDetailsState(
            @Path("image_id") String image_id,
            @Query("client_id") String apiKey

    );

    @GET("photos/{image_id}/related")
    Call<ImageRelatedModel> getImageDetailsRelated(
            @Path("image_id") String image_id,
            @Query("client_id") String apiKey

    );

    @GET("collections/{collection_id}/related")
    Call<ArrayList<UserCollectionsModel>> getCollectionsDetailsRelated(
            @Path("collection_id") String collection_id,
            @Query("client_id") String apiKey

    );

    @GET("users/{user_id}")
    Call<UserModel> getUserDetails(
            @Path("user_id") String user_id,
            @Query("client_id") String apiKey

    );

    @GET("users/{user_id}/photos")
    Call<ArrayList<UserPhotosModel>> getUserPhotos(
            @Path("user_id") String user_id,
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey,
            @Query("page") int pageIndex

    );

    @GET("users/{user_id}/likes")
    Call<ArrayList<UserPhotosModel>> getUserLikes(
            @Path("user_id") String user_id,
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey,
            @Query("page") int pageIndex

    );

    @GET("users/{user_id}/collections")
    Call<ArrayList<UserCollectionsModel>> getUserCollections(
            @Path("user_id") String user_id,
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey,
            @Query("page") int pageIndex

    );

    @GET("collections")
    Call<ArrayList<UserCollectionsModel>> getCollections(
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey,
            @Query("page") int pageIndex

    );

    @GET("collections/{collection_id}")
    Call<UserCollectionsModel> getUserCollections(
            @Path("collection_id") String collection_id,
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey
    );

    @GET("collections/{collection_id}/photos")
    Call<ArrayList<UserPhotosModel>> getUserCollectionsDetails(
            @Path("collection_id") String collection_id,
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey,
            @Query("page") int pageIndex
    );

    @GET("/photos/random")
    Call<ImageDetailModel> getRandom(
            @Query("client_id") String apiKey,
            @Query("orientation") String orientation,
            @Query("query") String query
    );

    @GET("/photos/random")
    Call<ImageDetailModel> getRandomAll(
            @Query("client_id") String apiKey,
            @Query("orientation") String orientation
    );

    @GET("/photos/random")
    Call<ImageDetailModel> getRandomFeatured(
            @Query("client_id") String apiKey,
            @Query("orientation") String orientation,
            @Query("featured") boolean query
    );

    @GET("/search/photos")
    Call<SearchPhotosModel> getSearchPhotos(
            @Query("query") String query,
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey,
            @Query("page") int pageIndex

    );

    @GET("/search/photos")
    Call<SearchPhotosModel> getSearchPhotosOrientated(
            @Query("query") String query,
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey,
            @Query("page") int pageIndex,
            @Query("orientation") String orientation
    );

    @GET("/search/collections")
    Call<SearchCollectionsModel> getSearchCollections(
            @Query("query") String query,
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey,
            @Query("page") int pageIndex

    );

    @GET("/search/users")
    Call<SearchUserModel> getSearchUsers(
            @Query("query") String query,
            @Query("per_page") int per_page,
            @Query("client_id") String apiKey,
            @Query("page") int pageIndex

    );


    @GET("/videos/popular")
    Call<VideoWallpaperModel> getVideoWallpaper(
            @Header("Authorization") String header,
            @Query("per_page") int per_page,
            @Query("page") int pageIndex
    );

    @GET("/v1/curated")
    Call<CuratedPhotosModel> getCuratedPhotos(
            @Header("Authorization") String header,
            @Query("per_page") int per_page,
            @Query("page") int pageIndex
    );


}
