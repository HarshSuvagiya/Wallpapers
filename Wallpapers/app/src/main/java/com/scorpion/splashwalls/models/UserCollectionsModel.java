package com.scorpion.splashwalls.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCollectionsModel {

    private Preview_photos[] preview_photos;

    private String featured;

    private String updated_at;

    private int total_photos;

    private String curated;

    private String share_key;

    private String description;

    private Links links;

    private String id;

    private String title;

    private String published_at;

    public Preview_photos[] getPreview_photos() {
        return preview_photos;
    }

    public void setPreview_photos(Preview_photos[] preview_photos) {
        this.preview_photos = preview_photos;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getTotal_photos() {
        return total_photos;
    }

    public void setTotal_photos(int total_photos) {
        this.total_photos = total_photos;
    }

    public String getCurated() {
        return curated;
    }

    public void setCurated(String curated) {
        this.curated = curated;
    }

    public String getShare_key() {
        return share_key;
    }

    public void setShare_key(String share_key) {
        this.share_key = share_key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private Object lastName;
        @SerializedName("twitter_username")
        @Expose
        private String twitterUsername;
        @SerializedName("portfolio_url")
        @Expose
        private String portfolioUrl;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("links")
        @Expose
        private HomeModel.Links_ links;
        @SerializedName("profile_image")
        @Expose
        private HomeModel.ProfileImage profileImage;
        @SerializedName("instagram_username")
        @Expose
        private String instagramUsername;
        @SerializedName("total_collections")
        @Expose
        private Integer totalCollections;
        @SerializedName("total_likes")
        @Expose
        private Integer totalLikes;
        @SerializedName("total_photos")
        @Expose
        private Integer totalPhotos;
        @SerializedName("accepted_tos")
        @Expose
        private Boolean acceptedTos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public Object getLastName() {
            return lastName;
        }

        public void setLastName(Object lastName) {
            this.lastName = lastName;
        }

        public String getTwitterUsername() {
            return twitterUsername;
        }

        public void setTwitterUsername(String twitterUsername) {
            this.twitterUsername = twitterUsername;
        }

        public String getPortfolioUrl() {
            return portfolioUrl;
        }

        public void setPortfolioUrl(String portfolioUrl) {
            this.portfolioUrl = portfolioUrl;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public HomeModel.Links_ getLinks() {
            return links;
        }

        public void setLinks(HomeModel.Links_ links) {
            this.links = links;
        }

        public HomeModel.ProfileImage getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(HomeModel.ProfileImage profileImage) {
            this.profileImage = profileImage;
        }

        public String getInstagramUsername() {
            return instagramUsername;
        }

        public void setInstagramUsername(String instagramUsername) {
            this.instagramUsername = instagramUsername;
        }

        public Integer getTotalCollections() {
            return totalCollections;
        }

        public void setTotalCollections(Integer totalCollections) {
            this.totalCollections = totalCollections;
        }

        public Integer getTotalLikes() {
            return totalLikes;
        }

        public void setTotalLikes(Integer totalLikes) {
            this.totalLikes = totalLikes;
        }

        public Integer getTotalPhotos() {
            return totalPhotos;
        }

        public void setTotalPhotos(Integer totalPhotos) {
            this.totalPhotos = totalPhotos;
        }

        public Boolean getAcceptedTos() {
            return acceptedTos;
        }

        public void setAcceptedTos(Boolean acceptedTos) {
            this.acceptedTos = acceptedTos;
        }
    }

    public class Preview_photos {
        private Urls urls;

        private String updated_at;

        private String created_at;

        private String id;

        public Urls getUrls() {
            return urls;
        }

        public void setUrls(Urls urls) {
            this.urls = urls;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ClassPojo [urls = " + urls + ", updated_at = " + updated_at + ", created_at = " + created_at + ", id = " + id + "]";
        }
    }

    public class Urls {
        private String small;

        private String thumb;

        private String raw;

        private String regular;

        private String full;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getRegular() {
            return regular;
        }

        public void setRegular(String regular) {
            this.regular = regular;
        }

        public String getFull() {
            return full;
        }

        public void setFull(String full) {
            this.full = full;
        }

        @Override
        public String toString() {
            return "ClassPojo [small = " + small + ", thumb = " + thumb + ", raw = " + raw + ", regular = " + regular + ", full = " + full + "]";
        }
    }

    public class Links {
        private String related;

        private String self;

        private String html;

        private String photos;

        public String getRelated() {
            return related;
        }

        public void setRelated(String related) {
            this.related = related;
        }

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public String getPhotos() {
            return photos;
        }

        public void setPhotos(String photos) {
            this.photos = photos;
        }

        @Override
        public String toString() {
            return "ClassPojo [related = " + related + ", self = " + self + ", html = " + html + ", photos = " + photos + "]";
        }
    }


}
