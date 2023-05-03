package com.scorpion.splashwalls.models;

public class CuratedPhotosModel {

    private String next_page;

    private String per_page;

    private String page;

    private Photos[] photos;

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Photos[] getPhotos() {
        return photos;
    }

    public void setPhotos(Photos[] photos) {
        this.photos = photos;
    }

    public static class Photos {
        private Src src;

        private String width;

        private String photographer;

        private String photographer_url;

        private String id;

        private String url;

        private String photographer_id;

        private String liked;

        private String height;

        public Src getSrc() {
            return src;
        }

        public void setSrc(Src src) {
            this.src = src;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getPhotographer() {
            return photographer;
        }

        public void setPhotographer(String photographer) {
            this.photographer = photographer;
        }

        public String getPhotographer_url() {
            return photographer_url;
        }

        public void setPhotographer_url(String photographer_url) {
            this.photographer_url = photographer_url;
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

        public String getPhotographer_id() {
            return photographer_id;
        }

        public void setPhotographer_id(String photographer_id) {
            this.photographer_id = photographer_id;
        }

        public String getLiked() {
            return liked;
        }

        public void setLiked(String liked) {
            this.liked = liked;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "ClassPojo [src = " + src + ", width = " + width + ", photographer = " + photographer + ", photographer_url = " + photographer_url + ", id = " + id + ", url = " + url + ", photographer_id = " + photographer_id + ", liked = " + liked + ", height = " + height + "]";
        }
    }

    public class Src {
        private String small;

        private String original;

        private String large;

        private String tiny;

        private String medium;

        private String large2x;

        private String portrait;

        private String landscape;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getTiny() {
            return tiny;
        }

        public void setTiny(String tiny) {
            this.tiny = tiny;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge2x() {
            return large2x;
        }

        public void setLarge2x(String large2x) {
            this.large2x = large2x;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getLandscape() {
            return landscape;
        }

        public void setLandscape(String landscape) {
            this.landscape = landscape;
        }

        @Override
        public String toString() {
            return "ClassPojo [small = " + small + ", original = " + original + ", large = " + large + ", tiny = " + tiny + ", medium = " + medium + ", large2x = " + large2x + ", portrait = " + portrait + ", landscape = " + landscape + "]";
        }
    }


}
