package com.scorpion.splashwalls.models;

public class UserModel {
    private String numeric_id;

    private int total_photos;

    private String accepted_tos;

    private String followed_by_user;

    private String twitter_username;

    private String last_name;

    private String bio;

    private String allow_messages;

    private int total_likes;

    private String portfolio_url;

    private Profile_image profile_image;

    private String updated_at;

    private String following_count;

    private String downloads;

    private Meta meta;

    private String followers_count;

    private String name;

    private String location;

    private Links links;

    private int total_collections;

    private String id;

    private String first_name;

    private String instagram_username;

    private String username;

    public String getNumeric_id() {
        return numeric_id;
    }

    public void setNumeric_id(String numeric_id) {
        this.numeric_id = numeric_id;
    }

    public int getTotal_photos() {
        return total_photos;
    }

    public void setTotal_photos(int total_photos) {
        this.total_photos = total_photos;
    }

    public String getAccepted_tos() {
        return accepted_tos;
    }

    public void setAccepted_tos(String accepted_tos) {
        this.accepted_tos = accepted_tos;
    }

    public String getFollowed_by_user() {
        return followed_by_user;
    }

    public void setFollowed_by_user(String followed_by_user) {
        this.followed_by_user = followed_by_user;
    }

    public String getTwitter_username() {
        return twitter_username;
    }

    public void setTwitter_username(String twitter_username) {
        this.twitter_username = twitter_username;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAllow_messages() {
        return allow_messages;
    }

    public void setAllow_messages(String allow_messages) {
        this.allow_messages = allow_messages;
    }

    public int getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(int total_likes) {
        this.total_likes = total_likes;
    }

    public String getPortfolio_url() {
        return portfolio_url;
    }

    public void setPortfolio_url(String portfolio_url) {
        this.portfolio_url = portfolio_url;
    }

    public Profile_image getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(Profile_image profile_image) {
        this.profile_image = profile_image;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(String following_count) {
        this.following_count = following_count;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(String followers_count) {
        this.followers_count = followers_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public int getTotal_collections() {
        return total_collections;
    }

    public void setTotal_collections(int total_collections) {
        this.total_collections = total_collections;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getInstagram_username() {
        return instagram_username;
    }

    public void setInstagram_username(String instagram_username) {
        this.instagram_username = instagram_username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ClassPojo [numeric_id = " + numeric_id + ", total_photos = " + total_photos + ", accepted_tos = " + accepted_tos + ", followed_by_user = " + followed_by_user + ", twitter_username = " + twitter_username + ", last_name = " + last_name + ", bio = " + bio + ", allow_messages = " + allow_messages + ", total_likes = " + total_likes + ", portfolio_url = " + portfolio_url + ", profile_image = " + profile_image + ", updated_at = " + updated_at + ", following_count = " + following_count + ", downloads = " + downloads + ", meta = " + meta + ", followers_count = " + followers_count + ", name = " + name + ", location = " + location + ", links = " + links + ", total_collections = " + total_collections + ", id = " + id + ", first_name = " + first_name + ", instagram_username = " + instagram_username + ", username = " + username + "]";
    }

    public class Meta {
        private String index;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "ClassPojo [index = " + index + "]";
        }
    }


    public class Profile_image {
        private String small;

        private String large;

        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        @Override
        public String toString() {
            return "ClassPojo [small = " + small + ", large = " + large + ", medium = " + medium + "]";
        }
    }

    public class Links {
        private String followers;

        private String portfolio;

        private String following;

        private String self;

        private String html;

        private String photos;

        private String likes;

        public String getFollowers() {
            return followers;
        }

        public void setFollowers(String followers) {
            this.followers = followers;
        }

        public String getPortfolio() {
            return portfolio;
        }

        public void setPortfolio(String portfolio) {
            this.portfolio = portfolio;
        }

        public String getFollowing() {
            return following;
        }

        public void setFollowing(String following) {
            this.following = following;
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

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        @Override
        public String toString() {
            return "ClassPojo [followers = " + followers + ", portfolio = " + portfolio + ", following = " + following + ", self = " + self + ", html = " + html + ", photos = " + photos + ", likes = " + likes + "]";
        }
    }


}
