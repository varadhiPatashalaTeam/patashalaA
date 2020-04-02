package in.varadhismartek.patashalaerp.ScheduleModule;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddScheduleManagementFragment extends Fragment implements View.OnClickListener {


    //Okay night after completing just send me code and Apk also i'll share my screen you attach your code in that module.
    //ok

    private CircleImageView civ_image;
    private ImageView iv_backBtn, iv_addExamSchedule;
    private Spinner spn_class, spn_section,spn_examType,spn_holidayType,spn_eventType, spn_subject;
    private EditText et_message;
    private LinearLayout ll_subjectRow;
    private TextView tv_add_class, tv_scheduleType, tv_division, tv_fromDate,tv_toDate;
    private TextView tv_submit, tv_title;
    private RecyclerView recycler_view_exam, rcv_class;
    private ScheduleModel scheduleModel;

    private String scheduleType = "", division = "", fromDate = "", toDate = "",examType = "", holidayType = "",
            eventType = "", scheduleTitle = "";

    private APIService mApiService, mApiServicePatashala;
    private ProgressDialog progressDialog;
    private ArrayList<String> examTypeList, holidayTypeList, eventTypeList, classList, sectionList, subjectList;
    private ArrayList<MyScheduleModel> subjectDateList;
    private String myDate = "", myTime = "";
    private ScheduleAdapter adapterExamRow, adapterClassSection;
    private ArrayList<MyScheduleModel> classSectionList;
    private String str_className = "", str_setction = "", str_subject = "";
    private int day, month, year;
    private final int REQUEST_PERMISSION_CODE = 1234;

    public AddScheduleManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_schedule_management, container, false);

        initViews(view);
        initListeners();
        getExatrBundle();

        getAllClasses();

        return view;
    }

    private void getExatrBundle() {

        Bundle bundle = getArguments();
        scheduleModel = (ScheduleModel) bundle.getSerializable("ScheduleModel");

        scheduleType=  scheduleModel.getSchedule_type();
        division =  scheduleModel.getDivision();
        fromDate =  scheduleModel.getFrom_date();
        toDate =  scheduleModel.getTo_date();
        scheduleTitle =  scheduleModel.getSchedule_title();

        String[] date = fromDate.split("-");

        year = Integer.parseInt(date[0]);
        month = Integer.parseInt(date[1]);
        day = Integer.parseInt(date[2]);

        tv_scheduleType.setText(scheduleType);
        tv_division.setText(division);
        tv_fromDate.setText(fromDate);
        tv_toDate.setText(toDate);
        tv_title.setText(scheduleTitle);

        if (!scheduleModel.getSchedule_image().equalsIgnoreCase("")){
            Glide.with(getActivity()).load(Constant.IMAGE_URL+scheduleModel.getSchedule_image()).into(civ_image);
        }

        Log.d("My_bundle", scheduleType+" "+division+" ll"+fromDate);

        switch (scheduleType){

            case "Exam":
                spn_examType.setVisibility(View.VISIBLE);
                ll_subjectRow.setVisibility(View.VISIBLE);
                getExamTypeAPI();
                setRecyclerViewForExamRow();

                break;

            case "Event":
                spn_eventType.setVisibility(View.VISIBLE);
                tv_add_class.setVisibility(View.VISIBLE);
                getEventTypesAPI();
                setRecyclerViewForClass();
                break;

            case "Holiday":
                spn_holidayType.setVisibility(View.VISIBLE);
                tv_add_class.setVisibility(View.VISIBLE);
                getHolidayTypesAPI();
                setRecyclerViewForClass();

                break;
        }

    }

    private void setRecyclerViewForExamRow() {

        adapterExamRow = new ScheduleAdapter(subjectDateList, getActivity(), Constant.RV_ADD_SCHEDULE_EXAM_DATE_TIME);
        recycler_view_exam.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_exam.setAdapter(adapterExamRow);
    }

    private void setRecyclerViewForClass() {

        adapterClassSection = new ScheduleAdapter(classSectionList, getActivity(), Constant.RV_ADD_SCHEDULE_CLASS_UPER);
        rcv_class.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcv_class.setAdapter(adapterClassSection);
    }

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
        iv_addExamSchedule.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        tv_add_class.setOnClickListener(this);
    }

    private void initViews(View view) {

        mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(getActivity());

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        civ_image = view.findViewById(R.id.civ_image);

        tv_scheduleType = view.findViewById(R.id.tv_scheduleType);
        tv_division = view.findViewById(R.id.tv_division);
        tv_fromDate = view.findViewById(R.id.tv_fromDate);
        tv_toDate = view.findViewById(R.id.tv_toDate);

        spn_examType = view.findViewById(R.id.spn_examType);
        spn_holidayType = view.findViewById(R.id.spn_holidayType);
        spn_eventType = view.findViewById(R.id.spn_eventType);
        spn_class = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);
        spn_subject = view.findViewById(R.id.spn_subject);
        iv_addExamSchedule = view.findViewById(R.id.iv_addExamSchedule);
        et_message = view.findViewById(R.id.et_message);
        ll_subjectRow = view.findViewById(R.id.ll_subjectRow);

        tv_add_class = view.findViewById(R.id.tv_add_class);
        tv_submit = view.findViewById(R.id.tv_submit);
        recycler_view_exam = view.findViewById(R.id.recycler_view_exam);
        rcv_class = view.findViewById(R.id.rcv_class);
        tv_title = view.findViewById(R.id.tv_title);

        examTypeList = new ArrayList<>();
        holidayTypeList = new ArrayList<>();
        eventTypeList = new ArrayList<>();

        classSectionList = new ArrayList<>();

        classList = new ArrayList<>();
        sectionList = new ArrayList<>();
        subjectList = new ArrayList<>();

        subjectDateList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.iv_addExamSchedule:

                if (str_subject.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Subject is required", Toast.LENGTH_SHORT).show();

                }else {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_add_exam_schedule);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    dialog.show();

                    TextView tv_subjectName = dialog.findViewById(R.id.tv_subjectName);
                    TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
                    TextView tv_add = dialog.findViewById(R.id.tv_add);

                    RelativeLayout rl_Date = dialog.findViewById(R.id.rl_Date);
                    RelativeLayout rl_time = dialog.findViewById(R.id.rl_time);

                    final TextView tv_add_date = dialog.findViewById(R.id.tv_add_date);
                    final TextView tv_add_time = dialog.findViewById(R.id.tv_add_time);

                    tv_subjectName.setText(str_subject);

                    rl_Date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.YEAR, i);
                                    calendar.set(Calendar.MONTH, i1);
                                    calendar.set(Calendar.DAY_OF_MONTH, i2);

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                    myDate = dateFormat.format(calendar.getTime());

                                    tv_add_date.setText(myDate);
                                }

                            }, year, month-1, day);

                            //dateDialog.getDatePicker().setMinDate();

                            dateDialog.show();

                        }
                    });

                    rl_time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

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
                                            myTime = format.format(time.getTime());
                                            tv_add_time.setText(format.format(time.getTime()));
                                        }
                                    },
                                    currentTime.get(Calendar.HOUR_OF_DAY), // Current hour value
                                    currentTime.get(Calendar.MINUTE), // Current minute value
                                    DateFormat.is24HourFormat(getActivity())); // Check 24 Hour or AM/PM format
                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();
                        }
                    });

                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    tv_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (myDate.equalsIgnoreCase("")){


                            }else if (myTime.equalsIgnoreCase("")){


                            }else {

                                if (subjectDateList.size() > 0){

                                    boolean b= true;

                                    for (int i=0; i<subjectDateList.size(); i++){

                                        if (subjectDateList.get(i).getSubject().equalsIgnoreCase(str_subject)){

                                            b = false;
                                            subjectDateList.set(i, new MyScheduleModel(myDate,str_subject,myTime));
                                        }
                                    }

                                    if (b){
                                        subjectDateList.add(new MyScheduleModel(myDate,str_subject,myTime));
                                    }

                                }else {

                                    subjectDateList.add(new MyScheduleModel(myDate,str_subject,myTime));

                                }

                                Log.d("subjectDateList_size",subjectDateList.size()+"");
                                adapterExamRow.notifyDataSetChanged();
                                dialog.dismiss();

                            }
                        }
                    });
                }

                break;

            case R.id.tv_add_class:

                if (spn_class.getSelectedItemPosition()==0){
                    Toast.makeText(getActivity(), "Class Is Required", Toast.LENGTH_SHORT).show();

                }else if(spn_section.getSelectedItemPosition()==0){
                    Toast.makeText(getActivity(), "Section Is Required", Toast.LENGTH_SHORT).show();

                }else {

                    String className = spn_class.getSelectedItem().toString().trim();
                    String sectionName = spn_section.getSelectedItem().toString().trim();

                    if (classSectionList.size() > 0) {

                        boolean b = true;

                        for (int i=0; i<classSectionList.size(); i++){

                            if (classSectionList.get(i).getClassName().equalsIgnoreCase(className)){
                                b = false;
                                boolean section = true;

                                for (int j=0; j<classSectionList.get(i).getSectionList().size(); j++){

                                    if (classSectionList.get(i).getSectionList().get(j).equalsIgnoreCase(sectionName)){
                                        section = false;
                                        Toast.makeText(getActivity(), "Section Is Already Added", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (section){
                                    classSectionList.get(i).getSectionList().add(sectionName);
                                }
                            }
                        }
                        if (b){
                            ArrayList<String> sectionList = new ArrayList<>();
                            sectionList.add(sectionName);
                            classSectionList.add(new MyScheduleModel(sectionList, className));
                        }

                    } else {

                        ArrayList<String> sectionList = new ArrayList<>();
                        sectionList.add(sectionName);
                        classSectionList.add(new MyScheduleModel(sectionList, className));
                    }

                    adapterClassSection.notifyDataSetChanged();
                }

                break;

            case R.id.tv_submit:

                switch (scheduleType){

                    case "Exam":
                        createExamsAPI();
                        break;

                    case "Event":
                        createEventAPI();
                        break;

                    case "Holiday":
                        createHolidayAPI();
                        break;
                }



                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION_CODE){

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

    private void getAttachmentMethod() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1111);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1111){

            if(data != null){

                Uri uri = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                    String path = saveImage(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();

                }
            }else {

            }

        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
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

    private void getExamTypeAPI(){

        progressDialog.show();

        mApiService.getExamType("05861673-1073-4029-b04d-6db1b1372a1d").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (examTypeList.size()>0)
                    examTypeList.clear();

                examTypeList.add(0, "-Exam Type-");

                if (response.isSuccessful()){

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Sucess")){

                            Log.d("examType_success", message1+" "+response.code());

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

                            progressDialog.dismiss();
                            setSpinnerForExamType();
                        }else {
                            progressDialog.dismiss();
                            Log.d("examType_success", message1+" "+response.code());
                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    try {
                        progressDialog.dismiss();
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("examType_fail", message);

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

    private void setSpinnerForExamType(){

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), examTypeList);
        spn_examType.setAdapter(customSpinnerAdapter);

        spn_examType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    examType = "";

                }else {
                    examType = spn_examType.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getHolidayTypesAPI(){

        progressDialog.show();

        mApiServicePatashala.getHolidayTypes("05861673-1073-4029-b04d-6db1b1372a1d").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (holidayTypeList.size()>0)
                    holidayTypeList.clear();

                holidayTypeList.add(0, "-Holiday Type-");

                if (response.isSuccessful()){

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){

                            Log.d("holidayType_success", message1+" "+response.code());

                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i=0; i<jsonArray.length();i++){

                                JSONObject jsonData = jsonArray.getJSONObject(i);

                                String holiday_name = jsonData.getString("holiday_name");

                                holidayTypeList.add(holiday_name);
                            }

                            progressDialog.dismiss();
                            setSpinnerForHolidayType();

                        }else {
                            progressDialog.dismiss();
                            setSpinnerForHolidayType();
                            Log.d("holidayType_else", message1+" "+response.code());
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    try {
                        progressDialog.dismiss();
                        setSpinnerForHolidayType();
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("holidayType_fail", message);

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

    private void setSpinnerForHolidayType(){

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), holidayTypeList);
        spn_holidayType.setAdapter(customSpinnerAdapter);

        spn_holidayType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    holidayType = "";

                }else {
                    holidayType = spn_holidayType.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getEventTypesAPI(){

        progressDialog.show();

        mApiServicePatashala.getEventTypes("05861673-1073-4029-b04d-6db1b1372a1d").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (eventTypeList.size()>0)
                    eventTypeList.clear();

                eventTypeList.add(0, "-Event Type-");

                if (response.isSuccessful()){

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){

                            Log.d("eventType_success", message1+" "+response.code());

                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i=0; i<jsonArray.length();i++){

                                JSONObject jsonData = jsonArray.getJSONObject(i);

                                String event_name = jsonData.getString("event_name");

                                eventTypeList.add(event_name);
                            }

                            progressDialog.dismiss();
                            setSpinnerForEventType();

                        }else {
                            progressDialog.dismiss();
                            setSpinnerForEventType();
                            Log.d("eventType_else", message1+" "+response.code());
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    try {
                        progressDialog.dismiss();
                        setSpinnerForEventType();
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("eventType_fail", message);

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

    private void setSpinnerForEventType(){

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), eventTypeList);
        spn_eventType.setAdapter(customSpinnerAdapter);

        spn_eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    eventType = "";

                }else {
                    eventType = spn_eventType.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAllClasses() {

        progressDialog.show();

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray.put(division);
            object.put("divisions",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Class_input_1", object.toString()+" hjhjh");

        if (classList.size()>0){
            classList.clear();
        }

        classList.add(0,"-Select Class-");

        mApiService.getClassSections("05861673-1073-4029-b04d-6db1b1372a1d",object).enqueue(new Callback<Object>() {
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

    private void setSpinnerForClass() {

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), classList);
        spn_class.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_className = spn_class.getSelectedItem().toString();
                getAllSection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAllSection() {

        progressDialog.show();

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray.put(division);
            object.put("divisions", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Class_input_2", object.toString());

        if (sectionList.size()>0){
            sectionList.clear();
        }

        sectionList.add(0,"-Section-");

        mApiService.getClassSections("05861673-1073-4029-b04d-6db1b1372a1d",object).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){
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

                                    if (class_name.equals(str_className)){

                                        JSONArray jsonArray1 = classData.getJSONArray("sections");
                                        Log.d("classData_array", jsonArray1.toString());

                                        for (int i = 0; i<jsonArray1.length();i++){

                                            sectionList.add(jsonArray1.getString(i));
                                            Log.d("classData_array "+i, jsonArray1.getString(i));
                                        }
                                    }
                                }
                            }
                            progressDialog.dismiss();
                            setSpinnerForSection();
                        }

                        progressDialog.dismiss();
                        setSpinnerForSection();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        setSpinnerForSection();

                    }
                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("section_api_fail", message);
                        progressDialog.dismiss();
                        setSpinnerForSection();

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

    private void setSpinnerForSection() {

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), sectionList);
        spn_section.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_setction  = spn_section.getSelectedItem().toString();

                getAllSubjectApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAllSubjectApi(){

        if (subjectList.size()>0){
            subjectList.clear();
        }

        subjectList.add(0,"-Subject-");

        progressDialog.show();
        mApiService.getSubjects("05861673-1073-4029-b04d-6db1b1372a1d", division,str_className).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("Subject_suc", response.code()+" "+message1);

                            JSONObject jsonData = object.getJSONObject("Section");

                            JSONObject sectionName = jsonData.getJSONObject(str_setction);

                            Iterator<String> key = sectionName.keys();

                            while (key.hasNext()){

                                String subjects = key.next();

                                subjectList.add(subjects);

                            }


                            setSpinnerForSubject();
                            progressDialog.dismiss();


                        }else {
                            progressDialog.dismiss();
                            setSpinnerForSubject();
                            Log.d("Subject_else", response.code()+" "+message1);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        setSpinnerForSubject();

                    }

                }else {
                    try {
                        progressDialog.dismiss();
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Subject_error", message);
                        setSpinnerForSubject();

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

    private void setSpinnerForSubject() {

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), subjectList);
        spn_subject.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    str_subject = "";
                }
                else {
                    str_subject = spn_subject.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void createExamsAPI(){

        String message  = et_message.getText().toString().trim();

        if (examType.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Exam Type is required", Toast.LENGTH_SHORT).show();

        }else if (str_className.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Class is required", Toast.LENGTH_SHORT).show();

        }else if (str_setction.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Section is required", Toast.LENGTH_SHORT).show();

        }else if (str_subject.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Section is required", Toast.LENGTH_SHORT).show();

        }else if (message.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Message is required", Toast.LENGTH_SHORT).show();

        }else if (subjectDateList.size()==0){
            Toast.makeText(getActivity(), "Create Exam Schedule", Toast.LENGTH_SHORT).show();

        }else {

            progressDialog.show();
            Log.d("Message_add_exam_input", fromDate+"   "+toDate);

            for (int i = 0; i < subjectDateList.size(); i++) {

                mApiServicePatashala.createExams("05861673-1073-4029-b04d-6db1b1372a1d", "1", fromDate, toDate,
                        scheduleType, division, str_className, str_setction, examType, subjectDateList.get(i).getDate(),
                        message, subjectDateList.get(i).getSubject(),
                        subjectDateList.get(i).getTime(), "e20dd4be-a3c3-4014-b6ce-7e58ed609399", "2020-03-12", "2021-03-01").enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                        if (response.isSuccessful()){

                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("Message_add_exam", response.code() + " " + response.message() + " "+object);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {


                        }

                        progressDialog.dismiss();
                        Log.d("Message_add_exam", response.code() + " " + response.message() + " " + response.raw());

                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                    }
                });
            }

            progressDialog.dismiss();
            getActivity().onBackPressed();
        }
    }

    private void createEventAPI(){

        String message = et_message.getText().toString().trim();

        if (spn_eventType.getSelectedItemPosition()==0){
            Toast.makeText(getActivity(), "Event Type is required", Toast.LENGTH_SHORT).show();

        }else if (message.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Message Type is required", Toast.LENGTH_SHORT).show();

        }else {

            progressDialog.show();

            JSONObject classObject = new JSONObject();

            try {
                for (int i=0; i<classSectionList.size();i++){

                    JSONArray jsonArray = new JSONArray();

                    for (int j=0; j<classSectionList.get(i).getSectionList().size();j++){
                        jsonArray.put(classSectionList.get(i).getSectionList().get(j));
                    }

                    classObject.put(classSectionList.get(i).getClassName(), jsonArray);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            String eventType = spn_eventType.getSelectedItem().toString().trim();

            mApiService.createEvents("05861673-1073-4029-b04d-6db1b1372a1d", "1", fromDate, toDate, scheduleType, division,
                    classObject, eventType, scheduleTitle, message, "e20dd4be-a3c3-4014-b6ce-7e58ed609399",
                    "2020-03-12", "2021-03-01").enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()){
                        Log.d("CreateEvent_success",response.code()+" "+response.raw()+" "+response.body());
                        Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        getActivity().onBackPressed();

                    }else {

                        try {
                            progressDialog.dismiss();
                            assert response.errorBody()!=null;
                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("createEvent_error", message+" "+response.code());
                            setSpinnerForSubject();
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

    private void createHolidayAPI(){

        String message = et_message.getText().toString().trim();

        if (spn_holidayType.getSelectedItemPosition()==0){
            Toast.makeText(getActivity(), "Event Type is required", Toast.LENGTH_SHORT).show();

        }else if (message.equalsIgnoreCase("")){
            Toast.makeText(getActivity(), "Message Type is required", Toast.LENGTH_SHORT).show();

        }else {

            progressDialog.show();

            JSONObject classObject = new JSONObject();

            try {
                for (int i=0; i<classSectionList.size();i++){

                    JSONArray jsonArray = new JSONArray();

                    for (int j=0; j<classSectionList.get(i).getSectionList().size();j++){
                        jsonArray.put(classSectionList.get(i).getSectionList().get(j));
                    }

                    classObject.put(classSectionList.get(i).getClassName(), jsonArray);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            String holidayType = spn_holidayType.getSelectedItem().toString().trim();

            mApiService.createHolidays("05861673-1073-4029-b04d-6db1b1372a1d", "1", fromDate, toDate, scheduleType,
                    division, classObject, holidayType, scheduleTitle, message, "e20dd4be-a3c3-4014-b6ce-7e58ed609399",
                    "2020-03-12", "2021-03-01").enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()){
                        Log.d("Holiday_success",response.code()+" "+response.raw()+" "+response.body());
                        Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        getActivity().onBackPressed();

                    }else {

                        try {
                            progressDialog.dismiss();
                            assert response.errorBody()!=null;
                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("Holiday_error", message+" "+response.code());
                            setSpinnerForSubject();
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
}
