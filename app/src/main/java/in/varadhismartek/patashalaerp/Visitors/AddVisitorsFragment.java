package in.varadhismartek.patashalaerp.Visitors;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.RangeTimePickerDialog;
import in.varadhismartek.patashalaerp.MainActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AddVisitorsFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "";
    private static final int FROM_CAMERA = 1111;
    private static final int FROM_GALLERY = 2222;
    private ImageView ivBack;
    private TextView tv_submit;
    private CircleImageView civ_visitor;
    private TextView tv_appTime, tv_appDate;
    private EditText et_visitorName, et_mobile, et_purpose, et_doorNo, et_street, et_locality, et_landmark,
            et_pincode, et_city, et_state, et_country;
    private RelativeLayout rl_start_date, rl_toDate;
    private TextView tv_entryDate, tv_entryTime;
    private TextView tv_roles, tv_department, tv_search;
    ImageView iv_idCard, iv_signature;
    Spinner spn_gender;

    private int year = 0, month = 0, date = 0;
    String currentDate = "", currentTime = "", entryDate = "", entryTime;
    SimpleDateFormat dateFormat, timeFormat;

    private APIService mApiService, mApiServicePatashala;
    private ProgressDialog progressDialog;
    private ArrayList<VisitorModel> searchList;
    VisitorsAdapter adapter;

    public static String EMPLOYEE_ID = "", ROLE = "", DEPARTMENT = "";
    File visitorFile, idCardFile, signFile;


    public AddVisitorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_visitors, container, false);

        initViews(view);
        initListeners();
        getCurrentDateAndTime();
        setSpinner();

        return view;
    }

    private void setSpinner() {

        ArrayList<String> myList = new ArrayList<>();

        myList.add("-Gender-");
        myList.add("Male");
        myList.add("Female");
        myList.add("Others");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), myList, "Staff");
        spn_gender.setAdapter(customSpinnerAdapter);
    }

    @SuppressLint("SimpleDateFormat")
    private void getCurrentDateAndTime() {

        Calendar calendar = Calendar.getInstance();
        timeFormat = new SimpleDateFormat("hh:mm a");
        currentTime = timeFormat.format(calendar.getTime());
        tv_appTime.setText(currentTime);

        //setting the date in the textview based upon the system time
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = dateFormat.format(calendar.getTime());
        tv_appDate.setText(currentDate);

    }


    private void initListeners() {

        ivBack.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        civ_visitor.setOnClickListener(this);
        rl_start_date.setOnClickListener(this);
        rl_toDate.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        iv_signature.setOnClickListener(this);
        iv_idCard.setOnClickListener(this);

    }

    private void initViews(View view) {

        mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(getActivity());

        ivBack = view.findViewById(R.id.ivBack);
        tv_submit = view.findViewById(R.id.tv_submit);
        civ_visitor = view.findViewById(R.id.civ_visitor);
        tv_appTime = view.findViewById(R.id.tv_appTime);
        tv_appDate = view.findViewById(R.id.tv_appDate);
        et_visitorName = view.findViewById(R.id.et_visitorName);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_purpose = view.findViewById(R.id.et_purpose);
        et_doorNo = view.findViewById(R.id.et_doorNo);
        et_street = view.findViewById(R.id.et_street);
        et_locality = view.findViewById(R.id.et_locality);
        et_landmark = view.findViewById(R.id.et_landmark);
        et_pincode = view.findViewById(R.id.et_pincode);
        et_city = view.findViewById(R.id.et_city);
        et_state = view.findViewById(R.id.et_state);
        et_country = view.findViewById(R.id.et_country);
        rl_start_date = view.findViewById(R.id.rl_start_date);
        rl_toDate = view.findViewById(R.id.rl_toDate);
        tv_entryDate = view.findViewById(R.id.tv_entryDate);
        tv_entryTime = view.findViewById(R.id.tv_entryTime);

        tv_search = view.findViewById(R.id.tv_search);
        tv_department = view.findViewById(R.id.tv_department);
        tv_roles = view.findViewById(R.id.tv_roles);

        iv_signature = view.findViewById(R.id.iv_signature);
        iv_idCard = view.findViewById(R.id.iv_idCard);
        spn_gender = view.findViewById(R.id.spn_gender);

        searchList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ivBack:
                getActivity().onBackPressed();
                break;

            case R.id.rl_start_date:

                if (getActivity() != null) {

                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, i);
                            calendar.set(Calendar.MONTH, i1);
                            calendar.set(Calendar.DAY_OF_MONTH, i2);

                            entryDate = dateFormat.format(calendar.getTime());
                            tv_entryDate.setText(entryDate);

                        }
                    },year, month, date);

                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dialog.show();
                }

                break;

            case R.id.rl_toDate:


                Calendar currentTime = Calendar.getInstance();

                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SimpleDateFormat")
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {

                                Calendar time = Calendar.getInstance();

                                time.set(Calendar.HOUR_OF_DAY, selectedHour);

                                time.set(Calendar.MINUTE, selectedMinute);
                                SimpleDateFormat format = new SimpleDateFormat(
                                        "hh:mm a");
                                entryTime = format.format(time.getTime());
                                tv_entryTime.setText(format.format(time.getTime()));



                            }
                        },

                        currentTime.get(Calendar.HOUR_OF_DAY), // Current hour value
                        currentTime.get(Calendar.MINUTE), // Current minute value
                        DateFormat.is24HourFormat(getActivity())); // Check 24 Hour or AM/PM format

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

                break;

            case R.id.tv_search:
                getStaffListAPI();

                break;

            case R.id.civ_visitor:
                TAG = "PHOTO_VISITOR";
                setImagesInDialog();
                break;

            case R.id.iv_idCard:
                TAG = "ID_CARD";
                setImagesInDialog();
                break;

            case R.id.iv_signature:
                TAG = "SIGNATURE";
                setImagesInDialog();
                break;

            case R.id.tv_submit:
                addVisitorAPI();
                break;


        }
    }


    private void setImagesInDialog() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_camera_gallery);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        LinearLayout camera = dialog.findViewById(R.id.ll_camera);
        LinearLayout gallery = dialog.findViewById(R.id.ll_gallery);
        TextView tv_date = dialog.findViewById(R.id.tv_date);
        TextView tv_add = dialog.findViewById(R.id.tv_add);
        EditText et_gallery_title = dialog.findViewById(R.id.et_gallery_title);

        tv_date.setVisibility(View.GONE);
        tv_add.setVisibility(View.GONE);
        et_gallery_title.setVisibility(View.GONE);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, FROM_CAMERA);
                dialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"), FROM_GALLERY);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FROM_GALLERY && resultCode == RESULT_OK){

            if (data.getData() != null) {

                Uri mImageUri = data.getData();
                setImages(mImageUri);
            }

        }else if (requestCode == FROM_CAMERA && resultCode == RESULT_OK){

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            Bitmap bmp = Bitmap.createScaledBitmap(photo, 100, 100, true);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, "Title", null);
            Uri filePathUri = Uri.parse(path);

            setImages(filePathUri);


        }
    }

    private void setImages(Uri mImageUri) {

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert bitmap!=null;
        String path = getRealPathFromURI(getImageUri(getActivity(), bitmap));

        switch (TAG){

            case "PHOTO_VISITOR":

                civ_visitor.setImageURI(mImageUri);
                visitorFile = new File(path);

                break;

            case "ID_CARD":
                iv_idCard.setImageURI(mImageUri);
                idCardFile = new File(path);

                break;

            case "SIGNATURE":
                iv_signature.setImageURI(mImageUri);
                signFile = new File(path);

                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        Bitmap bmp = Bitmap.createScaledBitmap(inImage, 100, 100, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bmp, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void searchDialog(ArrayList<VisitorModel> list){

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_search_emp);
        //noinspection ConstantConditions
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RecyclerView recycler_view_dialog = dialog.findViewById(R.id.rv_search);
        EditText et_search = dialog.findViewById(R.id.et_search);

        adapter = new VisitorsAdapter(getActivity(), list, dialog,tv_search,tv_department, tv_roles, Constant.RV_NOTIFICATION_STAFF_SEARCH_RESULT);
        recycler_view_dialog.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_dialog.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dialog.show();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                employeeFilter(editable.toString());
            }
        });

    }

    private void employeeFilter(String text) {

        ArrayList<VisitorModel> filteredStaffList = new ArrayList<>();
        for (VisitorModel item : searchList) {
            if (item.getFirst_name().toLowerCase().contains(text.toLowerCase())) {
                filteredStaffList.add(item);
            }
        }
        adapter.empFilter(filteredStaffList);
    }

    private void getStaffListAPI(){

        progressDialog.show();

        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (searchList.size()>0)
                    searchList.clear();

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status1 = object.getString("status");
                        String message1 = object.getString("message");

                        Log.d("STUDENT_LIST_Succ", response.code()+" "+message1);

                        if (status1.equalsIgnoreCase("Success")){

                            JSONObject jsonData = object.getJSONObject("data");

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                JSONObject keyData = jsonData.getJSONObject(key.next());

                                String first_name = keyData.getString("first_name");
                                String custom_employee_id ="";
                                if (!keyData.isNull("custom_employee_id")){
                                    custom_employee_id = keyData.getString("custom_employee_id");
                                }
                                String last_name = keyData.getString("last_name");
                                String photo = keyData.getString("employee_photo");
                                String role = keyData.getString("role");
                                String employee_uuid = keyData.getString("employee_uuid");
                                String department_name = keyData.getString("department_name");

                                searchList.add(new VisitorModel(photo, first_name, last_name, department_name,
                                        role, employee_uuid,custom_employee_id));

                            }

                            Log.d("STUDENT_LIST", response.code()+" "+searchList.size());

                            searchDialog(searchList);
                            progressDialog.dismiss();

                        }else {
                            Log.d("STUDENT_LIST_ELSE", response.code()+" "+message1);
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
                        Log.d("STUDENT_LIST_FAIL", message);
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

    private void addVisitorAPI(){

        String visitiorName = et_visitorName.getText().toString().trim();
        String mobile = et_mobile.getText().toString().trim();
        String purpose = et_purpose.getText().toString().trim();
        String doorNo = et_doorNo.getText().toString().trim();
        String street = et_street.getText().toString().trim();
        String locality = et_locality.getText().toString().trim();
        String landmark = et_landmark.getText().toString().trim();
        String pincode = et_pincode.getText().toString().trim();
        String city = et_city.getText().toString().trim();
        String state = et_state.getText().toString().trim();
        String country = et_country.getText().toString().trim();

        if (visitorFile == null){
            Toast.makeText(getActivity(), "Visitor Photo Is Required", Toast.LENGTH_SHORT).show();

        }else if (visitiorName.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Visitor Name Is Required", Toast.LENGTH_SHORT).show();

        }else if (spn_gender.getSelectedItemPosition()==0){
            Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();

        }else if (mobile.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Mobile Is Required", Toast.LENGTH_SHORT).show();

        }else if (purpose.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Purpose Is Required", Toast.LENGTH_SHORT).show();

        }else if (doorNo.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Door No Is Required", Toast.LENGTH_SHORT).show();

        }else if (street.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Street Is Required", Toast.LENGTH_SHORT).show();

        }else if (locality.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Locality Is Required", Toast.LENGTH_SHORT).show();

        }else if (landmark.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Landmark Is Required", Toast.LENGTH_SHORT).show();

        }else if (pincode.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Pincode Is Required", Toast.LENGTH_SHORT).show();

        }else if (city.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "City Is Required", Toast.LENGTH_SHORT).show();

        }else if (state.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "State Is Required", Toast.LENGTH_SHORT).show();

        }else if (country.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Country Is Required", Toast.LENGTH_SHORT).show();

        }else if (entryDate.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Entry Date Is Required", Toast.LENGTH_SHORT).show();
        }else if (entryTime.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Entry Time Is Required", Toast.LENGTH_SHORT).show();

        }else if (EMPLOYEE_ID.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Select Employee", Toast.LENGTH_SHORT).show();

        }else if (idCardFile == null){
            Toast.makeText(getActivity(), "Id Card Photo Is Required", Toast.LENGTH_SHORT).show();

        }else if (signFile == null){
            Toast.makeText(getActivity(), "Signature Photo Is Required", Toast.LENGTH_SHORT).show();

        }else {

            progressDialog.show();

            String gender = spn_gender.getSelectedItem().toString().trim();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            Log.d("my_data", Constant.EMPLOYEE_ID+" "+ROLE+" "+DEPARTMENT+" "+EMPLOYEE_ID);

            builder.addFormDataPart("school_id",Constant.SCHOOL_ID);
            builder.addFormDataPart("added_employeeid",Constant.EMPLOYEE_BY_ID);
            //builder.addFormDataPart("role",ROLE);
            builder.addFormDataPart("department", DEPARTMENT);
            builder.addFormDataPart("visitor_name", visitiorName);
            builder.addFormDataPart("gender", gender);
            builder.addFormDataPart("staff", EMPLOYEE_ID);
            builder.addFormDataPart("purpose", purpose);
            builder.addFormDataPart("contact_number", mobile);
            builder.addFormDataPart("address", doorNo+", "+street+", "+"near "+landmark+", "+
                    locality+", "+city+", "+state+", "+", "+country+" - "+pincode);
            builder.addFormDataPart("entry_time", entryTime);
            builder.addFormDataPart("entry_date", entryDate);

            builder.addFormDataPart("visitor_photo", visitorFile.getName(), RequestBody.
                    create(MediaType.parse("multipart/form-data"), visitorFile));

            builder.addFormDataPart("signature", signFile.getName(), RequestBody.
                    create(MediaType.parse("multipart/form-data"), signFile));

            builder.addFormDataPart("visitor_idcard_photo", idCardFile.getName(), RequestBody.
                    create(MediaType.parse("multipart/form-data"), idCardFile));

            MultipartBody requestBody = builder.build();

            mApiServicePatashala.addVisitor(requestBody).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()){

                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String status = object.getString("status");
                            String message = object.getString("message");

                            if (status.equalsIgnoreCase("Success")){
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                Log.d("Add_staff_success",response.code()+" "+message);
                                progressDialog.dismiss();

                                getActivity().onBackPressed();

                            }else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                Log.d("Add_staff_else",response.code()+" "+message);
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
                            Log.d("Add_staff_fail", message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Log.d("Add_staff_fail", "FAIL");

                }
            });
        }

    }
}
