package in.varadhismartek.patashalaerp.GalleryModule;

import java.io.Serializable;
import java.util.ArrayList;

public class GalleryModel implements Serializable {

    private String date;
    private String galleryTitle, galleryId;
    private ArrayList<String> uriArrayList;
    private  String imageCount;

    public GalleryModel(String date, String galleryTitle,String galleryId, ArrayList<String> uriArrayList) {
        this.date = date;
        this.galleryTitle = galleryTitle;
        this.uriArrayList = uriArrayList;
        this.galleryId = galleryId;
    }

    public GalleryModel(String date, String galleryTitle,String galleryId, ArrayList<String> uriArrayList,String imageCount) {
        this.date = date;
        this.galleryTitle = galleryTitle;
        this.uriArrayList = uriArrayList;
        this.galleryId = galleryId;
        this.imageCount = imageCount;
    }


    public String getImageCount() {
        return imageCount;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getUriArrayList() {
        return uriArrayList;
    }

    public String getGalleryTitle() {
        return galleryTitle;
    }

    public String getGalleryId() {
        return galleryId;
    }
}
