package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.apache.commons.collections.ArrayStack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.HomeworkAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_QuestionBankList extends Fragment implements View.OnClickListener {
    RecyclerView recycler_view;
    HomeworkAdapter questionBankAdapter;
    APIService apiService, mApiService;
    private ArrayList<String> attachQuesBank;
    private ArrayList<QuestionBankModel> questionBankModels = new ArrayStack();
    FloatingActionButton fab;

    ArrayList<String> listDivision = new ArrayList<>();
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    ArrayList<String> listSectionNew;
    ArrayList<String> listSubject;
    ArrayList<ClassSectionModel> modelArrayList;
    ArrayList<ClassSectionModel> sectionSubjectList = new ArrayList<>();

    CustomSpinnerAdapter customSpinnerAdapter;
    Spinner spDivision, spnClass, spn_section, spn_Subject;
    String strDiv = "", str_class = "", strSection = "", str_subject = "";
    String className, section, subject, quesBankTitle, desc, quesBankId, addedBy, addedDate, strAttachment;
    ImageView iv_backBtn;

    public Fragment_QuestionBankList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__question_bank_list, container, false);
        recycler_view = view.findViewById(R.id.recycler_view);

        spDivision = view.findViewById(R.id.spn_division);
        spnClass = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);
        spn_Subject = view.findViewById(R.id.spn_Subject);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
        apiService = ApiUtilsPatashala.getService();
        mApiService = ApiUtils.getAPIService();
        getQuestionBankList();
        getDivisionApi();

        listClass = new ArrayList<>();

        modelArrayList = new ArrayList<>();
        listClass.add("-Class-");

        setClassIntoSpinner(listClass);

        return view;

    }


    private void getDivisionApi() {


        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision.clear();
                listDivision.add(0, "-Division-");
                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String Id = object1.getString("division");
                                statusDivision = object1.getBoolean("status");


                                String division = object1.getString("division");

                                listDivision.add(division);

                                Log.i("Division Object***", "456" + listDivision + "***" + statusDivision);

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

                //  setSpinnerForClass();

                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                try {
                    array.put(strDiv);
                    jsonObject.put("divisions", array);
                    Constant.DIVISION_NAME = strDiv;

                } catch (JSONException e) {
                    e.printStackTrace();
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
        listClass.add(0, "-Class-");

        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("CLASS_SECTIONDDD", "" + response.body());

                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            if (object.getJSONObject("data").toString().equals("{}")) {


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

                                    setClassIntoSpinner(listClass);

                                }
                            }


                        }

                    } catch (JSONException je) {

                    }

                } else {

                    listClass.clear();
                    listClass.add("-Class-");

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strSection = spn_section.getSelectedItem().toString();
                getSubject(strDiv, str_class);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setClassIntoSpinner(ArrayList<String> listClass) {

        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spnClass.setAdapter(customSpinnerAdapter);


        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_class = spnClass.getSelectedItem().toString();
                listSectionNew = new ArrayList<>();
                listSectionNew.clear();
                listSectionNew.add("-Section-");

                boolean b = true;

                for (int j = 0; j < modelArrayList.size(); j++) {
                    if (modelArrayList.get(j).getClassName().contains(str_class)) {
                        listSectionNew.clear();

                        for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {

                            listSectionNew.add(modelArrayList.get(j).getListSection().get(k));

                        }
                        //  getSubject(strDiv, str_class);
                    }
                }


                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSectionNew);
                spn_section.setAdapter(customSpinnerAdapter);

                //  getSubject(strDiv, str_class);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getSubject(final String strDiv, final String str_class) {

        mApiService.getSubject(Constant.SCHOOL_ID, strDiv, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                listSubject = new ArrayList<>();

                if (response.isSuccessful()) {
                    try {
                        sectionSubjectList.clear();
                        listSubject.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");


                        HashSet<String> hashSet = new HashSet<String>();
                        if (status.equalsIgnoreCase("Success")) {


                            JSONObject jsonObject1 = object.getJSONObject("Section");

                            Iterator<String> keys = jsonObject1.keys();



                            while (keys.hasNext()) {
                                String sectionkey = keys.next();

                                JSONObject jsonSection = jsonObject1.getJSONObject(sectionkey);

                                Iterator<String> keys2 = jsonSection.keys();


                                while (keys2.hasNext()) {
                                    String subjectkey = keys2.next();
                                    Log.i("*New_subjectkey*", "" + subjectkey);

                                    JSONObject jsonSubject = jsonSection.getJSONObject(subjectkey);

                                    String strType = jsonSubject.getString("subject_type");
                                    String strCode = jsonSubject.getString("subject_code");
                                    boolean strStatus = jsonSubject.getBoolean("status");

                                    listSubject.add(subjectkey);



                                    hashSet.addAll(listSubject);
                                    listSubject.clear();
                                    listSubject.addAll(hashSet);


                                }
                                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                                spn_Subject.setAdapter(customSpinnerAdapter);


                                // setRecyclerView();
                            }
                            Gson g = new Gson();
                            System.out.println("GSON DATA**" + g.toJson(sectionSubjectList));

                        } else {

                            Log.i("SubjectList***FF", "" + response.code());
                            listSubject.clear();
                            listSubject.add(0, "-Subject-");
                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                            spn_Subject.setAdapter(customSpinnerAdapter);

                            questionBankModels.clear();
                            questionBankAdapter.notifyDataSetChanged();

                        }

                    } catch (JSONException je) {

                    }
                } else {

                    Log.i("SubjectList***F", "" + response.code());
                    listSubject.clear();
                    listSubject.add(0, "-Subject-");
                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                    spn_Subject.setAdapter(customSpinnerAdapter);

                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SubjectList***B", "" + t.getMessage());

            }
        });

        spn_Subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_subject = spn_Subject.getSelectedItem().toString();
                if (str_subject.equalsIgnoreCase("-Subject-")) {
                    // Toast.makeText(getActivity(), "-Subject-", Toast.LENGTH_SHORT).show();


                   /* questionBankModels.clear();
                    questionBankAdapter.notifyDataSetChanged();*/


                } else {

                    getQuestionListBySubject();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getQuestionListBySubject() {
        ArrayList<QuestionBankModel> questionBankModelsFilter = new ArrayList<>();
        questionBankModelsFilter.clear();
        for (int i = 0; i < questionBankModels.size(); i++) {
            Log.i("questionBankMo***12",""+ questionBankModels.get(i).getSubject());
            if (str_subject.contains(questionBankModels.get(i).getSubject())) {

                questionBankModelsFilter.add(new QuestionBankModel(
                        questionBankModels.get(i).getClassName(),
                        questionBankModels.get(i).getSection(),
                        questionBankModels.get(i).getSubject(),
                        questionBankModels.get(i).getQuesBankTitle(),
                        questionBankModels.get(i).getDesc(),
                        questionBankModels.get(i).getQuesBankId(),
                        questionBankModels.get(i).getAddedBy(),
                        questionBankModels.get(i).getAddedDate(),
                        questionBankModels.get(i).getQuestionBankAttachList()
                ));
                Log.i("questionBankMo***", "" + questionBankModelsFilter.size());
                setRecyclerViewDataNew(questionBankModelsFilter);
            }

        }

        /*className, section, subject, quesBankTitle,
                                        desc, quesBankId, addedBy, addedDate, images*/

/*  questionBankModels.add(new QuestionBankModel(className, section, subject, quesBankTitle,
                                                desc, quesBankId, addedBy, addedDate, images));*/
      /*  apiService.getQuestionBankBysubject(Constant.SCHOOL_ID, str_class, strSection, str_subject)
                .enqueue(new Callback<Object>() {

                    public void onResponse(Call<Object> call, Response<Object> response) {

                        Log.i("QuestionList**BAC", "" + response.body());


                        if (response.isSuccessful()) {
                            questionBankModels.clear();

                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");
                                if (status.equalsIgnoreCase("success")) {
                                    JSONObject jsonObject = object.getJSONObject("data");
                                    JSONObject jsonBank = jsonObject.getJSONObject("question_bank_details");

                                    Iterator<String> keys = jsonBank.keys();

                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONObject objectData = jsonBank.getJSONObject(key);

                                        Log.i("QuestionList**B", "" + objectData);
                                        if (objectData.has("classes")) {
                                            className = objectData.getString("classes");
                                        } else {
                                            className = str_class;
                                        }
                                        if (objectData.has("sections")) {
                                            section = objectData.getString("sections");
                                        } else {
                                            section = strSection;
                                        }
                                        if (objectData.has("classes")) {
                                            subject = objectData.getString("subject");
                                        } else {
                                            subject = str_subject;
                                        }

                                        quesBankTitle = objectData.getString("question_bank_title");
                                        desc = objectData.getString("description");
                                        quesBankId = objectData.getString("question_bank_uuid");
                                        addedBy = objectData.getString("added_by");
                                        addedDate = objectData.getString("added_datetime");


                                        JSONArray jsonArray1 = objectData.getJSONArray("question_bank_attachement");

                                        ArrayList<String> images = new ArrayList<>();

                                        for (int i = 0; i < jsonArray1.length(); i++) {
                                            images.add(jsonArray1.get(i).toString());
                                        }


                                        questionBankModels.add(new QuestionBankModel(className, section, subject, quesBankTitle,
                                                desc, quesBankId, addedBy, addedDate, images));

                                    }
                                    setRecyclerViewData(questionBankModels);


                                }


                            } catch (JSONException je) {

                            }
                        } else {
                            questionBankModels.clear();
                            questionBankAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });*/
    }

    private void setRecyclerViewDataNew(ArrayList<QuestionBankModel> questionBankModelsFilter) {
        questionBankAdapter = new HomeworkAdapter(Constant.RV_QUESTION_BANK_ROW, getActivity(), questionBankModelsFilter);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(questionBankAdapter);
        questionBankAdapter.notifyDataSetChanged();
    }

    private void setRecyclerViewData(ArrayList<QuestionBankModel> questionBankModels) {

        questionBankAdapter = new HomeworkAdapter(Constant.RV_QUESTION_BANK_ROW, getActivity(), questionBankModels);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(questionBankAdapter);
        questionBankAdapter.notifyDataSetChanged();
    }


    private void getQuestionBankList() {
        apiService.getQuestionBank(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    questionBankModels.clear();


                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject = object.getJSONObject("data");
                            JSONObject jsonBank = jsonObject.getJSONObject("question_bank_details");

                            Iterator<String> keys = jsonBank.keys();


                            ArrayList<String> questionBankAttachList = new ArrayList<>();
                            questionBankAttachList.clear();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject objectData = jsonBank.getJSONObject(key);

                                Log.i("QuestionList**B", "" + objectData);
                                if (objectData.has("classes")) {
                                    className = objectData.getString("classes");
                                } else {
                                    className = str_class;
                                }
                                if (objectData.has("sections")) {
                                    section = objectData.getString("sections");
                                } else {
                                    section = strSection;
                                }
                                if (objectData.has("classes")) {
                                    subject = objectData.getString("subject");
                                } else {
                                    subject = str_subject;
                                }


                                quesBankTitle = objectData.getString("question_bank_title");
                                desc = objectData.getString("description");
                                quesBankId = objectData.getString("question_bank_uuid");
                                addedBy = objectData.getString("added_by");
                                addedDate = objectData.getString("added_datetime");


                                JSONArray jsonArray1 = objectData.getJSONArray("question_bank_attachement");
                                ArrayList<String> images = new ArrayList<>();

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    images.add(jsonArray1.get(i).toString());
                                }


                                questionBankModels.add(new QuestionBankModel(className, section, subject, quesBankTitle,
                                        desc, quesBankId, addedBy, addedDate, images));

                            }
                            setRecyclerViewData(questionBankModels);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                DashBoardMenuActivity dashBoardMenuActivity = (DashBoardMenuActivity) getActivity();
                dashBoardMenuActivity.loadFragment(Constant.QUESTIONBANK__CREATE, null);

                break;
            case R.id.iv_backBtn:

                getActivity().onBackPressed();

        }
    }
}
