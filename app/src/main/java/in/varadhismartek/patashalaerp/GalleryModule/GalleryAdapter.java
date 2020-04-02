package in.varadhismartek.patashalaerp.GalleryModule;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GalleryModule.ViewHolders.GalleryHolder;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryHolder> {

    private ArrayList<GalleryModel> galleryModelArrayList;
    private ArrayList<String> listImage;
    private Context context;
    private String recyclerTag;
    private APIService mApiServicePatashala;
    private GalleryFragment.RefreshGallery refreshGallery;
    private ProgressDialog progressDialog;
    ArrayList<Uri> mArrayUri;
    public GalleryAdapter(ArrayList<GalleryModel> galleryModelArrayList, Context context, String recyclerTag, GalleryFragment.RefreshGallery refreshGallery){

        this.galleryModelArrayList = galleryModelArrayList;
        this.context = context;
        this.recyclerTag = recyclerTag;
        this.refreshGallery = refreshGallery;
        mApiServicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");

    }

    public GalleryAdapter(Context context, ArrayList<String> listImage, String recyclerTag) {

        this.listImage = listImage;
        this.context = context;
        this.recyclerTag = recyclerTag;
    }
    public GalleryAdapter(ArrayList<Uri> mArrayUri, Context context, String recyclerTag) {
        this.mArrayUri = mArrayUri;
        this.context = context;
        this.recyclerTag = recyclerTag;

    }




    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag){

            case Constant.RV_GALLERY_NEW_LAYOUT:
                return new GalleryHolder(LayoutInflater.from(context).inflate(R.layout.layout_folder_view, viewGroup, false));

            case Constant.RV_GALLERY_IMAGES:
                return new GalleryHolder(LayoutInflater.from(context).inflate(R.layout.layout_image_select, viewGroup, false));

            case Constant.RV_QUESTION_BANK:
                return new GalleryHolder(LayoutInflater.from(context).inflate(R.layout.layout_image_select, viewGroup, false));

            case Constant.RV_DIALOG_IMAGE_GALLERY:
                return new GalleryHolder(LayoutInflater.from(context).inflate(R.layout.layout_gallery_row, viewGroup, false));


        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, @SuppressLint("RecyclerView") final int position) {

        final String imageUrlg = "http://13.127.95.246:8000/media/";
        switch (recyclerTag){


            case Constant.RV_DIALOG_IMAGE_GALLERY:

                if (mArrayUri.size() > 0) {
                    // File image = fileList.get(position);
                    final String image1 = imageUrlg + mArrayUri.get(position);

                    holder.ivImages.setImageURI(mArrayUri.get(position));
                }

                break;

                case Constant.RV_QUESTION_BANK:

                if (mArrayUri.size() > 0) {
                    // File image = fileList.get(position);
                    final String image1 = imageUrlg + mArrayUri.get(position);

                    holder.ivImages.setImageURI(mArrayUri.get(position));
                }

                break;




            case Constant.RV_GALLERY_NEW_LAYOUT:


                if (galleryModelArrayList.get(position).getUriArrayList().size()>0) {

                    final String image = imageUrlg + galleryModelArrayList.get(position).getUriArrayList().get(0);
                    Glide.with(context)
                            .load(image)
                            .into(holder.iv_event_image);
                }

                holder.tv_count.setText(galleryModelArrayList.get(position).getImageCount());
                holder.tv_event_title.setText(galleryModelArrayList.get(position).getGalleryTitle());

                holder.iv_event_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GalleryActivity galleryActivity = (GalleryActivity) context;

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list",galleryModelArrayList.get(position).getUriArrayList());
                        bundle.putString("date",galleryModelArrayList.get(position).getDate());
                        bundle.putString("title",galleryModelArrayList.get(position).getGalleryTitle());
                        bundle.putSerializable("GalleryModel",galleryModelArrayList.get(position));
                        galleryActivity.loadFragment(Constant.GALLERY_GRID_FRAGMENT, bundle);
                    }
                });

                holder.iv_event_image.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        deleteDialog(galleryModelArrayList.get(position).getGalleryId(), galleryModelArrayList.get(position).getGalleryTitle());

                        return false;
                    }
                });






                break;

            case Constant.RV_GALLERY_IMAGES:

                Log.d("ImageLogB",imageUrlg + listImage.get(position));


                if (listImage.size()>0){
                    final String image = imageUrlg + listImage.get(position);
                    Glide.with(context).load(image).into(holder.ivImages);
                }

                holder.ivImages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GalleryActivity galleryActivity = (GalleryActivity) context;


                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list", listImage);
                        galleryActivity.loadFragment(Constant.GALLERY_VIEW_FRAGMENT, bundle);

                    }
                });

                break;

        }
    }

    @Override
    public int getItemCount() {

        switch (recyclerTag) {

            case Constant.RV_GALLERY_NEW_LAYOUT:
                return galleryModelArrayList.size();

            case Constant.RV_GALLERY_IMAGES:
                return listImage.size();

            case Constant.RV_QUESTION_BANK:
                return mArrayUri.size();

            case Constant.RV_DIALOG_IMAGE_GALLERY:
                return mArrayUri.size();

        }

        return 0;
    }

    private void deleteDialog(final String galleryId, String title) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_notice_board);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView tv_dialog_title = dialog.findViewById(R.id.tv_dialog_title);
        LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);
        LinearLayout ll_update = dialog.findViewById(R.id.ll_update);
        Button btn_no = dialog.findViewById(R.id.btn_no);
        Button bt_yes = dialog.findViewById(R.id.bt_yes);
        TextView tv_text = dialog.findViewById(R.id.tv_text);

        tv_dialog_title.setText("Delete "+title);
        tv_text.setText("Are You Sure ? You Want To Delete "+title);
        ll_delete.setVisibility(View.VISIBLE);
        ll_update.setVisibility(View.GONE);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("GALLERY_DELETE_ELSE",Constant.SCHOOL_ID+" "+Constant.EMPLOYEE_ID+" "+galleryId+" "+
                        Constant.POC_NAME+" "+Constant.LAST_NAME+" "+Constant.POC_Mobile_Number+" "+
                        Constant.AADHAR_NO);


                progressDialog.show();
                mApiServicePatashala.deleteSchoolGallery(Constant.SCHOOL_ID,Constant.EMPLOYEE_ID, galleryId,
                        Constant.FIRST_NAME,Constant.LAST_NAME,Constant.POC_Mobile_Number,
                        Constant.AADHAR_NO).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                        if (response.isSuccessful()){

                            try {

                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = object.getString("status");
                                String message = object.getString("message");

                                if (status.equalsIgnoreCase("Success")){

                                    Log.d("GALLERY_DELETE_ELSE",response.code()+" "+message);
                                    Toast.makeText(context,"Gallery Delete Sucessfully",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    progressDialog.dismiss();
                                    refreshGallery.refresh();

                                }else {

                                    Log.d("GALLERY_DELETE_ELSE",response.code()+" "+message);
                                    progressDialog.dismiss();
                                    dialog.dismiss();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                dialog.dismiss();
                            }

                        }else {
                            try {
                                assert response.errorBody()!=null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("ADMIN_API", message);
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                dialog.dismiss();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                    }
                });

            }
        });
    }
}







































                /*if (galleryModelArrayList.get(position).getUriArrayList().size()>0) {

                    final String image = imageUrlg + galleryModelArrayList.get(position).getUriArrayList().get(0);
                    Glide.with(context)
                            .load(image)
                            .into(holder.iv_event_image);
                }

                holder.tv_count.setText(String.valueOf(galleryModelArrayList.get(position).getUriArrayList().size()));
                holder.tv_event_title.setText(galleryModelArrayList.get(position).getGalleryTitle());

                holder.iv_event_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GalleryActivity galleryActivity = (GalleryActivity) context;


                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list",galleryModelArrayList.get(position).getUriArrayList());
                        bundle.putString("date",galleryModelArrayList.get(position).getDate());
                        bundle.putString("title",galleryModelArrayList.get(position).getGalleryTitle());
                        galleryActivity.loadFragment(Constant.GALLERY_GRID_FRAGMENT, bundle);
                    }
                });

                holder.iv_event_image.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        deleteDialog(galleryModelArrayList.get(position).getGalleryId(), galleryModelArrayList.get(position).getGalleryTitle());

                        return false;
                    }
                });
*/
