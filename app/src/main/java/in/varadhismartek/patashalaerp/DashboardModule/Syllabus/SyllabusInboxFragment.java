package in.varadhismartek.patashalaerp.DashboardModule.Syllabus;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
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
public class SyllabusInboxFragment extends Fragment implements View.OnClickListener {

    ImageView img_back;
    RecyclerView recycler_view;
    Spinner sp_ExamType,sp_class, sp_section;
    FloatingActionButton fab_button;

    APIService mApiServicePatashala;
    APIService mApiService;

    ArrayList<String> classList;
    ArrayList<SyllabusModel> syllabusList;
    ArrayList<SyllabusModel> newSyllabusList;
    SyllabusAdapter adapter;
    String str_class = "",str_section="";
    ProgressDialog progressDialog;

    Toolbar toolbar;


    public SyllabusInboxFragment() {
        // Required empty public constructor
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_syllabus_inbox, container, false);

        initViews(view);
        initListeners();
        //setRecyclerView(syllabusList);
        getAllClasses();

        toolbar.setBackgroundResource(R.drawable.toolbar_syllabus);
        fab_button.setVisibility(View.VISIBLE);


        return view;
    }

    private void setRecyclerView(ArrayList<SyllabusModel> syllabusList) {

        adapter = new SyllabusAdapter(syllabusList, getActivity(), Constant.RV_SYLLABUS_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setSpinnerForClasses(){


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), classList, Constant.audience_type);
        sp_class.setAdapter(customSpinnerAdapter);
        customSpinnerAdapter.notifyDataSetChanged();

        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (syllabusList.size()>0) {
                    syllabusList.clear();
                    adapter.notifyDataSetChanged();
                }

                if (i==0){
                    str_class = "";
                    getSchoolSyllabusAPI();

                }else {
                    str_class = sp_class.getSelectedItem().toString();

                }

                getAllSection(str_class);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setSpinnerForSection(ArrayList<String> subList){

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), subList, Constant.audience_type);
        sp_section.setAdapter(customSpinnerAdapter);
        adapter.notifyDataSetChanged();

        sp_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               /* if (syllabusList.size()>0) {
                    syllabusList.clear();
                    adapter.notifyDataSetChanged();
                }*/

                if (i==0){

                    str_section = "";
                }else {
                    str_section = sp_section.getSelectedItem().toString();
                    getClassSyllabusAPI();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initListeners() {

        img_back.setOnClickListener(this);
        fab_button.setOnClickListener(this);
    }

    private void initViews(View view) {

        mApiServicePatashala = ApiUtilsPatashala.getService();
        mApiService = ApiUtils.getAPIService();
        progressDialog = new ProgressDialog(getActivity());


        toolbar = view.findViewById(R.id.toolbar);
        img_back = view.findViewById(R.id.img_back);
        fab_button = view.findViewById(R.id.fab_button);
        recycler_view = view.findViewById(R.id.recycler_view);
        sp_ExamType = view.findViewById(R.id.sp_ExamType);
        sp_class = view.findViewById(R.id.sp_class);
        sp_section = view.findViewById(R.id.sp_section);

        classList = new ArrayList<>();
        syllabusList = new ArrayList<>();
        newSyllabusList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.img_back:
                getActivity().onBackPressed();
                break;

            case R.id.fab_button:

                SyllabusActivity syllabusActivity = (SyllabusActivity) getActivity();

                syllabusActivity.loadFragment(Constant.SYLLABUS_ADD_FRAGMENT, null);

                break;
        }
    }

    private void getSchoolSyllabusAPI(){

        if (syllabusList.size()>0) {
            syllabusList.clear();
            adapter.notifyDataSetChanged();

        }
        progressDialog.show();

        mApiServicePatashala.getSchoolSyllabus(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message1 = object.getString("message");
                        String status1 = object.getString("status");


                        if (status1.equalsIgnoreCase("Success")){

                            Log.d("Class_api_1",status1+" "+message1);

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("Class_api_2"," "+jsonData);

                            JSONObject school_syllabus = jsonData.getJSONObject("syllabus_deatils_data");
                            Log.d("Class_api_3"," "+school_syllabus);

                            Iterator<String> key = school_syllabus.keys();

                            while (key.hasNext()){

                                JSONObject keyData = school_syllabus.getJSONObject(key.next());

                                Log.d("Class_api_keydata"," "+keyData);


                                String classes,syllabus_title,added_by,syllabus_uuid,subject,school_id,
                                        sections,exam_type,added_datetime,description;

                                ArrayList<String> imageList = new ArrayList<>();

                                classes = keyData.getString("classes");
                                syllabus_title = keyData.getString("syllabus_title");
                                added_by = keyData.getString("added_by");
                                syllabus_uuid = keyData.getString("syllabus_uuid");
                                subject = keyData.getString("subject");
                                school_id = keyData.getString("school_id");
                                sections = keyData.getString("sections");
                                if (!keyData.has("exam_type")){
                                    exam_type="";
                                }else {
                                    exam_type = keyData.getString("exam_type");
                                }

                                added_datetime = keyData.getString("added_datetime");
                                description = keyData.getString("description");

                                Log.d("Class_api_description"," "+description);

                                if (!keyData.isNull("syllabus_attachement")){
                                    JSONArray attachement = keyData.getJSONArray("syllabus_attachement");

                                    for (int i = 0; i<attachement.length();i++){

                                        imageList.add(attachement.get(i).toString());

                                    }

                                    Log.d("imageListSize", imageList.size()+"");

                                }

                                syllabusList.add(new SyllabusModel(classes,syllabus_title,added_by,syllabus_uuid,subject,school_id
                                        ,sections,exam_type,added_datetime,description,imageList));
                            }
                            setRecyclerView(syllabusList);
                            Log.d("DTATgdk", "data by school   "+ syllabusList.size());
                            progressDialog.dismiss();
                            adapter.notifyDataSetChanged();

                        }else {
                            Log.d("Syllabus_API", response.code()+" "+message1);
                            //Toast.makeText(getActivity(), message1, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        progressDialog.dismiss();
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
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

    private void getClassSyllabusAPI(){

        if (syllabusList.size()>0) {
            syllabusList.clear();
            adapter.notifyDataSetChanged();

        }

        progressDialog.show();

        mApiServicePatashala.getClassSyllabus(Constant.SCHOOL_ID, str_class, str_section).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("Class_api_1",status1+" "+message1);

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("Class_api_2"," "+jsonData);

                            JSONObject school_syllabus = jsonData.getJSONObject("School_syllabus");
                            Log.d("Class_api_3"," "+school_syllabus);

                            Iterator<String> key = school_syllabus.keys();

                            while (key.hasNext()){

                                JSONObject keyData = school_syllabus.getJSONObject(key.next());

                                Log.d("Class_api_keydata"," "+keyData);

                                String classes,syllabus_title,added_by,syllabus_uuid,subject,school_id,
                                        sections,added_datetime,description,exam_type;

                                ArrayList<String> imageList = new ArrayList<>();

                                classes = keyData.getString("classes");
                                syllabus_title = keyData.getString("syllabus_title");
                                added_by = keyData.getString("added_by");
                                syllabus_uuid = keyData.getString("syllabus_uuid");
                                subject = keyData.getString("subject");
                                school_id = keyData.getString("school_id");
                                sections = keyData.getString("sections");
                                if (!keyData.has("exam_type")){
                                    exam_type="";
                                }else {
                                    exam_type = keyData.getString("exam_type");
                                }
                                added_datetime = keyData.getString("added_datetime");
                                description = keyData.getString("description");


                                Log.d("Class_api_description"," "+description);

                                if (!keyData.isNull("syllabus_attachement")){
                                    JSONArray attachement = keyData.getJSONArray("syllabus_attachement");

                                    for (int i = 0; i<attachement.length();i++){

                                        imageList.add(attachement.get(i).toString());

                                    }

                                    Log.d("imageListSize", imageList.size()+"");

                                }

                                syllabusList.add(new SyllabusModel(classes,syllabus_title,added_by,syllabus_uuid,subject,school_id
                                        ,sections,exam_type,added_datetime,description,imageList));
                            }

                            Log.d("DTATgdk", "data by school   "+ syllabusList.size());
                            progressDialog.dismiss();
                            adapter.notifyDataSetChanged();

                        }else {
                            Log.d("Syllabus_API", response.code()+" "+message1);
                            Toast.makeText(getActivity(), message1, Toast.LENGTH_SHORT).show();
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
                        Log.d("ADMIN_API", message);
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

    private void getAllClasses() {

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
                            setSpinnerForClasses();
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

    private void getAllSection(final String strClass) {

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

        final ArrayList<String> sections = new ArrayList<>();
        sections.add(0,"Select Section");

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

                                    JSONArray jsonArray1 = classData.getJSONArray("sections");
                                    Log.d("classData_array", jsonArray1.toString());

                                    if (strClass.equalsIgnoreCase(class_name)) {

                                        for (int i = 0; i < jsonArray1.length(); i++) {

                                            sections.add(jsonArray1.getString(i));
                                            Log.d("classData_array " + i, jsonArray1.getString(i));
                                        }
                                    }

                                }
                            }

                            progressDialog.dismiss();
                            setSpinnerForSection(sections);

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


}
