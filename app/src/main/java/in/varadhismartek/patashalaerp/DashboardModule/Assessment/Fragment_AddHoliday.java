package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddWing.AddWingsModel;
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

import static android.widget.Toast.LENGTH_SHORT;

public class Fragment_AddHoliday extends Fragment implements View.OnClickListener{

    private TextView tvTitle;
    private EditText edAddExam;
    private ImageView ivAddExam, ivSendExam, ivBack;
    private RecyclerView rcvAddWings;
    APIService apiService,mApiServicePatashala;
    private ArrayList<AddWingsModel> addExamArrayList;
    private ArrayList<String> examList = new ArrayList<>();
    AssessmentAdapter assessmentAdapter;
    int spanCount = 2;
    private Button btnSubmit;
    String editvalue;
    ArrayList<String> academicList,listDivision,listClass,listSection,listSectionNew ,listSectionNew1;
    Spinner sp_academic,sp_sessionNo,spn_division,spn_class,spn_section;
    ArrayList<ClassSectionModel> modelArrayList;
    String strDiv,strClass,strSection,strAcdaemicYear;
    CustomSpinnerAdapter customSpinnerAdapter;
    private DateConvertor convertor;
    Calendar calendar;
    String fromYear, toYear, strfromDate, str_toDate;
    Date date1, date2;
    String serialNo = "";
    String fromDate = "";
    String toDate = "";
    String strAcademicFDate,strAcademicTDate;
    private ArrayList<String> serialList;
    int year, month, date;
    RelativeLayout re_fromdate, re_todate;
    ImageView iv_attachmentImage,iv_attach;
    TextView tv_from_date, tv_to_date, tv_event_type,tv_Name, tv_Type, tv_title;
    File userProfile;
    EditText tv_event_title,tv_event_name1,ed_message;
    RecyclerView recycler_view;
    Spinner tv_event_name;
    ArrayList<String> holidayTypeList= new ArrayList<>();
    String strHolidayName;
    public Fragment_AddHoliday() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        initListener();
        getDivisionApi();

