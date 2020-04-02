package in.varadhismartek.patashalaerp.DashboardModule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Random;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

class DashBoardGalleryAdapter extends RecyclerView.Adapter <DashBoardGalleryAdapter.ViewHolder> {

    private ArrayList<String> myList;
    private Context mContext;
    private Random mRandom = new Random();

    public DashBoardGalleryAdapter(Context context, ArrayList<String> myList) {
        this.myList = myList;
        this.mContext = context;


    }



    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.staggered_layout_image, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {





        // Set a random height for TextView
        holder.imageView.getLayoutParams().height = getRandomIntInRange(200, 200);

        if(!myList.get(position).equalsIgnoreCase("") ){

            Glide.with(mContext)
                    .load(Constant.IMAGE_URL+myList.get(position)).override(200,200)
                    .into(holder.imageView);

            Log.d("ImageUrl", Constant.IMAGE_URL+myList.get(position));


        }
    }

    @Override
    public int getItemCount() {

        if(myList.size()>7){
            return 7;
        }
        else {
            return myList.size();
        }
    }

    // Custom method to get a random number between a range
    protected int getRandomIntInRange(int max, int min) {
        Log.d("random Number", ""+mRandom.nextInt((max - min) + min) + min);

        return mRandom.nextInt((max - min) + min) + min;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.iv);
        }
    }

}