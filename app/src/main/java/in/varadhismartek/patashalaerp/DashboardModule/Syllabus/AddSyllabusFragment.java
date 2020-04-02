package in.varadhismartek.patashalaerp.DashboardModule.Syllabus;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSyllabusFragment extends Fragment implements View.OnClickListener {

    ImageView img_back;
    Spinner sp_class, sp_subject,sp_ExamType;
    EditText edt_Syllabus_Title, edt_Description;
    RecyclerView rv_attachment;
    SyllabusAdapter adapter;
    ArrayList<Uri> imageList;
    ImageView tv_Attachment;
    private final int REQUEST_PERMISSION_CODE = 1234;
    private File userProfile;
    private static final int PICK_IMAGE_MULTIPLE = 2;

    Button btn_submit;

    public AddSyllabusFragment() {
        // Required empty public constructor
    }

    List<File> fileList;
    ProgressDialog progressDialog;
    ArrayList<String> classList;
    ArrayList<String> subjectList;
    ArrayList<String> examTypeList;

    APIService mApiService;
    APIService mApiServicePatashala;
    String str_class = "", str_subject = "", str_ExamType = "";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_syllabus, container, false);

        initViews(view);
        initListeners();
        setRecyclerView();
        getAllClasses();
        getExamTypeAPI();
        setSpinnerForSubject();

        return view;
    }

    private void setRecyclerView() {

        adapter = new SyllabusAdapter(getActivity(), Constant.RV_ADD_SYLLABUS_ATTACHMENT,imageList);
        rv_attachment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_attachment.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void setSpinnerForExamType(){

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), examTypeList, Constant.audience_type);
        sp_ExamType.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sp_ExamType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i==0){
                    str_ExamType = "";
                }else {
                    str_ExamType = sp_ExamType.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setSpinnerForClass() {

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), classList, Constant.audience_type);
        sp_class.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    str_class = "";
                    if (subjectList.size()>0){
                        subjectList.clear();
                    }
                    subjectList.add(0,"-Select Subject-");
                    setSpinnerForSubject();
                }
                else {
                    str_class = sp_class.getSelectedItem().toString();
                    getAllSubjectApi();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerForSubject() {

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), subjectList, "orange");
        sp_subject.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();

        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    str_subject = "";
                }
                else {
                    str_subject = sp_subject.getSelectedItem().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initListeners() {

        img_back.setOnClickListener(this);
        tv_Attachment.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

    }

    private void initViews(View view) {

        progressDialog = new ProgressDialog(getActivity());
        mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();

        btn_submit = view.findViewById(R.id.btn_submit);
        img_back = view.findViewById(R.id.img_back);
        tv_Attachment = view.findViewById(R.id.tv_Attachment);
        sp_class = view.findViewById(R.id.sp_class);
        sp_subject = view.findViewById(R.id.sp_subject);
        sp_ExamType = view.findViewById(R.id.sp_ExamType);
        edt_Syllabus_Title = view.findViewById(R.id.edt_Syllabus_Title);
        edt_Description = view.findViewById(R.id.edt_Description);
        rv_attachment = view.findViewById(R.id.rv_attachment);

        imageList = new ArrayList<>();
        fileList = new ArrayList<>();
        classList = new ArrayList<>();
        subjectList = new ArrayList<>();
        examTypeList = new ArrayList<>();
        subjectList.add(0,"-Select Subject-");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.img_back:
                getActivity().onBackPressed();
                break;

            case R.id.tv_Attachment:
                initPermission();
                break;

            case R.id.btn_submit:
                Log.d("file name_1",str_class+" "+str_subject);
                addSchoolSyllabusAPI();
                break;
        }
    }

    private void initPermission() {

        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                Toast.makeText(getActivity(), "Need To Permissions", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);

                getAttachmentMethod();
            }

        }else {

            getAttachmentMethod();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION_CODE){



        }

    }

    private void getAttachmentMethod() {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"), PICK_IMAGE_MULTIPLE);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK) {

            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();

                for (int i = 0; i < mClipData.getItemCount(); i++) {

                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                        String path = saveImage(bitmap);

                        File file = new File(path);
                        fileList.add(file);

                        Log.d("File_PAth2", String.valueOf(fileList.size()));


                        imageList.add(uri);
                        adapter.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }else {

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();
                    Bitmap bitmap = null;
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),mImageUri);
                        String path = saveImage(bitmap);

                        File file = new File(path);
                        fileList.add(file);

                        Log.d("File_PAth4", String.valueOf(fileList.size()));


                        imageList.add(mImageUri);
                        adapter.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

        }/*else if (requestCode == PICK_BY_CAMERA && data.getClipData()!=null){

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            Bitmap bmp = Bitmap.createScaledBitmap(photo, 1024, 600, true);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, "Title", null);

        }*/
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void getAllClasses() {

        progressDialog.show();

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray.put("All");
            object.put("divisions",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Class_api_2", object.toString());

        if (classList.size()>0){
            classList.clear();
        }

        classList.add(0,"-Select Class-");

        mApiService.getClassSections(Constant.SCHOOL_ID,object).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful() || response.code() == 500){
                    try {
                        JSONObject obj = new JSONObject(new Gson().toJson(response.body()));
                        String status = obj.getString("status");

                        if (status.equalsIgnoreCase("Success")){
                            JSONObject jsonData = obj.getJSONObject("data");
                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                String myDivisionKey = key.next();
                                JSONObject keyData = jsonData.getJSONObject(myDivisionKey);
                                Iterator<String> classKey = keyData.keys();

                                while (classKey.hasNext()){
                                    JSONObject classData = keyData.getJSONObject(classKey.next());
                                    String class_name = classData.getString("class_name");
                                    classList.add(class_name);
                                    JSONArray jsonArray1 = classData.getJSONArray("sections");
                                    Log.d("classData_array", jsonArray1.toString());

                                    ArrayList<String> sections = new ArrayList<>();

                                    for (int i = 0; i<jsonArray1.length();i++){

                                        sections.add(jsonArray1.getString(i));
                                        Log.d("classData_array "+i, jsonArray1.getString(i));
                                    }

                                }
                            }
                            progressDialog.dismiss();
                            setSpinnerForClass();
                        }

                        progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("create_API", message);
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

    private void getAllSubjectApi(){

        if (subjectList.size()>0){
            subjectList.clear();
        }

        subjectList.add(0,"-Subject-");

        //progressDialog.show();
        mApiService.getClassSubjectsSchool(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("Subject_suc", response.code()+" "+message1);

                            JSONObject jsonData = object.getJSONObject("data");

                            JSONArray subArray = jsonData.getJSONArray(str_class);

                            for (int i=0; i<subArray.length();i++){

                                if (subjectList.size()>0){

                                    if (subjectList.contains(subArray.get(i).toString())){

                                    }else {
                                        subjectList.add(subArray.get(i).toString());
                                    }

                                }else {
                                    subjectList.add(subArray.get(i).toString());
                                }

                                Log.d("subArray2", String.valueOf(subjectList.size()));

                            }

                            Log.d("subArray", subArray.toString());

                            setSpinnerForSubject();

                        }else {
                            Log.d("Subject_else", response.code()+" "+message1);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Subject_error", message);
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

    private void getExamTypeAPI(){

        if(examTypeList.size()>0)
            examTypeList.clear();

        examTypeList.add(0,"-Exam Type-");
        progressDialog.show();
        mApiService.getExamType(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status1 = object.getString("status");
                        String message1 = object.getString("message");

                        if (status1.equalsIgnoreCase("Sucess")){
                            Log.d("getExam_Success", response.code()+" "+message1);

                            JSONObject jsonData = object.getJSONObject("data");

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                JSONObject keyData = jsonData.getJSONObject(key.next());

                                boolean status = keyData.getBoolean("status");

                                if (status){
                                    String exam_type = keyData.getString("exam_type");
                                    examTypeList.add(exam_type);
                                }
                            }
                            setSpinnerForExamType();
                            progressDialog.dismiss();

                        }else {
                            Log.d("getExam_Fail", response.code()+" "+message1);
                            setSpinnerForExamType();
                            progressDialog.dismiss();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    setSpinnerForExamType();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

    private void addSchoolSyllabusAPI(){

        String title = edt_Syllabus_Title.getText().toString();
        String desc =  edt_Description.getText().toString();

        if (str_class.equals("")) {
            Toast.makeText(getActivity(), "Classes Is Required", Toast.LENGTH_SHORT).show();

        }else if(str_ExamType.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Exam Type Is Required", Toast.LENGTH_SHORT).show();

        }else if (str_subject.equals("")) {
            Toast.makeText(getActivity(), "Subject Is Required", Toast.LENGTH_SHORT).show();

        }else if (title.equals(""))
            Toast.makeText(getActivity(), "Title Is Required", Toast.LENGTH_SHORT).show();

        else if (desc.equals("")) {
            Toast.makeText(getActivity(), "Description Is Required", Toast.LENGTH_SHORT).show();

        }else {

            progressDialog.show();

            Toast.makeText(getActivity(), str_class+" "+str_subject+" "+title+" "+desc, Toast.LENGTH_SHORT).show();
            Log.d("file name_1",str_class+" "+str_subject+" "+title+" "+desc);

            JSONArray jsonArray = new JSONArray();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            for (int i = 0; i < imageList.size(); i++) {

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageList.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String path = saveImage(bitmap);
                File file = new File(path);

                jsonArray.put("file"+i);
                builder.addFormDataPart("file"+i, file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));

            }

            builder.addFormDataPart("school_id",Constant.SCHOOL_ID);
            builder.addFormDataPart("classes",str_class);
            builder.addFormDataPart("subject",str_subject);
            builder.addFormDataPart("syllabus_title",title);
            builder.addFormDataPart("description",desc);
            builder.addFormDataPart("added_employeeid",Constant.EMPLOYEE_BY_ID);
            builder.addFormDataPart("exam_type",str_ExamType);
            builder.addFormDataPart("attachement",       jsonArray.toString());

            Log.d("file size", String.valueOf(fileList.size()));

            MultipartBody requestBody = builder.build();

            mApiServicePatashala.addSchoolSyllabus(requestBody).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    Log.d("ksjgdkj", String.valueOf(response.code()));

                    if (response.isSuccessful()){
                        getActivity().onBackPressed();
                        progressDialog.dismiss();

                    }else {
                        try {
                            assert response.errorBody()!=null;
                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("ADMIN_API", message);
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
    }
}
