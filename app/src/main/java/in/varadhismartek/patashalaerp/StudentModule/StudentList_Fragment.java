package in.varadhismartek.patashalaerp.StudentModule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import in.varadhismartek.patashalaerp.DashboardModule.NotificationModule.NotificationModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.Gravity.CENTER;

public class StudentList_Fragment extends Fragment {
    APIService apiService;
    TextView viewTitle;
    RecyclerView recyclerView;
    StudentRecyclerAdapter studentRecyclerAdapter;
    ArrayList<StudentModel> studentModels = new ArrayList<>();
    String strDivision, strClass, strSection, strFirstName, strLastName, strDOB, strStudentID, strCertificateNo,
            strStatus, strdeleted, strPhoto;


    ArrayList<String> classList;
    ArrayList<String> divisionList = new ArrayList<>();
    ArrayList<NotificationModel> classSectionList;
    Spinner sp_class, sp_section;
    String strSpClass, strSpSection;
    ImageView iv_backBtn;
FloatingActionButton fab;
    public StudentList_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_all_list, container, false);

        initView(view);
        getAllClasses();
        studentAll();
        return view;
    }

    @SuppressLint("WrongConstant")
    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        viewTitle = view.findViewById(R.id.tvTitle);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        sp_class = view.findViewById(R.id.sp_class);
        sp_section = view.findViewById(R.id.sp_section);
        fab = view.findViewById(R.id.fab);
        viewTitle.setText("Student List");

        fab.setVisibility(View.GONE);
        classList = new ArrayList<>();
        classSectionList = new ArrayList<>();

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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

                // if (divisionList.size() > 0) {

                Log.d("Class_api_1", String.valueOf(divisionList.size()));

                JSONObject object = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                jsonArray.put("All");


                try {
                    object.put("divisions", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Class_api_2", object.toString());

                if (classList.size() > 0)
                    classList.clear();
                classList.add("--Classes--");

                apiService.getClassSections(Constant.SCHOOL_ID, object).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                        if (response.isSuccessful()) {
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

                                            ArrayList<String> sections = new ArrayList<>();
                                            sections.add("--Sections--");
                                            for (int i = 0; i < jsonArray1.length(); i++) {

                                                sections.add(jsonArray1.getString(i));
                                                Log.d("classData_array " + i, jsonArray1.getString(i));

                                            }

                                            classSectionList.add(new NotificationModel(class_name, sections));
                                        }
                                    }

                                }

                                CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), classList);
                                sp_class.setAdapter(customSpinnerAdapter);

                                sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        strSpClass = parent.getItemAtPosition(position).toString();

                                        ArrayList<String> listSection = new ArrayList<>();
                                        if (!strSpClass.equals("--Classes--")) {

                                            listSection.clear();
                                            for (int i = 0; i < classSectionList.size(); i++) {
                                                if (strSpClass.contains(classSectionList.get(i).getClassName())) {
                                                    for (int j = 0; j < classSectionList.get(i).getSectionName().size(); j++) {
                                                        String sec = classSectionList.get(i).getSectionName().get(j).toString();
                                                        listSection.add(sec);


                                                        System.out.println("classSectionList*** Sec" + sec);
                                                    }

                                                }
                                            }
                                        }else {
                                            listSection.add("--Sections--");
                                        }


                                        CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(getActivity(),
                                                listSection);
                                        sp_section.setAdapter(customSpinnerAdapter1);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                                sp_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        strSpSection = parent.getItemAtPosition(position).toString();
                                        Log.i("Sections**", "" + strSpSection);
                                        if (!strSpSection.equalsIgnoreCase("--Sections--")) {
                                            studentListByClassSection(strSpClass, strSpSection);
                                        }
                                        //

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

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

            // }
        }, 2000);


    }


    private void studentListByClassSection(String strSpClass, String strSpSection) {
        apiService.getStudentAllByClassSection(Constant.SCHOOL_ID, strSpClass, strSpSection)
                .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("studentList_byclassaa**", "" + response.body() + "**" + response.code());
                if (response.isSuccessful()) {

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

                            }
                            if (studentModels.size() > 0) {
                                setRecyclerView(studentModels);
                            } else {

                            }

                        }
                    } catch (JSONException je) {

                    }
                }

                else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                        Log.d("ADMIN_API", message);



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void studentAll() {
        apiService.getStudentAll(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("studentList_all", "" + response.body() + "***" + response.code());
                if (response.isSuccessful()) {
                    Log.i("studentList_all", "" + response.raw().request().url());


                    try {
                        studentModels.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        Log.i("MEssage**C*", "" + object.getString("message"));

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

                            }
                            if (studentModels.size() > 0) {
                                setRecyclerView(studentModels);
                            } else {

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

    private void setRecyclerView(ArrayList<StudentModel> studentModels) {
        studentRecyclerAdapter = new StudentRecyclerAdapter(studentModels, getActivity(), Constant.RV_STUDENT_ALL_LIST);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(studentRecyclerAdapter);
        studentRecyclerAdapter.notifyDataSetChanged();
    }


}
