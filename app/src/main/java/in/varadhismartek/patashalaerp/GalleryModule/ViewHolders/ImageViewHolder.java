package in.varadhismartek.patashalaerp.GalleryModule.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import in.varadhismartek.patashalaerp.R;


public class ImageViewHolder extends RecyclerView.ViewHolder {

    public ImageView galleryImage;

    public ImageViewHolder(View view) {
        super(view);

        galleryImage = (ImageView) view.findViewById(R.id.clj_gallery_image);
    }
}