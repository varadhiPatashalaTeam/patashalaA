package in.varadhismartek.patashalaerp.GalleryModule.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class GalleryHolder extends RecyclerView.ViewHolder {

    public ImageView iv_event_image, ivImages;
    public TextView tv_count, tv_event_title;

    public GalleryHolder(@NonNull View itemView) {
        super(itemView);

        ivImages = itemView.findViewById(R.id.ivImages);


        iv_event_image = itemView.findViewById(R.id.iv_event_image);
        tv_count = itemView.findViewById(R.id.tv_count);
        tv_event_title = itemView.findViewById(R.id.tv_event_title);




    }
}
