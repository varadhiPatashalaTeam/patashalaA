package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.GalleryModule.GalleryAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.FilePath;
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

import static android.app.Activity.RESULT_OK;

public class Fragment_QuestionBank extends Fragment implements View.OnClickListener {

    String strDiv = "", str_class = "", strSection = "", str_subject = "", str_toDate = "", endYear = "", currentDate = "";
    APIService mApiService, apiService;
    ArrayList<String> listDivision;
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    ArrayList<String> listSectionNew;
    ArrayList<String> listSubject;
    ArrayList<ClassSectionModel> modelArrayList;
    CustomSpinnerAdapter customSpinnerAdapter;
    TextView tvFromDate;
    ImageView iv_backBtn;
    Spinner spDivision, spnClass, spn_section, spn_Subject;
    RecyclerView recycler_view;
    Button btnSave;
    EditText edQuestionTitle, edQuestionDesc;
    ImageView ivAttachmentImage;
    int PICK_IMAGE_MULTIPLE = 1;
    int PICK_IMAGE_MULTIPLE_2 = 2;

    String imageEncoded;
    List<String> imagesEncodedList = new ArrayList<>();
    List<String> imageFilePathList = new ArrayList<>();
    ArrayList<Uri> mArrayUri = new ArrayList<>();
    private String selectedFilePath;
    public Fragment_QuestionBank() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__question_bank, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        //   initListeners();
        getDivisionApi();


