package in.varadhismartek.patashalaerp.Birthday;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
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
public class BirthdayInboxFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar;
    ImageView iv_backBtn;
    CardView cv_class_section;
    Spinner spn_class, spn_section, spn_department;
    RecyclerView recycler_view;
    LinearLayout ll_main;

    TextView tv_teacher,tv_student,tvNorecords;

    ArrayList<String> deptList;
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    ArrayList<BirthdayModel> birthdayList;

    APIService mApiService;
    APIService mApiServicePatashala;

    ProgressDialog progressDialog;
    BirthdayAdapter adapter;
    String str_class = "", strSection = "";


    public BirthdayInboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_birthday_inbox, container, false);

        initView(view);
        initListeners();
        setRecyclerView();

        getDepartmentAPI();

        cv_class_section.setVisibility(View.GONE);
        spn_department.setVisibility(View.VISIBLE);


        toolbar.setBackgroundResource(R.color.orange_colorPrimaryDark);
        ll_main.setBackgroundResource(R.color.orange_colorPrimaryDark);

        return view;
    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        tv_teacher.setOnClickListener(this);
        tv_student.setOnClickListener(this);
    }

    private void initView(View view) {

        mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(getActivity());

        toolbar = view.findViewById(R.id.toolbar);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        tvNorecords = view.findViewById(R.id.tvNorecords);
        cv_class_section = view.findViewById(R.id.cv_class_section);
        spn_class = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);
        spn_department = view.findViewById(R.id.spn_department);
        recycler_view = view.findViewById(R.id.recycler_view);
        ll_main = view.findViewById(R.id.ll_main);

        tv_teacher = view.findViewById(R.id.tv_teacher);
        tv_student = view.findViewById(R.id.tv_student);
        tvNorecords.setVisibility(View.GONE);
        deptList = new ArrayList<>();
        listClass = new ArrayList<>();
        listSection = new ArrayList<>();
        birthdayList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_teacher:

                tv_teacher.setTextColor(Color.WHITE);
                tv_student.setTextColor(Color.GRAY);
                tv_teacher.setBackgroundResource(R.drawable.button_shape_other);
                tv_student.setBackground(null);

                cv_class_section.setVisibility(View.GONE);
                spn_department.setVisibility(View.VISIBLE);

                getDepartmentAPI();

                break;

            case R.id.tv_student:

                tv_student.setTextColor(Color.WHITE);
                tv_teacher.setTextColor(Color.GRAY);
                tv_student.setBackgroundResource(R.drawable.button_shape_other);
                tv_teacher.setBackground(null);

                cv_class_section.setVisibility(View.VISIBLE);
                spn_department.setVisibility(View.GONE);

                getClassListAPI();

                break;
        }
    }

    private void setRecyclerView(){

            recycler_view.setVisibility(View.VISIBLE);
            adapter = new BirthdayAdapter(birthdayList, getActivity(), Constant.RV_BIRTHDAY_ROW);
            recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler_view.setAdapter(adapter);
            adapter.notifyDataSetChanged();

    }

    private void initSpinnerSelectDepartment(ArrayList<String> list) {

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getContext(),list,Constant.audience_type);
        spn_department.setAdapter(customSpinnerAdapter);
        customSpinnerAdapter.notifyDataSetChanged();

        spn_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                getEmployeeBirthdayAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerForClass(){

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), listClass, Constant.audience_type);
        spn_class.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String className = spn_class.getSelectedItem().toString();
                getSectionListAPI(className);

                if (i==0){
                    str_class = "";

                }else {
                    str_class = spn_class.getSelectedItem().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setSpinnerForSection(){
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), listSection, Constant.audience_type);
        spn_section.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i==0){
                    strSection = "";

                }else {
                    strSection = spn_section.getSelectedItem().toString();
                }

                getStudentBirthdayAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getDepartmentAPI() {

        JSONObject object = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray.put("All");
            object.put("wings",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (deptList.size()> 0) {
            deptList.clear();
        }

        deptList.add(0, "-Select Department-");

        progressDialog.show();

        mApiService.gettingDept(Constant.SCHOOL_ID, object).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                Log.d("DEPARTMENT_4", String.valueOf(response.code()+" url"+response.raw().request().url()));

                if (response.isSuccessful()) {
                    try {

                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject1 = json1.getJSONObject("data");

                            JSONObject jsonObject2 = jsonObject1.getJSONObject("departments");

                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);

                                String name = jsonObjectValue.getString("name");
                                boolean wings_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (wings_status)
                                    deptList.add(name);

                            }
                            initSpinnerSelectDepartment(deptList);
                            progressDialog.dismiss();

                        }else {
                            if (String.valueOf(response.code()).equals("409")) {
                                Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                                deptList.clear();
                            }
                            if (String.valueOf(response.code()).equals("404")) {
                                Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                                deptList.clear();
                            }

                            progressDialog.dismiss();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();

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

                    progressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });


    }

    private void getClassListAPI() {

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        try {
            array.put("All");
            jsonObject.put("divisions", array);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("classes_input", jsonObject.toString());

        progressDialog.show();

        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("CLASS_SECTIONDDD", "" + response.code());

                listClass.clear();
                listClass.add(0, "-Select Class-");

                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("myDivisionKey_data1", jsonData.toString());


                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                String myDivisionKey = key.next();
                                Log.d("myDivisionKey", myDivisionKey);

                                JSONObject keyData = jsonData.getJSONObject(myDivisionKey);
                                Log.d("myDivisionKey_data", keyData.toString());

                                Iterator<String> classKey = keyData.keys();

                                while (classKey.hasNext()){

                                    JSONObject classData = keyData.getJSONObject(classKey.next());
                                    Log.d("classData", classData.toString());

                                    String class_name = classData.getString("class_name");
                                    Log.d("className", class_name);
                                    listClass.add(class_name);
                                }
                            }

                            Log.d("classData_array ", String.valueOf(listClass.size()));
                            setSpinnerForClass();
                            progressDialog.dismiss();

                        } else {

                            Log.d("Class_api_else", String.valueOf(response.code()));
                            setSpinnerForClass();
                            progressDialog.dismiss();
                        }

                    } catch (JSONException je) {
                        je.printStackTrace();
                    }

                } else {

                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Class_api_fail", message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    setSpinnerForClass();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });

    }

    private void getSectionListAPI(final String className) {

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        try {
            array.put("All");
            jsonObject.put("divisions", array);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("classes_input", jsonObject.toString());

        progressDialog.show();

        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("CLASS_SECTIONDDD", "" + response.code());

                listSection.clear();
                listSection.add(0, "-Select Section-");

                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonData = object.getJSONObject("data");

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                String myDivisionKey = key.next();

                                JSONObject keyData = jsonData.getJSONObject(myDivisionKey);

                                Iterator<String> classKey = keyData.keys();

                                while (classKey.hasNext()){

                                    JSONObject classData = keyData.getJSONObject(classKey.next());
                                    Log.d("classDataSection", classData.toString());

                                    String class_name = classData.getString("class_name");

                                    Log.d("className", classData.toString());


                                    if (class_name.equalsIgnoreCase(className)){

                                        JSONArray sectionArray = classData.getJSONArray("sections");

                                        for (int i=0;i<sectionArray.length();i++){

                                            listSection.add(sectionArray.get(i).toString());
                                            Log.d("classSection", sectionArray.get(i).toString());
                                        }
                                    }

                                }
                            }

                            Log.d("classData_array ", String.valueOf(listClass.size()));
                            setSpinnerForSection();
                            progressDialog.dismiss();

                        } else {

                            Log.d("Class_api_else", String.valueOf(response.code()));
                            setSpinnerForSection();
                            progressDialog.dismiss();
                        }

                    } catch (JSONException je) {
                        je.printStackTrace();
                    }

                } else {

                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Class_api_fail", message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    setSpinnerForSection();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });

    }

    private void getEmployeeBirthdayAPI(){

        String str_dept = spn_department.getSelectedItem().toString();

        Log.d("emp_birthday_input", Constant.SCHOOL_ID+" "+ str_dept);

        if (birthdayList.size()>0){
            birthdayList.clear();
        }

        progressDialog.show();

        mApiServicePatashala.getEmployeeBirthday(Constant.SCHOOL_ID, str_dept).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                Log.d("emp_birthday_success", response.code()+" "+response.body());

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status1 = object.getString("status");
                        String message1 = object.getString("message");

                        if (status1.equalsIgnoreCase("Success")){

                            Log.d("emp_birthday_success", response.code()+" "+message1);


                            JSONArray jsonData = object.getJSONArray("data");

                            for (int i = 0; i<jsonData.length();i++){

                                JSONObject data = jsonData.getJSONObject(i);

                                String employee_custom_id = "";

                                String employee_fistname = data.getString("employee_fistname");
                                String employee_lastname = data.getString("employee_lastname");
                                String employee_image = data.getString("employee_image");
                                String employee_uuid = data.getString("employee_uuid");
                                String employee_birthday = data.getString("employee_birthday");

                                if (!data.isNull("employee_custom_id")){
                                    employee_custom_id = data.getString("employee_custom_id");
                                }

                                birthdayList.add(new BirthdayModel(employee_fistname,employee_lastname,employee_custom_id,
                                        employee_image, employee_uuid,employee_birthday));
                            }

//                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }else {
                            Log.d("emp_birthday_else", response.code()+" "+message1);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        progressDialog.dismiss();
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("emp_birthday_fail", message);
                        adapter.notifyDataSetChanged();


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void getStudentBirthdayAPI(){

        Log.d("stu_birthday_input", Constant.SCHOOL_ID+" "+ str_class+" "+strSection);

        if (birthdayList.size()>0){
            birthdayList.clear();
        }

        progressDialog.show();

        mApiServicePatashala.getStudentBirthday(Constant.SCHOOL_ID, str_class,strSection).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {


                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status1 = object.getString("status");
                        String message1 = object.getString("message");

                        if (status1.equalsIgnoreCase("Success")){

                            Log.d("stu_birthday_success", response.code()+" "+message1);


                            JSONArray jsonData = object.getJSONArray("data");

                            for (int i = 0; i<jsonData.length();i++){

                                JSONObject data = jsonData.getJSONObject(i);

                                String student_custom_id = "";

                                String student_birthday = data.getString("student_birthday");
                                String student_uuid = data.getString("student_uuid");
                                String student_lastname = data.getString("student_lastname");
                                String student_fistname = data.getString("student_fistname");
                                String student_image = data.getString("student_image");

                                if (!data.isNull("employee_custom_id")){
                                    student_custom_id = data.getString("student_custom_id");
                                }

                                birthdayList.add(new BirthdayModel(student_fistname,student_lastname,student_custom_id,
                                        student_image, student_uuid,student_birthday));
                            }

                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }else {
                            Log.d("stu_birthday_else", response.code()+" "+message1);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        progressDialog.dismiss();
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("stu_birthday_fail", message);
                        adapter.notifyDataSetChanged();

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
