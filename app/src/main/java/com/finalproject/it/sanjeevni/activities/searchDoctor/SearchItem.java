package com.finalproject.it.sanjeevni.activities.searchDoctor;

import com.google.firebase.storage.StorageReference;

public class SearchItem {
    private String key;
    private String name;
    private String category;
    private String rating;
    private StorageReference getImageUrl;

    public SearchItem(String key, String name, String category, String rating, StorageReference getImageUrl) {
        this.key = key;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.getImageUrl = getImageUrl;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getRating() {
        return rating;
    }

    public StorageReference getGetImageUrl() {
        return getImageUrl;
    }
}
