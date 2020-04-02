package in.varadhismartek.patashalaerp.AddDepartment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.Adapters.RecyclerAdapter;
import in.varadhismartek.patashalaerp.AddRoles.RolesLandingActivity;
import in.varadhismartek.patashalaerp.AddWing.AddWingsAdapter;
import in.varadhismartek.patashalaerp.AddWing.AddWingsModel;
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
public class AddDepartmentFragment extends Fragment implements View.OnClickListener {

    EditText edit_enter;
    ImageButton add_image;
    Button button_added;
    RecyclerView recycler_view;
    ArrayList<Data> list;
    ArrayList<String> checker;
    ArrayList<String> maker;
    RecyclerAdapter recyclerAdapter;
    ArrayList<String> checkedArrayList;
    ArrayList<String> unCheckedArrayList;
    Spinner wingsSpinner;
    ImageView ivAddDept, ivBack;
    private ArrayList<AddWingsModel> addWingsModelArrayList;
    AddWingsAdapter addWingsAdapter;
    APIService mApiService;
    boolean wings_status = false;

    ArrayList<String> arrayListWings;


    private ProgressDialog progressDialog = null;
    private String wingsValue;


    public AddDepartmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_department, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        wingsSpinner = view.findViewById(R.id.spinnerForTypeOfWings);
        ivAddDept = view.findViewById(R.id.iv_sendAddDept);
        ivAddDept.setOnClickListener(this);
        list = new ArrayList<>();
        checker = new ArrayList<>();
        checkedArrayList = new ArrayList<>();
        unCheckedArrayList = new ArrayList<>();
        maker = new ArrayList<>();
        arrayListWings = new ArrayList<>();
        mApiService = ApiUtils.getAPIService();
        edit_enter = view.findViewById(R.id.edit_enter);
        add_image = view.findViewById(R.id.add_image);
        button_added = view.findViewById(R.id.button_added);
        ivBack = view.findViewById(R.id.iv_backBtn);
        button_added.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        recycler_view = view.findViewById(R.id.recycler_view);
        setSpinnerForRolesType();


        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);


        //This is for Add the new Selectable values

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addValuesIntoList();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }

        });


        return view;

    }

    private void addValuesIntoList() {
        String value = "";
        String editvalue = edit_enter.getText().toString();
        if (editvalue.equals("")) {
            Toast.makeText(getContext(), "Please enter the Value", Toast.LENGTH_SHORT).show();
        }else if (wingsSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please select wings", Toast.LENGTH_SHORT).show();
        }
        else {
            if (list.size() > 0) {
                boolean b = true;
                for (int i = 0; i < list.size(); i++) {
                    if ((list.get(i).getName()).contains(editvalue)) {
                        b = false;
                        Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();
                    }
                }
                if (b) {
                    list.add(new Data(editvalue, true));

                }
            }
            else {
                list.add(new Data(editvalue, true));
            }
            recyclerAdapter = new RecyclerAdapter(getContext(), list, Constant.DEPARTMENT_FRAG);
            recycler_view.setAdapter(recyclerAdapter);
            edit_enter.setText("");


        }

    }

    private void setSpinnerForRolesType() {


        arrayListWings.add(0, "Select Wings");
        mApiService.gettingWings(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                Log.d("jsonSliderKey", key.toString());

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                Log.d("jsonSliderValue", jsonObjectValue.toString());
                                String wing_name = jsonObjectValue.getString("wing_name");
                                wings_status = jsonObjectValue.getBoolean("status");
                                if (wings_status) {
                                    arrayListWings.add(wing_name);
                                }


                            }


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("ROLES_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();

                    }
                    if (String.valueOf(response.code()).equals("404")) {
                        Toast.makeText(getActivity(), "Name does not exists.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();

                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), arrayListWings);
        wingsSpinner.setAdapter(customSpinnerAdapter);
        wingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                 // if (position != 0) {
                wingsValue = parent.getSelectedItem().toString();
                Constant.wingName = wingsValue;
                try {
                    array.put(Constant.wingName);
                    jsonObject.put("wings", array);
                    Constant.DEPART_NAME = Constant.wingName;

                    checkDepartmentValues(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


               /* } else {
                    try {
                        Constant.DEPART_NAME = "departments";
                        array.put("All");// 0 position by default
                        jsonObject.put("wings", array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("jsonObject", "" + jsonObject);
                Log.i("jsonObject", "wingsValue" + wingsValue);
                checkDepartmentValues(jsonObject);
*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void checkDepartmentValues(JSONObject jsonObject) {

        if (list.size() != 0) {
            list.clear();
            recyclerAdapter.notifyDataSetChanged();
        }

        mApiService.gettingDept(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {
                        list.clear();
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject1 = json1.getJSONObject("data");

                            JSONObject jsonObject2 = jsonObject1.getJSONObject(Constant.DEPART_NAME);

                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String wing_name = jsonObjectValue.getString("name");
                                boolean wings_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                list.add(new Data(wing_name, wings_status));
                                recyclerAdapter = new RecyclerAdapter(getContext(), list, Constant.DEPARTMENT_FRAG);
                                recycler_view.setAdapter(recyclerAdapter);

                            }

                        }else {
                            if (String.valueOf(response.code()).equals("409")) {
                                Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                                list.clear();

                            }
                            if (String.valueOf(response.code()).equals("404")) {
                                Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                                list.clear();

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("DEPT_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                        list.clear();

                    }
                    if (String.valueOf(response.code()).equals("404")) {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
                        list.clear();

                    } else {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();
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
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
            case R.id.button_added:
// save button
                Log.d("liseSize", list.size() + "");

                if (wingsSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select wings", Toast.LENGTH_SHORT).show();
                } else {

                    for (int i = 0; i < list.size(); i++) {
                        Log.d("deptName", list.get(i).getName());
                        Data data = list.get(i);

                        if (data.isSelect()) {
                            Log.d("deptNameChecked", "" + data.getName());
                            Toast.makeText(getActivity(), "Department has save", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), "Select Department ", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

                break;

            case R.id.iv_sendAddDept:
// next button

                JSONArray array = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    Data data = list.get(i);
                    if (data.isSelect()) {
                        Log.d("deptNameChecked", "" + data.getName());
                        array.put(data.getName());
                    }
                }

                JSONObject departments = new JSONObject();
                for (int i = 0; i <= list.size(); i++) {

                    JSONObject objDeptments = new JSONObject();

                    try {

                        objDeptments.put("name", array.get(i));
                        objDeptments.put("status", "true");

                        departments.put("" + i, objDeptments);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONObject obj = new JSONObject();
                try {
                    obj.put("departments", departments);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                mApiService.addingDept(Constant.SCHOOL_ID, wingsValue, obj, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        if (response.isSuccessful()) {
                            Log.d("askjdhb", "" + response.body());


                            //  Toast.makeText(getActivity(), "Added Successfully. ", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("ROLES_FAIL", String.valueOf(response.code()));
                            if (String.valueOf(response.code()).equals("409")) {
                                Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();

                            }
                            if (String.valueOf(response.code()).equals("404")) {
                                //  Toast.makeText(getActivity(), "Name does not exists.", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

                if (list.size() > 0) {
                    Intent in = new Intent(getActivity(), RolesLandingActivity.class);
                    getActivity().startActivity(in);
                } else {
                    Toast.makeText(getActivity(), "Please Add Department", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }


}
