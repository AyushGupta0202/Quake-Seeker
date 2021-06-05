package com.eggdevs.thequakeseeker.data;

public class EarthquakeDos {

    private int imageId;
    private String imageDescription;

    public EarthquakeDos(int imageId, String imageDescription) {
        this.imageId = imageId;
        this.imageDescription = imageDescription;
    }

    public int getImageId() {
        return imageId;
    }

    public String getImageDescription() {
        return imageDescription;
    }
}
