package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
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


public class Fragment_ScheduleBarriers extends Fragment implements View.OnClickListener {
    APIService apiService, mApiServicePatashala;
    private ArrayList<String> academicList, listDivision, listClass, listSection, sessionList, listSectionNew,
            listscheduleType;
    Spinner sp_academic, spn_division, spn_class, spn_section, sp_sessionNo, spn_schedule_type;
    String strAcdaemicYear, acdaemicFromDate, acdaemicToDate, strFromDate, strToDate, strScheduleType;
    private String strDiv, strSection, str_class;
    ArrayList<ClassSectionModel> modelArrayList;
    Button btnSubmit;
    RelativeLayout re_fromdate, re_todate;
    TextView tv_from_date, tv_to_date, tv_Name, tv_Type, tv_title;
    EditText tv_schedule_title, tv_schedule_type, tv_schedule_name;
    Calendar calendar;
    String fromYear, toYear, strfromDate, str_toDate;
    Date date1, date2;
    ImageView iv_attachmentImage,iv_attach,iv_backBtn;
    int year, month, date;
    private ArrayList<String> serialList;
    private DateConvertor convertor;
    private Utilities utilities;
    EditText ed_message;
    String serialNo = "";
    String fromDate = "";
    String toDate = "";
    TextView tvTitle;
FloatingActionButton fab;
    File userProfile;

    String strAcademicFDate,strAcademicTDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_schedule, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        getDivisionApi();
        getAcademicYearAPI();

        // getAcadmicYear();
        // initListener();

