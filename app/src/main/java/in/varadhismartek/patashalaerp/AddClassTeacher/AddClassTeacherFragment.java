package in.varadhismartek.patashalaerp.AddClassTeacher;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
public class AddClassTeacherFragment extends Fragment implements View.OnClickListener {

    public static String SEARCH_ID = "";
    ImageView iv_backBtn;
    TextView tv_all, tv_teacher, tv_save_teacher;
    Spinner spn_class, spn_section;
    LinearLayout ll_search;
    TextView tv_save, tv_search;
    LinearLayout ll_viewList;
    RecyclerView recycler_view;
    ProgressDialog progressDialog;

    ArrayList<String> classList;
    ArrayList<String> sectionList;
    ArrayList<ClassTeacherModel> searchList;
    ArrayList<ClassTeacherModel> classTeacherList;

    APIService mApiService;
    APIService mApiServicePatashala;
    String str_class = "", str_section = "";
    ClassTeacherAdapter classTeacherAdapter;
    ClassTeacherAdapter adapterTeacherList;
    RecyclerView rv_search;
    Dialog dialog,dialogClassTeacher;
    FloatingActionButton floatingActionButton;

    public AddClassTeacherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_class_teacher, container, false);


        initViewsNew(view);
        initListenersNew();
        getClassTeacherAPI();

      /*  initViews(view);
        initListeners();
        getAllClassesAPI();
        setRecyclerViewClassTeacher();
        getClassTeacherAPI();*/

        return view;
    }

    private void initListenersNew() {

        iv_backBtn.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);


    }

    private void initViewsNew(View view) {

        mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(getActivity());
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        recycler_view = view.findViewById(R.id.recycler_view);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        classTeacherList = new ArrayList<>();
    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        tv_teacher.setOnClickListener(this);
        tv_save_teacher.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        ll_search.setOnClickListener(this);
    }

    private void initViews(View view) {

      /*  mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(getActivity());

         recycler_view = view.findViewById(R.id.recycler_view);
        tv_teacher.setVisibility(View.GONE);
        tv_save_teacher.setVisibility(View.GONE);

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        tv_all = view.findViewById(R.id.tv_all);
        tv_teacher = view.findViewById(R.id.tv_teacher);
        tv_save_teacher = view.findViewById(R.id.tv_save_teacher);
        tv_save = view.findViewById(R.id.tv_save);
        ll_search = view.findViewById(R.id.ll_search);


        spn_class = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);
        tv_search = view.findViewById(R.id.tv_search);
        ll_viewList = view.findViewById(R.id.ll_viewList);

        classList = new ArrayList<>();
        sectionList = new ArrayList<>();
        searchList = new ArrayList<>();
        classTeacherList = new ArrayList<>();*/
    }

    private void setRecyclerViewClassTeacher(){

        adapterTeacherList = new ClassTeacherAdapter(classTeacherList, getActivity(), Constant.RV_CLASS_TEACHER_LIST, myRefreshPage);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapterTeacherList);
        adapterTeacherList.notifyDataSetChanged();
    }

    private void setSpinnerForClass(){

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), classList);
        spn_class.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    str_class = "";
                    spn_section.setSelection(0);
                    if (sectionList.size()>0){
                        sectionList.clear();
                        sectionList.add(0, "-Section-");
                        setSpinnerForSection();
                        Log.d("shg","sgdsgdo");
                    }
                }
                else {
                    str_class = spn_class.getSelectedItem().toString();
                    getAllSection();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerForSection() {

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), sectionList, "Green");
        spn_section.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    str_section = "";
                }
                else {
                    str_section = spn_section.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

           /* case R.id.tv_all:
                break;

            case R.id.tv_teacher:

                break;

            case R.id.tv_save_teacher:
                break;

            case R.id.tv_save:

                addClassTeacherAPI();

                break;

            case R.id.ll_search:
                openSearchDialog();
                break;



*/
            case R.id.floatingActionButton:
                openAddClassTeacherDialog();
                break;


            case R.id.ll_search:
                openSearchDialog();
                break;

            case R.id.tv_save:

                addClassTeacherAPI();

                break;

        }
    }

    private void openAddClassTeacherDialog() {

        dialogClassTeacher = new Dialog(getActivity());
        dialogClassTeacher.setContentView(R.layout.add_class_teacher_dialog);
        dialogClassTeacher.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogClassTeacher.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);

        tv_save_teacher = dialogClassTeacher.findViewById(R.id.tv_save_teacher);
        tv_save = dialogClassTeacher.findViewById(R.id.tv_save);
        ll_search = dialogClassTeacher.findViewById(R.id.ll_search);


        spn_class = dialogClassTeacher.findViewById(R.id.spn_class);
        spn_section = dialogClassTeacher.findViewById(R.id.spn_section);
        tv_search = dialogClassTeacher.findViewById(R.id.tv_search);
        ll_viewList = dialogClassTeacher.findViewById(R.id.ll_viewList);

        tv_save_teacher.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        ll_search.setOnClickListener(this);

        classList = new ArrayList<>();
        sectionList = new ArrayList<>();
        searchList = new ArrayList<>();
        getAllClassesAPI();
        dialogClassTeacher.show();
    }

    private void openSearchDialog() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_search_emp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();

        EditText et_search = dialog.findViewById(R.id.et_search);
        rv_search = dialog.findViewById(R.id.rv_search);

        getStaffListAPI();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                teacherFilter(editable.toString());
            }
        });

    }

    private void teacherFilter(String text) {

        ArrayList<ClassTeacherModel> filteredStaffList = new ArrayList<>();
        for (ClassTeacherModel item : searchList) {
            if (item.getFirst_name().toLowerCase().contains(text.toLowerCase())) {
                filteredStaffList.add(item);
            }
        }
        classTeacherAdapter.StafffilterList(filteredStaffList);
    }


    private void getAllClassesAPI() {

        progressDialog.show();

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray.put("All");
            object.put("divisions",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Class_api_2", object.toString());

        if (classList.size()>0){
            classList.clear();
        }

        classList.add(0,"-Select Class-");

        mApiService.getClassSections(Constant.SCHOOL_ID,object).enqueue(new Callback<Object>() {
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

                                    ArrayList<String> sections = new ArrayList<>();

                                    for (int i = 0; i<jsonArray1.length();i++){

                                        sections.add(jsonArray1.getString(i));
                                        Log.d("classData_array "+i, jsonArray1.getString(i));
                                    }
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

    private void getAllSection() {

        progressDialog.show();

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray.put("All");
            object.put("divisions",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Class_api_2", object.toString());

        if (sectionList.size()>0){
            sectionList.clear();
        }

        sectionList.add(0,"-Section-");

        mApiService.getClassSections(Constant.SCHOOL_ID,object).enqueue(new Callback<Object>() {
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

                                    if (class_name.equals(str_class)){

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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("create_API", message);
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

    private void getStaffListAPI(){

        searchList.clear();
        progressDialog.show();

        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

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
                                String phone_number = keyData.getString("phone_number");
                                String added_datetime = keyData.getString("added_datetime");
                                String sex = keyData.getString("sex");
                                String last_name = keyData.getString("last_name");
                                String adhaar_card_no = keyData.getString("adhaar_card_no");
                                String photo = keyData.getString("employee_photo");
                                String wing_name = keyData.getString("wing_name");
                                String role = keyData.getString("role");
                                //String employee_status = keyData.getString("employee_status");
                                String employee_uuid = keyData.getString("employee_uuid");
                                String employee_deleted = keyData.getString("employee_deleted");
                                String department_name = keyData.getString("department_name");
                                //String date_of_joining = keyData.getString("date_of_joining");

                                searchList.add(new ClassTeacherModel(photo, first_name, last_name, department_name,
                                        role, employee_uuid));

                            }

                            setRecyclerViewSearch();
                            progressDialog.dismiss();
                            Log.d("STUDENT_LIST_SIZE", " "+searchList.size());



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

    private void setRecyclerViewSearch(){

        classTeacherAdapter = new ClassTeacherAdapter(searchList, getActivity(), Constant.RV_CLASS_TEACHER_SEARCH_ROW, tv_search, dialog);
        rv_search.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_search.setAdapter(classTeacherAdapter);
        classTeacherAdapter.notifyDataSetChanged();
    }

    private void getClassTeacherAPI(){

        if (classTeacherList.size()>0){
            classTeacherList.clear();
        }

        progressDialog.show();

        mApiServicePatashala.getClassTeachers(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status1 = object.getString("status");
                        String message1 = object.getString("message");

                        if (status1.equalsIgnoreCase("Success")){

                            JSONArray jsonData = object.getJSONArray("data");
                            Log.d("API_Data", jsonData.toString());

                            for (int i=0; i<jsonData.length();i++){

                                JSONObject data = jsonData.getJSONObject(i);
                                Log.d("API_Object_Data", data.toString());

                                Iterator<String> classKey = data.keys();

                                while (classKey.hasNext()){

                                    String className = classKey.next();
                                    Log.d("API_class_name", className.toString());

                                    JSONObject classData = data.getJSONObject(className);
                                    Log.d("API_class_data", classData.toString());

                                    Iterator<String> sectionKey = classData.keys();

                                    while (sectionKey.hasNext()){

                                        String sectionName = sectionKey.next();
                                        Log.d("API_section_name", className.toString());
                                        JSONObject sectionData = classData.getJSONObject(sectionName);
                                        Log.d("API_section_data", classData.toString());

                                        String teacher_custom_id = "";

                                        if (!sectionData.isNull("teacher_custom_id")){
                                            teacher_custom_id = sectionData.getString("teacher_custom_id");
                                        }

                                        String teacher_first_name = sectionData.getString("teacher_first_name");
                                        String added_employee_uuid = sectionData.getString("added_employee_uuid");
                                        boolean status = sectionData.getBoolean("status");
                                        String added_employee_first_name = sectionData.getString("added_employee_first_name");
                                        String teacher_uuid = sectionData.getString("teacher_uuid");
                                        String added_by_employee_lastname = sectionData.getString("added_by_employee_lastname");
                                        String teacher_last_name = sectionData.getString("teacher_last_name");
                                        String added_datetime = sectionData.getString("added_datetime");

                                        Log.d("API_final_data", added_datetime);

                                        classTeacherList.add(new ClassTeacherModel(className,sectionName,teacher_custom_id,
                                                teacher_first_name,teacher_last_name,status,teacher_uuid));
                                    }
                                }
                            }
                            setRecyclerViewClassTeacher();
                            adapterTeacherList.notifyDataSetChanged();
                            progressDialog.dismiss();



                        }else {
                            Log.d("ClassTeacher_else", response.code()+" "+message1);
                            adapterTeacherList.notifyDataSetChanged();
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
                        Log.d("ClassTeacher_fail", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        adapterTeacherList.notifyDataSetChanged();
                        progressDialog.dismiss();

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

    private void addClassTeacherAPI() {

        progressDialog.show();

        mApiServicePatashala.addClassTeacher(Constant.SCHOOL_ID, Constant.EMPLOYEE_BY_ID,
                str_class, str_section, SEARCH_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                Log.d("STUDENT_LIST_FAIL", response.code()+"");


                if (response.isSuccessful()){

                    Toast.makeText(getActivity(), "Added SuccessFully", Toast.LENGTH_LONG).show();
                    getClassTeacherAPI();
                    progressDialog.dismiss();
                    dialogClassTeacher.dismiss();
                    adapterTeacherList.notifyDataSetChanged();

                    //getClassTeacherAPI();

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("STUDENT_LIST_FAIL", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialogClassTeacher.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        dialogClassTeacher.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }


    MyRefreshPage myRefreshPage = new MyRefreshPage() {
        @Override
        public void refreshPage() {
            getClassTeacherAPI();
        }
    };

    public interface MyRefreshPage{

        void refreshPage();
    }

}
