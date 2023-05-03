package com.scorpion.splashwalls.models;

public class ImageStateModel {
    private Downloads downloads;

    private String id;

    private Views views;

    private Likes likes;

    public Downloads getDownloads ()
    {
        return downloads;
    }

    public void setDownloads (Downloads downloads)
    {
        this.downloads = downloads;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Views getViews ()
    {
        return views;
    }

    public void setViews (Views views)
    {
        this.views = views;
    }

    public Likes getLikes ()
    {
        return likes;
    }

    public void setLikes (Likes likes)
    {
        this.likes = likes;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [downloads = "+downloads+", id = "+id+", views = "+views+", likes = "+likes+"]";
    }

    public class Likes
    {
        private String total;

        private Historical historical;

        public String getTotal ()
        {
            return total;
        }

        public void setTotal (String total)
        {
            this.total = total;
        }

        public Historical getHistorical ()
        {
            return historical;
        }

        public void setHistorical (Historical historical)
        {
            this.historical = historical;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [total = "+total+", historical = "+historical+"]";
        }
    }

    public class Views
    {
        private String total;

        private Historical historical;

        public String getTotal ()
        {
            return total;
        }

        public void setTotal (String total)
        {
            this.total = total;
        }

        public Historical getHistorical ()
        {
            return historical;
        }

        public void setHistorical (Historical historical)
        {
            this.historical = historical;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [total = "+total+", historical = "+historical+"]";
        }
    }

    public class Values
    {
        private String date;

        private String value;

        public String getDate ()
        {
            return date;
        }

        public void setDate (String date)
        {
            this.date = date;
        }

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [date = "+date+", value = "+value+"]";
        }
    }

    public class Downloads
    {
        private String total;

        private Historical historical;

        public String getTotal ()
        {
            return total;
        }

        public void setTotal (String total)
        {
            this.total = total;
        }

        public Historical getHistorical ()
        {
            return historical;
        }

        public void setHistorical (Historical historical)
        {
            this.historical = historical;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [total = "+total+", historical = "+historical+"]";
        }
    }

    public class Historical
    {
        private String quantity;

        private String change;

        private Values[] values;

        private String resolution;

        public String getQuantity ()
        {
            return quantity;
        }

        public void setQuantity (String quantity)
        {
            this.quantity = quantity;
        }

        public String getChange ()
        {
            return change;
        }

        public void setChange (String change)
        {
            this.change = change;
        }

        public Values[] getValues ()
        {
            return values;
        }

        public void setValues (Values[] values)
        {
            this.values = values;
        }

        public String getResolution ()
        {
            return resolution;
        }

        public void setResolution (String resolution)
        {
            this.resolution = resolution;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [quantity = "+quantity+", change = "+change+", values = "+values+", resolution = "+resolution+"]";
        }
    }

}