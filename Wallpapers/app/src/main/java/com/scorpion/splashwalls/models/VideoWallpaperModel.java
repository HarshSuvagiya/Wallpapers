package com.scorpion.splashwalls.models;

public class VideoWallpaperModel {

    private int per_page;

    private Videos[] videos;

    private String page;

    private String url;

    private int total_results;

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public Videos[] getVideos() {
        return videos;
    }

    public void setVideos(Videos[] videos) {
        this.videos = videos;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }


    public static class Videos {
        private String duration;

        private String image;

        private String width;

        private Video_files[] video_files;

        private String id;

        private String full_res;

        private User user;

        private String url;

        private String height;

        private String[] tags;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public Video_files[] getVideo_files() {
            return video_files;
        }

        public void setVideo_files(Video_files[] video_files) {
            this.video_files = video_files;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String

        getFull_res() {
            return full_res;
        }

        public void setFull_res(String full_res) {
            this.full_res = full_res;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String[] getTags() {
            return tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "ClassPojo [duration = " + duration + ", image = " + image + ", width = " + width + ", video_files = " + video_files + ", id = " + id + ", full_res = " + full_res + ", user = " + user + ", url = " + url + ", height = " + height + ", tags = " + tags + "]";
        }
    }

    public class Video_files {
        private String file_type;

        private String width;

        private String link;

        private String id;

        private String quality;

        private String height;

        public String getFile_type() {
            return file_type;
        }

        public void setFile_type(String file_type) {
            this.file_type = file_type;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "ClassPojo [file_type = " + file_type + ", width = " + width + ", link = " + link + ", id = " + id + ", quality = " + quality + ", height = " + height + "]";
        }
    }

    public class User {
        private String name;

        private String id;

        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "ClassPojo [name = " + name + ", id = " + id + ", url = " + url + "]";
        }
    }


}
