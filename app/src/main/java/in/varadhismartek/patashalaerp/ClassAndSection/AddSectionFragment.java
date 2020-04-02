package in.varadhismartek.patashalaerp.ClassAndSection;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import in.varadhismartek.patashalaerp.DivisionModule.AddDivisionActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddSectionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageView iv_backBtn, iv_sendAddSubmit;
    Spinner spn_division, spn_class;
    Button btn_add, btnSave;
    RecyclerView rv_class_section;

    ArrayList<ClassSectionModel> modelArrayList;
    String str_class = "";
    int asciNo = 95;
    APIService mApiService;
    ArrayList<String> listDivision;
    ArrayList<String> listClass;
    String strDiv = "";
    ArrayList<String> list;
    ArrayList<String> sectionClist;

    CustomSpinnerAdapter customSpinnerAdapter;

    public AddSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_section, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        initViews(view);
        initListeners();
        setSpinnerForClass();
        setRecyclerView();
        getDivisionApi();


        return view;
    }


    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        iv_sendAddSubmit = view.findViewById(R.id.iv_sendAddSubmit);
        spn_division = view.findViewById(R.id.spn_division);
        spn_class = view.findViewById(R.id.spn_class);
        btn_add = view.findViewById(R.id.btn_add);
        btnSave = view.findViewById(R.id.btnSave);
        rv_class_section = view.findViewById(R.id.rv_class_section);

        listDivision = new ArrayList<>();
        modelArrayList = new ArrayList<>();


    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        iv_sendAddSubmit.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        spn_division.setOnItemSelectedListener(this);
        spn_class.setOnItemSelectedListener(this);
    }


    private void getDivisionApi() {
        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision.clear();
                listDivision.add(0, "Select Division");
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
                                    String school_id = object1.getString("school_id");
                                    String added_by_id = object1.getString("added_by_id");

                                    listDivision.add(division);
                                }


                            }
                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
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
                modelArrayList.clear();

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
        listClass = new ArrayList<>();
        listClass.clear();
        listClass.add(0, "Select Class");
        customSpinnerAdapter.notifyDataSetChanged();
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
                                modelArrayList.clear();
                                customSpinnerAdapter.notifyDataSetChanged();
                                setSpinnerForClass();

                            } else {

                                JSONObject jsonObject2 = jsonObject1.getJSONObject(strDiv);
                                Iterator<String> keys = jsonObject2.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                    String className = jsonObjectValue.getString("class_name");
                                    JSONArray jsonArray = jsonObjectValue.getJSONArray("sections");

                                    sectionClist = new ArrayList<>();
                                    String Section = "";

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Section = jsonArray.getString(i);
                                        sectionClist.add(Section);
                                    }

                                    listClass.add(className);
                                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
                                    spn_class.setAdapter(customSpinnerAdapter);

                                    modelArrayList.add(new ClassSectionModel(className, sectionClist));
                                }
                            }


                        }


                        setRecyclerView();

                    } catch (JSONException je) {

                    }

                } else {
                    if (String.valueOf(response.code()).equals("400")) {
                        Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                    } else if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                    } else {
                        //setSpinnerForClass();// no data in getsectionAOI
                    }
                    setSpinnerForClass();// no data in getsectionAOI
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }


    private void setSpinnerForClass() {
        listClass = new ArrayList<>();
        listClass.clear();
        listClass.add(0, "Select Class");

        SharedPreferences preferences = getActivity().getSharedPreferences("DIV_CLASS", Context.MODE_PRIVATE);
        String strSecDivClass = preferences.getString("DIV_CLASS", "");
        try {
            JSONArray jsonArray = new JSONArray(strSecDivClass);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String DivName = jsonArray.getJSONObject(i).getString("divisionName");

                if (strDiv.isEmpty() || strDiv.equals("")) {
                } else if (strDiv.equals(DivName)) {
                    JSONArray jsonArray1 = jsonObject.getJSONArray("classList");
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        String ClassName = jsonArray1.getJSONObject(j).getString("name");
                        listClass.add(ClassName);
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spn_class.setAdapter(customSpinnerAdapter);

        spn_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    str_class = "";

                } else {
                    str_class = adapterView.getSelectedItem().toString();
                    asciNo = 95;
                    System.out.println("str_class****" + str_class);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setRecyclerView() {
        ClassSectionAdapter adapter = new ClassSectionAdapter(getActivity(), modelArrayList, btnSave, Constant.RV_SECTION_ROW);
        rv_class_section.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_class_section.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.btn_add:

                if (spn_class.getSelectedItemPosition() == 0 || spn_division.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select Section or Class", Toast.LENGTH_SHORT).show();

                } else if (str_class.equals("")) {
                    Toast.makeText(getActivity(), "Please Select Class", Toast.LENGTH_SHORT).show();

                } else {

                    addNewSection();
                }

                break;

            case R.id.iv_sendAddSubmit:
                if (modelArrayList.size() > 0) {

                    /*AddDivisionActivity addDivisionActivity = (AddDivisionActivity) getActivity();
                    addDivisionActivity.loadFragment(Constant.ADD_SESSION_FRAGMENT, null);*/

                    Intent intent = new Intent(getActivity(), AddDivisionActivity.class);
                    intent.putExtra("BARRIERS_TYPE", "SESSIONS_BARRIER");
                    getActivity().startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Please Add Class & Sections", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
        }

    }

    private void addNewSection() {


        if (modelArrayList.size() > 0) {

            boolean b1 = true;

            for (int i = 0; i < modelArrayList.size(); i++) {

                if (modelArrayList.get(i).getClassName().equals(str_class)) {
                    b1 = false;
                    boolean b = true;

                    Constant.CLASS = str_class;
                    Log.d("MY_ASCII_class_name", str_class);

                    if (modelArrayList.get(i).getListSection().size() > 0) {

                        int size = modelArrayList.get(i).getListSection().size();

                        String value = modelArrayList.get(i).getListSection().get(size - 1);

                        char ch = value.charAt(0);

                        int asciVal = ch;

                        asciVal = asciVal + 1;

                        char ch2 = (char) asciVal;

                        String lastVal = String.valueOf(ch2);

                        modelArrayList.get(i).getListSection().add(String.valueOf(ch2));

                        Log.d("MY_ASCII_sec_size1", String.valueOf(modelArrayList.get(i).getListSection().size()));
                        Log.d("MY_ASCII_sec_size2", String.valueOf(size));
                        Log.d("MY_ASCII_value", value);
                        Log.d("MY_ASCII_ch", String.valueOf(ch));
                        Log.d("MY_ASCII_asciVal1", String.valueOf(asciVal));
                        Log.d("MY_ASCII_asciVal2", String.valueOf(asciVal));
                        Log.d("MY_ASCII_ch2", String.valueOf(ch2));
                        Log.d("MY_ASCII_ch2", lastVal);
                        Log.d("MY_ASCII_m_size", String.valueOf(modelArrayList.size()));
                        Log.d("MY_ASCII_s_size", String.valueOf(modelArrayList.get(0).getListSection().size()));

                        setRecyclerView();
                    }
                }

            }

            if (b1) {

                ArrayList<String> list = new ArrayList<>();
                list.add("A");
                modelArrayList.add(new ClassSectionModel(str_class, list));
                setRecyclerView();

            }


        } else {

            ArrayList<String> list = new ArrayList<>();
            list.add("A");
            modelArrayList.add(new ClassSectionModel(str_class, list));

            setRecyclerView();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {

         /*   case R.id.spn_division:
                break;
            case R.id.spn_class:
                break;*/
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}