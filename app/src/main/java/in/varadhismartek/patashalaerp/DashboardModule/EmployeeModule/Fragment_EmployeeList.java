package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_EmployeeList extends Fragment {
    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<EmpLeaveModel> employeeList, newEmployeeList;
    Spinner spn_employee;
    APIService mApiService;
    RecyclerView recycler_view;
    String empFname = "", empLname = "", empUUId, empPhoneNo, empAdhaarNo, empDept,custom_EMPID;
    String fragmentType = "", employee_status, wing_name, employee_photo, role, sex;
    TextView tvTitle;

    ArrayList<String> arrayListWings = new ArrayList<>();
    ArrayList<String> arrayListDept = new ArrayList<>();
    ArrayList<String> arrayListRoles = new ArrayList<>();

    ArrayList<Data> list = new ArrayList<>();
    Spinner sp_wings, sp_dept, sp_roles;
    JSONObject wingsJsonObject = new JSONObject();
    String deptValue, strRoles;
    ImageView iv_backBtn;
    ProgressDialog mProgressDialog;

    public Fragment_EmployeeList() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);

        initViews(view);
        getWings();

        return view;
    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();
        mProgressDialog = new ProgressDialog(getActivity());
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        recycler_view = view.findViewById(R.id.recyclerview);
        sp_wings = view.findViewById(R.id.sp_wings);
        sp_dept = view.findViewById(R.id.sp_dept);
        sp_roles = view.findViewById(R.id.sp_roles);
        tvTitle = view.findViewById(R.id.tvTitle);

        tvTitle.setText("Employee List ");

        employeeList = new ArrayList<>();
        newEmployeeList = new ArrayList<>();

        arrayListWings.clear();
        arrayListRoles.clear();
        arrayListWings.add("-Wings-");
        arrayListRoles.add("-Roles-");

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


    }

    private void getWings() {

        mProgressDialog.show();
        mProgressDialog.setMessage("Loading Data Please Wait... ");
        mApiService.gettingWings(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (arrayListWings.size()>0)
                    arrayListWings.clear();

                arrayListWings.add(0, "-Wings-");

                if (response.isSuccessful()) {
                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        //Log.d("wing", json1.toString());
                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();

                            Log.d("wing", jsonObject1.toString());


                            while (keys.hasNext()) {
                                String key = keys.next();

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                String wing_name = jsonObjectValue.getString("wing_name");
                                boolean wings_status = jsonObjectValue.getBoolean("status");
                                if (wings_status) {
                                    arrayListWings.add(wing_name);
                                }

                            }

                            mProgressDialog.dismiss();
                            setWingstoSpinner(arrayListWings);

                        }else {
                            mProgressDialog.dismiss();
                            setWingstoSpinner(arrayListWings);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressDialog.dismiss();

                    }
                } else {
                    mProgressDialog.dismiss();
                    setWingstoSpinner(arrayListWings);

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                mProgressDialog.dismiss();

            }
        });



    }

    private void setWingstoSpinner(ArrayList<String> arrayListWings) {

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), arrayListWings);
        sp_wings.setAdapter(customSpinnerAdapter);
        sp_wings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONArray wingsArray = new JSONArray();

                if (position != 0) {
                    String wingsValue = parent.getSelectedItem().toString();
                    Constant.wingName = wingsValue;
                    try {
                        wingsArray.put(Constant.wingName);
                        wingsJsonObject.put("wings", wingsArray);
                        Constant.DEPART_NAME = Constant.wingName;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        getAllEmployeeAPI();

                        Constant.DEPART_NAME = "departments";
                        wingsArray.put("All");
                        wingsJsonObject.put("wings", wingsArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                getDeptBasedOnWings(wingsJsonObject);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDeptBasedOnWings(final JSONObject wingsJsonObject) {

        mProgressDialog.show();

        mApiService.gettingDept(Constant.SCHOOL_ID, wingsJsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (arrayListDept.size()>0)
                    arrayListDept.clear();

                arrayListDept.add(0, "-Department-");

                if (response.isSuccessful()) {

                    try {

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
                                boolean dept_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (dept_status) {
                                    arrayListDept.add(wing_name);
                                }

                            }

                            mProgressDialog.dismiss();
                            setDeptIntoSpinner();

                        }else {
                            mProgressDialog.dismiss();
                            setDeptIntoSpinner();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    mProgressDialog.dismiss();
                    setDeptIntoSpinner();

                    Log.d("ROLES_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {
                        // Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();

                    }
                    if (String.valueOf(response.code()).equals("404")) {
                        Toast.makeText(getActivity(), " Name does not exists.", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("Role ERROR", "" + t.toString());

            }
        });


    }

    private void setDeptIntoSpinner() {

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), arrayListDept);
        sp_dept.setAdapter(customSpinnerAdapter);
        sp_dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONArray deptArray = new JSONArray();
                JSONObject deptJsonObject = new JSONObject();

                // if (position != 0) {

                deptValue = parent.getSelectedItem().toString();
                Constant.deptName = deptValue;

                try {
                    Constant.ROLES_NAME = deptValue;
                    deptArray.put(Constant.deptName);
                    deptJsonObject.put("departments", deptArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getRolesBasedOnDepartment(wingsJsonObject, deptJsonObject);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRolesBasedOnDepartment(JSONObject wingsJsonObject, JSONObject deptJsonObject) {

        mProgressDialog.show();

        mApiService.gettingRoles(Constant.SCHOOL_ID, wingsJsonObject, deptJsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (arrayListRoles.size()>0)
                    arrayListRoles.clear();

                arrayListRoles.add(0,"-Roles-");

                if (response.isSuccessful()) {

                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject(Constant.ROLES_NAME);


                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String rolesName = jsonObjectValue.getString("role");
                                boolean roles_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (roles_status) {
                                    arrayListRoles.add(rolesName);

                                }

                            }

                            mProgressDialog.dismiss();
                            setRolesToSpinner(arrayListRoles);

                        } else {
                            mProgressDialog.dismiss();
                            setRolesToSpinner(arrayListRoles);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("ROLES_FAIL", String.valueOf(response.code()));
                    mProgressDialog.dismiss();
                    setRolesToSpinner(arrayListRoles);

                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }

    private void setRolesToSpinner(ArrayList<String> arrayListRoles) {

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), arrayListRoles);
        sp_roles.setAdapter(customSpinnerAdapter);
        sp_roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    getAllEmployeeAPI();

                }else {
                    strRoles = sp_roles.getItemAtPosition(position).toString();
                    System.out.println("strROLS**" + strRoles);
                    //fileter(deptValue, strRoles);
                    fileter(strRoles);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //private void fileter(String deptValue, String strRoles) {
    private void fileter(String strRoles) {
        if (employeeList.size() > 0) {
            newEmployeeList.clear();
            for (int i = 0; i < employeeList.size(); i++) {
                if (strRoles.contains(employeeList.get(i).getEmpDept()) || (strRoles.contains(employeeList.get(i).getRole()))) {
                    String empName = employeeList.get(i).getEmpFname();
                    Log.i("empName**", empName);

                    newEmployeeList.add(new EmpLeaveModel(
                            employeeList.get(i).getCustom_EMPID(),
                            employeeList.get(i).getEmpFname(),
                            employeeList.get(i).getEmpLname(),
                            employeeList.get(i).getEmpUUId(),
                            employeeList.get(i).getEmpPhoneNo(),
                            employeeList.get(i).getEmpAdhaarNo(),
                            employeeList.get(i).getEmpDept(),
                            employeeList.get(i).getEmployee_status(),
                            employeeList.get(i).getWing_name(),
                            employeeList.get(i).getEmployee_photo(),
                            employeeList.get(i).getRole(),
                            employeeList.get(i).getSex()

                    ));
                }
            }
            setRecylerViewFilter(newEmployeeList);

        }

    }

    private void setRecylerViewFilter(ArrayList<EmpLeaveModel> newEmployeeList) {

        EmployeeAdapter adapter = new EmployeeAdapter(getActivity(), newEmployeeList, Constant.RV_EMPLOYEE_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void getAllEmployeeAPI() {
        mProgressDialog.show();
        mProgressDialog.setMessage("Loading Data Please Wait... ");

        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        employeeNameList.clear();
                        employeeList.clear();
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


                                if (keyData.isNull("custom_employee_id")) {
                                    custom_EMPID = "";
                                } else {
                                    custom_EMPID = keyData.getString("custom_employee_id");
                                }



                                if (keyData.isNull("first_name")) {
                                    empFname = "";
                                } else {
                                    empFname = keyData.getString("first_name");
                                }

                                if (keyData.isNull("last_name")) {
                                    empLname = "";
                                } else {
                                    empLname = keyData.getString("last_name");
                                }

                                if (keyData.isNull("employee_uuid")) {
                                    empUUId = "";
                                } else {
                                    empUUId = keyData.getString("employee_uuid");
                                }


                                if (keyData.isNull("phone_number")) {
                                    empPhoneNo = "";
                                } else {
                                    empPhoneNo = keyData.getLong("phone_number") + "";
                                }

                                if (keyData.isNull("adhaar_card_no")) {
                                    empAdhaarNo = "";
                                } else {
                                    empAdhaarNo = keyData.getString("adhaar_card_no");
                                }
                                if (keyData.isNull("department_name")) {
                                    empDept = "";
                                } else {
                                    empDept = keyData.getString("department_name");
                                }
                                if (keyData.isNull("employee_status")) {
                                    employee_status = "";
                                } else {
                                    employee_status = keyData.getString("employee_status");
                                }
                                if (keyData.isNull("wing_name")) {
                                    wing_name = "";
                                } else {
                                    wing_name = keyData.getString("wing_name");
                                }


                                if (keyData.isNull("employee_photo")) {
                                    employee_photo = "";
                                } else {
                                    employee_photo = keyData.getString("employee_photo");
                                }


                                if (keyData.isNull("role")) {
                                    role = "";
                                } else {
                                    role = keyData.getString("role");
                                }

                                if (keyData.isNull("sex")) {
                                    sex = "";
                                } else {
                                    sex = keyData.getString("sex");
                                }


                                String empName = empFname + " " + empLname;
                                employeeNameList.add(empName);
                                //if (employee_status.equalsIgnoreCase("Approved")) {

                                employeeList.add(new EmpLeaveModel(custom_EMPID,empFname, empLname, empUUId, empPhoneNo, empAdhaarNo, empDept
                                        , employee_status, wing_name, employee_photo, role, sex));

                                // }

                                System.out.println("data1****" + employeeNameList.size());


                            }
                            setRecylerView(employeeList);
                            mProgressDialog.dismiss();

                        }


                    } catch (JSONException je) {

                        je.printStackTrace();
                        mProgressDialog.dismiss();
                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                mProgressDialog.dismiss();

            }
        });

    }

    private void setRecylerView(ArrayList<EmpLeaveModel> employeeList) {




        EmployeeAdapter adapter = new EmployeeAdapter(getActivity(), employeeList, Constant.RV_EMPLOYEE_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }

}
