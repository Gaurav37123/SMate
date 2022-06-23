package com.example.smate.Classes;

import com.example.smate.SliderItem;

import java.util.ArrayList;

public class FeedItem {
    ArrayList<SliderItem> url = new ArrayList<>();
    int rating;
    String download;
    String contentDesc;
    String publisher;
    String node;

    public FeedItem(ArrayList<SliderItem> url, int rating, String download, String contentDesc, String publisher,String node) {
        this.url = url;
        this.rating = rating;
        this.download = download;
        this.contentDesc = contentDesc;
        this.publisher = publisher;
        this.node = node;
    }

    public FeedItem(FeedItem fd)
    {
        this.url = fd.url;
        this.rating = fd.rating;
        this.download = fd.download;
        this.contentDesc = fd.contentDesc;
        this.publisher = fd.publisher;
        this.node = fd.node;
    }
    public FeedItem() {
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public ArrayList<SliderItem> getUrl() {
        return url;
    }

    public void setUrl(ArrayList<SliderItem> url) {
        this.url = url;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }
}
