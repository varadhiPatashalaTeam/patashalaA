package in.varadhismartek.patashalaerp.DashboardModule.House;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.HouseModule;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.MyModel;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.StudentModule.StudentModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Houses extends Fragment implements View.OnClickListener {
    private RecyclerView rvAddHouses;
    private ImageView ivBack;
    private FloatingActionButton fab;
    private ArrayList<MyModel> arrayList = new ArrayList<>();
    private ArrayList<HouseModule> houseModules = new ArrayList<>();
    private String colorName;
    APIService apiService;
    Spinner sp_teacher1, sp_Caption, sp_ViceCaption;
    String HouseName = "", HouseID = "", HouseColor = "";
    EditText housename;

    // TextView tvAdd;
    String strColor;// edhouseName;

    String number_of_students = "", house_name = "", house_color = "", house_uuid = "",
            mentor_fname, mentor_lname = "", mentor_id = "", mentor_no = "", mentor_adharno = "",
            viceC_fname = "", viceC_lname = "", viceC_id = "", viceCDOB = "", viceCbirthID = "",
            C_fname = "", C_lname, C_Id = "", C_DOB = "", C_BID = "";

    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<String> captionNameList = new ArrayList<>();
    ArrayList<String> viceCaptionNameList = new ArrayList<>();

    ArrayList<EmpLeaveModel> employeeList, captionList;
    ArrayList<String> Colorcategories = new ArrayList<>();
    ArrayList<StudentModel> studentModels = new ArrayList<>();
    ArrayList<StudentModel> viceCaptionList = new ArrayList<>();
    String empFname = "", empLname = "", empUUId, empPhoneNo, empAdhaarNo, empDept, strTeacherName, strTeacherUUID;
    String strCaptionName = "", strCaprionId = "", strViceCaptionName = "", strViceCaprionId = "";
    String strDivision, strClass, strSection, strFirstName, strLastName, strDOB, strStudentID, strCertificateNo,
            strStatus, strdeleted, strPhoto;
    TextView tvAdd;
    Spinner tvcolorcode;

    public Fragment_Houses() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_houses, container, false);
        getBundle();
        initViews(view);
        initListeners();
        getEmployeeList();

        studentAll();
        getHouseApi();
        //  setRecyclerView();




        return view;
    }

    private void getBundle() {
        Bundle b = getArguments();
        if (b != null) {
            HouseName = b.getString("HouseName");
            HouseID = b.getString("HouseID");
            HouseColor = b.getString("HouseColor");

            System.out.println("HouseName**" + HouseName);


        }

        switch (HouseColor) {

            case "#7CB342":
                colorName = "Green";
                break;

            case "#1E88E5":
                colorName = "Blue";
                break;


            case "E53935":
                colorName = "Red";
                break;

            case "#FF9800":
                colorName = "Yellow";
                break;

            case "#8E24AA":
                colorName = "Orange";
                break;
            default:
                colorName = "White";

        }
    }


    private void getEmployeeList() {
        apiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        employeeNameList.clear();
                        employeeList.clear();
                        //   employeeNameList.add("Select Teacher");
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
                                empPhoneNo = keyData.getString("phone_number");
                                empAdhaarNo = keyData.getString("adhaar_card_no");
                                empDept = keyData.getString("department_name");

                                String empName = empFname + " " + empLname;
                                employeeNameList.add(empName);

                                employeeList.add(new EmpLeaveModel(empFname, empLname, empUUId, empPhoneNo, empAdhaarNo, empDept));
                            }

                            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), employeeNameList);
                            sp_teacher1.setAdapter(customSpinnerAdapter);


                        }

                    } catch (JSONException je) {

                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        sp_teacher1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                strTeacherName = sp_teacher1.getItemAtPosition(position).toString();
                int empOreder = sp_teacher1.getSelectedItemPosition();

                captionList.clear();
                captionNameList.clear();

                for (int i = 0; i < employeeNameList.size(); i++) {
                    if (empOreder == i) {
                        String strEmpId = employeeList.get(i).getEmpFname();
                        strTeacherUUID = employeeList.get(i).getEmpUUId();


                    } else {

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void studentAll() {
        apiService.getStudentAll(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("studentList_all", "" + response.raw().request().url());
                    Log.i("studentList_all", "" + response.body() + "***" + response.code());

                    try {
                        studentModels.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        System.out.println("MEssage**C*" + object.getString("message"));
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

                                captionNameList.add(strFirstName + " " + strLastName);
                                viceCaptionNameList.add(strFirstName + " " + strLastName);

                            }
                            System.out.println("captionNameList***" + captionNameList.size());

                            setSpinnerCaption();
                            setSpinnerViceCaption();
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

    private void setSpinnerViceCaption() {
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), viceCaptionNameList);
        sp_ViceCaption.setAdapter(customSpinnerAdapter);
        sp_ViceCaption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strViceCaptionName = sp_ViceCaption.getItemAtPosition(position).toString();
                int captionOreder = sp_ViceCaption.getSelectedItemPosition();


                for (int i = 0; i < viceCaptionNameList.size(); i++) {
                    if (captionOreder == i) {
                        strViceCaprionId = studentModels.get(i).getStrStudentID();

                        System.out.println("captionNameList**strCaprionId*" + strCaprionId);
                    } else {


                       /* strDivision = studentModels.get(i).getStrDivision();
                        strClass =  studentModels.get(i).getStrClass();
                        strSection = studentModels.get(i).getStrSection();
                        strFirstName =  studentModels.get(i).getStrFirstName();
                        strLastName =  studentModels.get(i).getStrLastName();
                        strDOB =  studentModels.get(i).getStrDOB();
                        strStudentID =  studentModels.get(i).getStrStudentID();;
                        strCertificateNo =  studentModels.get(i).getStrCertificateNo();
                        strStatus =  studentModels.get(i).getStrStatus();
                        strdeleted =  studentModels.get(i).getStrdeleted();
                        strPhoto =  studentModels.get(i).getStrPhoto();

                        viceCaptionList.add(new StudentModel(strDivision, strClass, strSection, strFirstName, strLastName,
                                strDOB, strStudentID, strCertificateNo, strStatus, strdeleted, strPhoto));

                        //viceCaptionNameList.add(strFirstName+" "+strLastName);*/
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSpinnerCaption() {

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), captionNameList);
        sp_Caption.setAdapter(customSpinnerAdapter);

        sp_Caption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strCaptionName = sp_Caption.getItemAtPosition(position).toString();
                int captionOreder = sp_Caption.getSelectedItemPosition();


                for (int i = 0; i < captionNameList.size(); i++) {
                    if (captionOreder == i) {
                        strCaprionId = studentModels.get(i).getStrStudentID();

                        System.out.println("captionNameList**strCaprionId*" + strCaprionId);
                    } else {


                     /*   strDivision = studentModels.get(i).getStrDivision();
                        strClass =  studentModels.get(i).getStrClass();
                        strSection = studentModels.get(i).getStrSection();
                        strFirstName =  studentModels.get(i).getStrFirstName();
                        strLastName =  studentModels.get(i).getStrLastName();
                        strDOB =  studentModels.get(i).getStrDOB();
                        strStudentID =  studentModels.get(i).getStrStudentID();;
                        strCertificateNo =  studentModels.get(i).getStrCertificateNo();
                        strStatus =  studentModels.get(i).getStrStatus();
                        strdeleted =  studentModels.get(i).getStrdeleted();
                        strPhoto =  studentModels.get(i).getStrPhoto();

                        viceCaptionList.add(new StudentModel(strDivision, strClass, strSection, strFirstName, strLastName,
                                strDOB, strStudentID, strCertificateNo, strStatus, strdeleted, strPhoto));

                        viceCaptionNameList.add(strFirstName+" "+strLastName);*/
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getHouseApi() {
        apiService.getHouseList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {

                    Log.i("House**", "" + response.body());
                    Log.i("House**", "" + response.code());
                    try {
                        houseModules.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        System.out.println("MEssage**C*" + object);
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject = object.getJSONObject("data");
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject objectData = jsonObject.getJSONObject(key);


                                number_of_students = objectData.getString("number_of_students");
                                house_name = objectData.getString("house_name");
                                house_color = objectData.getString("house_color");
                                house_uuid = objectData.getString("house_uuid");


                                String mName = "", CName = "";
                                if (objectData.getJSONObject("house_mentor").toString().equalsIgnoreCase("{}")) {

                                    mentor_fname = "";
                                    mentor_lname = "";
                                    mentor_id = "";
                                    mentor_no = "";
                                    mentor_adharno = "";

                                } else {
                                    JSONObject mentorJSON = objectData.getJSONObject("house_mentor");

                                    mentor_fname = mentorJSON.getString("Mentor first name");
                                    mentor_lname = mentorJSON.getString("Mentor last name");
                                    mentor_id = mentorJSON.getString("Mentor_id");
                                    mentor_no = mentorJSON.getString("Mentor contact no");
                                    mentor_adharno = mentorJSON.getString("Mentor adhar card no");

                                    mName = mentor_fname + " " + mentor_lname;

                                    System.out.println("mName******" + mName);
                                }


                                if (objectData.getJSONObject("house_captain").toString().equalsIgnoreCase("{}")
                                        || objectData.getJSONObject("house_captain").toString().isEmpty()) {
                                    System.out.println("CName1**" + CName);

                                    C_fname = "";
                                    C_lname = "";
                                    C_Id = "";
                                    C_DOB = "";
                                    C_BID = "";
                                    CName = C_fname + " " + C_lname;
                                } else {
                                    JSONObject captionJSON = objectData.getJSONObject("house_captain");

                                    C_fname = captionJSON.getString("Captain first name");
                                    C_lname = captionJSON.getString("Captain last name");
                                    C_Id = captionJSON.getString("Captain id");
                                    C_DOB = captionJSON.getString("Captain dob");
                                    C_BID = captionJSON.getString("Captain birth certificate number");

                                    CName = C_fname + " " + C_lname;

                                    System.out.println("CName**" + CName);
                                }

                      /*         if (objectData.getJSONObject("house_vice_captain").toString().equalsIgnoreCase("{}")){

                                    viceC_fname="";
                                    viceC_lname="";
                                    viceC_id="";
                                    viceCDOB="";
                                    viceCbirthID="";
                                }else {

                                    viceC_fname= objectData.getString("Vice captain first name");
                                    viceC_lname= objectData.getString("Vice captain last name");
                                    viceC_id= objectData.getString("Vice captain id");
                                    viceCDOB= objectData.getString("Vice captain dob");
                                    viceCbirthID= objectData.getString("Vice captain birth certificate number");
                                }

                               */


                                houseModules.add(new HouseModule(
                                        number_of_students, house_name, house_color, house_uuid,
                                        mentor_id, mentor_no, mentor_adharno,
                                        C_Id, C_DOB, C_BID, mName, CName));

                               /* houseModules.add(new House_Activity(
                                        number_of_students,house_name,house_color,house_uuid,
                                        mentor_fname,mentor_lname,mentor_id,mentor_no,mentor_adharno));*/
/*

                                houseModules.add(new House_Activity(
                                        number_of_students,house_name,house_color,house_uuid,

                                        mentor_fname,mentor_lname,mentor_id,mentor_no,mentor_adharno,
                                        viceC_fname,viceC_lname,viceC_id,viceCDOB,viceCbirthID,
                                        C_fname,C_lname,C_Id,C_DOB,C_BID));*/


                            }
                        }
                        System.out.println("houseModules*****" + houseModules.size() + "****" + houseModules.get(0).getCName());
                        //  setRecyclerView();
                    } catch (JSONException je) {

                    }


                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void initListeners() {
        ivBack.setOnClickListener(this);
//        fab.setOnClickListener(this);
    }

    private void initViews(View v) {
        apiService = ApiUtils.getAPIService();
        rvAddHouses = v.findViewById(R.id.rvAddHouses);
        ivBack = v.findViewById(R.id.ivBack);


        employeeList = new ArrayList<>();
        captionList = new ArrayList<>();
        viceCaptionList = new ArrayList<>();

        housename = v.findViewById(R.id.etHouseName);
        tvcolorcode = v.findViewById(R.id.tvcolorcode);
        sp_teacher1 = v.findViewById(R.id.sp_teacher1);
        sp_Caption = v.findViewById(R.id.sp_Caption);
        sp_ViceCaption = v.findViewById(R.id.sp_ViceCaption);

        housename.setText(HouseName);
        // tvcolorcode.setText(colorName);


        tvAdd = v.findViewById(R.id.tvadd);
        tvAdd.setOnClickListener(this);

        arrayList.clear();

        Colorcategories.add("Green");
        Colorcategories.add("Blue");
        Colorcategories.add("Red");
        Colorcategories.add("Yellow");
        Colorcategories.add("Purple");
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), Colorcategories);
        tvcolorcode.setAdapter(customSpinnerAdapter);

        tvcolorcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // strColor = tvcolorcode.getItemAtPosition(position).toString();

                switch (position) {

                    case 0:
                        strColor = "#1CBE22";
                        break;

                    case 1:
                        strColor = "#023EF8";
                        break;

                    case 2:
                        strColor = "#F81200";
                        break;

                    case 3:
                        strColor = "#FF9800";
                        break;

                    case 4:
                        strColor = "#FF5722";
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        assert getActivity() != null;
        switch (v.getId()) {
            case R.id.tvadd:
                addHouse();
                break;


            case R.id.ivBack:
                getActivity().onBackPressed();
                break;

        }
    }

    private void addHouse() {

        Log.i("respupdate***", "" + Constant.SCHOOL_ID + "***" + strTeacherUUID + "***" +
                strCaprionId + "***" + strColor + HouseID);


    }


}






