        return view;
    }

    private void initViews(View view) {
        apiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();

        iv_attachmentImage = view.findViewById(R.id.iv_attachmentImage);
        iv_attach = view.findViewById(R.id.iv_attach);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        tvTitle = view.findViewById(R.id.tvTitle);
        sp_academic = view.findViewById(R.id.sp_academic);
        sp_sessionNo = view.findViewById(R.id.sp_sessionNo);
        spn_division = view.findViewById(R.id.spn_division);
        spn_class = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);
        spn_schedule_type = view.findViewById(R.id.spn_schedule_type);

        btnSubmit = view.findViewById(R.id.btn_ok);
        tv_schedule_title = view.findViewById(R.id.tv_schedule_title);
        tv_schedule_type = view.findViewById(R.id.tv_schedule_type);
        tv_schedule_name = view.findViewById(R.id.tv_schedule_name);
        re_fromdate = view.findViewById(R.id.re_fromdate);
        re_todate = view.findViewById(R.id.re_todate);
        tv_from_date = view.findViewById(R.id.tv_from_date);
        tv_to_date = view.findViewById(R.id.tv_to_date);
        tv_title = view.findViewById(R.id.tv_title);
        tv_Type = view.findViewById(R.id.tv_type);
        tv_Name = view.findViewById(R.id.tv_Name);
        ed_message = view.findViewById(R.id.ed_message);


        iv_attachmentImage.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        re_fromdate.setOnClickListener(this);
        re_todate.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);


        academicList = new ArrayList<>();
        serialList = new ArrayList<>();
        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        listSectionNew = new ArrayList<>();
        modelArrayList = new ArrayList<>();
        listscheduleType = new ArrayList<>();
        convertor = new DateConvertor();

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);
        listscheduleType.clear();
        tvTitle.setText("Add Schedule");

    }


    private void getAcademicYearAPI() {
        Log.d("jwhdlff", ";jlfjdgfj;");

        if (academicList.size() > 0) {
            academicList.clear();
        }

        academicList.add(0, "-Academic Year-");

        mApiServicePatashala.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()) {

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("AcademicYear", jsonData.toString());

                            JSONObject academicYear = jsonData.getJSONObject("Academic_years");

                            Iterator<String> key = academicYear.keys();

                            while (key.hasNext()) {

                                JSONObject years = academicYear.getJSONObject(key.next());

                                String start_date = years.getString("start_date");
                                String end_date = years.getString("end_date");

                                Log.i("AcademicYear_11", start_date + " " + end_date);

                                //String from = convertor.getDateByTZ(start_date);
                               // String to = convertor.getDateByTZ(end_date);

                                Log.i("AcademicYear_12", start_date + " " + end_date);

                                academicList.add(start_date + "/" + end_date);
                                Log.i("AcademicYear_2", "" + academicList.size());
                            }
                            Log.i("lakfblkasAAAA", "" + academicList.size());


                            setSpinnerForAcademicYear(academicList);

                        } else {
                            Log.d("AcademicYear_3", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
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
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

    private void setSpinnerForAcademicYear(ArrayList<String> academicList) {
        Log.d("asflkaf1", "kgdkdsgkgd");
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), academicList);
        sp_academic.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sp_academic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("asflkaf2", "kgdkdsgkgd" + i);


                if (i == 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(0, "-Session-");
                    setSpinnerForSerialNo(list, "", "");

                } else {

                    strAcdaemicYear = sp_academic.getSelectedItem().toString();
                    String[] date = strAcdaemicYear.split("/");

                    fromDate = date[0];
                    toDate = date[1];

                    Log.d("asflkaf", "kgdkdsgkgd"+fromDate+toDate);
                    getSessionAPI(fromDate, toDate);


                    SimpleDateFormat inputFormate = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat outputFormate = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date inputFromDate = inputFormate.parse(fromDate);
                        Date inputToDate = inputFormate.parse(toDate);

                        strAcademicFDate = outputFormate.format(inputFromDate);
                        strAcademicTDate = outputFormate.format(inputToDate);

                        Log.i("strAcademicFDate",""+strAcademicFDate);
                        Log.i("strAcademicTDate",""+strAcademicTDate);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getSessionAPI(final String fromDate, final String toDate) {

        if (serialList.size() > 0) {
            serialList.clear();
        }

        serialList.add(0, "-Session-");

        mApiServicePatashala.getSessions(Constant.SCHOOL_ID, fromDate, toDate).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");

                            JSONObject sessionsData = jsonData.getJSONObject("sessions");

                            Iterator<String> key = sessionsData.keys();

                            while (key.hasNext()) {

                                JSONObject keyData = sessionsData.getJSONObject(key.next());

                                int session_serial_no = keyData.getInt("session_serial_no");
                                Log.d("sessionSerialNO", String.valueOf(session_serial_no));

                                serialList.add(session_serial_no + "");
                            }

                            setSpinnerForSerialNo(serialList, fromDate, toDate);

                        } else {
                            Log.d("ldafhhlkafhd", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API2", message);
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

    private void setSpinnerForSerialNo(ArrayList<String> list, final String fromDate, final String toDate) {

        CustomSpinnerAdapter adapter1 = new CustomSpinnerAdapter(getActivity(), list, "Green");
        sp_sessionNo.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        sp_sessionNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if (i == 0) {


                } else {

                    serialNo = sp_sessionNo.getSelectedItem().toString();
                    Log.d("slhdlhsf", serialNo);

                    String from = convertor.getDateDMY_to_YMD(fromDate);
                    String to = convertor.getDateDMY_to_YMD(toDate);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getDivisionApi() {
        listDivision.clear();
        listDivision.add("Division");
        apiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String added_datetime = object1.getString("added_datetime");
                                String Id = object1.getString("id");
                                statusDivision = object1.getBoolean("status");
                                if (statusDivision) {
                                    String division = object1.getString("division");

                                    listDivision.add(division);
                                }


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
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());

            }
        });

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
        spn_division.setAdapter(customSpinnerAdapter);

        spn_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                if (i == 0) {
                    listscheduleType.add("Schedule Type");


                } else {

                    strDiv = spn_division.getSelectedItem().toString();

                    listscheduleType.clear();
                    listscheduleType.add("Schedule Type");
                    listscheduleType.add("Exam");
                    listscheduleType.add("Holiday");
                    listscheduleType.add("Event");


                }

                CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listscheduleType);
                spn_schedule_type.setAdapter(customSpinnerAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spn_schedule_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    strScheduleType = "Schedule";
                } else {
                    strScheduleType = spn_schedule_type.getSelectedItem().toString();

                }

                tv_title.setText("");
                tv_title.setText(strScheduleType + " Title");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_attachmentImage:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);

                break;

                case R.id.re_fromdate:
                fromdateDialog();
                break;


                case R.id.iv_backBtn:

                    ExamActivity examActivity =(ExamActivity)getActivity();
                    examActivity.loadFragment(Constant.SCHEDULE_BARRIERS_LIST, null);
                break;

            case R.id.re_todate:
                toDateDialog();
                break;
            case R.id.btn_ok:
                /*if (strAcdaemicYear.equalsIgnoreCase("-Academic Year-")) {
                    Toast.makeText(getActivity(), "Select Academic year", Toast.LENGTH_SHORT).show();

                } else if (serialNo.equalsIgnoreCase("-Session-")) {
                    Toast.makeText(getActivity(), "Select Session", Toast.LENGTH_SHORT).show();

                }else if (strDiv.equalsIgnoreCase("Division")) {
                    Toast.makeText(getActivity(), "Select Division", Toast.LENGTH_SHORT).show();

                } else if (strScheduleType.equalsIgnoreCase("Schedule Type")) {
                        Toast.makeText(getActivity(), "Select Schedule Type", Toast.LENGTH_SHORT).show();

                }
                lse if (tv_schedule_type.getText().toString().isEmpty() || tv_schedule_type.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter Schedule Type", Toast.LENGTH_SHORT).show();

                }* else if (tv_from_date.getText().toString().equalsIgnoreCase("From Date")) {
                   else */




                if (tv_schedule_title.getText().toString().isEmpty() || tv_schedule_title.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter Schedule Title", Toast.LENGTH_SHORT).show();

                } else if (tv_to_date.getText().toString().equalsIgnoreCase("To Date")) {
                    Toast.makeText(getActivity(), "Select from date", Toast.LENGTH_SHORT).show();

                } else if (ed_message.getText().toString().isEmpty() || ed_message.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter Notes", Toast.LENGTH_SHORT).show();

                } else {
                    addScheduleApiCall();

                }
                break;
        }
    }

    private void toDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                toYear = yearFormat.format(calendar.getTime());
                str_toDate = simpleDateFormat.format(calendar.getTime());
                Log.d("CHECK_DATE", str_toDate);
                date2 = new Date();
                try {
                    date2 = simpleDateFormat.parse(str_toDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                tv_to_date.setText(str_toDate);

            }
        }, year, month, date);

        //dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void fromdateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());


                fromYear = yearFormat.format(calendar.getTime());
                strfromDate = simpleDateFormat.format(calendar.getTime());
                Log.d("CHECK_DATE", strfromDate);
                date1 = new Date();
                try {
                    date1 = simpleDateFormat.parse(strfromDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                tv_from_date.setText(strfromDate);

            }
        }, year, month, date);

        //dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

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

    private void addScheduleApiCall() {

        if (userProfile != null) {


            Log.d("dateFormat", strAcademicFDate+" "+strAcademicTDate+fromDate);

            utilities.displayProgressDialog(getActivity(), "Processing...", false);

            RequestBody res_schoolId = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
            RequestBody res_employeeId = RequestBody.create(MediaType.parse("text/plain"),  Constant.EMPLOYEE_BY_ID);
            RequestBody res_fromDate = RequestBody.create(MediaType.parse("text/plain"), fromDate);
            RequestBody res_toDate = RequestBody.create(MediaType.parse("text/plain"), toDate);


            RequestBody res_res_serialNo = RequestBody.create(MediaType.parse("text/plain"), serialNo.trim());
            RequestBody res_strDiv = RequestBody.create(MediaType.parse("text/plain"), strDiv.trim());
            RequestBody res_strScheduleType = RequestBody.create(MediaType.parse("text/plain"), strScheduleType.trim());
            RequestBody res_tv_schedule_title = RequestBody.create(MediaType.parse("text/plain"), tv_schedule_title.getText().toString().trim());
            RequestBody res_strfromDate = RequestBody.create(MediaType.parse("text/plain"), strfromDate.trim());
            RequestBody res_str_toDate = RequestBody.create(MediaType.parse("text/plain"), str_toDate.trim());
            RequestBody res_ed_message = RequestBody.create(MediaType.parse("text/plain"), ed_message.getText().toString().trim());


            Log.i("res_schoolId**",""+res_schoolId);
            Log.i("res_schoolId**",""+res_employeeId);
            Log.i("res_schoolId**",""+res_fromDate+res_fromDate+res_res_serialNo+res_strDiv);
            Log.i("res_schoolId**",""+res_strScheduleType+res_tv_schedule_title);
            Log.i("res_schoolId**",""+res_strfromDate+res_str_toDate);
            Log.i("res_schoolId**",""+res_ed_message);


            final RequestBody mFileProfile = RequestBody.create(MediaType.parse("image"), userProfile);
            final MultipartBody.Part fileToUploadProfilepicture = MultipartBody.Part.createFormData("schedule_attachment", userProfile.getName(), mFileProfile);

            Log.d("Multipart", fileToUploadProfilepicture.toString() + "  " + userProfile.getName() + " " + mFileProfile);

            apiService.create_schedule(res_schoolId, res_employeeId,
                    res_fromDate, res_toDate, res_res_serialNo, res_strDiv, res_strScheduleType,
                    res_tv_schedule_title, res_strfromDate,res_str_toDate,res_ed_message,
                    fileToUploadProfilepicture).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    Log.d("NOTI_API_DEPT_", response.code() + " " + response.body()+response.raw());
                    if (response.isSuccessful()) {
                        utilities.cancelProgressDialog();


                        if (response.code()==200){
                            Toast.makeText(getActivity(),"Schedule has been created successfully",Toast.LENGTH_SHORT).show();

                            sp_academic.setSelection(0);
                            sp_sessionNo.setSelection(0);
                            spn_division.setSelection(0);
                            spn_schedule_type.setSelection(0);
                            tv_schedule_title.setText("");
                            tv_from_date.setText("From Date");
                            tv_to_date.setText("To Date");
                            ed_message.setText("");

                        }

                    } else {

                        try {
                            utilities.cancelProgressDialog();
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
                    utilities.cancelProgressDialog();
                    Log.d("NOTI_API_DEPT_ERROR", t.toString());
                }
            });
        } else {

            Toast.makeText(getActivity(), "Please Attach a image", Toast.LENGTH_SHORT).show();
        }



    }
}

















































    /*
from_date:2021-12-22
to_date:2021-12-31
schedule_type:Holiday
division:PUC
schedule_title:Dance Competition
session_serial_no:1
note:it's a holiday!!
added_employeeid:54ff8e7e-a3d7-482e-b464-d87b9bcfd1d7
school_id:55e9cd8c-052a-46b1-b81c-14f85e11a8fe


academic_start_date:2021-5-2
academic_end_date:2022-5-2
schedule_attachment:File_upload*/











