package in.varadhismartek.patashalaerp.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.HouseModule;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.MyModel;
import in.varadhismartek.patashalaerp.DashboardModule.House.House_Activity;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.StudentModule.StudentModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by varadhi10 on 8/5/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private ArrayList<Data> dataArrayList;
    private ArrayList<MyModel> houseList;
    private ArrayList<HouseModule> newhouseList;
    ArrayList<Data> checkedList;
    private Context context;
    Data data;
    private String tag;
    private APIService mApiService;

    String colorName;
    private ArrayList<String> employeeNameList = new ArrayList<>();
    private ArrayList<String> captionNameList;
    private ArrayList<String> Colorcategories = new ArrayList<>();
    private ArrayList<StudentModel> studentModels = new ArrayList<>();

    private ArrayList<EmpLeaveModel> employeeList;
    private String empFname = "", empLname = "", empUUId, empPhoneNo, empAdhaarNo, empDept, strTeacherName, strTeacherUUID;
    private Spinner sp_teacher1, sp_Caption, sp_ViceCaption, tvcolorcode;
    private String strDivision, strClass, strSection, strFirstName, strLastName, strDOB, strStudentID, strCertificateNo, strCustomId,
            strStatus, strdeleted, strPhoto;
    private String strCaptionName = "", strCaprionId = "", strViceCaptionName = "", strViceCaprionId = "", strColor = "";

    public RecyclerAdapter() {
    }

    public RecyclerAdapter(Context context, ArrayList<Data> dataArrayList, String tag) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.tag = tag;
        mApiService = ApiUtils.getAPIService();

    }


    // house module
    public RecyclerAdapter(ArrayList<HouseModule> newhouseList, Context context, String tag) {
        this.newhouseList = newhouseList;
        this.context = context;
        this.tag = tag;
        mApiService = ApiUtils.getAPIService();
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (tag) {

            case Constant.BARRIERS_FRAG:
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_card, parent, false));

            case Constant.DEPARTMENT_FRAG:
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_card, parent, false));
            case Constant.RV_ADD_HOUSES:
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.house_row, parent, false));


        }
        return null;
    }


    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {


        switch (tag) {
            case Constant.RV_ADD_HOUSES:

                holder.tvHouseName.setText(newhouseList.get(position).getHouse_name());

                String colorCode = "", strColorName = "";

                colorCode = newhouseList.get(position).getHouse_color();
                Log.i("colorCode**456", "" + newhouseList.get(position).getHouse_name());

                Log.i("colorCode**1", "" + newhouseList.get(position).getmName());
                Log.i("colorCode**2", "" + newhouseList.get(position).getCName());
                Log.i("colorCode**3", "" + newhouseList.get(position).getvCName());

                Log.i("colorCode**", "" + colorCode);

                if (
                        colorCode.equalsIgnoreCase("#7CB342") ||
                                colorCode.equalsIgnoreCase("#1E88E5") ||
                                colorCode.equalsIgnoreCase("#E53935") ||
                                colorCode.equalsIgnoreCase("#FDD835") ||
                                colorCode.equalsIgnoreCase("#8E24AA")) {

                    holder.tvStudentCount.setTextColor(Color.parseColor(colorCode));
                    holder.llHouseAddress.setBackgroundColor(Color.parseColor(colorCode));
                    holder.tvStudent.setTextColor(Color.parseColor(colorCode));

                } else {
                    holder.tvStudentCount.setTextColor(Color.parseColor("#000000"));
                    holder.llHouseAddress.setBackgroundColor(Color.parseColor("#000000"));
                    holder.tvStudent.setTextColor(Color.parseColor("#000000"));
                }


                holder.llHouseAddress.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        String colorName = newhouseList.get(position).getHouse_color();

                        updateHouseDialog(newhouseList.get(position).getHouse_uuid(),
                                newhouseList.get(position).getHouse_name(), colorName);

                        Log.i("colorCode**", "" + colorName);
                        return false;
                    }
                });


                holder.llHouseAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mName = "", vCName = "", CName = "";
                        if (newhouseList.get(position).getmName().isEmpty() || newhouseList.get(position).getmName().equals("")) {
                            mName = "Undefine";
                        } else {
                            mName = newhouseList.get(position).getmName();
                        }

                        if (newhouseList.get(position).getvCName().isEmpty() || newhouseList.get(position).getvCName().equals("")) {
                            vCName = "Undefine";
                        } else {
                            vCName = newhouseList.get(position).getvCName();
                        }

                        if (newhouseList.get(position).getCName().isEmpty() || newhouseList.get(position).getCName().equals("")) {
                            CName = "Undefine";
                        } else {
                            CName = newhouseList.get(position).getCName();
                        }


                        String houseId = newhouseList.get(position).getHouse_uuid();
                        String houseName = newhouseList.get(position).getHouse_name();
                        String houseColor = newhouseList.get(position).getHouse_color();
                        String noOfstudent = newhouseList.get(position).getNumber_of_students() + "";


                        Log.i("houseColor**32", "" + houseColor);
                        houeseDialogView(houseId, houseName, houseColor, noOfstudent, mName, CName, vCName);
                        Log.i("colorCode**12", "" + newhouseList.get(position).getmName());
                        Log.i("colorCode**22", "" + newhouseList.get(position).getCName());
                        Log.i("colorCode**32", "" + newhouseList.get(position).getvCName());


                    }
                });


                break;

            case Constant.BARRIERS_FRAG:

                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataArrayList.get(position).setSelect(isChecked);
                        holder.check_box.setChecked(isChecked);
                        if (isChecked) {
                            holder.check_box.setBackgroundColor(Color.parseColor("#FF63DC67"));
                            holder.check_box.setTextColor(Color.WHITE);
                        } else {

                            holder.check_box.setBackgroundColor(Color.WHITE);
                            holder.check_box.setTextColor(Color.BLACK);
                        }
                    }
                });

                //This is for Whatever click the values that are Selectable

                data = dataArrayList.get(position);
                holder.check_box.setText(data.getName());
                holder.check_box.setChecked(data.isSelect());
                if (data.isSelect()) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#FF63DC67"));
                    holder.check_box.setTextColor(Color.WHITE);
                    Log.d("Added", "onBindViewHolder: ");
                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                }


                break;


            case Constant.DEPARTMENT_FRAG:

                Log.d("asdf", dataArrayList.size() + "");

                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataArrayList.get(position).setSelect(isChecked);
                        holder.check_box.setChecked(isChecked);
                        if (isChecked) {

                            holder.check_box.setBackgroundColor(Color.parseColor("#FF63DC67"));
                            holder.check_box.setTextColor(Color.WHITE);
                        } else {

                            holder.check_box.setBackgroundColor(Color.WHITE);
                            holder.check_box.setTextColor(Color.BLACK);
                        }


                    }
                });

                //This is for Whatever click the values that are Selectable

                data = dataArrayList.get(position);
                holder.check_box.setText(data.getName());
                holder.check_box.setChecked(data.isSelect());
                if (data.isSelect()) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#FF63DC67"));
                    holder.check_box.setTextColor(Color.WHITE);
                    Log.d("Added", "onBindViewHolder: ");
                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                }

                holder.check_box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status;
                        if (holder.check_box.isChecked()) {
                            status = "true";
                        } else {
                            status = "false";
                        }
                        String deptName = holder.check_box.getText().toString();

                        JSONObject objectSize = new JSONObject();
                        JSONObject objectDept = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", deptName);
                            jsonObject.put("status", status);
                            objectSize.put("1", jsonObject);
                            objectDept.put("departments", objectSize);

                        } catch (JSONException je) {

                        }
                        updateDepartmentStatus(Constant.wingName, objectDept);

                    }
                });


                break;


        }


    }

    private void houeseDialogView(String houseId, String houseName, String houseColor, String noOfstudent,
                                  String mName, String CName, String vCName) {
//mName,CName,vCName
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.house_view_details);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.setCancelable(true);

        TextView ll_cancel = dialog.findViewById(R.id.ll_cancel);
        TextView tv_house_name = dialog.findViewById(R.id.tv_house_name);
        TextView tv_house_id = dialog.findViewById(R.id.tv_house_id);
        TextView tv_house_color = dialog.findViewById(R.id.tv_house_color);
        TextView tv_noof_student = dialog.findViewById(R.id.tv_noof_student);
        TextView tvMentore = dialog.findViewById(R.id.tvMentore);
        TextView tvcaption = dialog.findViewById(R.id.tvcaption);
        TextView tvVcaption = dialog.findViewById(R.id.tvVcaption);



        switch (houseColor) {
            case "#FDD835":
                 colorName="Yellow";
                break;
            case "#7CB342":
                colorName="Green";
                break;
            case "#8E24AA":
                colorName="Purple";
                break;
            case "#E53935":
                colorName="Red";
                break;
            case "#1E88E5":
                colorName="Blue";
                break;
            default:
                colorName="Black";
        }



        tv_house_id.setText(houseId);
        tv_house_name.setText(houseName);
        tv_house_color.setText(colorName);
        tvMentore.setText(mName);
        tvcaption.setText(CName);
        tvVcaption.setText(vCName);

        tv_noof_student.setText(noOfstudent);

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void updateHouseDialog(final String house_uuid, String house_name, final String colorName) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_update_houses);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        getEmployeeList();
        // studentAll();

        sp_teacher1 = dialog.findViewById(R.id.sp_teacher1);
        tvcolorcode = dialog.findViewById(R.id.tvcolorcode);
        sp_Caption = dialog.findViewById(R.id.sp_Caption);
        sp_ViceCaption = dialog.findViewById(R.id.sp_ViceCaption);

        TextView tvSubmit = dialog.findViewById(R.id.tvadd);
        TextView tvcancel = dialog.findViewById(R.id.tvcancel);
        final EditText etHouseName = dialog.findViewById(R.id.etHouseName);

        etHouseName.setText(house_name);
        String strColorNameNew = colorName;
        System.out.println("strColorNameNew***" + strColorNameNew);


        employeeList = new ArrayList<>();
        captionNameList = new ArrayList<>();

        captionNameList.clear();
        Colorcategories.clear();

        Colorcategories.add("Green");
        Colorcategories.add("Blue");
        Colorcategories.add("Red");
        Colorcategories.add("Yellow");
        Colorcategories.add("Purple");
        Colorcategories.add("Color");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(context, Colorcategories);
        tvcolorcode.setAdapter(customSpinnerAdapter);

        if (strColorNameNew.equals("#7CB342")) {
            tvcolorcode.setSelection(0);
        } else if (strColorNameNew.equals("#1E88E5")) {
            tvcolorcode.setSelection(1);
        } else if (strColorNameNew.equals("#E53935")) {
            tvcolorcode.setSelection(2);
        } else if (strColorNameNew.equals("#FDD835")) {
            tvcolorcode.setSelection(3);
        } else if (strColorNameNew.equals("#8E24AA")) {
            tvcolorcode.setSelection(4);
        } else {
            tvcolorcode.setSelection(5);
        }

        tvcolorcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strColorName = tvcolorcode.getItemAtPosition(position).toString();

                if (!strColorName.equals("Color")) {

                    switch (position) {

                        case 0:
                            strColor = "#7CB342";
                            break;

                        case 1:
                            strColor = "#1E88E5";
                            break;

                        case 2:
                            strColor = "#E53935";
                            break;

                        case 3:
                            strColor = "#FDD835";
                            break;

                        case 4:
                            strColor = "#8E24AA";
                            break;

                    }

                    System.out.println("colorName***ADCOLORAD** " + strColorName + "***" + strColor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_teacher1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (employeeNameList.size() > 0) {

                    strTeacherName = sp_teacher1.getItemAtPosition(position).toString();

                    if (strTeacherName.equals("-Mentor-")) {

                        captionNameList.add("-Caption-");
                        CustomSpinnerAdapter cAdapter = new CustomSpinnerAdapter(context, captionNameList);
                        sp_Caption.setAdapter(cAdapter);
                        sp_ViceCaption.setAdapter(cAdapter);
                        cAdapter.notifyDataSetChanged();

                    } else {
                        int empOreder = parent.getSelectedItemPosition();

                        System.out.println("employeeNameList**CLICK**" + employeeNameList.size()
                                + "++++" + empOreder + "***" + strTeacherName);

                        for (int i = 0; i < employeeNameList.size(); i++) {
                            if (empOreder == i) {
                                String strEmpId = employeeList.get(i).getEmpFname();
                                strTeacherUUID = employeeList.get(i).getEmpUUId();
                                studentAll(house_uuid);


                            } else {

                            }

                        }
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_Caption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strCaptionName = parent.getItemAtPosition(position).toString();

                if (!strCaptionName.equals("-Caption-")) {
                    int captionOreder = parent.getSelectedItemPosition();

                    try {
                        if (captionNameList.size() > 0) {
                            for (int i = 0; i < captionNameList.size(); i++) {
                                if (captionOreder == i) {
                                    strCaprionId = studentModels.get(i).getStrStudentID();

                                    System.out.println("strCaprionId*" + strCaprionId);
                                } else {


                                }


                            }
                        }
                    } catch (Exception e) {

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_ViceCaption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {

                strViceCaptionName = sp_ViceCaption.getItemAtPosition(position).toString();
                int captionOreder = sp_ViceCaption.getSelectedItemPosition();
                try {
                    if (captionNameList.size() > 0) {
                        for (int i = 0; i < captionNameList.size(); i++) {
                            if (captionOreder == i) {
                                strViceCaprionId = studentModels.get(i).getStrStudentID();

                                System.out.println("strViceCaprionId***" + strViceCaprionId);
                            } else {


                            }


                        }
                    }
                } catch (Exception e) {

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.dismiss();

                notifyDataSetChanged();
                APIService apiService = ApiUtils.getAPIService();


                apiService.updateHouse(Constant.SCHOOL_ID, etHouseName.getText().toString(),
                        strTeacherUUID, strCaprionId, strViceCaprionId, strColor, Constant.EMPLOYEE_BY_ID, house_uuid)
                        .enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Log.i("respupdate***", "" + response.body() + "***" + response.code() + "**"
                                        + response.message());

                                if (response.isSuccessful()) {
                                    try {
                                        Toast.makeText(context, "House updated successfully", Toast.LENGTH_SHORT).show();
                                        etHouseName.setText("");
                                        // edStudentNo.setText("");
                                        sp_teacher1.setSelection(0);
                                        sp_Caption.setSelection(0);
                                        sp_ViceCaption.setSelection(0);
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService
                                                (Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                        final House_Activity examActivity = (House_Activity) context;
                                        examActivity.loadFragment(Constant.ADD_HOUSES, null);

                                    } catch (Exception e) {

                                    }
                                } else {
                                    try {
                                        assert response.errorBody() != null;
                                        JSONObject object = new JSONObject(response.errorBody().string());
                                        String message = object.getString("message");
                                        Log.d("HOUSE_UPDATE", message);
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
        });

        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


    private void getEmployeeList() {

        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        employeeNameList.clear();
                        employeeList.clear();
                        employeeNameList.add("-Mentor-");
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

                                employeeList.add(new EmpLeaveModel(empFname, empLname, empUUId, empPhoneNo,
                                        empAdhaarNo, empDept));
                            }

                            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(context, employeeNameList);
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


    }

    private void studentAll(final String house_uuid) {
        mApiService.get_house_student_list(Constant.SCHOOL_ID, house_uuid).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    Log.i("studentList_house_uuid", "" + response.raw().request().url() + "**" + house_uuid);
                    Log.i("studentList_all_house", "" + response.body() + "***" + response.code());
                    try {
                        studentModels.clear();
                        captionNameList.clear();

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject = object.getJSONObject("data");
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject objectData = jsonObject.getJSONObject(key);



                                if (objectData.isNull("first_name")) {
                                    strFirstName = "";
                                } else {
                                    strFirstName = objectData.getString("first_name");
                                }

                                if (objectData.isNull("last_name")) {
                                    strLastName = "";
                                } else {
                                    strLastName = objectData.getString("last_name");
                                }

                                if (objectData.isNull("dob")) {
                                    strDOB = "";
                                } else {
                                    strDOB = objectData.getString("dob");
                                }

                                if (objectData.isNull("student_uuid")) {
                                    strStudentID = "";
                                } else {
                                    strStudentID = objectData.getString("student_uuid");
                                }

                                if (objectData.isNull("birth_certificate_number")) {
                                    strCertificateNo = "";
                                } else {
                                    strCertificateNo = objectData.getString("birth_certificate_number");
                                }

                                if (objectData.isNull("custom_id")) {
                                    strCustomId = "";
                                } else {
                                    strCustomId = objectData.getString("custom_id");
                                }

                                if (objectData.isNull("photo")) {
                                    strPhoto = objectData.getString("photo");
                                } else {
                                    strPhoto = objectData.getString("photo");
                                }


                                studentModels.add(new StudentModel(strFirstName, strLastName,
                                        strDOB, strStudentID, strCertificateNo, strCustomId, strPhoto));

                                String name = strFirstName + " " + strLastName;
                                captionNameList.add(name);

                                //viceCaptionNameList.add(strFirstName + " " + strLastName);


                            }

                            CustomSpinnerAdapter customSpinnerAdapter2 = new CustomSpinnerAdapter(context, captionNameList);
                            sp_Caption.setAdapter(customSpinnerAdapter2);


                            CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(context, captionNameList);
                            sp_ViceCaption.setAdapter(customSpinnerAdapter1);

                        }
                    } catch (JSONException je) {

                    }

                } else {

                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API_HOUSE", message);
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        captionNameList.clear();
                        captionNameList.add("-Caption-");
                        CustomSpinnerAdapter c = new CustomSpinnerAdapter(context, captionNameList);
                        sp_Caption.setAdapter(c);
                        sp_ViceCaption.setAdapter(c);
                        c.notifyDataSetChanged();

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


    private void updateDepartmentStatus(String wingName, JSONObject objectDept) {
        mApiService.updateDeptStatus(Constant.SCHOOL_ID, wingName, objectDept, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("STATUS_UPDATE", "" + response.body());
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });


    }


    private void OpenDialogUpdateAndDeleteRoles(final String role) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_delete_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText etOldRole = dialog.findViewById(R.id.etOldRole);
        final EditText newRole = dialog.findViewById(R.id.etNewRole);
        Button updateRole = dialog.findViewById(R.id.btnUpdateRole);
        final Button deleteRole = dialog.findViewById(R.id.btnDeleteRole);

        etOldRole.setText(role);
        etOldRole.setFocusable(false);
        etOldRole.setFocusableInTouchMode(false);


        updateRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updateRole = newRole.getText().toString();
                if (!updateRole.equals("")) {
                    //updateRolesApi(role, updateRole);

                }

            }
        });


        deleteRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  deleteRoleApi(role);

            }
        });


        dialog.show();


    }


    @Override
    public int getItemCount() {
        switch (tag) {
            case Constant.BARRIERS_FRAG:
                return dataArrayList.size();

            case Constant.DEPARTMENT_FRAG:
                return dataArrayList.size();

            case Constant.RV_ADD_HOUSES:
                return newhouseList.size();
            //return houseList.size();

        }
        return 0;
    }


    public void newValues(String s) {
        dataArrayList.add(new Data(s, true));
        notifyDataSetChanged();
    }

}





