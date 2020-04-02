package in.varadhismartek.patashalaerp.ClassAndSection;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DivisionModule.AddDivisionActivity;
import in.varadhismartek.patashalaerp.DivisionModule.ClassSectionAndDivisionModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddClassesFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn, iv_sendAddClasses;
    EditText edit_enter;
    ImageButton add_image;
    RecyclerView recycler_view;
    Spinner spinnerForDivision;
    ArrayList<DivisionClassModel> divisionClassModels;
    ArrayList<ClassSectionModel> list;
    ArrayList<ClassSectionAndDivisionModel> classList, newClassList;

    ArrayList<String> listDivision;
    ProgressDialog progressDialog;
    APIService mApiService;
    Button buttonAdd;
    String divisionValue = "";
    CustomSpinnerAdapter customSpinnerAdapter;
    ClassSectionAdapter classSectionAdapter;

    public AddClassesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_classes, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        initListeners();

        getDivisionApi();
        setSpinnerForDivision();
        getClassToDataBase();


        return view;
    }

    private void getClassToDataBase() {
        SharedPreferences preferences = getActivity().getSharedPreferences("DIV_CLASS", Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = preferences.edit();
        String strSecDivClass = preferences.getString("DIV_CLASS", "");
        System.out.println("strSecDivClassAAA****" + strSecDivClass);

        try {
            newClassList.clear();
            JSONArray jsonArray = new JSONArray(strSecDivClass);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String DivName = jsonArray.getJSONObject(i).getString("divisionName");
                //System.out.println("DivName****" + DivName);
                System.out.println("ClassName****111" + divisionValue);

                if (divisionValue.isEmpty() || divisionValue.equals("")) {
                    System.out.println("ClassName****999");
                } else if (divisionValue.equals(DivName)) {
                    JSONArray jsonArray1 = jsonObject.getJSONArray("classList");
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        String ClassName = jsonArray1.getJSONObject(j).getString("name");

                        newClassList.add(new ClassSectionAndDivisionModel(ClassName, true));
                    }
                }


            }

            classSectionAdapter = new ClassSectionAdapter(newClassList
                    , getActivity(), /*buttonAdd,*/ iv_sendAddClasses, Constant.RV_CLASSES_CARD);

            recycler_view.setAdapter(classSectionAdapter);
            classSectionAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void initViews(View view) {

        progressDialog = new ProgressDialog(getActivity());
        mApiService = ApiUtils.getAPIService();

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        spinnerForDivision = view.findViewById(R.id.spinnerForDivision);
        edit_enter = view.findViewById(R.id.edit_enter);
        add_image = view.findViewById(R.id.add_image);
        buttonAdd = view.findViewById(R.id.button_added);
        recycler_view = view.findViewById(R.id.recycler_view);
        iv_sendAddClasses = view.findViewById(R.id.iv_sendAddClasses);

        list = new ArrayList<>();
        listDivision = new ArrayList<>();
        classList = new ArrayList<>();

        newClassList = new ArrayList<>();
        divisionClassModels = new ArrayList<>();
        listDivision.clear();

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
    }

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
        add_image.setOnClickListener(this);
        iv_sendAddClasses.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);

    }

    private void setSpinnerForDivision() {

        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
        spinnerForDivision.setAdapter(customSpinnerAdapter);

        spinnerForDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classList.clear();
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                divisionValue = parent.getSelectedItem().toString();
                System.out.println("divisionValue****" + divisionValue);

                Constant.Division = divisionValue;
                try {
                    array.put(Constant.Division);
                    jsonObject.put("divisions", array);
                    Constant.DIVISION_NAME = Constant.Division;
                    getClassToDataBase();
                    getClassSectionList(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("jsonObject****" + jsonObject);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getClassSectionList(JSONObject jsonObject) {
        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("CLASS_SECTION", "" + response.body());
                if (response.isSuccessful()) {

                } else {
                    if (String.valueOf(response.code()).equals("400")) {
                        Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                    } else if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.add_image:
                String editvalue = edit_enter.getText().toString();
                if (editvalue.equals("")) {
                    Toast.makeText(getContext(), "Please enter the Value", Toast.LENGTH_SHORT).show();
                } else if (spinnerForDivision.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Division", Toast.LENGTH_SHORT).show();
                } else {
                    if (classList.size() > 0) {

                        boolean b = true;
                        for (int i = 0; i < classList.size(); i++) {
                            if ((classList.get(i).getName()).contains(editvalue)) {
                                b = false;
                                Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (b) {


                            classList.add(new ClassSectionAndDivisionModel(editvalue, true));

                        }

                    } else {
                        classList.add(new ClassSectionAndDivisionModel(editvalue, true));
                    }

                    newClassList.add(new ClassSectionAndDivisionModel(editvalue, true));

                    classSectionAdapter = new ClassSectionAdapter(classList
                            , getActivity(), /*buttonAdd,*/ iv_sendAddClasses, Constant.RV_CLASSES_CARD);

                    recycler_view.setAdapter(classSectionAdapter);
                    classSectionAdapter.notifyDataSetChanged();

                    edit_enter.setText("");

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                }

                break;
            case R.id.button_added:

                Gson gson = new Gson();
                if (newClassList.size() > 0) {
                    Toast.makeText(getActivity(), "Save Successfully", Toast.LENGTH_SHORT).show();
                    ArrayList<ClassSectionAndDivisionModel> newList = new ArrayList<>();
                    newList.addAll(classList);
                    divisionClassModels.add(new DivisionClassModel(divisionValue, newList));

                } else {
                    Toast.makeText(getActivity(), "Please Add Class", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.iv_sendAddClasses:

                Bundle bundle = new Bundle();

                if (classList.size() > 0) {
                    Gson gson1 = new Gson();
                    String strDiv = gson1.toJson(divisionClassModels);
                    SharedPreferences preferences = getActivity().getSharedPreferences("DIV_CLASS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("DIV_CLASS", strDiv);
                    editor.commit();

                    /*AddDivisionActivity addDivisionActivity = (AddDivisionActivity) getActivity();
                    bundle.putString("BARRIERS_TYPE", "CLASS_SECTIONS_BARRIER");
                    addDivisionActivity.loadFragment(Constant.ADD_SECTION_FRAGMENT, bundle);*/

                    Intent intent = new Intent(getActivity(), AddDivisionActivity.class);
                    intent.putExtra("BARRIERS_TYPE", "CLASS_SECTIONS_BARRIER");
                    getActivity().startActivity(intent);

                }
                if (newClassList.size() > 0) {
                    //AddDivisionActivity addDivisionActivity = (AddDivisionActivity) getActivity();
                    //  addDivisionActivity.loadFragment(Constant.ADD_SECTION_FRAGMENT, null);

                    Intent intent = new Intent(getActivity(), AddDivisionActivity.class);
                    intent.putExtra("BARRIERS_TYPE", "CLASS_SECTIONS_BARRIER");
                    getActivity().startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Please Add Class", Toast.LENGTH_SHORT).show();
                }

                break;


            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
        }

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

                                setSpinnerForDivision();
                            }


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

    }

}