/*
    private void getAcadmicYear() {
        mApiServicePatashala.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                academicList.clear();
                if (response.isSuccessful()) {

                    try {
                        academicList.add("Academic Year");

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("AcademicYear", jsonData.toString());

                            JSONObject academicYear = jsonData.getJSONObject("Academic_years");

                            Iterator<String> key = academicYear.keys();

                            while (key.hasNext()) {

                                JSONObject years = academicYear.getJSONObject(key.next());

                                String start_date = years.getString("start_date");
                                String end_date = years.getString("end_date");

                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date fromYear = formater.parse(start_date);
                                    Date toYear = formater.parse(end_date);

                                    String from = formatterDate.format(fromYear);
                                    String to = formatterDate.format(toYear);

                                    String acadmicListData = from + " / " + to;
                                    academicList.add(acadmicListData);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            setSpinnerForAcademicYear(academicList);

                        } else {
                            Log.d("ldafhhlka", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
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
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });

    }*/

  /*  private void setSpinnerForAcademicYear(ArrayList<String> academicList) {


        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), academicList);
        sp_academic.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sp_academic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                strAcdaemicYear = sp_academic.getSelectedItem().toString();
                if (strAcdaemicYear.equalsIgnoreCase("Academic Year")) {

                } else {
                    String[] date = strAcdaemicYear.split("/");


                    acdaemicFromDate = date[0];
                    acdaemicToDate = date[1];

                    Log.i("ACD***", "" + acdaemicFromDate + acdaemicToDate);

                    SimpleDateFormat inputFormate = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputFormate = new SimpleDateFormat("dd-MM-yyyy");

                    try {
                        Date inputFromDate = inputFormate.parse(acdaemicFromDate);
                        Date inputToDate = inputFormate.parse(acdaemicToDate);

                        strFromDate = outputFormate.format(inputFromDate);
                        strToDate = outputFormate.format(inputToDate);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

   */

   /* private void getClassSectionList(JSONObject jsonObject) {
        listClass.clear();
        listClass.add("Class");
        apiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.i("CLASS_SECTIONDDD", "" + response.body());

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            if (object.getJSONObject("data").toString().equals("{}")) {
                                // modelArrayList.clear();
                                //  customSpinnerAdapter.notifyDataSetChanged();
                                //   setSpinnerForClass();


                            } else {

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


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spn_class.setAdapter(customSpinnerAdapter);


        spn_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_class = spn_class.getSelectedItem().toString();

                listSectionNew.clear();
                listSectionNew.add("Section");

                boolean b = true;

                for (int j = 0; j < modelArrayList.size(); j++) {
                    if (modelArrayList.get(j).getClassName().contains(str_class)) {
                        listSectionNew.clear();

                        for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {

                            listSectionNew.add(modelArrayList.get(j).getListSection().get(k));

                        }
                    }
                }


                CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSectionNew);
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
                if (strSection.equalsIgnoreCase("Section")) {
                    Toast.makeText(getActivity(), "Select Section", Toast.LENGTH_SHORT).show();

                }
                //getSubject(strDiv, str_class);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/