        getAcademicYearAPI();
        getHoliday();
        getHolidayTypeBarrier();
        return view;

    }


    private void initViews(View view) {
        apiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();

        tvTitle = view.findViewById(R.id.tvTitle);
        tv_event_type = view.findViewById(R.id.tv_event_type);
        recycler_view = view.findViewById(R.id.recycler_view);
        tv_event_title = view.findViewById(R.id.tv_event_title);
        tv_event_name = view.findViewById(R.id.tv_event_name);
        ed_message = view.findViewById(R.id.ed_message);

        iv_attachmentImage = view.findViewById(R.id.iv_attachmentImage);
        iv_attach = view.findViewById(R.id.iv_attach);
        sp_academic = view.findViewById(R.id.sp_academic);
        sp_sessionNo = view.findViewById(R.id.sp_sessionNo);
        spn_division = view.findViewById(R.id.spn_division);
        spn_class = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);

        edAddExam = view.findViewById(R.id.etAddWings);
        ivBack = view.findViewById(R.id.iv_backBtn);
        btnSubmit = view.findViewById(R.id.btn_ok);

        ivAddExam = view.findViewById(R.id.ivAddWings);
        ivSendExam = view.findViewById(R.id.iv_sendAddWings);
        rcvAddWings = view.findViewById(R.id.rcvAddWings);
        re_fromdate = view.findViewById(R.id.re_fromdate);
        re_todate = view.findViewById(R.id.re_todate);
        tv_from_date = view.findViewById(R.id.tv_from_date);
        tv_to_date = view.findViewById(R.id.tv_to_date);
        convertor = new DateConvertor();

        tvTitle.setText("Add Holiday");
        edAddExam.setHint("Add Holiday");


        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        rcvAddWings.setLayoutManager(linearLayoutManager);
        rcvAddWings.setHasFixedSize(true);
        addExamArrayList = new ArrayList<>();

        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        listSection = new ArrayList<>();
        listSectionNew = new ArrayList<>();
        listSectionNew1 = new ArrayList<>();
        academicList = new ArrayList<>();
        serialList = new ArrayList<>();
        modelArrayList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

    }
    private void initListener() {
        iv_attachmentImage.setOnClickListener(this);
        ivAddExam.setOnClickListener(this);
        ivSendExam.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        re_fromdate.setOnClickListener(this);
        re_todate.setOnClickListener(this);
    }


    private void getHolidayTypeBarrier() {
        holidayTypeList.clear();
        holidayTypeList.add("Holiday Type");
        mApiServicePatashala.getHolidayBarrier(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("ADMIN_API_1",""+response.raw()+response.body());
                if (response.isSuccessful()){
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        JSONArray jsonArray = object.getJSONArray("data");

                        Log.d("ADMIN_API_2", jsonArray.toString());

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Log.d("ADMIN_API_3", jsonObject.toString());

                            boolean strStatus = jsonObject.getBoolean("status");
                            String holiday_name = jsonObject.getString("holiday_name");

                            Log.d("ADMIN_API_4", holiday_name);
                            holidayTypeList.add(holiday_name);

                        }

                    }catch (Exception e){
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
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(),holidayTypeList);
        tv_event_name.setAdapter(customSpinnerAdapter);
        tv_event_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position ==0){
                    strHolidayName="Holiday Type";
                }else {
                     strHolidayName = tv_event_name.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
                            spn_division.setAdapter(customSpinnerAdapter);

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

        spn_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strDiv = parent.getSelectedItem().toString();

                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                if (strDiv.equalsIgnoreCase("Division")) {
                    spn_class.setSelection(0);
                    spn_section.setSelection(0);

                } else {
                    spn_class.setSelection(0);
                    spn_section.setSelection(0);
                    try {
                        array.put(strDiv);
                        jsonObject.put("divisions", array);
                        Constant.DIVISION_NAME = strDiv;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
                                modelArrayList.clear();
                                customSpinnerAdapter.notifyDataSetChanged();
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


        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spn_class.setAdapter(customSpinnerAdapter);


        spn_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                strClass = spn_class.getSelectedItem().toString();

                listSectionNew.clear();
                listSectionNew1.clear();
                listSectionNew.add("Section");

                if (strClass.equalsIgnoreCase("Class")) {
                    Log.i("ClassName***", "" + strClass);
                    spn_section.setSelection(0);
                } else {
                    Log.i("ClassName***1", "" + strClass + "***" + modelArrayList);


                    spn_section.setSelection(0);
                    boolean b = true;

                    for (int j = 0; j < modelArrayList.size(); j++) {
                        if (modelArrayList.get(j).getClassName().contains(strClass)) {

                            for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {

                                listSectionNew.add(modelArrayList.get(j).getListSection().get(k));
                                listSectionNew1.add(modelArrayList.get(j).getListSection().get(k));

                            }
                        }

                    }
                    Gson gson = new Gson();
                    System.out.println("DATA***"+strClass+"**"+listSectionNew+"**"+listSectionNew1);

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
                //getSubject(strDiv, str_class);
                if (strSection.equalsIgnoreCase("Section")) {
                } else {

                }
                // getSubject(strDiv, str_class);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

                               // String from = convertor.getDateByTZ(start_date);
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

                    Log.d("asflkaf", "kgdkdsgkgd" + fromDate + toDate);
                    getSessionAPI(fromDate, toDate);


                    SimpleDateFormat inputFormate = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat outputFormate = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date inputFromDate = inputFormate.parse(fromDate);
                        Date inputToDate = inputFormate.parse(toDate);

                        strAcademicFDate = outputFormate.format(inputFromDate);
                        strAcademicTDate = outputFormate.format(inputToDate);

                        Log.i("strAcademicFDate", "" + strAcademicFDate);
                        Log.i("strAcademicTDate", "" + strAcademicTDate);

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


            case R.id.re_todate:
                toDateDialog();
                break;

            case R.id.ivAddWings:

                String value = "";
                editvalue = edAddExam.getText().toString();
                if (editvalue.equals("")) {
                    Toast.makeText(getActivity(), "Please enter the Value", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }else {
                    if (addExamArrayList.size()>0){
                        for (int i = 0; i < addExamArrayList.size(); i++) {
                            if (addExamArrayList.get(i).getWingsName().toLowerCase().equals(editvalue.toLowerCase())) {
                                value = addExamArrayList.get(i).getWingsName();
                            }
                        }

                        if (value.toLowerCase().equals(editvalue.toLowerCase())) {
                            Toast.makeText(getActivity(), "Already Exist", LENGTH_SHORT).show();
                        } else {
                            assessmentAdapter.newValues(editvalue);
                            examList.add(editvalue);
                            System.out.println("addExamArrayList**11  "+examList);

                        }

                        edAddExam.setText("");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    else{

                        assessmentAdapter = new AssessmentAdapter(addExamArrayList,
                                getActivity(), ivSendExam, Constant.RV_EXAMS_ROW);
                        rcvAddWings.setAdapter(assessmentAdapter);
                        assessmentAdapter.newValues(editvalue);
                        edAddExam.setText("");
                        examList.add(editvalue);


                    }
                }

                break;
            case R.id.btn_ok:
                System.out.println("addExamArrayList**"+addExamArrayList.size()+"**"+editvalue+"**"+examList);

                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                for (int i = 0; i < listSectionNew1.size(); i++) {
                    array.put(listSectionNew1.get(i));

                }

                try {
                    jsonObject.put(strClass, array);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("jsonClass***"+jsonObject);

                System.out.println("objExam***"+array);
                System.out.println("objExam***"+jsonObject);





//classes:{"11_standard":["a","b"],"12_standard":["a","b","c"]}




            /*    JSONObject jsonClass = new JSONObject();
                try {
                    jsonClass.put(strClass,listSectionNew1);
                }catch (JSONException je){

                }*/

                createHolidayAPI(jsonObject);

                break;


            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
        }
    }

    private void createHolidayAPI(JSONObject jsonClass) {


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        File f = new File(String.valueOf(userProfile));
         builder.addFormDataPart("file" , userProfile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), f));

/* @Part("school_id") RequestBody school_id,
          @Part("session_serial_no") RequestBody session_serial_no,
          @Part("schedule_type") RequestBody schedule_type,
          @Part("holiday_type") RequestBody holiday_type,
          @Part("holiday_name") RequestBody holiday_name,
          @Part("division") RequestBody division,
          @Part("classes") RequestBody classes,
          @Part("from_date") RequestBody from_date,
          @Part("to_date") RequestBody to_date,
          @Part("added_employeeid") RequestBody added_employeeid,
          @Part("academic_start_date") RequestBody academic_start_date,
          @Part("academic_end_date") RequestBody academic_end_date,
          @Part("message") RequestBody message,
          @Part MultipartBody.Part image*/


        builder.addFormDataPart("school_id", Constant.SCHOOL_ID);
        builder.addFormDataPart("session_serial_no", serialNo.trim());
        builder.addFormDataPart("schedule_type", tv_event_type.getText().toString().trim());
        builder.addFormDataPart("holiday_type", tv_event_title.getText().toString().trim());
        builder.addFormDataPart("holiday_name", strHolidayName.trim());
        builder.addFormDataPart("division", strDiv.trim());
        builder.addFormDataPart("classes", jsonClass.toString());
        builder.addFormDataPart("from_date", strfromDate.trim());
        builder.addFormDataPart("to_date", str_toDate.trim());
        builder.addFormDataPart("added_employeeid", Constant.EMPLOYEE_BY_ID);
        builder.addFormDataPart("academic_start_date", strAcademicFDate.trim());
        builder.addFormDataPart("academic_end_date", strAcademicTDate.trim());
        builder.addFormDataPart("message", ed_message.getText().toString().trim());
        builder.addFormDataPart("image", f.toString());

        Log.i("Data****",""+ serialNo.trim());
        Log.i("Data****",""+ tv_event_type.getText().toString().trim());
        Log.i("Data****",""+ tv_event_title.getText().toString().trim());
        Log.i("Data****",""+ strHolidayName.trim());
        Log.i("Data****",""+ ed_message.getText().toString().trim());
        Log.i("Data****",""+ strDiv.trim());
        Log.i("Data****",""+ strfromDate.trim());
        Log.i("Data****",""+ str_toDate.trim());
        Log.i("Data****",""+ strAcademicFDate.trim());
        Log.i("Data****",""+ strAcademicTDate.trim());
        Log.i("Data****",""+ jsonClass.toString());

        MultipartBody requestBody = builder.build();
        apiService.createHoliday(requestBody).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.i("Data****",""+ response.code()+response.body());
                Log.d("responseAddIamge", "onResponse: " + response.raw());

                try {
                    assert response.errorBody()!=null;
                    JSONObject object = new JSONObject(response.errorBody().string());
                    String message = object.getString("message");
                    Log.d("ADMIN_API", message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


       /* if (userProfile != null) {

            RequestBody res_schoolId = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
            RequestBody res_employeeId = RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID);
            RequestBody res_fromDate = RequestBody.create(MediaType.parse("text/plain"), strAcademicFDate);
            RequestBody res_toDate = RequestBody.create(MediaType.parse("text/plain"), strAcademicTDate);

            RequestBody res_res_serialNo = RequestBody.create(MediaType.parse("text/plain"), serialNo.trim());
            RequestBody res_strDiv = RequestBody.create(MediaType.parse("text/plain"), strDiv.trim());
            RequestBody res_class = RequestBody.create(MediaType.parse("text/plain"), jsonClass.toString());

            RequestBody res_strfromDate = RequestBody.create(MediaType.parse("text/plain"), strfromDate.trim());
            RequestBody res_str_toDate = RequestBody.create(MediaType.parse("text/plain"), str_toDate.trim());

            RequestBody res_strScheduleType = RequestBody.create(MediaType.parse("text/plain"), tv_event_type.getText().toString().trim());
            RequestBody res_holidayType = RequestBody.create(MediaType.parse("text/plain"), tv_event_title.getText().toString().trim());
            RequestBody res_holidayName = RequestBody.create(MediaType.parse("text/plain"), tv_event_name.getText().toString().trim());
            RequestBody res_ed_message = RequestBody.create(MediaType.parse("text/plain"), ed_message.getText().toString().trim());

            final RequestBody mFileProfile = RequestBody.create(MediaType.parse("image"), userProfile);
            final MultipartBody.Part fileToUploadProfilepicture = MultipartBody.Part.createFormData("image", userProfile.getName(), mFileProfile);

            Log.i("jsonClass.toString()**", "" + jsonClass.toString());
            Log.d("Multipart", fileToUploadProfilepicture.toString() + "  " + userProfile.getName() + " " + mFileProfile);


            *//* @Part("school_id") RequestBody school_id,
          @Part("session_serial_no") RequestBody session_serial_no,
          @Part("schedule_type") RequestBody schedule_type,
          @Part("holiday_type") RequestBody holiday_type,
          @Part("holiday_name") RequestBody holiday_name,
          @Part("division") RequestBody division,
          @Part("classes") RequestBody classes,
          @Part("from_date") RequestBody from_date,
          @Part("to_date") RequestBody to_date,
          @Part("added_employeeid") RequestBody added_employeeid,
          @Part("academic_start_date") RequestBody academic_start_date,
          @Part("academic_end_date") RequestBody academic_end_date,
          @Part("message") RequestBody message,
          @Part MultipartBody.Part image*//*

           *//* apiService.createHoliday(res_schoolId,res_res_serialNo,res_strScheduleType,
                    res_holidayType,res_holidayName,res_strDiv,res_class,res_strfromDate,res_str_toDate,
                    res_employeeId,res_fromDate,res_toDate,res_ed_message,fileToUploadProfilepicture)
                    .enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Log.i("Holiday response**",""+response.code()+response.body());
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });

        }else {
//            Log.d("Multipart12",""+ userProfile.getName());

        }*//*
        }*/

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

    private void getHoliday() {

    }
    private void addHolidayAPIMethod(JSONArray array) {

        apiService.addExamType(Constant.SCHOOL_ID,array,Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("Exam_getlist *", "" + response.raw().request().url());
                        Log.i("Exam_getlist_R *", "" + response.body());
                        Log.i("Exam_getlist *", "" + response.code());
                        Log.i("Exam_getlist *", "" + Constant.SCHOOL_ID);
                        Log.i("Exam_getlist *", "" + Constant.EMPLOYEE_BY_ID);
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity(),"Data have save successfully", LENGTH_SHORT).show();
                           // getExamType();
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }
}
