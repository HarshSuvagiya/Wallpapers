package com.scorpion.splashwalls.models;

public class SearchUserModel {
    private String total;

    private String total_pages;

    private Results[] results;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public Results[] getResults() {
        return results;
    }

    public void setResults(Results[] results) {
        this.results = results;
    }


    public static class Results {
        private String total_photos;

        private String accepted_tos;

        private String followed_by_user;

        private String twitter_username;

        private String last_name;

        private String bio;

        private String total_likes;

        private Photos[] photos;

        private String portfolio_url;

        private Profile_image profile_image;

        private String updated_at;

        private String name;

        private String location;

        private Links links;

        private String total_collections;

        private String id;

        private String first_name;

        private String instagram_username;

        private String username;

        public String getTotal_photos() {
            return total_photos;
        }

        public void setTotal_photos(String total_photos) {
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

        public String

        getTwitter_username() {
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

        public String getTotal_likes() {
            return total_likes;
        }

        public void setTotal_likes(String total_likes) {
            this.total_likes = total_likes;
        }

        public Photos[] getPhotos() {
            return photos;
        }

        public void setPhotos(Photos[] photos) {
            this.photos = photos;
        }

        public String

        getPortfolio_url() {
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String

        getLocation() {
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

        public String getTotal_collections() {
            return total_collections;
        }

        public void setTotal_collections(String total_collections) {
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

        public String

        getInstagram_username() {
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
            return "ClassPojo [total_photos = " + total_photos + ", accepted_tos = " + accepted_tos + ", followed_by_user = " + followed_by_user + ", twitter_username = " + twitter_username + ", last_name = " + last_name + ", bio = " + bio + ", total_likes = " + total_likes + ", photos = " + photos + ", portfolio_url = " + portfolio_url + ", profile_image = " + profile_image + ", updated_at = " + updated_at + ", name = " + name + ", location = " + location + ", links = " + links + ", total_collections = " + total_collections + ", id = " + id + ", first_name = " + first_name + ", instagram_username = " + instagram_username + ", username = " + username + "]";
        }
    }


    public class Photos {
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
