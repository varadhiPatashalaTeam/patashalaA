package in.varadhismartek.patashalaerp.DashboardModule.House;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.Adapters.RecyclerAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.HouseModule;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.StudentModule.StudentModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_HouseBarrier extends Fragment implements View.OnClickListener {
    APIService mApiService;
    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<String> captionNameList = new ArrayList<>();
    ArrayList<String> viceCaptionNameList = new ArrayList<>();

    ArrayList<EmpLeaveModel> employeeList, captionList ;

    String empFname = "", empLname = "", empUUId, empPhoneNo, empAdhaarNo, empDept, colorName;
    Spinner spTeacher, sp_Caption, sp_ViceCaption;
    CustomSpinnerAdapter customSpinnerAdapter;
    String empName = "";
    String strTeacherName, strTeacherUUID, strCaptionName, strCaprionId, strViceCaptionName, strViceCaprionId;
    LinearLayout linearLayoutAdd;
    EditText edStudentNo, edHouseName;
    Spinner tvColorCode;
    ArrayList<String> Colorcategories = new ArrayList<>();
    ArrayList<StudentModel> studentModels = new ArrayList<>();
    ArrayList<StudentModel> viceCaptionList = new ArrayList<>();

    String strDivision, strClass, strSection, strFirstName, strLastName, strDOB, strStudentID, strCertificateNo,
            strStatus, strdeleted, strPhoto;
    private ArrayList<HouseModule> houseModules = new ArrayList<>();

    String number_of_students = "", house_name = "", house_color = "", house_uuid = "",
            mentor_fname, mentor_lname = "", mentor_id = "", mentor_no = "", mentor_adharno = "", strColorName="",
            viceC_fname = "", viceC_lname = "", viceC_id = "", viceCDOB = "", viceCbirthID = "",
            C_fname = "", C_lname, C_Id = "", C_DOB = "", C_BID = "";
    private RecyclerView rvAddHouses;
    TextView tvAdd;
    private ImageView ivBack;
    String mName = "", CName = "",vCName="";
    public Fragment_HouseBarrier() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_houses_dialog, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        intiView(view);

        getHouseApi();

        return view;

    }

    private void getHouseApi() {
        mApiService.getHouseList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {

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


                                number_of_students = objectData.getLong("number_of_students")+"";
                                house_name = objectData.getString("house_name");
                                house_color = objectData.getString("house_color");
                                house_uuid = objectData.getString("house_uuid");

                                if (objectData.getJSONObject("house_mentor").toString().equals("{}")){
                                   // mentor_fname = "";
                                }else {
                                    JSONObject mintoreJson = objectData.getJSONObject("house_mentor");

                                    mentor_fname = mintoreJson.getString("mentor_first_name");
                                    mentor_lname = mintoreJson.getString("mentor_last_name");
                                    mentor_id = mintoreJson.getString("mentor_id");
                                    mentor_no = mintoreJson.getString("mentor_contact_no");
                                    mentor_adharno = mintoreJson.getString("mentor_adhar_card_no");
                                    mName = mentor_fname + " " + mentor_lname;

                                    System.out.println("mentor_fname***"+mName);

                                }


                                if (objectData.getJSONObject("house_captain").toString().equals("{}")){


                                }else {
                                    JSONObject captionJSON = objectData.getJSONObject("house_captain");

                                    C_fname = captionJSON.getString("captain_first_name");
                                    C_lname = captionJSON.getString("captain_last_name");
                                    C_Id = captionJSON.getString("captain_id");
                                    C_DOB = captionJSON.getString("captain_dob");
                                    C_BID = captionJSON.getString("captain_birth_certificate_no");

                                    CName = C_fname + " " + C_lname;

                                    System.out.println("CName***"+CName);

                                }




                                if (objectData.getJSONObject("house_vice_captain").toString().equals("{}")){

                                }else {
                                    JSONObject vCaptionJSON = objectData.getJSONObject("house_vice_captain");

                                    viceC_fname= vCaptionJSON.getString("vice_captain_first_name");
                                    viceC_lname= vCaptionJSON.getString("vice_captain_last_name");
                                    viceC_id= vCaptionJSON.getString("vice_captain_id");
                                    viceCDOB= vCaptionJSON.getString("vice_captain_dob");
                                    viceCbirthID= vCaptionJSON.getString("vice_captain_birth_certificate_number");
                                    vCName =viceC_fname+" "+viceC_lname;
                                    System.out.println("vCName***"+vCName);
                                }


                                houseModules.add(new HouseModule(number_of_students,house_name,house_color,house_uuid,
                                        mName,CName,vCName));

                                /*houseModules.add(new HouseModule(
                                        number_of_students,house_name,house_color,house_uuid,
                                        mentor_fname,mentor_lname,mentor_id,mentor_no,mentor_adharno));
*/

                            }
                        }
                        System.out.println("houseModules*****" + houseModules.size() + "****" + houseModules.get(0).getCName());
                        setRecyclerView();
                    } catch (JSONException je) {

                    }


                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setRecyclerView() {

        RecyclerAdapter adapter = new RecyclerAdapter(houseModules, getActivity(), Constant.RV_ADD_HOUSES);
        rvAddHouses.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvAddHouses.setAdapter(adapter);


    }

    private void intiView(View view) {

        mApiService = ApiUtils.getAPIService();
        ivBack = view.findViewById(R.id.ivBack);
        rvAddHouses = view.findViewById(R.id.rvAddHouses);
        spTeacher = view.findViewById(R.id.sp_teacher1);
        sp_Caption = view.findViewById(R.id.sp_Caption);
        sp_ViceCaption = view.findViewById(R.id.sp_ViceCaption);
       // linearLayoutAdd = view.findViewById(R.id.llAdd);
        edHouseName = view.findViewById(R.id.etHouseName);
        edStudentNo = view.findViewById(R.id.ed_studentNo);
        tvColorCode = view.findViewById(R.id.tvcolorcode);
        tvAdd = view.findViewById(R.id.tvAdd);
//        linearLayoutAdd.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        employeeList = new ArrayList<>();
        captionList = new ArrayList<>();
        viceCaptionList = new ArrayList<>();

        Colorcategories.clear();

        Colorcategories.add("-Color-");
        Colorcategories.add("Green");
        Colorcategories.add("Blue");
        Colorcategories.add("Red");
        Colorcategories.add("Yellow");
        Colorcategories.add("Purple");


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), Colorcategories);

        tvColorCode.setAdapter(customSpinnerAdapter);

        tvColorCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 strColorName = tvColorCode.getItemAtPosition(position).toString();
                if (strColorName.equals("-Color-")){

                }else {
                    switch (position) {

                        case 0:
                            colorName = "#000000";
                            break;
                        case 1:
                            colorName = "#7CB342";
                            break;

                        case 2:
                            colorName = "#1E88E5";
                            break;

                        case 3:
                            colorName = "#E53935";
                            break;

                        case 4:
                            colorName = "#FDD835";
                            break;

                        case 5:
                            colorName = "#8E24AA";
                            break;


                    }
                    System.out.println("colorName***ADCOLOR** "+strColorName+"***"+colorName);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                getActivity().onBackPressed();
                break;

            case R.id.tvAdd:

                if (edHouseName.getText().toString().isEmpty() ) {
                    Toast.makeText(getActivity(), "Enter house name", Toast.LENGTH_SHORT).show();
                } else if (strColorName.equals("-Color-")){
                    Toast.makeText(getActivity(), "Select Color for house", Toast.LENGTH_SHORT).show();

                }
                else {

                    mApiService.AddHouse(Constant.SCHOOL_ID, edHouseName.getText().toString(), colorName, Constant.EMPLOYEE_BY_ID)
                            .enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    Log.i("ADD_HOUSE**2", "" + Constant.SCHOOL_ID+
                                            edHouseName.getText().toString()+colorName+
                                            Constant.EMPLOYEE_BY_ID);

                                    Log.i("ADD_HOUSE**2", "" + response.body() + "**" + response.code());
                                    if (response.isSuccessful()) {
                                        try {
                                            Toast.makeText(getActivity(), "House Barriers have save successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            getHouseApi();
                                            edHouseName.setText("");
                                            edStudentNo.setText("");
                                            spTeacher.setSelection(0);
                                            sp_Caption.setSelection(0);
                                            sp_ViceCaption.setSelection(0);
                                        } catch (Exception e) {

                                        }
                                    }else {
                                        Toast.makeText(getActivity(), "Already Added to particular color of house ",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {

                                }
                            });

                }

                break;
        }

    }
}

