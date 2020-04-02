package in.varadhismartek.patashalaerp.GalleryModule.Models;

public class GalleryImage {

    private String imageUrl;
    private boolean isCenter = false;

    public boolean isCenter() {
        return isCenter;
    }

    public void setCenter(boolean center) {
        isCenter = center;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
