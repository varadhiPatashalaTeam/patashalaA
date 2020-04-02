package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_AddSubject extends Fragment implements View.OnClickListener {
    private Spinner spDivision, spClass, spSection, spSubject, edSubType;
    EditText edSubName, edSubCode;
    APIService apiService;
    ArrayList<String> listDivision, listClass, sectionClist, listSection, listSubject;
    ArrayList<ClassSectionModel> modelArrayList;
    ArrayList<SectionSubjectModel> sectionSubjectModels, sectionSubjectModelsNew;
    String strDiv = "", str_class = "", strSection = "", editvalue = "", strSubType;
    CustomSpinnerAdapter customSpinnerAdapter;
    RecyclerView recyclerView;
    AssessmentAdapter adapter;
    LinearLayout ll_subject;
    TextView tvAdd, tvSection, tvNorecords;
    ImageView iv_backBtn;
    ArrayList<String> subjectList = new ArrayList<>();

    ArrayList<SectionSubjectModel> arrayListSubjectAndSubjectType;

    public Fragment_AddSubject() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_subject, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        initView(view);
        initListener();
        getDivision();


        return view;

    }


    private void initListener() {
        tvAdd.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);

    }

    private void initView(View view) {
        spDivision = view.findViewById(R.id.spDivision);
        spClass = view.findViewById(R.id.spClass);
        spSection = view.findViewById(R.id.spSection);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvSection = view.findViewById(R.id.section_tv);
        ll_subject = view.findViewById(R.id.ll_subject);


        tvAdd = view.findViewById(R.id.tvAdd);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        edSubName = view.findViewById(R.id.ed_sub_name);
        edSubCode = view.findViewById(R.id.ed_sub_code);
        edSubType = view.findViewById(R.id.ed_subject_type);
        tvNorecords = view.findViewById(R.id.tvNorecords);
        apiService = ApiUtils.getAPIService();


        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        modelArrayList = new ArrayList<>();
        listSubject = new ArrayList<>();

        listSection = new ArrayList<>();
        sectionSubjectModels = new ArrayList<>();
        sectionSubjectModelsNew = new ArrayList<>();
        arrayListSubjectAndSubjectType = new ArrayList<>();

        spSection.setVisibility(View.GONE);
        edSubName.setVisibility(View.GONE);
        edSubCode.setVisibility(View.GONE);
        edSubType.setVisibility(View.GONE);

        subjectList.add("Subject Type");
        subjectList.add("Theory");
        subjectList.add("Practical");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), subjectList);
        edSubType.setAdapter(customSpinnerAdapter);
        edSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSubType = edSubType.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void getDivision() {
        apiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    listDivision.clear();
                    listDivision.add(0, "-Division-");

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String Id = object1.getString("id");
                                statusDivision = object1.getBoolean("status");
                                if (statusDivision) {
                                    String division = object1.getString("division");

                                    listDivision.add(division);

                                }


                            }
                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
                            spDivision.setAdapter(customSpinnerAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {


                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());

            }
        });

        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                strDiv = parent.getSelectedItem().toString();
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                if (strDiv.equalsIgnoreCase("-Division-")) {
                    spClass.setSelection(0);
                } else {
                    listClass.clear();
                    listSection.clear();
                    listSubject.clear();
                    tvNorecords.setVisibility(View.GONE);
                    spSection.setVisibility(View.GONE);
                    edSubName.setVisibility(View.GONE);
                    edSubCode.setVisibility(View.GONE);
                    edSubType.setVisibility(View.GONE);
                    tvSection.setText("");
                    tvSection.setBackground(null);


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
        sectionSubjectModels.clear();
        sectionSubjectModelsNew.clear();

        listClass.clear();
        listClass.add(0, "-Class-");
        apiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("CLASS_SECTIONDDD", "" + response.body() + "***" + response.code());

                if (response.isSuccessful()) {
                    modelArrayList.clear();
                    listSection.clear();

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            if (object.getJSONObject("data").toString().equals("{}")) {

                                modelArrayList.clear();
                                customSpinnerAdapter.notifyDataSetChanged();

                            } else {

                                JSONObject jsonObject2 = jsonObject1.getJSONObject(strDiv);
                                Iterator<String> keys = jsonObject2.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                    String className = jsonObjectValue.getString("class_name");
                                    JSONArray jsonArray = jsonObjectValue.getJSONArray("sections");

                                    sectionClist = new ArrayList<>();
                                    sectionClist.clear();
                                    String Section = "";

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Section = jsonArray.getString(i);
                                        sectionClist.add(Section);
                                    }


                                    listClass.add(className);


                                    modelArrayList.add(new ClassSectionModel(className, sectionClist));


                                }
                                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
                                spClass.setAdapter(customSpinnerAdapter);
                            }


                        }

                    } catch (JSONException je) {

                    }

                } else {
                    listClass.clear();
                    listClass.add(0, "-Class-");
                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
                    spClass.setAdapter(customSpinnerAdapter);

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                str_class = spClass.getItemAtPosition(position).toString();
                HashSet hashSet = new HashSet();
                if (!str_class.equalsIgnoreCase("-Class-")) {
                    listSection.clear();
                    tvNorecords.setVisibility(View.GONE);
                    sectionSubjectModels.clear();
                    sectionSubjectModelsNew.clear();
                    for (int j = 0; j < modelArrayList.size(); j++) {
                        if (modelArrayList.get(j).getClassName().contains(str_class)) {
                            for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {
                                listSection.add(modelArrayList.get(j).getListSection().get(k));
                                // arrayListSubjectAndSubjectType.add(new SectionSubjectModel());

                                Collections.sort(listSection);

                                //Printing integer array list into Ascending order.

                                for(int i=0; i < listSection.size(); i++){
Log.i("LLLL",""+listSection.get(i));
                                }


                                hashSet.addAll(listSection);
                                listSection.clear();
                                listSection.addAll(hashSet);

                            }

                        }
                    }

                    listSection.add(0, "-Section-");
                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSection);
                    spSection.setAdapter(customSpinnerAdapter);


                    spSection.setVisibility(View.VISIBLE);
                    edSubName.setVisibility(View.VISIBLE);
                    edSubCode.setVisibility(View.VISIBLE);
                    edSubType.setVisibility(View.VISIBLE);

                    getSubject(strDiv, str_class);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (strDiv.equals("-Division-")) {

                } else {

                    strSection = spSection.getItemAtPosition(position).toString();
                    Log.i("STRSECTION**N**", "" + strSection + "***" + sectionSubjectModels.size());

                    if (!strSection.equalsIgnoreCase("-Section-")) {
                        tvSection.setText("Section : "+strSection);
                        tvSection.setBackground(getResources().getDrawable(R.drawable.maker_submit));

                        if (sectionSubjectModels.size() > 0) {
                            sectionSubjectModelsNew.clear();
                            listSubject.clear();
                            //  spSubject.setVisibility(View.VISIBLE);
                            edSubName.setVisibility(View.VISIBLE);

                            for (int p = 0; p < sectionSubjectModels.size(); p++) {
                                if (sectionSubjectModels.get(p).getSectionName().contains(strSection)) {

                                    listSubject.add(sectionSubjectModels.get(p).getSubjectName());
                                    arrayListSubjectAndSubjectType.add(new SectionSubjectModel(sectionSubjectModels.get(p).getSubjectName(), sectionSubjectModels.get(p).getSubType()));


                                    sectionSubjectModelsNew.add(new SectionSubjectModel(
                                            sectionSubjectModels.get(p).getSectionName(),
                                            sectionSubjectModels.get(p).getSubjectName(),
                                            sectionSubjectModels.get(p).getSubCode(),
                                            sectionSubjectModels.get(p).getSubType(),
                                            sectionSubjectModels.get(p).getDivisionName(),
                                            sectionSubjectModels.get(p).getClassName(),
                                            sectionSubjectModels.get(p).isIsselect()
                                    ));
                                }
                            }


                            if (sectionSubjectModelsNew.size() > 0) {
                                tvNorecords.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                setRecyclerView();
                            } else {
                                tvNorecords.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }


                        } else {

                            edSubName.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Log.i("STRSECTION**N2**", "" + strSection);
                        sectionSubjectModelsNew.clear();
                        sectionSubjectModels.clear();
                        listSubject.clear();
                        tvSection.setText("");
                        tvSection.setBackground(null);
                        setRecyclerView();

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setRecyclerView() {

        adapter = new AssessmentAdapter(sectionSubjectModelsNew, Constant.RV_SUBJECT_ROW, getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    private void getSubject(final String strDiv, final String str_class) {


        apiService.getSubject(Constant.SCHOOL_ID, strDiv, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {
                        sectionSubjectModels.clear();
                        sectionSubjectModelsNew.clear();
                        ll_subject.setVisibility(View.VISIBLE);

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        System.out.println("subject object***" + object);
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("Section");

                            Log.i("Section***111*",""+strSection+"***"+jsonObject1.toString());

                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String sectionkey = keys.next();
                                JSONObject jsonSection = jsonObject1.getJSONObject(sectionkey);
                                System.out.println("subject object**12*" + jsonSection);


                                Iterator<String> keys2 = jsonSection.keys();

                                while (keys2.hasNext()) {
                                    String subjectkey = keys2.next();
                                    JSONObject jsonSubject = jsonSection.getJSONObject(subjectkey);

                                    String strType = jsonSubject.getString("subject_type");
                                    String strCode = jsonSubject.getString("subject_code");
                                    boolean strStatus = jsonSubject.getBoolean("status");


                                    sectionSubjectModels.add(new SectionSubjectModel(sectionkey, subjectkey, strCode, strType,
                                            strDiv, str_class, strStatus));

                                }



                            /*    if (sectionSubjectModelsNew.size() > 0) {

                                    setRecyclerViewNew(sectionSubjectModels);
                                }*/

                                if (!strSection.equals("-Section-")) {
                                    setRecyclerViewNew(sectionSubjectModels);

                                }
                            }
                        }
                    } catch (JSONException je) {

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
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.tvAdd:
                editvalue = edSubName.getText().toString();
                if (strDiv.equalsIgnoreCase("-Division-")) {
                    Toast.makeText(getActivity(), "Please Select Division", Toast.LENGTH_SHORT).show();
                } else if (str_class.equalsIgnoreCase("-Class-")) {
                    Toast.makeText(getActivity(), "Please Select Class", Toast.LENGTH_SHORT).show();
                } else if (strSection.equalsIgnoreCase("-Section-")) {
                    Toast.makeText(getActivity(), "Please Select Sections", Toast.LENGTH_SHORT).show();
                } else if (edSubName.getText().toString().trim().isEmpty() || edSubName.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Please Enter Subject Name ", Toast.LENGTH_SHORT).show();
                } else if (edSubCode.getText().toString().trim().isEmpty() || edSubCode.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Please Enter Subject Code ", Toast.LENGTH_SHORT).show();
                }else if (strSubType.equalsIgnoreCase("Subject Type")){
                    Toast.makeText(getActivity(), "Please Select Subject Type ", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (listSubject.size() > 0) {

                        boolean b = true;
                        for (int i = 0; i < listSubject.size(); i++) {
                            if ((listSubject.get(i)).contains(editvalue)) {
                                b = false;
                                Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (b) {

                            listSubject.add(editvalue);

                            sectionSubjectModels.add(new SectionSubjectModel(strSection, editvalue, edSubCode.getText().toString(),
                                    strSubType, strDiv, str_class, true));
                            Log.i("ADDSUB***B*", "" + Constant.SCHOOL_ID + "***" + Constant.EMPLOYEE_BY_ID + "**" + str_class + "**" + strDiv);


                            subjectSave();

                        }

                    } else {
                        sectionSubjectModels.add(new SectionSubjectModel(strSection, editvalue, edSubCode.getText().toString(),
                                strSubType, strDiv, str_class, true));
                        listSubject.add(editvalue);

                        subjectSave();
                    }

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    System.out.println("listSubject***" + listSubject);


                    setRecyclerView();

                }
                break;
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

        }
    }

    private void subjectSave() {
        Log.i("ADDSUB***C*", "" + Constant.SCHOOL_ID + "***" + Constant.EMPLOYEE_BY_ID + "**" + str_class + "**" + strDiv);

        JSONObject objectMain = new JSONObject();
        JSONObject objectSec = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();


        try {
            object.put("subject_name", edSubName.getText().toString());
            object.put("subject_code", edSubCode.getText().toString());
            object.put("subject_type", strSubType);
            jsonArray.put(object);

            objectSec.put(strSection, jsonArray);
            objectMain.put("sections", objectSec);

        } catch (JSONException je) {

        }

        addSubjectApi(objectMain);

    }

    private void addSubjectApi(JSONObject objectMain) {

        apiService.addSubject(Constant.SCHOOL_ID, strDiv, str_class, objectMain, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {


                        if (response.isSuccessful()) {

                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                String message = jsonObject.getString("status");

                                if (message.equalsIgnoreCase("Success")) {

                                    Toast.makeText(getActivity(), "Subject added successfully", Toast.LENGTH_SHORT).show();
                                    Log.i("ADDSUB***Save*", "" + response.body() + response.code());

                                    spDivision.setSelection(0);
                                    spClass.setSelection(0);
                                    spSection.setSelection(0);
                                    edSubName.setText("");
                                    edSubCode.setText("");
                                    edSubType.setSelection(0);
                                    sectionSubjectModels.clear();
                                    sectionSubjectModelsNew.clear();


                                    getSubjectAfterAdd(strDiv, str_class,strSection);

                                    Log.i("ADDSUB***Save*", "" + strDiv + str_class + strSection);


                                    adapter.notifyDataSetChanged();


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                            try {
                                assert response.errorBody() != null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("Add_Subject", message);
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.i("EROR**", "" + t.toString());

                    }
                });


    }

    private void getSubjectAfterAdd(final String strDiv, final String str_class, final String strSection) {
        apiService.getSubject(Constant.SCHOOL_ID, strDiv, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {
                        sectionSubjectModels.clear();
                        sectionSubjectModelsNew.clear();
                        ll_subject.setVisibility(View.VISIBLE);

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        System.out.println("subject object***" + object);
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("Section");

                            if (jsonObject1.equals(strSection)){
                                Log.i("Section****",""+strSection+"***"+jsonObject1.toString());
                                Iterator<String> keys = jsonObject1.keys();

                                while (keys.hasNext()) {
                                    String sectionkey = keys.next();
                                    Log.i("Section****",""+sectionkey);
                                    JSONObject jsonSection = jsonObject1.getJSONObject(sectionkey);
                                    System.out.println("subject object**12*" + jsonSection);


                                    Iterator<String> keys2 = jsonSection.keys();

                                    while (keys2.hasNext()) {
                                        String subjectkey = keys2.next();
                                        JSONObject jsonSubject = jsonSection.getJSONObject(subjectkey);

                                        String strType = jsonSubject.getString("subject_type");
                                        String strCode = jsonSubject.getString("subject_code");
                                        boolean strStatus = jsonSubject.getBoolean("status");


                                        sectionSubjectModels.add(new SectionSubjectModel(sectionkey, subjectkey,
                                                strCode, strType, strDiv, str_class, strStatus));

                                    }



                            /*    if (sectionSubjectModelsNew.size() > 0) {

                                    setRecyclerViewNew(sectionSubjectModels);
                                }*/

                                    if (!strSection.equals("-Section-")) {
                                        setRecyclerViewNew(sectionSubjectModels);

                                    }
                                }
                            }

                        }
                    } catch (JSONException je) {

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
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }

    private void setRecyclerViewNew(ArrayList<SectionSubjectModel> sectionSubjectModels) {

        adapter = new AssessmentAdapter(sectionSubjectModels, Constant.RV_SUBJECT_ROW_CLASS, getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}