package in.varadhismartek.patashalaerp.DashboardModule.NotificationModule;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import in.varadhismartek.patashalaerp.StudentModule.StudentModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNotificationFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn, iv_search, iv_attach;
    Button bt_submit, bt_attachment, bt_on_off;
    Spinner spn_send_to;
    TextView tv_select;
    EditText et_title, et_message, et_search;
    LinearLayout ll_search, ll_schedule_row, ll_date, ll_time;
    TextView tv_date, tv_time;

    String str_sendTo = "";
    String strSelectVal = "";

    static String TAG_SEARCH = "";
    private final int REQUEST_PERMISSION_CODE = 1234;

    ArrayList<NotificationModel> searchList = new ArrayList<>();

    public static ArrayList<String> SELECTED_LIST = new ArrayList<>();
    static String SEARCH_ID = "";
    ArrayList<String> spnList;
    APIService mApiServicePatashala;
    APIService mApiService;

    ArrayList<String> deptList;
    ArrayList<String> classList;
    ArrayList<String> rolesList;
    ArrayList<String> divisionList = new ArrayList<>();

    String currentDate = "";
    String currentTime = "";

    int date = 0, month = 0, year = 0;

    ArrayList<NotificationModel> classSectionList;

    File userProfile;
    NotificationAdapter adapter;

    private ArrayList<StudentModel> studentModels;
    private ArrayList<String> studentNameList;
    private String strDivision, strClass, strSection, strFirstName, strLastName, strDOB, strStudentID, strCertificateNo,
            strStatus, strdeleted, strPhoto;
    ArrayList<EmpLeaveModel> employeeList;
    String empFname = "", empLname = "", empUUId, empPhoneNo, empAdhaarNo, empDept, strTeacherName, strTeacherUUID;

    public CreateNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_notification, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        initListeners();
        getCurrentDateAndTime();
        setSpinnerForSend();
    //    getNotificationAPI();

        return view;
    }

    private void getCurrentDateAndTime() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa");

        Calendar calendar = Calendar.getInstance();

        currentDate = simpleDateFormat.format(calendar.getTime());
        currentTime = simpleTimeFormat.format(calendar.getTime());

        Log.d("CURRENT_DATE_TIME", currentDate + " " + currentTime);

        date = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        Log.d("CURRENT TIME", date + " " + month + " " + year);

    }

    private void setSpinnerForSend() {

        //spnList.add(0, "-- Select --");
        spnList.add("-- Select --");
        spnList.add("departments");
        spnList.add( "divisions");
        spnList.add("classes");
        spnList.add( "roles");
        spnList.add( "staffs");
        spnList.add( "students");

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), spnList, "Green");
        spn_send_to.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_send_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (SELECTED_LIST.size() > 0)
                    SELECTED_LIST.clear();

                et_search.setText("");

                if (i == 0) {
                    str_sendTo = "";
                    strSelectVal = "";
                    tv_select.setText("- Select -");
                    tv_select.setVisibility(View.VISIBLE);
                    ll_search.setVisibility(View.GONE);

                } else {
                    str_sendTo = spn_send_to.getSelectedItem().toString();

                    switch (str_sendTo) {

                        case "departments":
                            getDepartmentAPI();
                            tv_select.setText("-Select Departments-");
                            tv_select.setVisibility(View.VISIBLE);
                            ll_search.setVisibility(View.GONE);

                            break;

                        case "divisions":
                            tv_select.setText("-Select Divisions-");
                            tv_select.setVisibility(View.VISIBLE);
                            ll_search.setVisibility(View.GONE);
                            getDivisionAPI();
                            openMyDialog("Select Divisions", divisionList);


                            break;

                        case "classes":
                            tv_select.setText("-Select Classes-");
                            getDivisionAPI();
                            getAllClasses();
                            tv_select.setVisibility(View.VISIBLE);
                            ll_search.setVisibility(View.GONE);

                            break;

                        case "roles":
                            tv_select.setText("-Select Roles-");
                            getRolesBasedOnDepartment();
                            tv_select.setVisibility(View.VISIBLE);
                            ll_search.setVisibility(View.GONE);

                            break;

                        case "staffs":
                            TAG_SEARCH = "staffs";
                            tv_select.setText("-Select Staff-");
                            tv_select.setVisibility(View.GONE);
                            ll_search.setVisibility(View.VISIBLE);

                            searchList.clear();
                            getEmployeeListAPI();



                            break;

                        case "parents":
                            TAG_SEARCH = "parents";
                            tv_select.setVisibility(View.GONE);
                            ll_search.setVisibility(View.VISIBLE);

                            searchList.clear();
                            searchList.add(new NotificationModel(1, "Shivam", "D1", "R1", "VS019BH0042", "9770203504"));
                            searchList.add(new NotificationModel(1, "Vinay", "D12", "R2", "VS019BH0042", "9770203504"));
                            searchList.add(new NotificationModel(1, "Abhishek", "D3", "R3", "VS019BH0042", "9770203504"));
                            searchList.add(new NotificationModel(1, "Nitish", "D4", "R4", "VS019BH0042", "9770203504"));
                            searchList.add(new NotificationModel(1, "Rahul", "D5", "R5", "VS019BH0042", "9770203504"));
                            searchList.add(new NotificationModel(1, "Kb Patil", "D6", "R6", "VS019BH0042", "9770203504"));

                            break;

                        case "students":
                            TAG_SEARCH = "students";
                            tv_select.setText("-Select Students-");
                            tv_select.setVisibility(View.GONE);
                            ll_search.setVisibility(View.VISIBLE);

                            getStudentAllAPI();


                            break;
                    }

                    Log.d("str_sendTo", str_sendTo);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getEmployeeListAPI() {

        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        // employeeNameList.clear();
                        employeeList.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("ADMIN_API_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("EMPLKEY", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);
                                Log.d("EMPKEYDATA", keyData.toString());


                                empUUId = keyData.getString("employee_uuid");
                                empFname = keyData.getString("first_name");
                                empLname = keyData.getString("last_name");
                                empPhoneNo = keyData.getLong("phone_number")+"";
                                empAdhaarNo = keyData.getString("adhaar_card_no");
                                empDept = keyData.getString("department_name");


                                searchList.add(new NotificationModel(empFname, empLname, empUUId, empPhoneNo, empAdhaarNo, empDept));
                            }


                        }

                    } catch (JSONException je) {

                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void getStudentAllAPI() {
        mApiService.getStudentAll(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("studentList_all", "" + response.raw().request().url());
                    Log.i("studentList_all", "" + response.body() + "***" + response.code());

                    try {
                        studentModels.clear();

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject = object.getJSONObject("data");
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject objectData = jsonObject.getJSONObject(key);


                                strDivision = objectData.getString("division");
                                strClass = objectData.getString("class");
                                strSection = objectData.getString("section");
                                strFirstName = objectData.getString("first_name");
                                strLastName = objectData.getString("last_name");
                                strDOB = objectData.getString("student_dob");
                                strStudentID = objectData.getString("student_id");
                                strCertificateNo = objectData.getString("birth_certificate_no");
                                strStatus = objectData.getString("status");
                                strdeleted = objectData.getString("student_deleted");
                                strPhoto = objectData.getString("photo");

                                studentModels.add(new StudentModel(strDivision, strClass, strSection, strFirstName, strLastName,
                                        strDOB, strStudentID, strCertificateNo, strStatus, strdeleted, strPhoto));

                                String name = strFirstName + " " + strLastName;
                                studentNameList.add(name);


                            }
                            System.out.println("captionNameList***" + studentNameList.size() + "**" + studentNameList);




                          /*  CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(context, captionNameList);
                            sp_ViceCaption.setAdapter(customSpinnerAdapter1);*/

                        }

                    } catch (JSONException je) {

                        je.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        bt_attachment.setOnClickListener(this);
        bt_on_off.setOnClickListener(this);
        ll_date.setOnClickListener(this);
        ll_time.setOnClickListener(this);
    }

    private void initViews(View view) {

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        iv_search = view.findViewById(R.id.iv_search);
        bt_submit = view.findViewById(R.id.bt_submit);
        spn_send_to = view.findViewById(R.id.spn_send_to);
        et_title = view.findViewById(R.id.et_title);
        et_message = view.findViewById(R.id.et_message);
        et_search = view.findViewById(R.id.et_search);
        tv_select = view.findViewById(R.id.tv_select);
        ll_search = view.findViewById(R.id.ll_search);
        bt_attachment = view.findViewById(R.id.bt_attachment);
        iv_attach = view.findViewById(R.id.iv_attach);
        bt_on_off = view.findViewById(R.id.bt_on_off);
        ll_schedule_row = view.findViewById(R.id.ll_schedule_row);

        ll_date = view.findViewById(R.id.ll_date);
        ll_time = view.findViewById(R.id.ll_time);
        tv_date = view.findViewById(R.id.tv_date);
        tv_time = view.findViewById(R.id.tv_time);

        spnList = new ArrayList<>();
        deptList = new ArrayList<>();
        classList = new ArrayList<>();
        rolesList = new ArrayList<>();
        studentNameList = new ArrayList<>();
        employeeList = new ArrayList<>();

        classSectionList = new ArrayList<>();
        studentModels = new ArrayList<>();
        mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        et_search.setText("Select");
        et_search.setEnabled(false);


    }

    @Override
    public void onClick(View view) {

        assert getActivity() != null;

        switch (view.getId()) {

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.bt_submit:

                if (spn_send_to.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Select Send To Is Required", Toast.LENGTH_SHORT).show();

                } else if (et_title.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter the title", Toast.LENGTH_SHORT).show();

                } else if (et_message.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter the message ", Toast.LENGTH_SHORT).show();

                } else if (currentDate.equals("")) {
                    Toast.makeText(getActivity(), "Select Date ", Toast.LENGTH_SHORT).show();

                } else if (currentTime.equals("")) {
                    Toast.makeText(getActivity(), "Select Time", Toast.LENGTH_SHORT).show();

                } else {

                    createNotification();
                }

                break;

            case R.id.iv_search:

                if (TAG_SEARCH.equalsIgnoreCase("students")) {
                    studentListDialog();

                } else if (TAG_SEARCH.equalsIgnoreCase("staffs")) {
                    staffListDialog();

                }



                break;

            case R.id.bt_attachment:
                //Toast.makeText(getActivity(), "tv_attachment Clicked...!", Toast.LENGTH_SHORT).show();
                initPermission();
                break;

            case R.id.bt_on_off:

                if (bt_on_off.getText().toString().equalsIgnoreCase("OFF")) {
                    bt_on_off.setBackgroundColor(Color.parseColor("#52b155"));
                    bt_on_off.setText("ON");
                    ll_schedule_row.setVisibility(View.VISIBLE);
                    currentDate = "";
                    currentTime = "";
                    Log.d("CURRENT_DATE_TIME_2", currentDate + " " + currentTime);
                    tv_date.setText("Date");
                    tv_time.setText("Time");


                } else {
                    bt_on_off.setBackgroundColor(Color.parseColor("#A0A0A0"));
                    bt_on_off.setText("OFF");
                    ll_schedule_row.setVisibility(View.GONE);
                    getCurrentDateAndTime();
                    Log.d("CURRENT_DATE_TIME_1", currentDate + " " + currentTime);
                }

                break;

            case R.id.ll_date:

                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, i);
                        cal.set(Calendar.MONTH, i1);
                        cal.set(Calendar.DAY_OF_MONTH, i2);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        currentDate = simpleDateFormat.format(cal.getTime());
                        Log.d("CURRENT_DATE_TIME_3", currentDate);
                        tv_date.setText(currentDate);
                    }
                }, year, month, date);
                dateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dateDialog.show();

                break;

            case R.id.ll_time:

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR, i);
                        calendar.set(Calendar.MINUTE, i1);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");

                        currentTime = simpleDateFormat.format(calendar.getTime());
                        Log.d("CURRENT_DATE_TIME_4", currentTime);
                        tv_time.setText(currentTime);

                    }
                }, 12, 12, false);

                timePickerDialog.show();

                break;
        }
    }

    private void staffListDialog() {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_for_section);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView toolbar = dialog.findViewById(R.id.toolbar);
        RecyclerView recycler_view_dialog = dialog.findViewById(R.id.recycler_view_dialog);
        EditText ed_search = dialog.findViewById(R.id.ed_search);
        Button bt_add_dialog = dialog.findViewById(R.id.bt_add_dialog);

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                staffFilter(s.toString());

            }
        });

        toolbar.setText("Search Result");
        toolbar.setBackgroundResource(R.drawable.ic_popup);
        bt_add_dialog.setVisibility(View.GONE);

        adapter = new NotificationAdapter(getActivity(),Constant.RV_NOTIFICATION_SEARCH_RESULT_STAFF,
                searchList, dialog, et_search);
        recycler_view_dialog.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_dialog.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        dialog.show();
    }

    private void staffFilter(String text) {
        ArrayList<NotificationModel> filteredStaffList = new ArrayList<>();

        for (NotificationModel item : searchList) {
            if (item.getEmpFname().toLowerCase().contains(text.toLowerCase())) {
                filteredStaffList.add(item);
            }
        }

        adapter.StafffilterList(filteredStaffList);
    }

    private void studentListDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_for_section);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView toolbar = dialog.findViewById(R.id.toolbar);
        RecyclerView recycler_view_dialog = dialog.findViewById(R.id.recycler_view_dialog);
        EditText ed_search = dialog.findViewById(R.id.ed_search);
        Button bt_add_dialog = dialog.findViewById(R.id.bt_add_dialog);

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

        toolbar.setText("Search Result");
        toolbar.setBackgroundResource(R.drawable.ic_popup);
        bt_add_dialog.setVisibility(View.GONE);

        adapter = new NotificationAdapter(getActivity(), studentModels, dialog, et_search,
                Constant.RV_NOTIFICATION_SEARCH_RESULT);
        recycler_view_dialog.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_dialog.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        dialog.show();
    }

    private void filter(String text) {
        ArrayList<StudentModel> filteredList = new ArrayList<>();

        for (StudentModel item : studentModels) {
            if (item.getStrFirstName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);

    }

    private void openMyDialog(String heading, ArrayList<String> listSelect) {

        assert getActivity() != null;

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_for_section);
        //noinspection ConstantConditions
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spn_send_to.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Send To Is Required", Toast.LENGTH_SHORT).show();

                } else {
                    dialog.show();

                }
            }
        });

        TextView toolbar = dialog.findViewById(R.id.toolbar);
        RecyclerView recycler_view_dialog = dialog.findViewById(R.id.recycler_view_dialog);
        Button bt_add_dialog = dialog.findViewById(R.id.bt_add_dialog);

        toolbar.setText(heading);
        toolbar.setBackgroundResource(R.drawable.ic_popup);
        bt_add_dialog.setBackgroundResource(R.color.colorPrimaryDark);

        NotificationAdapter adapter = new NotificationAdapter(listSelect, getActivity(), bt_add_dialog, dialog,
                tv_select, Constant.RV_NOTIFICATION_CREATE_DIALOG);
        recycler_view_dialog.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_dialog.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void initPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "Need To Permissions", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);

                getAttachmentMethod();
            }

        } else {

            getAttachmentMethod();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {


        }

    }

    private void getAttachmentMethod() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1111);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111) {

            if (data != null) {

                Uri uri = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    String path = saveImage(bitmap);

                    userProfile = new File(path);

                    iv_attach.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            } else {

            }

        }
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


    private void createNotification() {

        switch (str_sendTo) {

            case "departments":

                if (SELECTED_LIST.size() > 0) {
                    Toast.makeText(getActivity(), "Need to Call departments notification api", Toast.LENGTH_SHORT).show();
                    //createNotificationDepartmentAPI(str_sendTo, et_message.getText().toString(), SELECTED_LIST);
                    createNotificationDepartmentAPI(str_sendTo, et_message.getText().toString(), SELECTED_LIST);

                } else
                    Toast.makeText(getActivity(), "Select departments", Toast.LENGTH_SHORT).show();

                break;

            case "divisions":
                if (SELECTED_LIST.size() > 0) {
                    Toast.makeText(getActivity(), "Need to Call divisions notification api", Toast.LENGTH_SHORT).show();
                    createNotificationDivisionAPI(str_sendTo, et_message.getText().toString(), SELECTED_LIST);

                } else
                    Toast.makeText(getActivity(), "Select divisions", Toast.LENGTH_SHORT).show();

                break;

            case "classes":
                if (SELECTED_LIST.size() > 0) {
                    Toast.makeText(getActivity(), "Need to Call classes notification api", Toast.LENGTH_SHORT).show();
                    createNotificationClassesAPI(str_sendTo, et_message.getText().toString(), SELECTED_LIST);
                } else
                    Toast.makeText(getActivity(), "Select classes", Toast.LENGTH_SHORT).show();

                break;

            case "roles":
                if (SELECTED_LIST.size() > 0) {
                    Toast.makeText(getActivity(), "Need to Call roles notification api", Toast.LENGTH_SHORT).show();
                    createNotificationRolesAPI(str_sendTo, et_message.getText().toString(), SELECTED_LIST);

                } else
                    Toast.makeText(getActivity(), "Select roles", Toast.LENGTH_SHORT).show();

                break;

            case "staffs":
                if (SEARCH_ID.length() > 0) {
                    createNotificationStaffAPI(str_sendTo, et_message.getText().toString());
                }
                else
                    Toast.makeText(getActivity(), "Select staffs", Toast.LENGTH_SHORT).show();

                break;

            case "students":
                if (SEARCH_ID.length() > 0)
                    createNotificationStudentsAPI(str_sendTo, et_message.getText().toString());
                else
                    Toast.makeText(getActivity(), "Select Students", Toast.LENGTH_SHORT).show();

                break;

            case "parents":
                if (SELECTED_LIST.size() > 0)
                    Toast.makeText(getActivity(), "Need to Call parents notification api", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Select parents", Toast.LENGTH_SHORT).show();

                break;


        }
    }



    // -------------------------------CREATE API HERE----------------------------------
/*
    private void getNotificationAPI() {

        if (spnList.size() > 0)
            spnList.clear();

        Log.d("GET_NOT_SIZE", String.valueOf(spnList.size()));

        mApiServicePatashala.get_NotificationAdmin(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                Log.d("DEPARTMENT_41", String.valueOf(response.code() + " url" + response.raw().request().url()));

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("GET_NOT_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("GET_NOT_KEY", myKey);

                                spnList.add(myKey);
                            }

                            setSpinnerForSend();

                        } else {
                            Log.d("GET_NOT_FAIL", String.valueOf(response.code()));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("GET_NOT_FAILED", String.valueOf(response.code()));
                    Log.d("GET_NOT_FAILED", response.message());

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }*/

    private void getDepartmentAPI() {

        JSONObject object = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray.put("All");
            object.put("wings", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (deptList.size() != 0) {
            deptList.clear();
        }

        Log.d("DEPARTMENT_0", object.toString());
        Log.d("DEPARTMENT_0", mApiService.toString());

        mApiService.gettingDept(Constant.SCHOOL_ID, object).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {


                Log.d("DEPARTMENT_4", String.valueOf(response.code() + " url" + response.raw().request().url()));

                if (response.isSuccessful()) {
                    try {
                        deptList.clear();
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject1 = json1.getJSONObject("data");

                            Log.d("DEPARTMENT_1", jsonObject1.toString());

                            JSONObject jsonObject2 = jsonObject1.getJSONObject("departments");
                            Log.d("DEPARTMENT_2", jsonObject2.toString());


                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);

                                String name = jsonObjectValue.getString("name");
                                boolean wings_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (wings_status)
                                    deptList.add(name);

                            }
                            Log.d("DEPARTMENT_3", String.valueOf(deptList.size()));

                            openMyDialog("Select Department", deptList);


                        } else {
                            if (String.valueOf(response.code()).equals("409")) {
                                Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                                deptList.clear();

                            }
                            if (String.valueOf(response.code()).equals("404")) {
                                Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                                deptList.clear();

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("DEPT_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                        deptList.clear();

                    }
                    if (String.valueOf(response.code()).equals("404")) {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                        deptList.clear();

                    } else {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });


    }

    private void getDivisionAPI() {

        if (divisionList.size() > 0)
            divisionList.clear();

        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.i("DIVISION**GET", "" + response.body());
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String added_datetime = object1.getString("added_datetime");
                                String Id = object1.getString("id");
                                boolean statusDivision = object1.getBoolean("status");

                                String division = object1.getString("division");
                                String school_id = object1.getString("school_id");
                                String added_by_id = object1.getString("added_by_id");

                                Log.d("MY_DATA", added_datetime + " " + Id + " " + statusDivision + " " +
                                        division + " " + school_id + " " + added_by_id);

                                divisionList.add(division);

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.toString());

            }
        });

    }

    private void getAllClasses() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Class_api_0", String.valueOf(divisionList.size()));

                progressDialog.dismiss();

                if (divisionList.size() > 0) {

                    Log.d("Class_api_1", String.valueOf(divisionList.size()));

                    JSONObject object = new JSONObject();
                    JSONArray jsonArray = new JSONArray();

                    for (int i = 0; i < divisionList.size(); i++) {
                        jsonArray.put(divisionList.get(i));
                    }

                    try {
                        object.put("divisions", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("Class_api_2", object.toString());

                    if (classList.size() > 0)
                        classList.clear();

                    mApiService.getClassSections(Constant.SCHOOL_ID, object).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                            if (response.isSuccessful() || response.code() == 500) {

                                try {
                                    JSONObject obj = null;

                                    obj = new JSONObject(new Gson().toJson(response.body()));
                                    Log.d("myDivisionKey_data2", String.valueOf(response.code()));

                                    //JSONObject obj = new JSONObject(new Gson().toJson(response.body()));

                                    String status = obj.getString("status");
                                    Log.d("myDivisionKey_data2", obj.toString());


                                    if (status.equalsIgnoreCase("Success")) {

                                        JSONObject jsonData = obj.getJSONObject("data");
                                        Log.d("myDivisionKey_data1", jsonData.toString());


                                        Iterator<String> key = jsonData.keys();

                                        while (key.hasNext()) {

                                            String myDivisionKey = key.next();
                                            Log.d("myDivisionKey", myDivisionKey);

                                            JSONObject keyData = jsonData.getJSONObject(myDivisionKey);
                                            Log.d("myDivisionKey_data", keyData.toString());

                                            Iterator<String> classKey = keyData.keys();

                                            while (classKey.hasNext()) {

                                                JSONObject classData = keyData.getJSONObject(classKey.next());
                                                Log.d("classData", classData.toString());

                                                String class_name = classData.getString("class_name");
                                                Log.d("className", class_name);
                                                classList.add(class_name);

                                                JSONArray jsonArray1 = classData.getJSONArray("sections");
                                                Log.d("classData_array", jsonArray1.toString());

                                                ArrayList<String> sections = new ArrayList<>();

                                                for (int i = 0; i < jsonArray1.length(); i++) {

                                                    sections.add(jsonArray1.getString(i));
                                                    Log.d("classData_array " + i, jsonArray1.getString(i));

                                                }

                                                classSectionList.add(new NotificationModel(class_name, sections));
                                            }
                                        }

                                        openMyDialog("Select Classes", classList);
                                        Log.d("classData_array ", String.valueOf(classList.size()));

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    assert response.errorBody() != null;
                                    JSONObject object = new JSONObject(response.errorBody().string());
                                    String message = object.getString("message");
                                    Log.d("create_API", message);
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

            }
        }, 2000);
    }

    private void getRolesBasedOnDepartment() {

        JSONObject wingsJsonObject = new JSONObject();
        JSONObject deptJsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        jsonArray.put("All");

        try {
            wingsJsonObject.put("wings", jsonArray);
            deptJsonObject.put("departments", jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("ROLES_JSON", wingsJsonObject + " " + deptJsonObject);


        if (rolesList.size() != 0) {
            rolesList.clear();
        }
        mApiService.gettingRoles(Constant.SCHOOL_ID, wingsJsonObject, deptJsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("roles");

                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String rolesName = jsonObjectValue.getString("role");
                                //boolean wings_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                rolesList.add(rolesName);

                            }

                            openMyDialog("Select Roles", rolesList);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("ROLES_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                        rolesList.clear();

                    }
                    if (String.valueOf(response.code()).equals("404")) {
                        Toast.makeText(getActivity(), "Wings matching query does not exist.", Toast.LENGTH_SHORT).show();
                        rolesList.clear();

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });


    }

    //-------------------------------APIs for creating notification------------------------------

    private void createNotificationDepartmentAPI(String sentTo, String message, ArrayList<String> list) {

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            jsonArray.put(list.get(i));
        }

        Log.d("JSON_ARRAY_DEPT", jsonArray.toString());

        Log.d("NOTI_API_DEPT_INPUT", Constant.SCHOOL_ID + " " + Constant.EMPLOYEE_BY_ID + " " + sentTo
                + " " + currentTime + " " + currentDate + " " + message + " " + jsonArray);


        if (userProfile != null) {

            RequestBody schoolId = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
            RequestBody employeeId = RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID);
            RequestBody sendTO = RequestBody.create(MediaType.parse("text/plain"), sentTo);
            RequestBody curTime = RequestBody.create(MediaType.parse("text/plain"), currentTime);
            RequestBody curDate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
            RequestBody msg = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody arrayJSON = RequestBody.create(MediaType.parse("text/plain"), jsonArray.toString());

            final RequestBody mFileProfile = RequestBody.create(MediaType.parse("image"), userProfile);
            final MultipartBody.Part fileToUploadProfilepicture = MultipartBody.Part.createFormData("notification_attachment", userProfile.getName(), mFileProfile);

            Log.d("Multipart", fileToUploadProfilepicture.toString() + "  " + userProfile.getName() + " " + mFileProfile);

            mApiServicePatashala.createNotificationDepartment(schoolId, employeeId,
                    sendTO, curTime, curDate, msg, arrayJSON, fileToUploadProfilepicture).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()) {

                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String message = object.getString("message");
                            Log.d("NOTI_API_DEPT_FAIL", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            assert getActivity() != null;
                            getActivity().onBackPressed();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            assert response.errorBody() != null;

                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("NOTI_API_DEPT_FAIL", response.code() + " " + message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                }
            });
        } else {

            Toast.makeText(getActivity(), "Please Attach a image", Toast.LENGTH_SHORT).show();
        }

    }

    private void createNotificationDivisionAPI(String sentTo, String message, ArrayList<String> list) {

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            jsonArray.put(list.get(i));
        }

        Log.d("JSON_ARRAY_DIVISION", jsonArray.toString());

        Log.d("API_DIV_INPUT", Constant.SCHOOL_ID + " " + Constant.EMPLOYEE_BY_ID + " " + sentTo
                + " " + currentTime + " " + currentDate + " " + message + " " + jsonArray);

        if (userProfile != null) {

            RequestBody schoolId = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
            RequestBody employeeId = RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID);
            RequestBody sendTO = RequestBody.create(MediaType.parse("text/plain"), sentTo);
            RequestBody curTime = RequestBody.create(MediaType.parse("text/plain"), currentTime);
            RequestBody curDate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
            RequestBody msg = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody arrayJSON = RequestBody.create(MediaType.parse("text/plain"), jsonArray.toString());

            final RequestBody mFileProfile = RequestBody.create(MediaType.parse("image"), userProfile);
            final MultipartBody.Part fileToUploadProfilepicture = MultipartBody.Part.createFormData("notification_attachment", userProfile.getName(), mFileProfile);

            Log.d("Multipart", fileToUploadProfilepicture.toString() + "  " + userProfile.getName() + " " + mFileProfile);

            mApiServicePatashala.createNotificationDivision(schoolId, employeeId,
                    sendTO, curTime, curDate, msg, arrayJSON, fileToUploadProfilepicture).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()) {

                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String message = object.getString("message");
                            Log.d("API_DIV_SUC", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            assert getActivity() != null;
                            getActivity().onBackPressed();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            assert response.errorBody() != null;

                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("API_DIV_FAIL", response.code() + " " + message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                }
            });

        } else {

            Toast.makeText(getActivity(), "Please Attach a image", Toast.LENGTH_SHORT).show();
        }

    }

    private void createNotificationClassesAPI(String sentTo, String message, ArrayList<String> list) {

        ArrayList<NotificationModel> listNotification = new ArrayList<>();

        Log.d("CLASS_LIST_SIZE1", String.valueOf(list.size()));
        Log.d("CLASS_LIST_SIZE2", String.valueOf(classSectionList.size()));

        for (int i = 0; i < list.size(); i++) {

            for (int j = 0; j < classSectionList.size(); j++) {

                if (list.get(i).equals(classSectionList.get(j).getClassName())) {

                    listNotification.add(classSectionList.get(j));
                }
            }
        }

        Log.d("CLASS_LIST_SIZE2", String.valueOf(listNotification.size()));

        JSONObject object = new JSONObject();

        for (int i = 0; i < listNotification.size(); i++) {

            JSONArray jsonArray = new JSONArray();
            for (int j = 0; j < listNotification.get(i).getSectionName().size(); j++) {

                jsonArray.put(listNotification.get(i).getSectionName().get(j));
            }

            try {
                object.put(listNotification.get(i).getClassName(), jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("CLASS_JSON_OBJECT", object.toString());

        if (userProfile != null) {

            RequestBody schoolId = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
            RequestBody employeeId = RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID);
            RequestBody sendTO = RequestBody.create(MediaType.parse("text/plain"), sentTo);
            RequestBody curTime = RequestBody.create(MediaType.parse("text/plain"), currentTime);
            RequestBody curDate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
            RequestBody msg = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody jsonObject = RequestBody.create(MediaType.parse("text/plain"), object.toString());

            final RequestBody mFileProfile = RequestBody.create(MediaType.parse("image"), userProfile);
            final MultipartBody.Part fileToUploadProfilepicture = MultipartBody.Part.createFormData("notification_attachment", userProfile.getName(), mFileProfile);

            Log.d("Multipart", fileToUploadProfilepicture.toString() + "  " + userProfile.getName() + " " + mFileProfile);

            mApiServicePatashala.createNotificationClasses(schoolId, employeeId,
                    sendTO, curTime, curDate, msg, jsonObject, fileToUploadProfilepicture).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()) {

                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String message = object.getString("message");
                            Log.d("API_CLASS_SUC", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            assert getActivity() != null;
                            getActivity().onBackPressed();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            assert response.errorBody() != null;
                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("API_CLASS_FAIL", response.code() + " " + message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                }
            });
        } else {

            Toast.makeText(getActivity(), "Please Attach a image", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationRolesAPI(String sentTo, String message, ArrayList<String> list) {

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            jsonArray.put(list.get(i));
        }

        Log.d("JSON_ARRAY_Roles", jsonArray.toString());

        Log.d("API_Roles_INPUT", Constant.SCHOOL_ID + " " + Constant.EMPLOYEE_BY_ID + " " + sentTo
                + " " + currentTime + " " + currentDate + " " + message + " " + jsonArray);

        if (userProfile != null) {

            RequestBody schoolId = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
            RequestBody employeeId = RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID);
            RequestBody sendTO = RequestBody.create(MediaType.parse("text/plain"), sentTo);
            RequestBody curTime = RequestBody.create(MediaType.parse("text/plain"), currentTime);
            RequestBody curDate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
            RequestBody msg = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody arrayJson = RequestBody.create(MediaType.parse("text/plain"), jsonArray.toString());

            final RequestBody mFileProfile = RequestBody.create(MediaType.parse("image"), userProfile);
            final MultipartBody.Part fileToUploadProfilepicture = MultipartBody.Part.createFormData("notification_attachment", userProfile.getName(), mFileProfile);

            Log.d("Multipart", fileToUploadProfilepicture.toString() + "  " + userProfile.getName() + " " + mFileProfile);

            mApiServicePatashala.createNotificationRoles(schoolId, employeeId,
                    sendTO, curTime, curDate, msg, arrayJson, fileToUploadProfilepicture).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()) {

                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String message = object.getString("message");
                            Log.d("API_Roles_success", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            assert response.errorBody() != null;

                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("API_Roles_FAIL", response.code() + " " + message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                }
            });
        } else {

            Toast.makeText(getActivity(), "Please Attach a image", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationStudentsAPI(String str_sendTo, String message) {

        JSONArray jsonArray = new JSONArray();
        JSONObject studentObject = new JSONObject();

        try {
            studentObject.put("student_uuid",SEARCH_ID);
            jsonArray.put(studentObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("JSON_ARRAY_Roles", jsonArray.toString());

        Log.d("API_Roles_INPUT", Constant.SCHOOL_ID+" "+Constant.EMPLOYEE_ID+" "+str_sendTo
                +" "+currentTime+" "+currentDate+" "+message+" "+jsonArray);

        if (userProfile != null) {

          //  progressDialog.show();
            String myTitle = et_title.getText().toString();
            RequestBody schoolId = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
            RequestBody employeeId = RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID);
            RequestBody sendTO = RequestBody.create(MediaType.parse("text/plain"), str_sendTo);
            RequestBody curTime = RequestBody.create(MediaType.parse("text/plain"), currentTime);
            RequestBody curDate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
            RequestBody msg = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody arrayJson = RequestBody.create(MediaType.parse("text/plain"), jsonArray.toString());
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), myTitle);

            final RequestBody mFileProfile = RequestBody.create(MediaType.parse("image"), userProfile);
            final MultipartBody.Part fileToUploadProfilepicture = MultipartBody.Part.createFormData("notification_attachment", userProfile.getName(), mFileProfile);

            Log.d("Multipart", fileToUploadProfilepicture.toString() + "  " + userProfile.getName() + " " + mFileProfile);

            mApiServicePatashala.createNotificationStudents(schoolId, employeeId,
                    sendTO, curTime, curDate, msg, arrayJson,title, fileToUploadProfilepicture).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    Log.i("response**",""+response.code()+response.body());
                    if (response.isSuccessful()) {

                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String message = object.getString("message");
                            Log.d("API_Roles_success", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            assert getActivity()!=null;
                            getActivity().onBackPressed();
                    //        progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            assert response.errorBody() != null;

                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("API_Roles_FAIL", response.code() + " " + message);
                        //    progressDialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                }
            });
        }else {

            Toast.makeText(getActivity(), "Please Attach a image", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationStaffAPI(String str_sendTo, String message) {

        JSONArray jsonArray = new JSONArray();
        JSONObject studentObject = new JSONObject();

        try {
            studentObject.put("employee_uuid",SEARCH_ID);
            jsonArray.put(studentObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("JSON_ARRAY_Roles", jsonArray.toString());

        Log.d("API_Roles_INPUT", Constant.SCHOOL_ID+" "+Constant.EMPLOYEE_ID+" "+str_sendTo
                +" "+currentTime+" "+currentDate+" "+message+" "+jsonArray);

        if (userProfile != null) {
         //   progressDialog.show();
            String myTitle = et_title.getText().toString();
            RequestBody schoolId = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
            RequestBody employeeId = RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID);
            RequestBody sendTO = RequestBody.create(MediaType.parse("text/plain"), str_sendTo);
            RequestBody curTime = RequestBody.create(MediaType.parse("text/plain"), currentTime);
            RequestBody curDate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
            RequestBody msg = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody arrayJson = RequestBody.create(MediaType.parse("text/plain"), jsonArray.toString());
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), myTitle);

            final RequestBody mFileProfile = RequestBody.create(MediaType.parse("image"), userProfile);
            final MultipartBody.Part fileToUploadProfilepicture = MultipartBody.Part.createFormData("notification_attachment", userProfile.getName(), mFileProfile);

            Log.d("Multipart", fileToUploadProfilepicture.toString() + "  " + userProfile.getName() + " " + mFileProfile);

            mApiServicePatashala.createNotificationStaff(schoolId, employeeId,
                    sendTO, curTime, curDate, msg, arrayJson,title, fileToUploadProfilepicture).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()) {

                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String message = object.getString("message");
                            Log.d("API_Staff_success", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            assert getActivity()!=null;
                            getActivity().onBackPressed();
                        //    progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            assert response.errorBody() != null;

                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("API_Staff_FAIL", response.code() + " " + message);
                         //   progressDialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                }
            });
        }else {

            Toast.makeText(getActivity(), "Please Attach a image", Toast.LENGTH_SHORT).show();
        }

    }

}
