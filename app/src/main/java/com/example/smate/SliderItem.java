package com.example.smate;

public class SliderItem {
    String Description;
    String ImageUrl;

    public SliderItem(String description, String imageUrl) {
        Description = description;
        ImageUrl = imageUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