        return view;
    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();
        apiService = ApiUtilsPatashala.getService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        spDivision = view.findViewById(R.id.spn_division);
        spnClass = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);
        spn_Subject = view.findViewById(R.id.spn_Subject);
        btnSave = view.findViewById(R.id.btn_submit);

        edQuestionTitle = view.findViewById(R.id.ed_question_title);
        edQuestionDesc = view.findViewById(R.id.ed_description);
        ivAttachmentImage = view.findViewById(R.id.imageView_attach);

        //  linearLayoutDate = view.findViewById(R.id.ll_date);
        tvFromDate = view.findViewById(R.id.tv_fromDate);
        recycler_view = view.findViewById(R.id.recycler_view);

        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();

        modelArrayList = new ArrayList<>();
        listClass.add("Class");

        btnSave.setOnClickListener(this);
        ivAttachmentImage.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);


    }

    private void getDivisionApi() {

        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision.clear();
                listDivision.add(0, "Division");
                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String Id = object1.getString("id");
                                statusDivision = object1.getBoolean("status");
                                if (statusDivision) {
                                    String division = object1.getString("division");

                                    listDivision.add(division);
                                }

                                Log.i("Division Object***", "" + listDivision + "***" + statusDivision);
                            }

                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
                            spDivision.setAdapter(customSpinnerAdapter);

                            /*JSONArray array = new JSONArray();
                            JSONObject jsonObject = new JSONObject();
                            for (int i = 0; i < listDivision.size(); i++) {


                                try {
                                    array.put(listDivision.get(i));
                                    jsonObject.put("divisions", array);
                                    Constant.DIVISION_NAME = strDiv;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                               // getClassSectionList(jsonObject);
                            }
                            getClassSectionList(jsonObject);
                            System.out.println("jsonObject***"+jsonObject);*/

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());

            }
        });


        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strDiv = parent.getSelectedItem().toString();

                //  setSpinnerForClass();

                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                try {
                    array.put(strDiv);
                    jsonObject.put("divisions", array);
                    Constant.DIVISION_NAME = strDiv;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getClassSectionList(jsonObject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getClassSectionList(JSONObject jsonObject) {

        listClass.clear();
        listClass.add(0, "Class");
        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("CLASS_SECTIONDDD", "" + response.body());

                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {

                            if (object.getJSONObject("data").toString().equals("{}")) {
                                //modelArrayList.clear();
                                // customSpinnerAdapter.notifyDataSetChanged();
                                //   setSpinnerForClass();


                            } else {
                                JSONObject jsonObject1 = object.getJSONObject("data");
                                JSONObject jsonObject2 = jsonObject1.getJSONObject(strDiv);
                                Iterator<String> keys = jsonObject2.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                    String className = jsonObjectValue.getString("class_name");
                                    JSONArray jsonArray = jsonObjectValue.getJSONArray("sections");
                                    listSection = new ArrayList<>();

                                    String Section = "";

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Section = jsonArray.getString(i);
                                        listSection.add(Section);
                                    }

                                    listClass.add(className);

                                    modelArrayList.add(new ClassSectionModel(className, listSection));

                                }
                            }


                        }

                    } catch (JSONException je) {

                    }

                } else {

                    listClass.clear();
                    listClass.add("Class");

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spnClass.setAdapter(customSpinnerAdapter);


        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_class = spnClass.getSelectedItem().toString();
                listSectionNew = new ArrayList<>();
                listSectionNew.clear();
                listSectionNew.add("Select Section");

                boolean b = true;

                for (int j = 0; j < modelArrayList.size(); j++) {
                    if (modelArrayList.get(j).getClassName().contains(str_class)) {
                        listSectionNew.clear();

                        for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {

                            listSectionNew.add(modelArrayList.get(j).getListSection().get(k));

                        }
                        getSubject(strDiv, str_class);
                    }
                }


                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSectionNew);
                spn_section.setAdapter(customSpinnerAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strSection = spn_section.getSelectedItem().toString();
                getSubject(strDiv, str_class);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getSubject(final String strDiv, final String str_class) {

        mApiService.getSubject(Constant.SCHOOL_ID, strDiv, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                listSubject = new ArrayList<>();

                if (response.isSuccessful()) {
                    try {

                        listSubject.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        Log.i("SubjectList***D", "" + response.code() + "**" + status);
                        if (status.equalsIgnoreCase("Success")) {


                            JSONObject jsonObject1 = object.getJSONObject("Section");

                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String sectionkey = keys.next();
                                JSONObject jsonSection = jsonObject1.getJSONObject(sectionkey);
                                Iterator<String> keys2 = jsonSection.keys();

                                while (keys2.hasNext()) {
                                    String subjectkey = keys2.next();
                                    JSONObject jsonSubject = jsonSection.getJSONObject(subjectkey);

                                    String strType = jsonSubject.getString("subject_type");
                                    String strCode = jsonSubject.getString("subject_code");
                                    boolean strStatus = jsonSubject.getBoolean("status");

                                    Log.i("SubjectList***E", "" + response.code() + "**" + status + "***" + subjectkey);
                                    listSubject.add(subjectkey);


                                }
                                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                                spn_Subject.setAdapter(customSpinnerAdapter);
                                // setRecyclerView();
                            }
                        } else {
                            listSubject.clear();
                            listSubject.add(0, "Subject");
                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                            spn_Subject.setAdapter(customSpinnerAdapter);
                        }

                    } catch (JSONException je) {

                    }
                } else {

                    Log.i("SubjectList***F", "" + response.code());
                    listSubject.clear();
                    listSubject.add(0, "Subject");
                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                    spn_Subject.setAdapter(customSpinnerAdapter);
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SubjectList***B", "" + t.getMessage());

            }
        });
        spn_Subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_subject = spn_Subject.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_submit:

                if (strDiv.equalsIgnoreCase("Division")) {
                    Toast.makeText(getActivity(), "Select Division ", Toast.LENGTH_SHORT).show();
                } else if (str_class.equalsIgnoreCase("Class")) {
                    Toast.makeText(getActivity(), "Select Class ", Toast.LENGTH_SHORT).show();
                } else if (str_subject.equals("") || strSection.isEmpty()) {
                    Toast.makeText(getActivity(), "Select Subject ", Toast.LENGTH_SHORT).show();
                } else if (edQuestionTitle.getText().toString().equals("") || edQuestionTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Question title ", Toast.LENGTH_SHORT).show();
                } else if (edQuestionDesc.getText().toString().equals("") || edQuestionDesc.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Question description ", Toast.LENGTH_SHORT).show();
                } else if (imageFilePathList.size() == 0) {
                    Toast.makeText(getActivity(), "Please attach Question bank ", Toast.LENGTH_SHORT).show();
                } else {

                    uploadQuestionBank();

                    InputMethodManager imm = (InputMethodManager) getActivity().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);



                }
                break;

            case R.id.imageView_attach:

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), PICK_IMAGE_MULTIPLE);


                break;

            case R.id.iv_backBtn:
               // showFileChooser();

                DashBoardMenuActivity dashBoardMenuActivity = (DashBoardMenuActivity) getActivity();
                dashBoardMenuActivity.loadFragment(Constant.QUESTIONBANK_LIST, null);

        }


    }

    private void showFileChooser() {
        //Intent intent = new Intent();
        //sets the select file to all types of files
        //intent.setType("*/*");
        //allows to select data and return it
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        //startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_IMAGE_MULTIPLE_2);

    }

    private void uploadQuestionBank() {

        JSONArray jsonArray = new JSONArray();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (int i = 0; i < imageFilePathList.size(); i++) {


            File f = new File(imageFilePathList.get(i));
            jsonArray.put("file" + i);
            builder.addFormDataPart("file" + i, f.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), f));

        }

        String title = edQuestionTitle.getText().toString();
        String desc = edQuestionDesc.getText().toString();


        Log.d("JSOARr", jsonArray.toString());
        builder.addFormDataPart("school_id", Constant.SCHOOL_ID);
        builder.addFormDataPart("class", str_class);
        builder.addFormDataPart("sections", strSection);
        builder.addFormDataPart("subject", str_subject);
        builder.addFormDataPart("added_employeeid", Constant.EMPLOYEE_BY_ID);
        builder.addFormDataPart("question_bank_title", title);
        builder.addFormDataPart("description", desc);
        builder.addFormDataPart("question_bank_attachement", jsonArray.toString());


        MultipartBody requestBody = builder.build();


        apiService.addQuestionBank(requestBody).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("QuestionBank_res", "" + response.body() + "***" + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Question Bank Added Successfully",
                            Toast.LENGTH_SHORT).show();
                    spDivision.setSelection(0);
                    spnClass.setSelection(0);
                    spn_section.setSelection(0);
                    spn_Subject.setSelection(0);

                    edQuestionTitle.setText("");
                    edQuestionDesc.setText("");


                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    Cursor cursor = getActivity().getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();


                    mArrayUri.add(mImageUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                    String path = getRealPathFromURI((Uri) getImageUri(getActivity(), bitmap));
                    imageFilePathList.add(path);
                    File f = new File(path);
                    System.out.println("mArrayUri**1 " + mArrayUri.size() + "**" + mArrayUri.get(0).getPath());
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
                                String path = getRealPathFromURI((Uri) getImageUri(getActivity(), bitmap));
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
                            System.out.println("mArrayUri**2 " + mArrayUri.size() + "**" + mArrayUri.get(0).getPath());
                            setRecyclerViewForDialog(mArrayUri);


                        } else {
                            Toast.makeText(getActivity(), "You can't select images more than 8", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();


            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Object getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void setRecyclerViewForDialog(ArrayList<Uri> mArrayUri) {
        GalleryAdapter questionbankAdapter = new GalleryAdapter(mArrayUri, getActivity(), Constant.RV_QUESTION_BANK);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_view.setAdapter(questionbankAdapter);

    }
/*   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if (resultCode == Activity.RESULT_OK) {
           if (requestCode == PICK_IMAGE_MULTIPLE) {
               if (data == null) {
                   //no data present
                   return;
               }


               Uri selectedFileUri = data.getData();
               selectedFilePath = FilePath.getPath(getActivity(), selectedFileUri);
               Log.i("TAGFILE", "Selected File Path:" + selectedFilePath);

               if (selectedFilePath != null && !selectedFilePath.equals("")) {
                   System.out.println("selectedFilePath**"+selectedFilePath);
                  // tvFileName.setText(selectedFilePath);
               } else {
                   Toast.makeText(getActivity(), "Cannot upload file to server", Toast.LENGTH_SHORT).show();
               }
           }
       }
   }*/

}







