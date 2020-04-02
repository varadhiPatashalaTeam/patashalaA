package in.varadhismartek.patashalaerp.GalleryModule;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment implements View.OnClickListener {

    Dialog dialog;
    TextView tv_date;
    EditText et_gallery_title, et_gallery_desc;
    TextView tv_add;
    RecyclerView recyclerViewDialog;
    LinearLayout camera;
    LinearLayout gallery;
    String currentDate;

    List<String> imagesEncodedListA;
    String imageEncoded;
    List<String> imageFilePathList = new ArrayList<>();
    List<Bitmap> compressedBitmapList = new ArrayList<>();
    List<String> imagesEncodedList;
    ArrayList<Uri> mArrayUri = new ArrayList<>();

    private static final int PICK_BY_CAMERA = 3;
    private static final int PICK_IMAGE_MULTIPLE = 2;

    private ImageView iv_backBtn, iv_upload;
    private RecyclerView recycler_view;
    private GalleryAdapter adapter;
    private ArrayList<GalleryModel> galleryModelArrayList;
    private APIService mApiServicePatashala;
    private DateConvertor convertor;
    private ProgressDialog progressDialog;

    LinearLayout ll_layout;
    Toolbar toolbar;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        initView(view);
        initListeners();
        setRecyclerView();
        getSchoolGalleryAPI();


        return view;
    }

    private void setRecyclerView() {

        adapter = new GalleryAdapter(galleryModelArrayList, getActivity(), Constant.RV_GALLERY_NEW_LAYOUT, refreshGallery);
        recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        iv_upload.setOnClickListener(this);
    }

    private void initView(View view) {

        mApiServicePatashala = ApiUtilsPatashala.getService();
        convertor = new DateConvertor();
        progressDialog = new ProgressDialog(getActivity());

        // ll_layout = view.findViewById(R.id.ll_layout);
        //toolbar = view.findViewById(R.id.toolbar);

        recycler_view = view.findViewById(R.id.recycler_view);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        iv_upload = view.findViewById(R.id.iv_upload);

        galleryModelArrayList = new ArrayList<>();

    }

    @Override
    public void onClick(View view) {

        // assert getActivity() != null;

        switch (view.getId()) {

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.iv_upload:
                uploadImageMethod();
                break;
        }
    }

    private void uploadImageMethod() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_camera_gallery);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        tv_date = dialog.findViewById(R.id.tv_date);
        et_gallery_title = dialog.findViewById(R.id.et_gallery_title);
        et_gallery_desc = dialog.findViewById(R.id.et_gallery_desc);
        tv_add = dialog.findViewById(R.id.tv_add);
        recyclerViewDialog = dialog.findViewById(R.id.recycler_view);
        camera = dialog.findViewById(R.id.ll_camera);
        gallery = dialog.findViewById(R.id.ll_gallery);

        tv_date.setText(currentDate);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Camera Selected...", Toast.LENGTH_SHORT).show();
                openCamera();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Gallery Selected...", Toast.LENGTH_SHORT).show();
                openGallery();
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_gallery_title.getText().toString().isEmpty() || et_gallery_desc.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill the title and description", Toast.LENGTH_SHORT).show();
                } else {

                    addDataToServer();
                }


            }
        });


    }

    private void addDataToServer() {
        JSONArray jsonArray = new JSONArray();
        String description = et_gallery_desc.getText().toString();
        String title = et_gallery_title.getText().toString();


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for (int i = 0; i < imageFilePathList.size(); i++) {


            File f = new File(imageFilePathList.get(i));
            jsonArray.put("file" + i);
            builder.addFormDataPart("file" + i, f.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), f));

        }

        Log.d("JSOARr", jsonArray.toString());
        builder.addFormDataPart("school_id", Constant.SCHOOL_ID);
        builder.addFormDataPart("added_employeeid", Constant.EMPLOYEE_BY_ID);
        builder.addFormDataPart("description", description);
        builder.addFormDataPart("title", title);
        builder.addFormDataPart("gallery_image", jsonArray.toString());


        MultipartBody requestBody = builder.build();

        mApiServicePatashala.addGallery(requestBody).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                Log.d("responseAddIamge", "onResponse: " + response.raw());
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equalsIgnoreCase("Success")) {
                            Toast.makeText(getActivity(),"Gallery Image Upload Successfully",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            getSchoolGalleryAPI();

                        }
                    }catch (JSONException je){

                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), PICK_IMAGE_MULTIPLE);

    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, PICK_BY_CAMERA);
    }


    private void getSchoolGalleryAPI() {

        if (galleryModelArrayList.size()>0)
            galleryModelArrayList.clear();

        progressDialog.show();

        mApiServicePatashala.getGallery(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equalsIgnoreCase("Success")){

                            JSONObject jsonData = object.getJSONObject("data");
                            JSONObject galleryDetails = jsonData.getJSONObject("gallery_details");
                            Iterator<String> key = galleryDetails.keys();

                            while (key.hasNext()){

                                String myKey = key.next();
                                JSONObject keyData = galleryDetails.getJSONObject(myKey);

                                //String description = keyData.getString("description");
                                String gallery_id = keyData.getString("gallery_id");
                                //String added_by = keyData.getString("added_by");
                                String added_datetime = keyData.getString("added_datetime");
                                String Imagecount = keyData.getString("total_files");

                                String title = "";

                                if (!keyData.isNull("title")) {
                                    title = keyData.getString("title");
                                }

                                String addedDate = convertor.getDateByTZ_SSS(added_datetime);

                                Log.d("addedDatetime", added_datetime);

                                ArrayList<String> list = new ArrayList<>();

                                JSONArray galleryImage = keyData.getJSONArray("gallery_image");

                                for (int i = 0; i<galleryImage.length(); i++){

                                    list.add(galleryImage.get(i).toString());
                                }

                                galleryModelArrayList.add(new GalleryModel(addedDate,title,gallery_id, list,Imagecount));
                                adapter.notifyDataSetChanged();

                            }
                            progressDialog.dismiss();
                            adapter.notifyDataSetChanged();

                        }else {
                            Log.d("GALLERY_API_ELSE", message+" "+response.code());
                            progressDialog.dismiss();

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {

                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

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


  /*  private void getSchoolGalleryAPI() {

        if (galleryModelArrayList.size() > 0)
            galleryModelArrayList.clear();

        progressDialog.show();

        mApiServicePatashala.getGallery(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            JSONObject galleryDetails = jsonData.getJSONObject("gallery_details");
                            Iterator<String> key = galleryDetails.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                JSONObject keyData = galleryDetails.getJSONObject(myKey);

                                //String description = keyData.getString("description");
                                String gallery_id = keyData.getString("gallery_id");
                                //String added_by = keyData.getString("added_by");
                                String added_datetime = keyData.getString("added_datetime");

                                String title = "";

                                if (!keyData.isNull("title")) {
                                    title = keyData.getString("title");
                                }

                                String addedDate = convertor.getDateByTZ_SSS(added_datetime);

                                Log.d("addedDatetime", added_datetime);

                                ArrayList<String> list = new ArrayList<>();

                                JSONArray galleryImage = keyData.getJSONArray("gallery_image");

                                for (int i = 0; i < galleryImage.length(); i++) {

                                    list.add(galleryImage.get(i).toString());
                                }

                                galleryModelArrayList.add(new GalleryModel(addedDate, title, gallery_id, list));
                                adapter.notifyDataSetChanged();

                            }
                            progressDialog.dismiss();
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d("GALLERY_API_ELSE", message + " " + response.code());
                            progressDialog.dismiss();

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }
*/

    RefreshGallery refreshGallery = new RefreshGallery() {
        @Override
        public void refresh() {
            getSchoolGalleryAPI();
        }
    };

    public interface RefreshGallery {

        void refresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();
                    //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();


                    mArrayUri.add(mImageUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                    String path = getRealPathFromURI(getImageUri(getActivity(), bitmap));
                    imageFilePathList.add(path);
                    File f = new File(path);

                    setRecyclerViewForDialog(mArrayUri);


                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();


                        if (mClipData.getItemCount() <= 8) {
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();


                                mArrayUri.add(uri);

                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                                String path = getRealPathFromURI(getImageUri(getActivity(), bitmap));
                                imageFilePathList.add(path);
                                // Get the cursor
                                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imageEncoded = cursor.getString(columnIndex);
                                imagesEncodedList.add(imageEncoded);
                                cursor.close();

                            }

                            setRecyclerViewForDialog(mArrayUri);


                        } else {
                            Toast.makeText(getActivity(), "You can't select images more than 8", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void setRecyclerViewForDialog(ArrayList<Uri> mArrayUri) {
        GalleryAdapter adapter = new GalleryAdapter(mArrayUri, getActivity(), Constant.RV_DIALOG_IMAGE_GALLERY);
        recyclerViewDialog.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerViewDialog.setAdapter(adapter);
        adapter.notifyDataSetChanged();



       /* questionBankAdapter = new HomeworkAdapter(Constant.RV_QUESTION_BANK_ROW, getActivity(), questionBankModels);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(questionBankAdapter);
        questionBankAdapter.notifyDataSetChanged();*/

    }
}















































/*

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK) {


            mArrayUri.clear();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            imagesEncodedList = new ArrayList<File>();


            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();

                for (int i = 0; i < mClipData.getItemCount(); i++) {

                  *//*  ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    File file = new File(uri.getPath());
                    Log.d("uriioewrwe", uri.toString() + "****" + file);
                    mArrayUri.add(uri);*//*

                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mArrayUri.add(uri);
                    Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn,
                            null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String  imageEncoded  = cursor.getString(columnIndex);
                    imagesEncodedList.add(new File(imageEncoded));

//                    file2 = new File(imageEncoded);

                    System.out.println("imageEncoded***"+imageEncoded);
                    cursor.close();

                }


                Log.v("LOG_TAG", "Selected Images" + mArrayUri.size()+"***"+mArrayUri.get(0));

                setRecyclerViewForDialog(mArrayUri);

            } else {

                mArrayUri.clear();

                if (data.getData() != null) {
                    Uri mImageUri = data.getData();
                    mArrayUri.add(mImageUri);

                    Log.d("mArrayUri**", "onActivityResult: " + mImageUri);

                    setRecyclerViewForDialog(mArrayUri);
                }

            }

        } else if (requestCode == PICK_BY_CAMERA) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            Bitmap bmp = Bitmap.createScaledBitmap(photo, 1024, 600, true);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, "Title", null);

            filePathUri = Uri.parse(path);
            Log.v("LOG_TAG_path", path);
            mArrayUri.add(filePathUri);
            setRecyclerViewForDialog(mArrayUri);
            Log.d("filePathUri**", "onActivityResult: " + filePathUri+"***"+mArrayUri.get(0).getPath());

        }
    }*/

//}
